package chat.simplex.common.views.onboarding

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import chat.simplex.common.model.SharedPreference
import chat.simplex.common.model.User
import chat.simplex.res.MR

@Composable
actual fun OnboardingActionButton(user: User?, onboardingStage: SharedPreference<OnboardingStage>, onclick: (() -> Unit)?) {
  if (user == null) {
    Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
      OnboardingActionButton(Modifier.fillMaxWidth(), labelId = MR.strings.qryptana_create_profile, onboarding = OnboardingStage.Step2_CreateProfile, onclick = onclick)
      OnboardingActionButton(Modifier.fillMaxWidth(), labelId = MR.strings.qryptana_link_device, onboarding = OnboardingStage.LinkAMobile, secondary = true, onclick = onclick)
    }
  } else {
    OnboardingActionButton(Modifier.fillMaxWidth(), labelId = MR.strings.make_private_connection, onboarding = OnboardingStage.OnboardingComplete, onclick = onclick)
  }
}
