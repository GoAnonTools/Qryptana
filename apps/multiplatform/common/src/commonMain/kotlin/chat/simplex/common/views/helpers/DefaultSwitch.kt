package chat.simplex.common.views.helpers

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import chat.simplex.common.platform.appPlatform

@Composable
fun DefaultSwitch(
  checked: Boolean,
  onCheckedChange: ((Boolean) -> Unit)?,
  modifier: Modifier = Modifier,
  enabled: Boolean = true,
  interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
  colors: SwitchColors? = null
) {
  val androidColors = SwitchDefaults.colors(
    checkedThumbColor = MaterialTheme.colors.onPrimary,
    checkedTrackColor = MaterialTheme.colors.primaryVariant,
    uncheckedThumbColor = MaterialTheme.colors.secondary,
    uncheckedTrackColor = MaterialTheme.colors.secondaryVariant,
    checkedTrackAlpha = 1f,
    uncheckedTrackAlpha = 1f,
  )
  val legacyColors = SwitchDefaults.colors(
    checkedThumbColor = MaterialTheme.colors.primary,
    uncheckedThumbColor = MaterialTheme.colors.secondary,
    checkedTrackAlpha = 0.0f,
    uncheckedTrackAlpha = 0.0f,
  )
  val resolvedColors = colors ?: if (appPlatform.isAndroid) androidColors else legacyColors
  val color = if (appPlatform.isAndroid) Color.Transparent
    else if (checked) MaterialTheme.colors.primary.copy(alpha = 0.3f)
    else MaterialTheme.colors.secondary.copy(alpha = 0.3f)
  val size = with(LocalDensity.current) { Size(40.dp.toPx(), 26.dp.toPx()) }
  val offset = with(LocalDensity.current) { Offset(4.dp.toPx(), 11.dp.toPx()) }
  val radius = with(LocalDensity.current) { 13.dp.toPx() }
  Switch(
    checked = checked,
    onCheckedChange = onCheckedChange,
    modifier.drawBehind { drawRoundRect(color, size = size, topLeft = offset, cornerRadius = CornerRadius(radius, radius)) },
    colors = resolvedColors,
    enabled = enabled,
    interactionSource = interactionSource,
  )
}
