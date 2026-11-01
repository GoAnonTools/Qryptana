package chat.simplex.app

import chat.simplex.common.platform.localized
import chat.simplex.res.MR
import chat.simplex.res.android_restore_from_backup
import chat.simplex.res.qryptana_wordmark
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class MokoResourcesDesktopTest {
  @Test
  fun generatedStringBundleAndWordmarkLoadAtRuntime() {
    assertEquals("Restore from backup", MR.strings.android_restore_from_backup.localized())

    val wordmark = MR.images.qryptana_wordmark.image
    assertTrue(wordmark.width > 0)
    assertTrue(wordmark.height > 0)
  }
}
