package chat.simplex.common.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import chat.simplex.common.platform.appPlatform

/**
 * Reusable Android-specific material tokens and modifiers for the approved
 * "Precision Titanium with Directional Material Depth" design direction.
 *
 * All functions are safely scoped to Android and respect light/dark themes.
 */
object QryptanaAndroidMaterialTokens {
  // Titanium surfaces (Dark)
  val DarkAppBarGradient = listOf(Color(0xFF1B2129), Color(0xFF15191F), Color(0xFF13171D))
  val DarkComposerShelfGradient = listOf(Color(0xFF181E26), Color(0xFF14171E), Color(0xFF111419))
  val DarkAppBarBorder = Color(0xFF20262E)
  val DarkSpecularHairline = Color(0xFFDDE2E8).copy(alpha = 0.14f)
  val DarkInputWellBackground = Color(0xFF0E1014)
  val DarkInputWellBorder = Color(0xFF28303C)

  // Titanium surfaces (Light)
  val LightAppBarGradient = listOf(Color(0xFFFFFFFF), Color(0xFFF8FAFC), Color(0xFFEEF2F6))
  val LightComposerShelfGradient = listOf(Color(0xFFFFFFFF), Color(0xFFF8FAFC), Color(0xFFEEF2F6))
  val LightAppBarBorder = Color(0xFFD6DCE2)
  val LightSpecularHairline = Color.White.copy(alpha = 0.85f)
  val LightInputWellBackground = Color(0xFFFFFFFF)
  val LightInputWellBorder = Color(0xFFBAC2CA)

  // Machined Copper Accent (Both themes)
  val CopperGradientStops = arrayOf(
    0.0f to Color(0xFFB27357),
    0.55f to Color(0xFF9F634C),
    1.0f to Color(0xFF874E3B)
  )
  val CopperBorder = Color(0xFF6F3B2A)
  val CopperSpecular = Color(0xFFF0D4C4).copy(alpha = 0.35f)
  val CopperShadow = Color(0x619F634C)
}

/**
 * Directionally lit top app bar background with top specular hairline.
 */
fun Modifier.qryptanaTopAppBarBackground(
  isDark: Boolean,
  onTop: Boolean,
  alpha: Float = 1f
): Modifier {
  if (!appPlatform.isAndroid) return this
  val gradient = if (isDark) QryptanaAndroidMaterialTokens.DarkAppBarGradient else QryptanaAndroidMaterialTokens.LightAppBarGradient
  val specularColor = if (isDark) QryptanaAndroidMaterialTokens.DarkSpecularHairline else QryptanaAndroidMaterialTokens.LightSpecularHairline
  return this.drawWithCache {
    val brush = Brush.verticalGradient(gradient)
    onDrawBehind {
      drawRect(brush, alpha = alpha)
      if (onTop) {
        drawLine(
          color = specularColor.copy(alpha = specularColor.alpha * alpha),
          start = Offset(0f, 0f),
          end = Offset(size.width, 0f),
          strokeWidth = 1f
        )
      }
    }
  }
}

/**
 * Directionally lit composer shelf with top specular highlight and structural top border.
 */
fun Modifier.qryptanaComposerShelf(isDark: Boolean): Modifier {
  if (!appPlatform.isAndroid) return this
  val gradient = if (isDark) QryptanaAndroidMaterialTokens.DarkComposerShelfGradient else QryptanaAndroidMaterialTokens.LightComposerShelfGradient
  val specularColor = if (isDark) QryptanaAndroidMaterialTokens.DarkSpecularHairline else QryptanaAndroidMaterialTokens.LightSpecularHairline
  val borderColor = if (isDark) QryptanaAndroidMaterialTokens.DarkAppBarBorder else QryptanaAndroidMaterialTokens.LightAppBarBorder
  return this.drawWithContent {
    val brush = Brush.verticalGradient(gradient)
    drawRect(brush)
    // 1px structural top border
    drawLine(
      color = borderColor,
      start = Offset(0f, 0f),
      end = Offset(size.width, 0f),
      strokeWidth = 1f
    )
    // 1px specular highlight just below the border
    drawLine(
      color = specularColor,
      start = Offset(0f, 1f),
      end = Offset(size.width, 1f),
      strokeWidth = 1f
    )
    drawContent()
  }
}

/**
 * Recessed input well with milled pill geometry and subtle top drop shadow.
 */
fun Modifier.qryptanaInputWell(
  isDark: Boolean,
  shape: Shape = RoundedCornerShape(20.dp)
): Modifier {
  if (!appPlatform.isAndroid) return this
  val bgColor = if (isDark) QryptanaAndroidMaterialTokens.DarkInputWellBackground else QryptanaAndroidMaterialTokens.LightInputWellBackground
  val borderColor = if (isDark) QryptanaAndroidMaterialTokens.DarkInputWellBorder else QryptanaAndroidMaterialTokens.LightInputWellBorder
  val shadowColor = if (isDark) Color.Black.copy(alpha = 0.50f) else Color.Black.copy(alpha = 0.07f)

  return this
    .clip(shape)
    .background(bgColor)
    .border(1.dp, borderColor, shape)
    .drawWithContent {
      drawContent()
      // Subtle top recession shadow falloff
      val shadowHeight = 3.5f.dp.toPx()
      drawRect(
        brush = Brush.verticalGradient(
          colors = listOf(shadowColor, Color.Transparent),
          startY = 0f,
          endY = shadowHeight
        ),
        topLeft = Offset.Zero,
        size = Size(size.width, shadowHeight)
      )
    }
}

/**
 * Machined copper control (FAB, send button, primary CTA) with directional gradient,
 * crisp 1px border, and top specular highlight crown.
 */
fun Modifier.qryptanaCopperControl(
  enabled: Boolean = true,
  shape: Shape = CircleShape
): Modifier {
  if (!appPlatform.isAndroid) return this
  if (!enabled) {
    return this
      .clip(shape)
      .background(Color(0xFF59636C).copy(alpha = 0.45f))
      .border(1.dp, Color(0xFF68737C).copy(alpha = 0.40f), shape)
  }

  return this
    .clip(shape)
    .background(Brush.verticalGradient(colorStops = QryptanaAndroidMaterialTokens.CopperGradientStops))
    .border(1.dp, QryptanaAndroidMaterialTokens.CopperBorder, shape)
    .drawWithContent {
      drawContent()
      // Top specular crown highlight catching directional light
      val startX = size.width * 0.22f
      val endX = size.width * 0.78f
      drawLine(
        color = QryptanaAndroidMaterialTokens.CopperSpecular,
        start = Offset(startX, 1.dp.toPx()),
        end = Offset(endX, 1.dp.toPx()),
        strokeWidth = 1.5f
      )
    }
}
