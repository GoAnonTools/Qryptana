package chat.simplex.common.platform

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class QryptanaServersTest {
  @Test
  fun clearnetOnlyDropsOnionHostsFromSmp() {
    val result = QryptanaServers.clearnetOnly(QryptanaServers.SMP)

    assertFalse(result.contains(".onion", ignoreCase = true))
    assertTrue(result.contains("smp.qryptana.foundation"))
    assertTrue(result.endsWith(":5224"))
  }

  @Test
  fun clearnetOnlyDropsOnionHostsFromXftp() {
    val result = QryptanaServers.clearnetOnly(QryptanaServers.XFTP)

    assertFalse(result.contains(".onion", ignoreCase = true))
    assertTrue(result.endsWith("@xftp2.goanon.foundation"))
  }

  @Test
  fun clearnetOnlyLeavesClearnetOnlyAddressUnchanged() {
    val clearnet = "smp://key@smp.qryptana.foundation:5224"
    assertEquals(clearnet, QryptanaServers.clearnetOnly(clearnet))
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
  fun hostsOfSplitsProductionSmpRelayIntoNormalizedHosts() {
    val hosts = QryptanaServers.hostsOf(QryptanaServers.SMP)

    assertTrue("smp.qryptana.foundation" in hosts)
    assertTrue(hosts.any { it.endsWith(".onion") })
    assertTrue(hosts.none { it.contains(":") })
  }

  @Test
  fun hostsOfSplitsProductionXftpRelayIntoNormalizedHosts() {
    val hosts = QryptanaServers.hostsOf(QryptanaServers.XFTP)

    assertTrue("xftp2.goanon.foundation" in hosts)
    assertTrue(hosts.any { it.endsWith(".onion") })
    assertTrue(hosts.none { it.contains(":") })
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
