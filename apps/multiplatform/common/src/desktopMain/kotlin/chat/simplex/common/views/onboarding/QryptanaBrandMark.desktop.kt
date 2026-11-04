package chat.simplex.common.views.onboarding

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import chat.simplex.res.MR
import chat.simplex.res.qryptana_symbol
import dev.icerock.moko.resources.compose.painterResource

@Composable
actual fun QryptanaBrandMark(modifier: Modifier) {
  Image(
    painter = painterResource(MR.images.qryptana_symbol),
    contentDescription = null,
    contentScale = ContentScale.Fit,
    modifier = modifier
  )
}
