package chat.simplex.common.views.onboarding

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import chat.simplex.common.model.SharedPreference
import chat.simplex.common.model.User
import chat.simplex.common.views.helpers.ModalManager
import chat.simplex.common.views.migration.RestoreFromBackupView
import chat.simplex.res.MR
import chat.simplex.res.android_restore_from_backup
import chat.simplex.res.make_private_connection
import chat.simplex.res.qryptana_create_profile
import dev.icerock.moko.resources.compose.stringResource

@Composable
actual fun OnboardingActionButton(user: User?, onboardingStage: SharedPreference<OnboardingStage>, onclick: (() -> Unit)?) {
  if (user == null) {
    // LinkAMobile creates a companion-device QR link. The secondary action
    // opens the archive-only restore flow instead.
    OnboardingActionButton(Modifier.fillMaxWidth(), labelId = MR.strings.qryptana_create_profile, onboarding = OnboardingStage.Step2_CreateProfile, onclick = onclick)
    TextButtonBelowOnboardingButton(
      stringResource(MR.strings.android_restore_from_backup),
      onClick = {
        ModalManager.fullscreen.showCustomModal(animated = false) { close ->
          RestoreFromBackupView(close)
        }
      }
    )
  } else {
    OnboardingActionButton(Modifier.fillMaxWidth(), labelId = MR.strings.make_private_connection, onboarding = OnboardingStage.OnboardingComplete, onclick = onclick)
  }
}
