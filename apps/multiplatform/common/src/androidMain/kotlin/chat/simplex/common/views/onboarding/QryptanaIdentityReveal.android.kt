package chat.simplex.common.views.onboarding

import android.provider.Settings
import chat.simplex.common.platform.androidAppContext
import chat.simplex.common.platform.tmpDir
import chat.simplex.common.platform.VideoPlayer
import com.google.android.exoplayer2.Player
import java.io.File
import java.net.URI

internal actual fun qryptanaIdentityRevealUri(): URI? = runCatching {
  val videoFile = File(tmpDir, QRYPTANA_IDENTITY_REVEAL_RESOURCE)
  if (!videoFile.exists()) {
    videoFile.parentFile?.mkdirs()
    androidAppContext.assets.open(QRYPTANA_IDENTITY_REVEAL_RESOURCE).use { input ->
      videoFile.outputStream().use { output -> input.copyTo(output) }
    }
  }
  videoFile.toURI()
}.getOrNull()

internal actual fun qryptanaIdentityRevealMotionEnabled(): Boolean = runCatching {
  Settings.Global.getFloat(
    androidAppContext.contentResolver,
    Settings.Global.ANIMATOR_DURATION_SCALE,
    1f
  ) > 0f
}.getOrDefault(true)

internal actual fun qryptanaPrepareIdentityRevealPlayer(player: VideoPlayer) {
  // Gallery videos normally repeat. The onboarding reveal is a one-shot
  // sequence and must hold its final frame instead of starting over.
  player.player.repeatMode = Player.REPEAT_MODE_OFF
}
