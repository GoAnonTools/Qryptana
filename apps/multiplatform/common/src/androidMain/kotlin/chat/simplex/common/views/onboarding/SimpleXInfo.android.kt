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
    // LinkAMobile creates a companion-device QR link. It is not a restore
    // entry point; Android restore/transfer is available from the profile
    // screen's MigrateButton, which opens MigrateToDeviceView.
    OnboardingActionButton(Modifier.fillMaxWidth(), labelId = MR.strings.qryptana_create_profile, onboarding = OnboardingStage.Step2_CreateProfile, onclick = onclick)
  } else {
    OnboardingActionButton(Modifier.fillMaxWidth(), labelId = MR.strings.make_private_connection, onboarding = OnboardingStage.OnboardingComplete, onclick = onclick)
  }
}
