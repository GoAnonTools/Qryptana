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
  // clearnet-only addresses (smp.qryptana.foundation:5224 / xftp.qryptana.foundation:444).
  const val SMP = "smp://PN_HLQSdhKOp62HM8hoUtOMskUvdeNzqVBniJMunJjo=@smp.qryptana.foundation,b544yscwxhrir2ilsy57dgwedr5vutl7zj525rio4dazsfka4f22k6yd.onion:5224"
  const val XFTP = "xftp://7pHBpUNapABy84mZpDEds4wSmSsCAX7lqMV_XiICBdc=@xftp.qryptana.foundation,bpdzfyp43fln46xa4hy67z7aqgpdylmlptgm5fwhoxl6tbiii3xireyd.onion:444"

  /**
   * Adds the Qryptana relays to the active user's server list if they are missing.
   * Safe to call more than once; servers are matched by their address.
   *
   * Throws [QryptanaServersException] instead of returning silently so onboarding
   * can show the failure and refuse to complete.
   */
  suspend fun installDefaultUserServers(rh: Long? = null) {
    val existing = chatController.getUserServers(rh)
      ?: throw QryptanaServersException("Could not read the current server list.")
    val present = existing.flatMap { it.smpServers + it.xftpServers }.map { it.server }.toSet()
    val smpServers = if (SMP in present) emptyList() else listOf(defaultServer(SMP))
    val xftpServers = if (XFTP in present) emptyList() else listOf(defaultServer(XFTP))
    if (smpServers.isEmpty() && xftpServers.isEmpty()) return
    val qryptanaGroup = UserOperatorServers(
      operator = null,
      smpServers = smpServers,
      xftpServers = xftpServers
    )
    if (!chatController.setUserServers(rh, existing + qryptanaGroup)) {
      // setUserServers already showed the generic "failed to save servers" alert.
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
    val qryptanaAddresses = setOf(SMP, XFTP)
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
}
