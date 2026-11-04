package chat.simplex.common.views.newchat

import SectionDividerSpaced
import SectionItemView
import SectionView
import TextIconSpaced
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.*
import androidx.compose.ui.focus.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextRange
import dev.icerock.moko.resources.compose.painterResource
import dev.icerock.moko.resources.compose.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import chat.simplex.common.model.*
import chat.simplex.common.model.ChatController.appPrefs
import chat.simplex.common.platform.*
import chat.simplex.common.ui.theme.*
import chat.simplex.common.views.chat.item.CIFileViewScope
import chat.simplex.common.views.chat.topPaddingToContent
import chat.simplex.common.views.chatlist.*
import chat.simplex.common.views.contacts.*
import chat.simplex.common.views.helpers.*
import chat.simplex.res.MR
import chat.simplex.res.contact_list_header_title
import chat.simplex.res.create_channel_button
import chat.simplex.res.create_group_button
import chat.simplex.res.deleted_chats
import chat.simplex.res.ic_add_link
import chat.simplex.res.ic_bigtop_updates
import chat.simplex.res.ic_filter_list
import chat.simplex.res.ic_group
import chat.simplex.res.ic_inventory_2
import chat.simplex.res.ic_qr_code
import chat.simplex.res.ic_search
import chat.simplex.res.new_chat
import chat.simplex.res.qryptana_symbol
import chat.simplex.res.no_filtered_contacts
import chat.simplex.res.search_or_paste_simplex_link
import chat.simplex.res.smart_invite_create
import chat.simplex.res.smart_invite_paste
import chat.simplex.res.smart_invite_scan
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter

@Composable
fun ModalData.NewChatSheet(rh: RemoteHostInfo?, close: () -> Unit) {
  DisposableEffect(Unit) {
    onDispose {
      connectProgressManager.cancelConnectProgress()
    }
  }

  val oneHandUI = remember { appPrefs.oneHandUI.state }

  Box {
    val closeAll = { ModalManager.start.closeModals() }

    Column(modifier = Modifier.fillMaxSize()) {
      NewChatSheetLayout(
        addContact = {
          ModalManager.start.showModalCloseable(endButtons = { AddContactLearnMoreButton() }) { _ -> NewChatView(chatModel.currentRemoteHost.value, NewChatOption.INVITE, close = closeAll) }
        },
        scanPaste = {
          ModalManager.start.showModalCloseable(endButtons = { AddContactLearnMoreButton() }) { _ -> NewChatView(chatModel.currentRemoteHost.value, NewChatOption.CONNECT, showQRCodeScanner = appPlatform.isAndroid, close = closeAll) }
        },
        createGroup = {
          ModalManager.start.showCustomModal { close -> AddGroupView(chatModel, chatModel.currentRemoteHost.value, close, closeAll) }
        },
        createChannel = {
          ModalManager.start.showCustomModal { close -> AddChannelView(chatModel, chatModel.currentRemoteHost.value, close, closeAll) }
        },
        rh = rh,
        close = close
      )
    }
    if (oneHandUI.value) {
      Column(Modifier.align(Alignment.BottomCenter)) {
        DefaultAppBar(
          navigationButton = { NavigationButtonBack(onButtonClicked = close) },
          fixedTitleText = generalGetString(MR.strings.new_chat),
          onTop = false,
        )
      }
    }
  }
}

enum class ContactType {
  CARD, CONTACT_WITH_REQUEST, REQUEST, RECENT, CHAT_DELETED, UNLISTED
}

fun chatContactType(chat: Chat): ContactType {
  return when (val cInfo = chat.chatInfo) {
    is ChatInfo.ContactRequest -> ContactType.REQUEST
    is ChatInfo.Direct -> {
      val contact = cInfo.contact
      when {
        contact.nextAcceptContactRequest -> ContactType.CONTACT_WITH_REQUEST
        contact.isContactCard -> ContactType.CARD
        contact.chatDeleted -> ContactType.CHAT_DELETED
        contact.contactStatus == ContactStatus.Active -> ContactType.RECENT
        else -> ContactType.UNLISTED
      }
    }
    else -> ContactType.UNLISTED
  }
}

@Composable
private fun ModalData.NewChatSheetLayout(
  rh: RemoteHostInfo?,
  addContact: () -> Unit,
  scanPaste: () -> Unit,
  createGroup: () -> Unit,
  createChannel: () -> Unit,
  close: () -> Unit,
) {
  val oneHandUI = remember { appPrefs.oneHandUI.state }
  val actionButtons = listOf(
    Triple(
      painterResource(MR.images.ic_add_link),
      stringResource(MR.strings.smart_invite_create),
      addContact,
    ),
    Triple(
      painterResource(MR.images.ic_qr_code),
      if (appPlatform.isAndroid) stringResource(MR.strings.smart_invite_scan) else stringResource(MR.strings.smart_invite_paste),
      scanPaste,
    ),
    Triple(
      painterResource(MR.images.ic_group),
      stringResource(MR.strings.create_group_button),
      createGroup,
    ),
    Triple(
      painterResource(MR.images.ic_bigtop_updates),
      stringResource(MR.strings.create_channel_button),
      createChannel,
    )
  )

  Box(Modifier.fillMaxSize()) {
    // A plain top-aligned column: logo, then the four actions, with all remaining
    // space left at the bottom of the sheet.
    Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Top) {
      if (appPlatform.isAndroid && oneHandUI.value) {
        Spacer(Modifier.windowInsetsTopHeight(WindowInsets.statusBars))
      }
      // Top to bottom: Create public channel, Create group, Scan an invite, Invite someone.
      QryptanaNewChatHeader(actionButtons.asReversed())
    }
    if (oneHandUI.value) {
      StatusBarBackground()
    }
  }
}

@Composable
private fun QryptanaNewChatHeader(actionButtons: List<Triple<Painter, String, () -> Unit>>) {
  Column(Modifier.fillMaxWidth()) {
    Box(
      Modifier.fillMaxWidth().padding(top = 32.dp, bottom = 32.dp),
      contentAlignment = Alignment.Center
    ) {
      Image(
        painterResource(MR.images.qryptana_symbol),
        contentDescription = null,
        modifier = Modifier.size(140.dp)
      )
    }
    Row {
      SectionView {
        actionButtons.map {
          NewChatButton(
            icon = it.first,
            text = it.second,
            click = it.third,
          )
        }
      }
    }
  }
}

@Composable
private fun NewChatButton(
  icon: Painter,
  text: String,
  click: () -> Unit,
  textColor: Color = Color.Unspecified,
  iconColor: Color = MaterialTheme.colors.primary,
  disabled: Boolean = false
) {
  SectionItemView(click, disabled = disabled) {
    Row {
      Icon(icon, text, tint = if (disabled) MaterialTheme.colors.secondary else iconColor)
      TextIconSpaced(false)
      Text(text, color = if (disabled) MaterialTheme.colors.secondary else textColor)
    }
  }
}

@Composable
fun ActionButton(
  text: String?,
  comment: String?,
  icon: Painter,
  disabled: Boolean = false,
  click: () -> Unit = {}
) {
  Surface(shape = RoundedCornerShape(18.dp), color = Color.Transparent, contentColor = LocalContentColor.current) {
    Column(
      Modifier
        .clickable(onClick = click)
        .padding(8.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      val tint = if (disabled) MaterialTheme.colors.secondary else MaterialTheme.colors.primary
      Icon(
        icon, text,
        tint = tint,
        modifier = Modifier
          .size(40.dp)
          .padding(bottom = 8.dp)
      )
      if (text != null) {
        Text(
          text,
          textAlign = TextAlign.Center,
          fontWeight = FontWeight.Bold,
          color = tint,
          modifier = Modifier.padding(bottom = 4.dp)
        )
      }
      if (comment != null) {
        Text(
          comment,
          textAlign = TextAlign.Center,
          style = MaterialTheme.typography.body2
        )
      }
    }
  }
}

@Composable
fun ActionButton(
  modifier: Modifier,
  text: String?,
  comment: String?,
  icon: Painter,
  tint: Color = MaterialTheme.colors.primary,
  disabled: Boolean = false,
  click: () -> Unit = {}
) {
  Surface(modifier, shape = RoundedCornerShape(18.dp), contentColor = LocalContentColor.current) {
    Column(
      Modifier
        .fillMaxWidth()
        .clickable(onClick = click)
        .padding(8.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      val tint = if (disabled) MaterialTheme.colors.secondary else tint
      Icon(
        icon, text,
        tint = tint,
        modifier = Modifier
          .size(40.dp)
          .padding(bottom = 8.dp)
      )
      if (text != null) {
        Text(
          text,
          textAlign = TextAlign.Center,
          fontWeight = FontWeight.Bold,
          color = tint,
          modifier = Modifier.padding(bottom = 4.dp)
        )
      }
      if (comment != null) {
        Text(
          comment,
          textAlign = TextAlign.Center,
          style = MaterialTheme.typography.body2
        )
      }
    }
  }
}

@Preview
@Composable
private fun PreviewNewChatSheet() {
  SimpleXTheme {
    ModalData().NewChatSheet(rh = null, close = {})
  }
}
