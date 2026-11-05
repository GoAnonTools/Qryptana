package chat.simplex.common.views.usersettings.networkAndServers

import chat.simplex.common.platform.QryptanaServers
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class NetworkAndServersTest {
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
}
