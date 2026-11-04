package chat.simplex.common.views.onboarding

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import chat.simplex.common.model.QryptanaServerFilters
import chat.simplex.common.model.ServerOperator
import dev.icerock.moko.resources.compose.painterResource

/** The Qryptana relay mark, drawn from the platform's vector/logo resource. */
@Composable
expect fun QryptanaBrandMark(modifier: Modifier = Modifier)

/** Shows the Qryptana mark for Qryptana operators and the upstream logo otherwise. */
@Composable
fun QryptanaOperatorLogo(operator: ServerOperator, modifier: Modifier = Modifier) {
  if (QryptanaServerFilters.isQryptanaOperator(operator)) {
    QryptanaBrandMark(modifier)
  } else {
    Image(
      painter = painterResource(operator.logo),
      contentDescription = operator.tradeName,
      contentScale = ContentScale.Fit,
      modifier = modifier
    )
  }
}
