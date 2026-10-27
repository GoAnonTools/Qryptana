package chat.simplex.common.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import chat.simplex.common.platform.appPlatform
import chat.simplex.common.views.onboarding.QryptanaMetal

/**
 * Messaging-only tokens derived from the active theme.
 *
 * These intentionally use MaterialTheme and AppColors rather than fixed Qryptana
 * colors so user-selected theme overrides continue to apply everywhere outside
 * the frozen onboarding experience.
 *
 * On desktop the black-titanium / copper-accent finish is fixed instead, so the
 * chat list, search, tags and conversation share one material.
 */
data class QryptanaMessagingTokens(
  val background: Color,
  val surface: Color,
  val elevatedSurface: Color,
  val sentMessage: Color,
  val receivedMessage: Color,
  val primaryText: Color,
  val secondaryText: Color,
  val accent: Color,
  val border: Color,
  val selectedSurface: Color,
  val inputSurface: Color,
  val inputBorder: Color,
  val placeholderText: Color,
  val disabledText: Color,
  val onAccent: Color,
  val rowHorizontalPadding: Dp = 12.dp,
  val rowVerticalPadding: Dp = 8.dp,
  val rowContentSpacing: Dp = 8.dp,
  val messageCornerRadius: Dp = 14.dp,
  val controlHeight: Dp = 44.dp,
)

@Composable
fun qryptanaMessagingTokens(): QryptanaMessagingTokens =
if (appPlatform.isDesktop) qryptanaDesktopTokens() else qryptanaThemeTokens()

  private fun qryptanaDesktopTokens(): QryptanaMessagingTokens = QryptanaMessagingTokens(
    background = QryptanaMetal.void,
    surface = QryptanaMetal.depth,
    elevatedSurface = QryptanaMetal.titaniumMid,
    sentMessage = Color(0xFF2E2A26),
                                                                                         receivedMessage = Color(0xFF1B1A18),
                                                                                         primaryText = QryptanaMetal.textBright,
                                                                                         secondaryText = QryptanaMetal.textTertiary,
                                                                                         accent = QryptanaMetal.copperLight,
                                                                                         border = QryptanaMetal.titaniumBorder.copy(alpha = 0.45f),
                                                                                         selectedSurface = QryptanaMetal.titaniumHigh.copy(alpha = 0.55f),
                                                                                         inputSurface = Color(0xFF121110),
                                                                                         inputBorder = QryptanaMetal.titaniumBorder.copy(alpha = 0.7f),
                                                                                         placeholderText = QryptanaMetal.textTertiary.copy(alpha = 0.85f),
                                                                                         disabledText = QryptanaMetal.textQuiet,
                                                                                         onAccent = QryptanaMetal.void,
  )

    @Composable
    private fun qryptanaThemeTokens(): QryptanaMessagingTokens = QryptanaMessagingTokens(
      background = MaterialTheme.colors.background,
      surface = MaterialTheme.colors.surface,
      elevatedSurface = MaterialTheme.appColors.primaryVariant2,
      sentMessage = MaterialTheme.appColors.sentMessage,
      receivedMessage = MaterialTheme.appColors.receivedMessage,
      primaryText = MaterialTheme.colors.onBackground,
      secondaryText = MaterialTheme.colors.secondary,
      accent = MaterialTheme.colors.primary,
      border = MaterialTheme.colors.secondaryVariant,
      selectedSurface = MaterialTheme.colors.onBackground.copy(alpha = 0.05f),
                                                                                         inputSurface = MaterialTheme.colors.surface,
                                                                                         inputBorder = MaterialTheme.colors.secondaryVariant,
                                                                                         placeholderText = MaterialTheme.colors.onSurface.copy(alpha = 0.60f),
                                                                                         disabledText = MaterialTheme.colors.onSurface.copy(alpha = 0.38f),
                                                                                         onAccent = MaterialTheme.colors.onPrimary,
    )
