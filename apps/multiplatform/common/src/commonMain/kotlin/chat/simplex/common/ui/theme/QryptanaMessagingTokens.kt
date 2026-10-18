package chat.simplex.common.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Messaging-only tokens derived from the active theme.
 *
 * These intentionally use MaterialTheme and AppColors rather than fixed Qryptana
 * colors so user-selected theme overrides continue to apply everywhere outside
 * the frozen onboarding experience.
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
fun qryptanaMessagingTokens(): QryptanaMessagingTokens = QryptanaMessagingTokens(
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
