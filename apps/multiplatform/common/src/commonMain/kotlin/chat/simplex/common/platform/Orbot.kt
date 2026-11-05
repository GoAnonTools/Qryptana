package chat.simplex.common.platform

data class OrbotState(
  /** null when Orbot did not report its mode. */
  val vpnMode: Boolean?,
  /** Orbot's advertised SOCKS endpoint, when in proxy mode. */
  val socksProxy: String?,
)

/** True when Orbot is installed. Always false on non-Android platforms. */
expect fun isOrbotInstalled(): Boolean

/**
 * Sends Orbot's START intent (with this package name so Orbot replies with
 * status broadcasts) and waits up to [timeoutMs] for an "on" status.
 * Returns null if Orbot is unavailable or the mode cannot be determined.
 */
expect suspend fun startOrbotAndAwaitStatus(timeoutMs: Long = 15_000L): OrbotState?
