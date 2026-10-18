package chat.simplex.common.views.chat

import androidx.compose.ui.text.TextRange
import kotlin.test.Test
import kotlin.test.assertEquals

class EmojiPickerTest {
  @Test
  fun emojiRowsPreserveAllItemsWithAtMostEightColumns() {
    val rows = composerEmojiRows(composerEmojis)

    assertEquals(composerEmojis, rows.flatten())
    assertEquals(true, rows.all { it.size <= 8 })
  }

  @Test
  fun insertsEmojiAtCursor() {
    assertEquals(
      ComposeMessage("hello😀 world", TextRange(7)),
      insertComposerEmoji(ComposeMessage("hello world", TextRange(5)), "😀")
    )
  }

  @Test
  fun insertsEmojiAtBeginningAndEnd() {
    assertEquals(
      ComposeMessage("😀hello", TextRange(2)),
      insertComposerEmoji(ComposeMessage("hello", TextRange.Zero), "😀")
    )
    assertEquals(
      ComposeMessage("hello😀", TextRange(7)),
      insertComposerEmoji(ComposeMessage("hello", TextRange(5)), "😀")
    )
  }

  @Test
  fun replacesSelectedTextAndPreservesSurroundingText() {
    assertEquals(
      ComposeMessage("h😀o", TextRange(3)),
      insertComposerEmoji(ComposeMessage("hello", TextRange(1, 4)), "😀")
    )
  }

  @Test
  fun clampsInvalidSelectionToMessageBounds() {
    assertEquals(
      ComposeMessage("hello😀", TextRange(7)),
      insertComposerEmoji(ComposeMessage("hello", TextRange(99, 120)), "😀")
    )
  }
}
