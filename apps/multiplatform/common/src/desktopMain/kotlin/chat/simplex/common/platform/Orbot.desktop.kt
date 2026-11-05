package chat.simplex.common.platform

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.InetSocketAddress
import java.net.Socket

actual fun isOrbotInstalled(): Boolean = false

actual suspend fun startOrbotAndAwaitStatus(timeoutMs: Long): OrbotState? = null

actual suspend fun isTorRoutingLive(vpnMode: Boolean, proxyHost: String, proxyPort: Int): Boolean =
  withContext(Dispatchers.IO) {
    if (vpnMode || proxyHost.lowercase() !in setOf("127.0.0.1", "::1", "localhost")) {
      false
    } else {
      try {
        Socket().use {
          it.connect(InetSocketAddress(proxyHost, proxyPort), 800)
          true
        }
      } catch (_: Exception) {
        false
      }
    }
  }

actual suspend fun isVpnTransportActive(): Boolean = false

actual fun openOrbot(): Boolean = false
