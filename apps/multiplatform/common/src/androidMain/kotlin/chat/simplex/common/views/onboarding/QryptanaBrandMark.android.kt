package chat.simplex.common.views.onboarding

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import chat.simplex.common.R

@Composable
actual fun QryptanaBrandMark(modifier: Modifier) {
  Image(
    painter = painterResource(R.drawable.qryptana_brand_mark),
    contentDescription = null,
    contentScale = ContentScale.Fit,
    modifier = modifier
  )
}
