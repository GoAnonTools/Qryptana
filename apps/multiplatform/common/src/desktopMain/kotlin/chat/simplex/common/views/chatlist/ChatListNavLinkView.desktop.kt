package chat.simplex.common.views.chatlist

import SectionDivider
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.node.DelegatableNode
import androidx.compose.ui.node.DrawModifierNode
import androidx.compose.ui.unit.dp
import chat.simplex.common.platform.onRightClick
import chat.simplex.common.ui.theme.qryptanaMessagingTokens
import chat.simplex.common.views.helpers.*
import chat.simplex.common.views.onboarding.QryptanaMetal

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
  val interactionSource = remember { MutableInteractionSource() }
  val hovered = interactionSource.collectIsHoveredAsState().value
  var modifier = Modifier.fillMaxWidth().hoverable(interactionSource)
  if (!disabled) modifier = modifier
    .combinedClickable(onClick = click, onLongClick = { showMenu.value = true })
    .onRightClick { showMenu.value = true }
    CompositionLocalProvider(
      LocalIndication provides if (selectedChat.value && !disabled) NoIndication else LocalIndication.current
    ) {
      Box(modifier) {
        // the highlight sits behind the row so the text keeps its full brightness
        if (selectedChat.value) {
          Box(
            Modifier.matchParentSize()
            .background(Brush.horizontalGradient(listOf(QryptanaMetal.titaniumHigh.copy(alpha = 0.75f), QryptanaMetal.titaniumMid.copy(alpha = 0.30f))))
          ) {
            Box(
              Modifier.align(Alignment.CenterStart).fillMaxHeight().width(2.dp)
              .background(Brush.verticalGradient(listOf(QryptanaMetal.copperLight.copy(alpha = 0.95f), QryptanaMetal.copperMid.copy(alpha = 0.55f), QryptanaMetal.copperShadow.copy(alpha = 0.35f))))
            )
          }
        } else if (hovered && !disabled) {
          Box(Modifier.matchParentSize().background(QryptanaMetal.titaniumMid.copy(alpha = 0.45f)))
        }
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
    }
    if (selectedChat.value || nextChatSelected.value) {
      Divider(modifier = Modifier, color = messagingTokens.border)
    } else {
      SectionDivider()
    }
}
