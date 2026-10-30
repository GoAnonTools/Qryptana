package chat.simplex.app

import chat.simplex.common.platform.AppUpdatesChannel
import chat.simplex.common.model.AppPreferences
import chat.simplex.common.views.helpers.chooseGitHubReleaseAssets
import com.russhwolf.settings.PropertiesSettings
import java.util.Properties
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AppUpdaterTest {
  @Test
  fun upstreamAssetsAreNeverOfferedAsQryptanaUpdates() {
    assertTrue(chooseGitHubReleaseAssets().isEmpty())
  }

  @Test
  fun updateChannelDefaultIsDisabled() {
    val values = Properties()
    val store = PropertiesSettings(values)
    val pref = AppPreferences.appUpdateChannelPreference(store)
    assertEquals(AppUpdatesChannel.DISABLED, pref.get())
    assertEquals(AppUpdatesChannel.DISABLED, pref.state.value)
    assertTrue(values.isEmpty(), "Reading a default must not rewrite preferences")

    for (channel in listOf(AppUpdatesChannel.BETA, AppUpdatesChannel.STABLE, AppUpdatesChannel.DISABLED)) {
      pref.set(channel)
      assertEquals(channel, pref.state.value)
      assertEquals(channel, AppPreferences.appUpdateChannelPreference(store).get())
    }
    values.setProperty("AppUpdateChannel", "unrecognized")
    assertEquals(AppUpdatesChannel.DISABLED, AppPreferences.appUpdateChannelPreference(store).get())
    assertEquals("unrecognized", values.getProperty("AppUpdateChannel"), "Ignore invalid values without rewriting them")
  }
}
