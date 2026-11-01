package chat.simplex.common.ui.theme

import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.colorspace.ColorSpaces
import kotlin.math.cos
import kotlin.math.sin

fun oklch(L: Float, C: Float, H: Float, alpha: Float = 1f): Color {
  val hRad = H * (Math.PI.toFloat() / 180f)
  return Color(L, C * cos(hRad), C * sin(hRad), alpha, ColorSpaces.Oklab)
}

val Indigo = Color(0xFF9966FF)
val QryptanaCopper = Color(0xFFC8794B)
val QryptanaCopperLight = Color(0xFFE0A06F)
val QryptanaCopperDark = Color(0xFFB95F3A)
val QryptanaDarkBackground = Color(0xFF171615)
val QryptanaDarkSurface = Color(0xFF20201E)
val QryptanaDarkCard = Color(0xFF292623)
val QryptanaBlackBackground = Color(0xFF10100F)
val QryptanaDarkText = Color(0xFFF5EFE7)
val QryptanaDarkSecondary = Color(0xFFB8B0A7)
val QryptanaDarkBorder = Color(0xFF3A3530)
val QryptanaLightBackground = Color(0xFFF7F0E6)
val QryptanaLightSurface = Color(0xFFFFF8EF)
val QryptanaLightCard = Color(0xFFFFFBF5)
val QryptanaLightText = Color(0xFF24201D)
val QryptanaLightSecondary = Color(0xFF6E655D)

/**
 * Android's semantic Qryptana roles. Material 2 has fewer slots than the
 * design language needs, so these names are the source of truth and the
 * palette factories below map them onto Material and app-specific colours.
 */
internal data class QryptanaAndroidColorRoles(
  val actionText: Color,
  val filledControl: Color,
  val onFilledControl: Color,
  val secondaryText: Color,
  val structuralBorder: Color,
  val background: Color,
  val surface: Color,
  val raisedSurface: Color,
  val heading: Color,
  val body: Color,
  val error: Color,
  val onError: Color,
  val sentMessage: Color,
  val sentQuote: Color,
  val receivedMessage: Color,
  val receivedQuote: Color,
  val neutralIcon: Color,
  val copperShadow: Color,
  val copperBody: Color,
  val copperHighlight: Color,
)

internal object QryptanaAndroidThemeRoles {
  val light = QryptanaAndroidColorRoles(
    actionText = Color(0xFF80513E),
    filledControl = Color(0xFF9F634C),
    onFilledControl = Color(0xFFFFFFFF),
    secondaryText = Color(0xFF59636C),
    structuralBorder = Color(0xFF7C858D),
    background = Color(0xFFF1F3F5),
    surface = Color(0xFFFCFDFE),
    raisedSurface = Color(0xFFFFFFFF),
    heading = Color(0xFF20262B),
    body = Color(0xFF30383F),
    error = Color(0xFFB3261E),
    onError = Color.White,
    sentMessage = Color(0xFFF1DFD3),
    sentQuote = Color(0xFFE7C6B0),
    receivedMessage = Color(0xFFFFFBF7),
    receivedQuote = Color(0xFFE9E2DB),
    neutralIcon = Color(0xFF68737C),
    copperShadow = Color(0xFF6A3E32),
    copperBody = Color(0xFFAD7257),
    copperHighlight = Color(0xFFF0D4C4),
  )

  val dark = QryptanaAndroidColorRoles(
    actionText = Color(0xFFD3A78F),
    filledControl = Color(0xFF9F634C),
    onFilledControl = Color(0xFFFFFFFF),
    secondaryText = Color(0xFF9AA5AE),
    structuralBorder = Color(0xFF68737C),
    background = Color(0xFF0E1114),
    surface = Color(0xFF191E23),
    raisedSurface = Color(0xFF20272D),
    heading = Color(0xFFEEF1F3),
    body = Color(0xFFEEF1F3),
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    sentMessage = Color(0xFF3A2B24),
    sentQuote = Color(0xFF4A3428),
    receivedMessage = Color(0xFF2B2926),
    receivedQuote = Color(0xFF3A3733),
    neutralIcon = Color(0xFFAEB8C0),
    copperShadow = Color(0xFF6A3E32),
    copperBody = Color(0xFFAD7257),
    copperHighlight = Color(0xFFF0D4C4),
  )

  // The named SimpleX preset remains available, now as a cooler titanium
  // variant rather than a saturated blue theme.
  val simplex = QryptanaAndroidColorRoles(
    actionText = Color(0xFFD3A78F),
    filledControl = Color(0xFF9F634C),
    onFilledControl = Color(0xFFFFFFFF),
    secondaryText = Color(0xFF9AA5AE),
    structuralBorder = Color(0xFF68737C),
    background = Color(0xFF0E1114),
    surface = Color(0xFF191E23),
    raisedSurface = Color(0xFF20272D),
    heading = Color(0xFFEEF1F3),
    body = Color(0xFFEEF1F3),
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    sentMessage = Color(0xFF222A30),
    sentQuote = Color(0xFF303A42),
    receivedMessage = Color(0xFF292C2F),
    receivedQuote = Color(0xFF383C40),
    neutralIcon = Color(0xFFAEB8C0),
    copperShadow = Color(0xFF6A3E32),
    copperBody = Color(0xFFAD7257),
    copperHighlight = Color(0xFFF0D4C4),
  )

  val black = QryptanaAndroidColorRoles(
    actionText = Color(0xFFD3A78F),
    filledControl = Color(0xFF9F634C),
    onFilledControl = Color(0xFFFFFFFF),
    secondaryText = Color(0xFF9AA5AE),
    structuralBorder = Color(0xFF68737C),
    background = Color(0xFF090C0F),
    surface = Color(0xFF151A1F),
    raisedSurface = Color(0xFF1C2329),
    heading = Color(0xFFEEF1F3),
    body = Color(0xFFEEF1F3),
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    sentMessage = Color(0xFF20272D),
    sentQuote = Color(0xFF2D353C),
    receivedMessage = Color(0xFF151A1F),
    receivedQuote = Color(0xFF252C32),
    neutralIcon = Color(0xFFAEB8C0),
    copperShadow = Color(0xFF6A3E32),
    copperBody = Color(0xFFAD7257),
    copperHighlight = Color(0xFFF0D4C4),
  )
}
// Verified in the pre-rebrand Color.kt at 423c8c0; mobile palette accents.
val UpstreamSimplexBlue = Color(0xFF0088FF)
// Kept under its compatibility name because other visual helpers still reference it.
val SimplexBlue = QryptanaCopper
val SimplexGreen = Color(77, 218, 103, 255)
val SecretColor = Color(0x40808080)
val LightGray = Color(241, 242, 246, 255)
val DarkGray = Color(43, 44, 46, 255)
val HighOrLowlight = Color(139, 135, 134, 255)
val MessagePreviewDark = Color(179, 175, 174, 255)
val MessagePreviewLight = Color(49, 45, 44, 255)
val ToolbarLight = Color(220, 220, 220, 12)
val ToolbarDark = Color(80, 80, 80, 12)
val SettingsSecondaryLight = Color(200, 196, 195, 90)
val GroupDark = Color(80, 80, 80, 60)
val IncomingCallLight = Color(239, 237, 236, 255)
val WarningOrange = Color(255, 127, 0, 255)
val WarningYellow = Color(255, 192, 0, 255)
val FileLight = Color(191, 194, 199, 255)
val FileDark = Color(94, 94, 98, 255)

val MenuTextColor: Color @Composable get () = if (isInDarkTheme()) LocalContentColor.current.copy(alpha = 0.8f) else Color.Black
val NoteFolderIconColor: Color @Composable get() = MaterialTheme.appColors.primaryVariant2
