package chat.simplex.common.views.onboarding

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import chat.simplex.common.model.QryptanaServerFilters
import chat.simplex.common.model.ServerOperator
import dev.icerock.moko.resources.compose.painterResource

enum class QryptanaIcon {
  Name,
  Network,
  Commitments,
  Support
}

/** The Qryptana branding mark for the given onboarding/support surface. */
@Composable
expect fun QryptanaBrandMark(icon: QryptanaIcon, modifier: Modifier = Modifier)

/** Shows the Qryptana mark for Qryptana operators and the upstream logo otherwise. */
@Composable
fun QryptanaOperatorLogo(operator: ServerOperator, modifier: Modifier = Modifier) {
  if (QryptanaServerFilters.isQryptanaOperator(operator)) {
    QryptanaBrandMark(QryptanaIcon.Network, modifier)
  } else {
    Image(
      painter = painterResource(operator.logo),
      contentDescription = operator.tradeName,
      contentScale = ContentScale.Fit,
      modifier = modifier
    )
  }
}
