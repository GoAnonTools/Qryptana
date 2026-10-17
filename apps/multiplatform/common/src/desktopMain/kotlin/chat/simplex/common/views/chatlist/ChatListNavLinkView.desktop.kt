package chat.simplex.common.views.chatlist

import SectionDivider
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.node.DelegatableNode
import androidx.compose.ui.node.DrawModifierNode
import androidx.compose.ui.unit.dp
import chat.simplex.common.platform.onRightClick
import chat.simplex.common.ui.theme.qryptanaMessagingTokens
import chat.simplex.common.views.helpers.*

object NoIndication : IndicationNodeFactory {
  // Should be as a class, not an object. Otherwise, crash
  private class NoIndicationInstance : Modifier.Node(), DrawModifierNode {
    override fun ContentDrawScope.draw() { drawContent() }
  }
  override fun create(interactionSource: InteractionSource): DelegatableNode = NoIndicationInstance()
  override fun hashCode(): Int = -1
  override fun equals(other: Any?) = other === this
}

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
  CompositionLocalProvider(
    LocalIndication provides if (selectedChat.value && !disabled) NoIndication else LocalIndication.current
  ) {
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
      if (selectedChat.value) {
        Box(Modifier.matchParentSize().background(messagingTokens.selectedSurface))
      }
      if (dropdownMenuItems != null) {
        DefaultDropdownMenu(showMenu, dropdownMenuItems = dropdownMenuItems)
      }
    }
  }
  if (selectedChat.value || nextChatSelected.value) {
    Divider(modifier = Modifier, color = messagingTokens.border)
  } else {
    SectionDivider()
  }
}
