package chat.simplex.common.views.usersettings

import SectionBottomSpacer
import SectionDividerSpaced
import SectionTextFooter
import SectionView
import androidx.compose.foundation.background
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import chat.simplex.common.model.ChatController.appPrefs
import chat.simplex.common.ui.theme.*
import chat.simplex.common.platform.*
import dev.icerock.moko.resources.compose.painterResource
import dev.icerock.moko.resources.compose.stringResource
import chat.simplex.common.views.TerminalView
import chat.simplex.common.views.helpers.*
import chat.simplex.res.MR
import chat.simplex.res.auth_log_in_using_credential
import chat.simplex.res.auth_open_chat_console
import chat.simplex.res.confirm_database_upgrades
import chat.simplex.res.debug_logs
import chat.simplex.res.deprecated_options_section
import chat.simplex.res.developer_options
import chat.simplex.res.developer_options_section
import chat.simplex.res.hide_dev_options
import chat.simplex.res.ic_avg_pace
import chat.simplex.res.ic_breaking_news
import chat.simplex.res.ic_code
import chat.simplex.res.ic_drive_folder_upload
import chat.simplex.res.ic_report
import chat.simplex.res.in_developing_desc
import chat.simplex.res.in_developing_title
import chat.simplex.res.settings_developer_tools
import chat.simplex.res.show_dev_options
import chat.simplex.res.show_developer_options
import chat.simplex.res.show_internal_errors
import chat.simplex.res.show_slow_api_calls

@Composable
fun DeveloperView(withAuth: (title: String, desc: String, block: () -> Unit) -> Unit
) {
  val m = chatModel
  ColumnWithScrollBar {
    val uriHandler = LocalUriHandler.current
    AppBarTitle(stringResource(MR.strings.settings_developer_tools))
    val developerTools = m.controller.appPrefs.developerTools
    val devTools = remember { developerTools.state }
    val unchangedHints = mutableStateOf(unchangedHintPreferences())
    SectionView {
      SourceCodeItem(uriHandler)
      ChatConsoleItem { withAuth(generalGetString(MR.strings.auth_open_chat_console), generalGetString(MR.strings.auth_log_in_using_credential)) { ModalManager.start.showModalCloseable { TerminalView(false) } } }
      ResetHintsItem(unchangedHints)
      SettingsPreferenceItem(painterResource(MR.images.ic_code), stringResource(MR.strings.show_developer_options), developerTools)
    }
    SectionTextFooter(
      generalGetString(if (devTools.value) MR.strings.show_dev_options else MR.strings.hide_dev_options) + " " +
          generalGetString(MR.strings.developer_options)
    )
    if (devTools.value) {
      SectionDividerSpaced()
      SectionView(stringResource(MR.strings.developer_options_section)) {
        SettingsActionItemWithContent(painterResource(MR.images.ic_breaking_news), stringResource(MR.strings.debug_logs)) {
          DefaultSwitch(
            checked = remember { appPrefs.logLevel.state }.value <= LogLevel.DEBUG,
            onCheckedChange = { appPrefs.logLevel.set(if (it) LogLevel.DEBUG else LogLevel.WARNING) }
          )
        }
        SettingsPreferenceItem(painterResource(MR.images.ic_drive_folder_upload), stringResource(MR.strings.confirm_database_upgrades), m.controller.appPrefs.confirmDBUpgrades)
        if (appPlatform.isDesktop) {
          TerminalAlwaysVisibleItem(m.controller.appPrefs.terminalAlwaysVisible) { checked ->
            if (checked) {
              withAuth(generalGetString(MR.strings.auth_open_chat_console), generalGetString(MR.strings.auth_log_in_using_credential)) {
                m.controller.appPrefs.terminalAlwaysVisible.set(true)
              }
            } else {
              m.controller.appPrefs.terminalAlwaysVisible.set(false)
            }
          }
        }
        SettingsPreferenceItem(painterResource(MR.images.ic_report), stringResource(MR.strings.show_internal_errors), appPreferences.showInternalErrors)
        SettingsPreferenceItem(painterResource(MR.images.ic_avg_pace), stringResource(MR.strings.show_slow_api_calls), appPreferences.showSlowApiCalls)
      }
    }
    SectionDividerSpaced()
    SectionView(stringResource(MR.strings.deprecated_options_section)) {
      val simplexLinkMode = chatModel.controller.appPrefs.simplexLinkMode
      SimpleXLinkOptions(chatModel.simplexLinkMode, onSelected = {
        simplexLinkMode.set(it)
        chatModel.simplexLinkMode.value = it
      })
    }
    SectionBottomSpacer()
  }
}

fun showInDevelopingAlert() {
  AlertManager.shared.showAlertMsg(
    title = generalGetString(MR.strings.in_developing_title),
    text = generalGetString(MR.strings.in_developing_desc)
  )
}
