package chat.simplex.common.views.chat

import androidx.compose.foundation.clickable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import chat.simplex.common.ui.theme.DEFAULT_PADDING_HALF
import chat.simplex.common.views.helpers.DefaultDropdownMenu
import chat.simplex.res.MR
import dev.icerock.moko.resources.compose.painterResource
import dev.icerock.moko.resources.compose.stringResource

internal data class ComposerEmoji(
  val value: String,
  val name: String,
  val category: EmojiCategory,
)

internal enum class EmojiCategory(val label: String) {
  Smileys("😀"),
  People("👍"),
  Animals("🐶"),
  Food("🍎"),
  Travel("🚗"),
  Activities("🎉"),
  Objects("💡"),
  Symbols("✅")
}

internal val composerEmojis = listOf(
  ComposerEmoji("😀", "grinning smile happy", EmojiCategory.Smileys), ComposerEmoji("😃", "smile happy", EmojiCategory.Smileys),
  ComposerEmoji("😄", "smile happy", EmojiCategory.Smileys), ComposerEmoji("😁", "grin happy", EmojiCategory.Smileys),
  ComposerEmoji("😂", "joy laugh tears", EmojiCategory.Smileys), ComposerEmoji("🤣", "rofl laugh", EmojiCategory.Smileys),
  ComposerEmoji("😊", "blush smile", EmojiCategory.Smileys), ComposerEmoji("🙂", "slightly smile", EmojiCategory.Smileys),
  ComposerEmoji("😉", "wink", EmojiCategory.Smileys), ComposerEmoji("😍", "heart eyes love", EmojiCategory.Smileys),
  ComposerEmoji("🥰", "smiling hearts love", EmojiCategory.Smileys), ComposerEmoji("😘", "kiss love", EmojiCategory.Smileys),
  ComposerEmoji("😎", "cool sunglasses", EmojiCategory.Smileys), ComposerEmoji("🤔", "think thinking", EmojiCategory.Smileys),
  ComposerEmoji("😢", "cry sad", EmojiCategory.Smileys), ComposerEmoji("😭", "sob cry sad", EmojiCategory.Smileys),
  ComposerEmoji("😡", "angry mad", EmojiCategory.Smileys), ComposerEmoji("😱", "scream fear", EmojiCategory.Smileys),
  ComposerEmoji("🤗", "hug", EmojiCategory.Smileys), ComposerEmoji("🤩", "star struck excited", EmojiCategory.Smileys),
  ComposerEmoji("👍", "thumbs up approve yes", EmojiCategory.People), ComposerEmoji("👎", "thumbs down no", EmojiCategory.People),
  ComposerEmoji("👏", "clap applause", EmojiCategory.People), ComposerEmoji("🙌", "raise hands celebrate", EmojiCategory.People),
  ComposerEmoji("🙏", "pray thanks please", EmojiCategory.People), ComposerEmoji("💪", "muscle strong", EmojiCategory.People),
  ComposerEmoji("👋", "wave hello goodbye", EmojiCategory.People), ComposerEmoji("🤝", "handshake agreement", EmojiCategory.People),
  ComposerEmoji("✌️", "victory peace", EmojiCategory.People), ComposerEmoji("🤞", "crossed fingers luck", EmojiCategory.People),
  ComposerEmoji("❤️", "heart love", EmojiCategory.People), ComposerEmoji("💔", "broken heart", EmojiCategory.People),
  ComposerEmoji("🐶", "dog puppy", EmojiCategory.Animals), ComposerEmoji("🐱", "cat kitten", EmojiCategory.Animals),
  ComposerEmoji("🐭", "mouse", EmojiCategory.Animals), ComposerEmoji("🐹", "hamster", EmojiCategory.Animals),
  ComposerEmoji("🐰", "rabbit bunny", EmojiCategory.Animals), ComposerEmoji("🦊", "fox", EmojiCategory.Animals),
  ComposerEmoji("🐻", "bear", EmojiCategory.Animals), ComposerEmoji("🐼", "panda", EmojiCategory.Animals),
  ComposerEmoji("🐨", "koala", EmojiCategory.Animals), ComposerEmoji("🐯", "tiger", EmojiCategory.Animals),
  ComposerEmoji("🦁", "lion", EmojiCategory.Animals), ComposerEmoji("🐸", "frog", EmojiCategory.Animals),
  ComposerEmoji("🍎", "apple fruit", EmojiCategory.Food), ComposerEmoji("🍐", "pear fruit", EmojiCategory.Food),
  ComposerEmoji("🍊", "orange fruit", EmojiCategory.Food), ComposerEmoji("🍋", "lemon fruit", EmojiCategory.Food),
  ComposerEmoji("🍉", "watermelon fruit", EmojiCategory.Food), ComposerEmoji("🍇", "grapes fruit", EmojiCategory.Food),
  ComposerEmoji("🍓", "strawberry fruit", EmojiCategory.Food), ComposerEmoji("🍕", "pizza", EmojiCategory.Food),
  ComposerEmoji("🍔", "burger hamburger", EmojiCategory.Food), ComposerEmoji("🍟", "fries", EmojiCategory.Food),
  ComposerEmoji("🍩", "donut", EmojiCategory.Food), ComposerEmoji("☕", "coffee drink", EmojiCategory.Food),
  ComposerEmoji("🚗", "car drive", EmojiCategory.Travel), ComposerEmoji("🚕", "taxi", EmojiCategory.Travel),
  ComposerEmoji("🚌", "bus", EmojiCategory.Travel), ComposerEmoji("🚲", "bike bicycle", EmojiCategory.Travel),
  ComposerEmoji("✈️", "plane travel flight", EmojiCategory.Travel), ComposerEmoji("🚀", "rocket space launch", EmojiCategory.Travel),
  ComposerEmoji("🏠", "house home", EmojiCategory.Travel), ComposerEmoji("🌍", "earth world", EmojiCategory.Travel),
  ComposerEmoji("⭐", "star", EmojiCategory.Activities), ComposerEmoji("🌟", "glowing star", EmojiCategory.Activities),
  ComposerEmoji("🎉", "party celebration", EmojiCategory.Activities), ComposerEmoji("🎁", "gift present", EmojiCategory.Activities),
  ComposerEmoji("⚽", "football soccer", EmojiCategory.Activities), ComposerEmoji("🏀", "basketball", EmojiCategory.Activities),
  ComposerEmoji("🎵", "music note", EmojiCategory.Activities), ComposerEmoji("🎮", "game controller", EmojiCategory.Activities),
  ComposerEmoji("💡", "idea light bulb", EmojiCategory.Objects), ComposerEmoji("📱", "phone mobile", EmojiCategory.Objects),
  ComposerEmoji("💻", "computer laptop", EmojiCategory.Objects), ComposerEmoji("🔒", "lock secure privacy", EmojiCategory.Objects),
  ComposerEmoji("🔑", "key", EmojiCategory.Objects), ComposerEmoji("📌", "pin", EmojiCategory.Objects),
  ComposerEmoji("✅", "check done yes", EmojiCategory.Symbols), ComposerEmoji("❌", "cross no", EmojiCategory.Symbols),
  ComposerEmoji("⚠️", "warning", EmojiCategory.Symbols), ComposerEmoji("❓", "question", EmojiCategory.Symbols),
  ComposerEmoji("‼️", "double exclamation", EmojiCategory.Symbols), ComposerEmoji("💯", "hundred perfect", EmojiCategory.Symbols),
)

internal fun insertComposerEmoji(message: ComposeMessage, emoji: String): ComposeMessage {
  val start = message.selection.min.coerceIn(0, message.text.length)
  val end = message.selection.max.coerceIn(start, message.text.length)
  val text = message.text.substring(0, start) + emoji + message.text.substring(end)
  val cursor = start + emoji.length
  return ComposeMessage(text, androidx.compose.ui.text.TextRange(cursor))
}

internal fun composerEmojiRows(items: List<ComposerEmoji>, columns: Int = 8): List<List<ComposerEmoji>> =
  items.chunked(columns.coerceAtLeast(1))

@Composable
fun ComposerEmojiButton(
  message: ComposeMessage,
  onMessageChange: (ComposeMessage) -> Unit,
  enabled: Boolean = true,
) {
  val showPicker = remember { mutableStateOf(false) }
  val recent = remember { mutableStateListOf<String>() }
  val currentMessage by rememberUpdatedState(message)
  Box {
    IconButton(
      onClick = { showPicker.value = !showPicker.value },
      enabled = enabled,
      modifier = Modifier.size(36.dp),
    ) {
      Icon(
        painterResource(MR.images.ic_add_reaction),
        contentDescription = "Emoji",
        tint = if (enabled) MaterialTheme.colors.primary else MaterialTheme.colors.secondary,
      )
    }
    DefaultDropdownMenu(
      showMenu = showPicker,
      modifier = Modifier.width(340.dp).heightIn(max = 430.dp),
    ) {
      ComposerEmojiPicker(
        recent = recent,
        onSelected = { emoji ->
          recent.remove(emoji)
          recent.add(0, emoji)
          onMessageChange(insertComposerEmoji(currentMessage, emoji))
          showPicker.value = false
        }
      )
    }
  }
}

@Composable
private fun ComposerEmojiPicker(
  recent: List<String>,
  onSelected: (String) -> Unit,
) {
  var search by remember { mutableStateOf("") }
  var category by remember { mutableStateOf<EmojiCategory?>(null) }
  val query = search.trim().lowercase()
  val recentItems = recent.mapNotNull { value -> composerEmojis.firstOrNull { it.value == value } }
  val filtered = if (query.isEmpty() && category == null && recentItems.isNotEmpty()) recentItems
  else composerEmojis.filter { (category == null || it.category == category) && (query.isEmpty() || it.name.contains(query) || it.value == query) }

  Column(Modifier.padding(DEFAULT_PADDING_HALF)) {
    TextField(
      value = search,
      onValueChange = { search = it; if (it.isNotEmpty()) category = null },
      modifier = Modifier.fillMaxWidth(),
      singleLine = true,
      placeholder = { Text(stringResource(MR.strings.search_verb)) },
      label = { Text("Emoji") },
    )
    Spacer(Modifier.height(4.dp))
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(2.dp)) {
      TextButton(onClick = { category = null; search = "" }) { Text("★") }
      EmojiCategory.values().forEach { item ->
        TextButton(onClick = { category = item; search = "" }) {
          Text(item.label, color = if (category == item) MaterialTheme.colors.primary else MaterialTheme.colors.secondary)
        }
      }
    }
    if (query.isEmpty() && category == null && recent.isNotEmpty()) {
      Text("Recent", style = MaterialTheme.typography.caption, color = MaterialTheme.colors.secondary)
    }
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .heightIn(min = 80.dp, max = 300.dp)
        .verticalScroll(rememberScrollState()),
      verticalArrangement = Arrangement.spacedBy(2.dp),
    ) {
      composerEmojiRows(filtered).forEach { rowItems ->
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(2.dp),
        ) {
          rowItems.forEach { item ->
            Box(
              Modifier.size(38.dp).clip(CircleShape).clickable { onSelected(item.value) },
              contentAlignment = Alignment.Center,
            ) {
              Text(item.value, style = MaterialTheme.typography.h6)
            }
          }
        }
      }
    }
  }
}
