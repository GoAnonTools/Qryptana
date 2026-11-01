package chat.simplex.common.views.database

import SectionItemView
import SectionTextFooter
import TextIconSpaced
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import chat.simplex.common.ui.theme.SimplexGreen
import chat.simplex.common.views.helpers.*
import chat.simplex.res.MR
import chat.simplex.res.android_change_profile_password_question
import chat.simplex.res.android_encrypt_profiles_question
import chat.simplex.res.android_password_needed_each_start
import chat.simplex.res.android_password_saved_after_restart
import chat.simplex.res.android_password_saved_in_keystore
import chat.simplex.res.android_password_saved_securely_in_keystore
import chat.simplex.res.android_profile_password_not_recoverable
import chat.simplex.res.android_profile_password_will_be_updated
import chat.simplex.res.android_profiles_not_password_protected
import chat.simplex.res.android_random_password_can_change
import chat.simplex.res.android_remove_password
import chat.simplex.res.android_remove_password_from_keystore
import chat.simplex.res.android_save_password_in_keystore
import chat.simplex.res.android_setup_profile_password
import chat.simplex.res.android_store_password_securely
import chat.simplex.res.android_update_profile_password
import chat.simplex.res.ic_vpn_key_filled
import chat.simplex.res.ic_vpn_key_off_filled
import chat.simplex.res.notifications_will_be_hidden
import dev.icerock.moko.resources.compose.painterResource
import dev.icerock.moko.resources.compose.stringResource

@Composable
actual fun SavePassphraseSetting(
  useKeychain: Boolean,
  initialRandomDBPassphrase: Boolean,
  storedKey: Boolean,
  minHeight: Dp,
  enabled: Boolean,
  smallPadding: Boolean,
  onCheckedChange: (Boolean) -> Unit,
) {
  SectionItemView(minHeight = minHeight) {
    Row(verticalAlignment = Alignment.CenterVertically) {
      Icon(
        if (storedKey) painterResource(MR.images.ic_vpn_key_filled) else painterResource(MR.images.ic_vpn_key_off_filled),
        stringResource(MR.strings.android_save_password_in_keystore),
        tint = if (storedKey) SimplexGreen else MaterialTheme.colors.secondary
      )
      if (smallPadding) {
        Spacer(Modifier.padding(horizontal = 4.dp))
      } else {
        TextIconSpaced(false)
      }
      Text(
        stringResource(MR.strings.android_save_password_in_keystore),
        Modifier.weight(1f).padding(end = 24.dp),
        color = Color.Unspecified
      )
      DefaultSwitch(
        checked = useKeychain,
        onCheckedChange = onCheckedChange,
        enabled = enabled
      )
    }
  }
}

@Composable
actual fun DatabaseEncryptionFooter(
  useKeychain: MutableState<Boolean>,
  chatDbEncrypted: Boolean?,
  storedKey: MutableState<Boolean>,
  initialRandomDBPassphrase: MutableState<Boolean>,
  migration: Boolean,
) {
  if (chatDbEncrypted == false) {
    SectionTextFooter(generalGetString(MR.strings.android_profiles_not_password_protected))
  } else if (useKeychain.value) {
    if (storedKey.value) {
      SectionTextFooter(generalGetString(MR.strings.android_password_saved_in_keystore))
      if (initialRandomDBPassphrase.value && !migration) {
        SectionTextFooter(generalGetString(MR.strings.android_random_password_can_change))
      } else {
        SectionTextFooter(annotatedStringResource(MR.strings.android_profile_password_not_recoverable))
      }
    } else {
      SectionTextFooter(generalGetString(MR.strings.android_password_saved_after_restart))
    }
  } else {
    SectionTextFooter(generalGetString(MR.strings.android_password_needed_each_start))
    SectionTextFooter(annotatedStringResource(MR.strings.android_profile_password_not_recoverable))
  }
}

actual fun encryptDatabaseSavedAlert(onConfirm: () -> Unit) {
  AlertManager.shared.showAlertDialog(
    title = generalGetString(MR.strings.android_encrypt_profiles_question),
    text = generalGetString(MR.strings.android_password_saved_securely_in_keystore) + "\n" + generalGetString(MR.strings.android_store_password_securely),
    confirmText = generalGetString(MR.strings.android_setup_profile_password),
    onConfirm = onConfirm,
    destructive = true,
  )
}

actual fun changeDatabaseKeySavedAlert(onConfirm: () -> Unit) {
  AlertManager.shared.showAlertDialog(
    title = generalGetString(MR.strings.android_change_profile_password_question),
    text = generalGetString(MR.strings.android_profile_password_will_be_updated) + "\n" + generalGetString(MR.strings.android_store_password_securely),
    confirmText = generalGetString(MR.strings.android_update_profile_password),
    onConfirm = onConfirm,
    destructive = false,
  )
}

actual fun removePassphraseAlert(onConfirm: () -> Unit) {
  AlertManager.shared.showAlertDialog(
    title = generalGetString(MR.strings.android_remove_password_from_keystore),
    text = generalGetString(MR.strings.notifications_will_be_hidden) + "\n" + storeSecurelyDanger(),
    confirmText = generalGetString(MR.strings.android_remove_password),
    onConfirm = onConfirm,
    destructive = true,
  )
}
