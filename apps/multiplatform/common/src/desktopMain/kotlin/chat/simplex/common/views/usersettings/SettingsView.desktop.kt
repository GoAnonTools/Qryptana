package chat.simplex.common.views.usersettings

import SectionTextFooter
import SectionView
import androidx.compose.runtime.*
import chat.simplex.common.model.ChatController.appPrefs
import chat.simplex.common.model.ChatModel
import chat.simplex.common.platform.*
import chat.simplex.common.views.helpers.*
import chat.simplex.res.MR
import chat.simplex.res.ic_code
import chat.simplex.res.qryptana_upstream_notes_description
import chat.simplex.res.qryptana_upstream_release_notes
import chat.simplex.res.settings_developer_tools
import dev.icerock.moko.resources.compose.painterResource
import dev.icerock.moko.resources.compose.stringResource

@Composable
actual fun AdvancedSettingsAppSection(
  showSettingsModal: (@Composable (ChatModel) -> Unit) -> (() -> Unit),
  withAuth: (title: String, desc: String, block: () -> Unit) -> Unit,
) {
  SectionView {
    SettingsActionItem(painterResource(MR.images.ic_code), stringResource(MR.strings.settings_developer_tools), showSettingsModal { DeveloperView(withAuth) })
    val selectedChannel = remember { appPrefs.appUpdateChannel.state }
    val values = AppUpdatesChannel.entries.map { it to it.text }
    ExposedDropDownSettingRow(stringResource(MR.strings.qryptana_upstream_release_notes), values, selectedChannel) {
      appPrefs.appUpdateChannel.set(it)
      setupUpdateChecker()
    }
    SectionTextFooter(stringResource(MR.strings.qryptana_upstream_notes_description))
  }
}

@Composable
actual fun AppShutdownItem() {}
