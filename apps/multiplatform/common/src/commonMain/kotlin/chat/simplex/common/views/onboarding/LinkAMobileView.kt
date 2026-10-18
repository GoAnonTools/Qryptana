package chat.simplex.common.views.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import SectionTextFooter
import SectionView
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import chat.simplex.common.model.ChatController.appPrefs
import chat.simplex.common.model.ChatModel
import chat.simplex.common.platform.*
import chat.simplex.common.ui.theme.DEFAULT_PADDING
import chat.simplex.common.ui.theme.QryptanaDarkBackground
import chat.simplex.common.ui.theme.QryptanaDarkSecondary
import chat.simplex.common.ui.theme.QryptanaDarkText
import chat.simplex.common.views.helpers.*
import chat.simplex.common.views.remote.AddingMobileDevice
import chat.simplex.common.views.remote.DeviceNameField
import chat.simplex.common.views.usersettings.PreferenceToggle
import chat.simplex.res.MR
import dev.icerock.moko.resources.compose.stringResource

@Composable
fun LinkAMobile() {
  val connecting = rememberSaveable { mutableStateOf(false) }
  val deviceName = chatModel.controller.appPrefs.deviceNameForRemoteAccess
  var deviceNameInQrCode by remember { mutableStateOf(chatModel.controller.appPrefs.deviceNameForRemoteAccess.get()) }
  val staleQrCode = remember { mutableStateOf(false) }

  LinkAMobileLayout(
    deviceName = remember { deviceName.state },
    connecting,
    staleQrCode,
    updateDeviceName = {
      withBGApi {
        if (it != "" && it != deviceName.get()) {
          chatModel.controller.setLocalDeviceName(it)
          deviceName.set(it)
          staleQrCode.value = deviceName.get() != deviceNameInQrCode
        }
      }
    }
  )
  KeyChangeEffect(staleQrCode.value) {
    if (!staleQrCode.value) {
      deviceNameInQrCode = deviceName.get()
    }
  }
}

@Composable
private fun LinkAMobileLayout(
  deviceName: State<String?>,
  connecting: MutableState<Boolean>,
  staleQrCode: MutableState<Boolean>,
  updateDeviceName: (String) -> Unit,
) {
  if (appPlatform.isDesktop) {
    LinkAMobileDesktop(deviceName, connecting, staleQrCode, updateDeviceName)
  } else ModalView({ appPrefs.onboardingStage.set(OnboardingStage.Step1_SimpleXInfo) }) {
    Column(Modifier.fillMaxSize().padding(top = AppBarHeight * fontSizeSqrtMultiplier)) {
      Box(Modifier.align(Alignment.CenterHorizontally)) {
        AppBarTitle(stringResource(if (remember { chatModel.remoteHosts }.isEmpty()) MR.strings.link_a_mobile else MR.strings.linked_mobiles), overrideTitleColor = MaterialTheme.colors.onBackground)
      }
      Row(Modifier.weight(1f).padding(horizontal = DEFAULT_PADDING * 2), verticalAlignment = Alignment.CenterVertically) {
        Column(
          Modifier.weight(0.3f),
               verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          SectionView(generalGetString(MR.strings.this_device_name).uppercase()) {
            DeviceNameField(deviceName.value ?: "") { updateDeviceName(it) }
            SectionTextFooter(generalGetString(MR.strings.this_device_name_shared_with_mobile))
            PreferenceToggle(stringResource(MR.strings.multicast_discoverable_via_local_network), checked = remember { ChatModel.controller.appPrefs.offerRemoteMulticast.state }.value) {
              ChatModel.controller.appPrefs.offerRemoteMulticast.set(it)
            }
          }
        }
        Box(Modifier.weight(0.7f)) {
          AddingMobileDevice(false, staleQrCode, connecting) {
            // currentRemoteHost will be set instantly but remoteHosts may be delayed
            if (chatModel.remoteHosts.isEmpty() && chatModel.currentRemoteHost.value == null) {
              chatModel.controller.appPrefs.onboardingStage.set(OnboardingStage.Step1_SimpleXInfo)
            } else {
              chatModel.controller.appPrefs.onboardingStage.set(OnboardingStage.OnboardingComplete)
            }
          }
        }
      }
    }
  }
}

@Composable
private fun LinkAMobileDesktop(
  deviceName: State<String?>,
  connecting: MutableState<Boolean>,
  staleQrCode: MutableState<Boolean>,
  updateDeviceName: (String) -> Unit,
) {
  val markReveal = rememberReveal(delayMs = 0, durationMs = 800)
  val titleReveal = rememberReveal(delayMs = 150, durationMs = 800)
  val deviceReveal = rememberReveal(delayMs = 400, durationMs = 900)
  val qrReveal = rememberReveal(delayMs = 650, durationMs = 900)
  val panelShape = RoundedCornerShape(16.dp)
  // The shared titanium plate: a faint lit body with a neutral hairline.
  val panel = Modifier
  .fillMaxWidth()
  .clip(panelShape)
  .background(Brush.verticalGradient(listOf(QryptanaMetal.titaniumHigh.copy(alpha = 0.22f), QryptanaMetal.titaniumLow.copy(alpha = 0.12f))))
  .border(1.dp, Brush.verticalGradient(listOf(QryptanaMetal.titaniumBorder.copy(alpha = 0.60f), QryptanaMetal.titaniumBorder.copy(alpha = 0.30f))), panelShape)
  .padding(horizontal = 22.dp, vertical = 20.dp)
  ModalView(
    { appPrefs.onboardingStage.set(OnboardingStage.Step1_SimpleXInfo) },
            background = QryptanaMetal.void,
            appBarBackground = QryptanaMetal.void,
            appBarContentColor = QryptanaMetal.textBright,
            appBarNavigationTint = QryptanaMetal.copperLight
  ) {
    Box(Modifier.fillMaxSize().qryptanaBackdrop(), contentAlignment = Alignment.Center) {
      Column(
        Modifier.fillMaxWidth().verticalScroll(rememberScrollState()).padding(horizontal = DEFAULT_PADDING, vertical = 24.dp),
             horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Box(Modifier.graphicsLayer { alpha = markReveal.value }) { QryptanaSymbol(size = 72.dp) }
        Spacer(Modifier.height(26.dp))
        Column(Modifier.graphicsLayer { alpha = titleReveal.value }, horizontalAlignment = Alignment.CenterHorizontally) {
          QryptanaSerifTitle(stringResource(if (remember { chatModel.remoteHosts }.isEmpty()) MR.strings.link_a_mobile else MR.strings.linked_mobiles), sizeSp = 34)
          Spacer(Modifier.height(16.dp))
          QryptanaHairline()
        }
        Spacer(Modifier.height(30.dp))
        // Stock switches / fields inside the plates pick up the copper accent and graphite text.
        MaterialTheme(colors = MaterialTheme.colors.copy(
          primary = QryptanaMetal.copperMid,
          onBackground = QryptanaMetal.textBright,
          onSurface = QryptanaMetal.textBright,
          secondary = QryptanaMetal.textTertiary
        )) {
          Column(Modifier.widthIn(max = 520.dp).fillMaxWidth().graphicsLayer { alpha = deviceReveal.value }.then(panel), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
              stringResource(MR.strings.this_device_name),
                 color = QryptanaMetal.textBright.copy(alpha = 0.86f),
                 style = MaterialTheme.typography.subtitle1,
                 letterSpacing = 0.3.sp
            )
            DeviceNameField(deviceName.value ?: "") { updateDeviceName(it) }
            SectionTextFooter(generalGetString(MR.strings.this_device_name_shared_with_mobile), color = QryptanaMetal.textTertiary)
            PreferenceToggle(stringResource(MR.strings.multicast_discoverable_via_local_network), checked = remember { ChatModel.controller.appPrefs.offerRemoteMulticast.state }.value) {
              ChatModel.controller.appPrefs.offerRemoteMulticast.set(it)
            }
          }
          Spacer(Modifier.height(18.dp))
          Box(Modifier.widthIn(max = 560.dp).fillMaxWidth().graphicsLayer { alpha = qrReveal.value }.then(panel)) {
            AddingMobileDevice(false, staleQrCode, connecting) {
              if (chatModel.remoteHosts.isEmpty() && chatModel.currentRemoteHost.value == null) {
                appPrefs.onboardingStage.set(OnboardingStage.Step1_SimpleXInfo)
              } else {
                appPrefs.onboardingStage.set(OnboardingStage.OnboardingComplete)
              }
            }
          }
        }
      }
    }
  }
}
