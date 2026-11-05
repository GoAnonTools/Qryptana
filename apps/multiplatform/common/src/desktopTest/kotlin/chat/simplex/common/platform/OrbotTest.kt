package chat.simplex.common.platform

import kotlinx.coroutines.runBlocking
import java.net.InetAddress
import java.net.ServerSocket
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class OrbotTest {
  @Test
  fun loopbackListenerIsLive() = runBlocking {
    ServerSocket(0, 1, InetAddress.getByName("127.0.0.1")).use { server ->
      assertTrue(isTorRoutingLive(vpnMode = false, proxyHost = "127.0.0.1", proxyPort = server.localPort))
    }
  }

  @Test
  fun nonLoopbackProxyIsNotLive() = runBlocking {
    assertFalse(isTorRoutingLive(vpnMode = false, proxyHost = "10.0.0.1", proxyPort = 9050))
  }

  @Test
  fun vpnModeIsNotLiveOnDesktop() = runBlocking {
    assertFalse(isTorRoutingLive(vpnMode = true, proxyHost = "127.0.0.1", proxyPort = 9050))
  }
}
