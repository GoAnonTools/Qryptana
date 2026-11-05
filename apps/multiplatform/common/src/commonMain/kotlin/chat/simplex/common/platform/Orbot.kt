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

/**
 * True when the configured Tor route is usable right now: the loopback SOCKS
 * proxy accepts connections in proxy mode, or a VPN transport is active in VPN
 * mode. Used so the UI never trusts a saved preference alone.
 */
expect suspend fun isTorRoutingLive(vpnMode: Boolean, proxyHost: String, proxyPort: Int): Boolean

/** True when an OS-level VPN transport is active. Always false on desktop. */
expect suspend fun isVpnTransportActive(): Boolean

/** Opens Orbot's UI. Returns false when it cannot be launched. */
expect fun openOrbot(): Boolean
