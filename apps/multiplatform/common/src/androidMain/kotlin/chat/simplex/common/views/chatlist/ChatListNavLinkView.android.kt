package chat.simplex.common.views.chatlist

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import chat.simplex.common.platform.onRightClick
import chat.simplex.common.ui.theme.qryptanaMessagingTokens
import chat.simplex.common.views.helpers.*

@Composable
actual fun ChatListNavLinkLayout(
  chatLinkPreview: @Composable () -> Unit,
  click: () -> Unit,
  dropdownMenuItems: (@Composable () -> Unit)?,
  showMenu: MutableState<Boolean>,
  disabled: Boolean,
  selectedChat: State<Boolean>,
  nextChatSelected: State<Boolean>,
) {
  val messagingTokens = qryptanaMessagingTokens()
  var modifier = Modifier.fillMaxWidth()

  if (!disabled) modifier = modifier
    .combinedClickable(onClick = click, onLongClick = { showMenu.value = true })
    .onRightClick { showMenu.value = true }
  Box(modifier) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(
            start = messagingTokens.rowHorizontalPadding,
            top = messagingTokens.rowVerticalPadding,
            end = messagingTokens.rowHorizontalPadding,
            bottom = messagingTokens.rowVerticalPadding
        ),
      verticalAlignment = Alignment.Top
    ) {
      chatLinkPreview()
    }
    if (dropdownMenuItems != null) {
      DefaultDropdownMenu(showMenu, dropdownMenuItems = dropdownMenuItems)
    }
  }
  Divider(
    Modifier.padding(horizontal = messagingTokens.rowHorizontalPadding),
    color = messagingTokens.border
  )
}
