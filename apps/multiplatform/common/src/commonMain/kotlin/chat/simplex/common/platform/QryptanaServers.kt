package chat.simplex.common.platform

import chat.simplex.common.model.ServerOperatorConditionsDetail
import chat.simplex.common.model.UserOperatorServers
import chat.simplex.common.model.UserServer

/**
 * Qryptana's self-hosted SMP and XFTP relay defaults.
 *
 * The bundled native core still contains the upstream SimpleX/Flux preset lists
 * (see src/Simplex/Chat/Operators/Presets.hs for the source of truth used when the
 * core is rebuilt). Until that core is rebuilt, onboarding installs these relays as
 * custom default servers for each newly created user.
 */
/** A failure that must be shown to the user; onboarding must not continue past it. */
class QryptanaServersException(message: String, val alertAlreadyShown: Boolean = false) : Exception(message)

object QryptanaServers {
  // Single-port onion format: the port is applied to the whole host list.
  // If the bundled core still rejects this during setUserServers, revert to
  // clearnet-only addresses.
  const val SMP = "smp://PN_HLQSdhKOp62HM8hoUtOMskUvdeNzqVBniJMunJjo=@smp.qryptana.foundation,b544yscwxhrir2ilsy57dgwedr5vutl7zj525rio4dazsfka4f22k6yd.onion:5224"
  const val XFTP = "xftp://P4pAiyTPwA2MNZbWZqL11v6ngAAMh7V8X50tc-_o3Lg=@xftp2.goanon.foundation,e6xroxqvdwcid42m54ck6bl4u3te45wu6x3ptaeupuowrgxxow7b6ayd.onion"

  /**
   * Adds the Qryptana relays to the active user's server list if they are missing.
   * Safe to call more than once; servers are matched by host.
   *
   * Throws [QryptanaServersException] instead of returning silently so onboarding
   * can show the failure and refuse to complete.
   */
  suspend fun installDefaultUserServers(rh: Long? = null) {
    val existing = chatController.getUserServers(rh)
      ?: throw QryptanaServersException("Could not read the current server list.")
    // Compare by host, not by the whole URI, so an address change does not seed a
    // duplicate relay. Hosts are compared case-insensitively and without ports.
    val presentHosts = existing.flatMap { it.smpServers + it.xftpServers }
      .filter { !it.deleted }
      .flatMap { hostsOf(it.server) }
      .toSet()
    fun isPresent(address: String) = hostsOf(address).any { it in presentHosts }
    // SMP (VPS1) and XFTP (VPS2) run on different hosts and root domains; keep one
    // group per relay so neither relay is bundled with the other's operator.
    val groups = mutableListOf<UserOperatorServers>()
    if (!isPresent(SMP)) {
      groups.add(UserOperatorServers(operator = null, smpServers = listOf(defaultServer(SMP)), xftpServers = emptyList()))
    }
    if (!isPresent(XFTP)) {
      groups.add(UserOperatorServers(operator = null, smpServers = emptyList(), xftpServers = listOf(defaultServer(XFTP))))
    }
    if (groups.isEmpty()) return
    if (saveUserServers(rh, existing + groups, showAlertOnError = false)) return
    // The core may reject the onion address (onion parsing, connectivity, or a core
    // build without the required format). Retry with clearnet-only addresses so a
    // new user can always complete onboarding even when Tor/onion is unavailable.
    Log.e(TAG, "Saving relays with onion hosts failed; retrying with clearnet-only addresses")
    val clearnetGroups = groups.map { it.clearnetOnly() }
    if (!saveUserServers(rh, existing + clearnetGroups, showAlertOnError = true)) {
      throw QryptanaServersException("Could not save the Qryptana servers.", alertAlreadyShown = true)
    }
  }

  /**
   * Called when onboarding completes. Disables the upstream preset operators and
   * their servers so new installs route through Qryptana only, and leaves the
   * Qryptana relays enabled. The operators remain available to re-enable manually
   * in Network & servers settings.
   *
   * Throws [QryptanaServersException] on any failure; the caller must show the
   * error and keep the user in onboarding.
   */
  suspend fun finalizeDefaults(rh: Long? = null): ServerOperatorConditionsDetail {
    installDefaultUserServers(rh)
    val groups = chatController.getUserServers(rh)
      ?: throw QryptanaServersException("Could not read the current server list.")
    // Include clearnet fallback variants, otherwise a fallback install would have its
    // own relays disabled by finalizeDefaults().
    val qryptanaAddresses = setOf(SMP, clearnetOnly(SMP), XFTP, clearnetOnly(XFTP))
    val updatedGroups = groups.map { group ->
      group.copy(
        smpServers = group.smpServers.map { srv -> if (srv.server in qryptanaAddresses) srv else srv.copy(enabled = false) },
        xftpServers = group.xftpServers.map { srv -> if (srv.server in qryptanaAddresses) srv else srv.copy(enabled = false) }
      )
    }
    if (!chatController.setUserServers(rh, updatedGroups)) {
      throw QryptanaServersException("Could not disable the upstream servers.", alertAlreadyShown = true)
    }
    val conditions = chatController.getServerOperators(rh)
      ?: throw QryptanaServersException("Could not read the server operators.")
    return chatController.setServerOperators(rh, conditions.serverOperators.map { it.copy(enabled = false) })
      ?: throw QryptanaServersException("Could not update the server operators.")
  }

  // preset = false keeps these entries through core preset migrations.
  private fun defaultServer(address: String) = UserServer(
    remoteHostId = null,
    serverId = null,
    server = address,
    preset = false,
    tested = null,
    enabled = true,
    deleted = false
  )

  private suspend fun saveUserServers(
    rh: Long?,
    servers: List<UserOperatorServers>,
    showAlertOnError: Boolean,
  ): Boolean =
    try {
      chatController.setUserServers(rh, servers, showAlertOnError).also {
        if (!it) Log.e(TAG, "setUserServers returned false")
      }
    } catch (e: Exception) {
      Log.e(TAG, "Failed to save user servers: ${e.stackTraceToString()}")
      false
    }

  private fun UserOperatorServers.clearnetOnly(): UserOperatorServers = copy(
    smpServers = smpServers.map { it.copy(server = clearnetOnly(it.server)) },
    xftpServers = xftpServers.map { it.copy(server = clearnetOnly(it.server)) },
  )

  private fun hostsOf(server: String): Set<String> =
    server.substringAfter('@', server)
      .substringBefore('/')
      .split(',')
      .map { it.trim().substringBefore(':').lowercase() }
      .filter { it.isNotEmpty() }
      .toSet()

  private fun clearnetOnly(address: String): String {
    val at = address.indexOf('@')
    if (at == -1) return address
    val hostsWithPort = address.substring(at + 1)
    val port = hostsWithPort.substringAfterLast(':', "")
      .takeIf { it.isNotEmpty() && it.all(Char::isDigit) }
      ?.let { ":$it" } ?: ""
    val hosts = if (port.isEmpty()) hostsWithPort else hostsWithPort.dropLast(port.length)
    val clearnet = hosts.split(',').filterNot { it.trim().endsWith(".onion", ignoreCase = true) }
    if (clearnet.isEmpty()) return address
    return address.substring(0, at + 1) + clearnet.joinToString(",") + port
  }
}
