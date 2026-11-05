package chat.simplex.common.platform

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class QryptanaServersTest {
  private val smpFull =
    "smp://PN_HLQSdhKOp62HM8hoUtOMskUvdeNzqVBniJMunJjo=@smp.qryptana.foundation,b544yscwxhrir2ilsy57dgwedr5vutl7zj525rio4dazsfka4f22k6yd.onion:5224"
  private val smpClearnet =
    "smp://PN_HLQSdhKOp62HM8hoUtOMskUvdeNzqVBniJMunJjo=@smp.qryptana.foundation:5224"
  private val xftpFull =
    "xftp://P4pAiyTPwA2MNZbWZqL11v6ngAAMh7V8X50tc-_o3Lg=@xftp2.goanon.foundation,e6xroxqvdwcid42m54ck6bl4u3te45wu6x3ptaeupuowrgxxow7b6ayd.onion"

  @Test
  fun clearnetOnlyDropsOnionHostAndKeepsPort() {
    assertEquals(smpClearnet, QryptanaServers.clearnetOnly(smpFull))
  }

  @Test
  fun clearnetOnlyDropsOnionHostWhenThereIsNoPort() {
    assertEquals(
      "xftp://P4pAiyTPwA2MNZbWZqL11v6ngAAMh7V8X50tc-_o3Lg=@xftp2.goanon.foundation",
      QryptanaServers.clearnetOnly(xftpFull)
    )
  }

  @Test
  fun clearnetOnlyLeavesClearnetOnlyAddressUnchanged() {
    assertEquals(smpClearnet, QryptanaServers.clearnetOnly(smpClearnet))
  }

  @Test
  fun clearnetOnlyKeepsEveryClearnetHost() {
    assertEquals(
      "smp://key@a.example,b.example:5224",
      QryptanaServers.clearnetOnly("smp://key@a.example,b.example,onion.onion:5224")
    )
  }

  @Test
  fun clearnetOnlyReturnsOriginalWhenOnlyOnionHostsRemain() {
    val onionOnly = "smp://key@abc.onion:5224"
    assertEquals(onionOnly, QryptanaServers.clearnetOnly(onionOnly))
  }

  @Test
  fun clearnetOnlyReturnsInputWithoutAuthSeparator() {
    assertEquals("not-a-server", QryptanaServers.clearnetOnly("not-a-server"))
  }

  @Test
  fun hostsOfReturnsLowercaseHostsWithoutPorts() {
    assertEquals(
      setOf("smp.qryptana.foundation", "b544yscwxhrir2ilsy57dgwedr5vutl7zj525rio4dazsfka4f22k6yd.onion"),
      QryptanaServers.hostsOf(smpFull)
    )
  }

  @Test
  fun hostsOfAcceptsAddressWithoutAuthAndNormalizesCase() {
    assertEquals(
      setOf("xftp2.goanon.foundation", "abc.onion"),
      QryptanaServers.hostsOf("XFTP2.GoAnon.Foundation,ABC.onion:443")
    )
  }

  @Test
  fun hostsOfEmptyInputIsEmpty() {
    assertTrue(QryptanaServers.hostsOf("").isEmpty())
  }
}
