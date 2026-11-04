package chat.simplex.common.views.onboarding

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import chat.simplex.common.R

@Composable
actual fun QryptanaBrandMark(icon: QryptanaIcon, modifier: Modifier) {
  val drawable = when (icon) {
    QryptanaIcon.Name -> R.drawable.qryptana_onboarding_name
    QryptanaIcon.Network -> R.drawable.qryptana_onboarding_network
    QryptanaIcon.Commitments -> R.drawable.qryptana_onboarding_commitments
    QryptanaIcon.Support -> R.drawable.qryptana_support_avatar
  }
  Image(
    painter = painterResource(drawable),
    contentDescription = null,
    contentScale = ContentScale.Fit,
    modifier = modifier
  )
}
