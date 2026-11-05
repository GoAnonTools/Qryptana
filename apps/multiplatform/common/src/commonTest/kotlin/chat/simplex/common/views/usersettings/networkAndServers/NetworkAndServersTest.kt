package chat.simplex.common.views.usersettings.networkAndServers

import chat.simplex.common.model.NetworkProxy
import chat.simplex.common.platform.QryptanaServers
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class NetworkAndServersTest {
  private val loopbackDefault = NetworkProxy(host = "127.0.0.1", port = 9050)

  @Test
  fun productionRelaysWithOnionHostsAreDetected() {
    assertTrue(serverHasOnionHost(QryptanaServers.SMP))
    assertTrue(serverHasOnionHost(QryptanaServers.XFTP))
  }

  @Test
  fun clearnetOnlyServerHasNoOnionHost() {
    assertFalse(serverHasOnionHost("smp://key@smp.qryptana.foundation:5224"))
    assertFalse(serverHasOnionHost("xftp://key@xftp2.goanon.foundation"))
  }

  @Test
  fun onionHostWithPortIsDetected() {
    assertTrue(serverHasOnionHost("smp://key@abc.onion:5224"))
  }

  @Test
  fun onionInAnyPositionIsDetected() {
    assertTrue(serverHasOnionHost("smp://key@abc.onion,clearnet.example:5224"))
  }

  @Test
  fun emptyOrNonServerInputHasNoOnionHost() {
    assertFalse(serverHasOnionHost(""))
    assertFalse(serverHasOnionHost("not-a-server"))
  }

  @Test
  fun parseOrbotProxyAcceptsLoopbackOnPort9050() {
    assertEquals(loopbackDefault, parseOrbotProxy("socks://127.0.0.1:9050"))
    assertEquals(loopbackDefault, parseOrbotProxy("socks5://127.0.0.1:9050"))
    assertEquals(NetworkProxy(host = "::1", port = 9050), parseOrbotProxy("socks5://[::1]:9050"))
    assertEquals(NetworkProxy(host = "localhost", port = 9050), parseOrbotProxy("localhost:9050"))
  }

  @Test
  fun parseOrbotProxyRejectsAnyOtherPort() {
    assertEquals(loopbackDefault, parseOrbotProxy("socks://127.0.0.1:12345"))
    assertEquals(loopbackDefault, parseOrbotProxy("127.0.0.1:9051"))
  }

  @Test
  fun parseOrbotProxyRejectsNonLoopbackHosts() {
    assertEquals(loopbackDefault, parseOrbotProxy("socks://10.0.0.5:9050"))
    assertEquals(loopbackDefault, parseOrbotProxy("proxy.example:9050"))
  }

  @Test
  fun parseOrbotProxyDefaultsWhenMissingOrInvalid() {
    assertEquals(loopbackDefault, parseOrbotProxy(null))
    assertEquals(loopbackDefault, parseOrbotProxy(""))
    assertEquals(loopbackDefault, parseOrbotProxy("socks://"))
  }
}
