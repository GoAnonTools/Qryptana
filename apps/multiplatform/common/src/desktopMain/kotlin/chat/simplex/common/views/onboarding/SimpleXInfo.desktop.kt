package chat.simplex.common.views.onboarding

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import chat.simplex.common.model.ChatModel.controller
import chat.simplex.common.model.SharedPreference
import chat.simplex.common.model.User
import chat.simplex.common.platform.chatModel
import chat.simplex.res.MR

@Composable
actual fun OnboardingActionButton(user: User?, onboardingStage: SharedPreference<OnboardingStage>, onclick: (() -> Unit)?) {
  if (user == null) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
      OnboardingActionButton(labelId = MR.strings.qryptana_create_profile, onboarding = OnboardingStage.Step2_CreateProfile, onclick = onclick)
      OnboardingActionButton(labelId = MR.strings.qryptana_link_device, onboarding = if (controller.appPrefs.initialRandomDBPassphrase.get() && !chatModel.desktopOnboardingRandomPassword.value) OnboardingStage.Step2_5_SetupDatabasePassphrase else OnboardingStage.LinkAMobile, secondary = true, onclick = onclick)
    }
  } else {
    OnboardingActionButton(Modifier.widthIn(min = 300.dp), labelId = MR.strings.make_private_connection, onboarding = OnboardingStage.OnboardingComplete, onclick = onclick)
  }
}
