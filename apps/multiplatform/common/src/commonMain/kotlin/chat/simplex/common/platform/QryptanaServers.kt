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
object QryptanaServers {
  const val SMP = "smp://PN_HLQSdhKOp62HM8hoUtOMskUvdeNzqVBniJMunJjo=@smp.qryptana.foundation:5224"
  const val XFTP = "xftp://7pHBpUNapABy84mZpDEds4wSmSsCAX7lqMV_XiICBdc=@xftp.qryptana.foundation:444"

  /**
   * Adds the Qryptana relays to the active user's server list if they are missing.
   * Safe to call more than once; servers are matched by their address.
   */
  suspend fun installDefaultUserServers(rh: Long? = null): Boolean {
    val existing = chatController.getUserServers(rh) ?: return false
    val present = existing.flatMap { it.smpServers + it.xftpServers }.map { it.server }.toSet()
    val smpServers = if (SMP in present) emptyList() else listOf(defaultServer(SMP))
    val xftpServers = if (XFTP in present) emptyList() else listOf(defaultServer(XFTP))
    if (smpServers.isEmpty() && xftpServers.isEmpty()) return true
    val qryptanaGroup = UserOperatorServers(
      operator = null,
      smpServers = smpServers,
      xftpServers = xftpServers
    )
    return chatController.setUserServers(rh, existing + qryptanaGroup)
  }

  /**
   * Called when onboarding completes. Disables the upstream preset operators and
   * their servers so new installs route through Qryptana only, and leaves the
   * Qryptana relays enabled. The operators remain available to re-enable manually
   * in Network & servers settings.
   */
  suspend fun finalizeDefaults(rh: Long? = null): ServerOperatorConditionsDetail? {
    if (!installDefaultUserServers(rh)) return null
    val groups = chatController.getUserServers(rh) ?: return null
    val qryptanaAddresses = setOf(SMP, XFTP)
    val updatedGroups = groups.map { group ->
      group.copy(
        smpServers = group.smpServers.map { srv -> if (srv.server in qryptanaAddresses) srv else srv.copy(enabled = false) },
        xftpServers = group.xftpServers.map { srv -> if (srv.server in qryptanaAddresses) srv else srv.copy(enabled = false) }
      )
    }
    if (!chatController.setUserServers(rh, updatedGroups)) return null
    val conditions = chatController.getServerOperators(rh) ?: return null
    return chatController.setServerOperators(rh, conditions.serverOperators.map { it.copy(enabled = false) })
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
