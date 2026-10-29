package chat.simplex.app

import chat.simplex.common.platform.AppUpdatesChannel
import chat.simplex.common.model.defaultAppUpdateChannel
import chat.simplex.common.views.helpers.chooseGitHubReleaseAssets
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
    assertEquals(AppUpdatesChannel.DISABLED, defaultAppUpdateChannel())
  }
}
