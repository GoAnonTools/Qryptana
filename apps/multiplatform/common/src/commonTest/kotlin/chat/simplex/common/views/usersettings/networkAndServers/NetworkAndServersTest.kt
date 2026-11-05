package chat.simplex.common.views.usersettings.networkAndServers

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class NetworkAndServersTest {
  private val smpFull =
    "smp://PN_HLQSdhKOp62HM8hoUtOMskUvdeNzqVBniJMunJjo=@smp.qryptana.foundation,b544yscwxhrir2ilsy57dgwedr5vutl7zj525rio4dazsfka4f22k6yd.onion:5224"
  private val xftpFull =
    "xftp://P4pAiyTPwA2MNZbWZqL11v6ngAAMh7V8X50tc-_o3Lg=@xftp2.goanon.foundation,e6xroxqvdwcid42m54ck6bl4u3te45wu6x3ptaeupuowrgxxow7b6ayd.onion"

  @Test
  fun serverWithOnionHostIsDetected() {
    assertTrue(serverHasOnionHost(smpFull))
    assertTrue(serverHasOnionHost(xftpFull))
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
