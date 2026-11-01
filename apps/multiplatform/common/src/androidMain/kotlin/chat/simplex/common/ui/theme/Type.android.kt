package chat.simplex.common.ui.theme

import androidx.compose.ui.text.font.*
import chat.simplex.res.MR
import chat.simplex.res.inter_bold
import chat.simplex.res.inter_italic
import chat.simplex.res.inter_light
import chat.simplex.res.inter_medium
import chat.simplex.res.inter_regular
import chat.simplex.res.inter_semibold

actual val Inter: FontFamily = FontFamily(
  Font(MR.fonts.inter_regular.fontResourceId),
  Font(MR.fonts.inter_italic.fontResourceId, style = FontStyle.Italic),
  Font(MR.fonts.inter_bold.fontResourceId, FontWeight.Bold),
  Font(MR.fonts.inter_semibold.fontResourceId, FontWeight.SemiBold),
  Font(MR.fonts.inter_medium.fontResourceId, FontWeight.Medium),
  Font(MR.fonts.inter_light.fontResourceId, FontWeight.Light)
)

actual val EmojiFont: FontFamily = FontFamily.Default
