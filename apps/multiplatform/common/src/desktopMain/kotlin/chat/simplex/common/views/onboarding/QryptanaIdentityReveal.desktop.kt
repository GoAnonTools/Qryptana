package chat.simplex.common.views.onboarding

import chat.simplex.common.platform.tmpDir
import chat.simplex.common.platform.VideoPlayer
import java.io.File
import java.net.URI

internal actual fun qryptanaIdentityRevealUri(): URI? = runCatching {
  val videoFile = File(tmpDir, QRYPTANA_IDENTITY_REVEAL_RESOURCE)
  if (!videoFile.exists()) {
    videoFile.parentFile?.mkdirs()
    val resource = object {}.javaClass.getResourceAsStream("/$QRYPTANA_IDENTITY_REVEAL_RESOURCE")
      ?: return null
    resource.use { input ->
      videoFile.outputStream().use { output -> input.copyTo(output) }
    }
  }
  videoFile.toURI()
}.getOrNull()

internal actual fun qryptanaIdentityRevealMotionEnabled(): Boolean = true

internal actual fun qryptanaPrepareIdentityRevealPlayer(player: VideoPlayer) = Unit
