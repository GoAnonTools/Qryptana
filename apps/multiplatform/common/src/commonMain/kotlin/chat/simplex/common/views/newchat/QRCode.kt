package chat.simplex.common.views.newchat

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.border
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.sp
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import chat.simplex.common.views.onboarding.QryptanaMetal
import chat.simplex.common.views.onboarding.rememberQryptanaSerif
import dev.icerock.moko.resources.compose.painterResource
import dev.icerock.moko.resources.compose.stringResource
import boofcv.alg.drawing.FiducialImageEngine
import boofcv.alg.fiducial.qrcode.*
import chat.simplex.common.model.*
import chat.simplex.common.platform.*
import chat.simplex.common.ui.theme.*
import chat.simplex.common.views.helpers.*
import chat.simplex.res.MR
import kotlinx.coroutines.launch

@Composable
fun SimpleXCreatedLinkQRCode(
  connLink: CreatedConnLink,
  short: Boolean,
  modifier: Modifier = Modifier,
  padding: PaddingValues = PaddingValues(horizontal = DEFAULT_PADDING * 2f, vertical = DEFAULT_PADDING_HALF),
                             tintColor: Color = Color(0xff062d56),
                             withLogo: Boolean = true,
                             onShare: (() -> Unit)? = null,
) {
  QRCode(
    connLink.simplexChatUri(short),
         small = short && connLink.connShortLink != null,
         modifier,
         padding,
         tintColor,
         withLogo,
         branded = true,
         onShare,
  )
}

@Composable
fun SimpleXLinkQRCode(
  connReq: String,
  modifier: Modifier = Modifier,
  padding: PaddingValues = PaddingValues(horizontal = DEFAULT_PADDING * 2f, vertical = DEFAULT_PADDING_HALF),
                      tintColor: Color = Color(0xff062d56),
                      withLogo: Boolean = true,
                      onShare: (() -> Unit)? = null,
) {
  QRCode(
    simplexChatLink(connReq),
         small = connReq.count() < 200,
         modifier,
         padding,
         tintColor,
         withLogo,
         branded = true,
         onShare,
  )
}

@Composable
fun QRCode(
  connReq: String,
  small: Boolean = false,
  modifier: Modifier = Modifier,
  padding: PaddingValues = PaddingValues(horizontal = DEFAULT_PADDING * 2f, vertical = DEFAULT_PADDING_HALF),
           tintColor: Color = Color(0xff062d56),
           withLogo: Boolean = true,
           branded: Boolean = false,
           onShare: (() -> Unit)? = null,
) {
  val scope = rememberCoroutineScope()
  var revealed by remember(connReq) { mutableStateOf(false) }
  LaunchedEffect(connReq) {
    revealed = false
    kotlinx.coroutines.delay(620)
    revealed = true
  }
  val revealAlpha by animateFloatAsState(
    targetValue = if (revealed) 1f else 0f,
                                         animationSpec = tween(420, easing = FastOutSlowInEasing)
  )
  val foilTransition = rememberInfiniteTransition()
  val foilPulse by foilTransition.animateFloat(
    initialValue = 0.88f,
    targetValue = 1f,
    animationSpec = infiniteRepeatable(tween(1800, easing = FastOutSlowInEasing), RepeatMode.Reverse)
  )
  val logoSize = if (small) 0.21f else 0.16f
  val errorLevel = if (small) QrCode.ErrorLevel.M else QrCode.ErrorLevel.L
  // Desktop smart invite: modules are near-black and the light areas are multiplied onto an engraved copper plate
  val copperPlaque = branded && appPlatform.isDesktop
  val tintColor = if (copperPlaque) QryptanaMetal.void else tintColor
  val qr = remember(connReq, tintColor, withLogo) {
    qrCodeBitmap(connReq, 1024, errorLevel).replaceColor(Color.Black.toArgb(), tintColor.toArgb())
    .let { if (withLogo) it.addLogo(logoSize) else it }
  }
  val isLightTheme = MaterialTheme.colors.isLight
  if (copperPlaque) {
    QryptanaCopperPlaque(
      qr = qr,
      contentDescription = stringResource(MR.strings.image_descr_qr_code),
                         title = stringResource(MR.strings.smart_invite_title),
                         hint = stringResource(MR.strings.smart_invite_scan_hint),
                         revealAlpha = revealAlpha,
                         pulse = foilPulse,
                         onClick = {
                           scope.launch {
                             val image = qrCodeBitmap(connReq, 1024, errorLevel).replaceColor(Color.Black.toArgb(), tintColor.toArgb())
                             .let { if (withLogo) it.addLogo(logoSize) else it }
                             val file = saveTempImageUncompressed(image, true)
                             if (file != null) {
                               shareFile("", CryptoFile.plain(file.absolutePath))
                               onShare?.invoke()
                             }
                           }
                         }
    )
    return
  }
  Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
    val cardModifier = Modifier
    .padding(horizontal = if (branded) DEFAULT_PADDING else 0.dp)
    .then(
      if (branded) Modifier
        .clip(RoundedCornerShape(20.dp))
        .border(
          width = (1f + if (branded) foilPulse * 0.8f else 0f).dp,
                brush = Brush.sweepGradient(
                  0f to MaterialTheme.colors.primary.copy(alpha = 0.25f),
                                            0.25f to MaterialTheme.colors.primary.copy(alpha = 0.95f),
                                            0.5f to Color(0xff6fe7ff).copy(alpha = 0.75f),
                                            0.75f to Color(0xff9b7bff).copy(alpha = 0.8f),
                                            1f to MaterialTheme.colors.primary.copy(alpha = 0.25f),
                ),
                shape = RoundedCornerShape(20.dp)
        )
        else Modifier
    )
    Card(
      modifier = cardModifier,
      backgroundColor = if (branded) MaterialTheme.colors.surface else Color.Transparent,
         elevation = if (branded) 6.dp else 0.dp,
         shape = RoundedCornerShape(20.dp)
    ) {
      Column(horizontalAlignment = Alignment.CenterHorizontally) {
        if (branded) {
          Row(Modifier.padding(top = DEFAULT_PADDING_HALF), verticalAlignment = Alignment.CenterVertically) {
            Image(painterResource(MR.images.qryptana_symbol), stringResource(MR.strings.qryptana_logo_description), Modifier.size(24.dp))
            Spacer(Modifier.width(DEFAULT_PADDING_HALF))
            Text(stringResource(MR.strings.smart_invite_title), style = MaterialTheme.typography.subtitle1, color = MaterialTheme.colors.primary)
          }
        }
        Box(contentAlignment = Alignment.Center) {
          Canvas(
            Modifier
            .matchParentSize()
            .alpha(if (branded) (1f - revealAlpha) else 0f)
          ) {
            val privacyGlow = if (isLightTheme) Color(0xffe7f5ff) else Color(0xff15233d)
            drawRoundRect(
              color = privacyGlow,
              cornerRadius = androidx.compose.ui.geometry.CornerRadius(18.dp.toPx())
            )
          }
          Image(
            bitmap = qr,
            contentDescription = stringResource(MR.strings.image_descr_qr_code),
                Modifier
                .alpha(if (branded) revealAlpha else 1f)
                .padding(padding)
                .widthIn(max = 400.dp)
                .fillMaxWidth(if (small) 0.63f else 1f)
                .aspectRatio(1f)
                .then(modifier)
                .clickable {
                  scope.launch {
                    val image = qrCodeBitmap(connReq, 1024, errorLevel).replaceColor(Color.Black.toArgb(), tintColor.toArgb())
                    .let { if (withLogo) it.addLogo(logoSize) else it }
                    val file = saveTempImageUncompressed(image, true)
                    if (file != null) {
                      shareFile("", CryptoFile.plain(file.absolutePath))
                      onShare?.invoke()
                    }
                  }
                }
          )
        }
        if (branded) {
          Text(stringResource(MR.strings.smart_invite_scan_hint), Modifier.padding(bottom = DEFAULT_PADDING_HALF), color = MaterialTheme.colors.secondary)
        }
      }
    }
  }
}

@Composable
private fun QryptanaCopperPlaque(
  qr: ImageBitmap,
  contentDescription: String,
  title: String,
  hint: String,
  revealAlpha: Float,
  pulse: Float,
  onClick: () -> Unit,
) {
  val shape = RoundedCornerShape(18.dp)
  val copperEdge = Brush.sweepGradient(
    0f to QryptanaMetal.copperShadow.copy(alpha = 0.55f),
                                       0.20f to QryptanaMetal.copperLight.copy(alpha = 0.95f * pulse),
                                       0.38f to QryptanaMetal.copperSheen.copy(alpha = 0.90f * pulse),
                                       0.55f to QryptanaMetal.copperMid.copy(alpha = 0.60f),
                                       0.78f to QryptanaMetal.copperCore.copy(alpha = 0.85f * pulse),
                                       1f to QryptanaMetal.copperShadow.copy(alpha = 0.55f),
  )
  Box(Modifier.fillMaxWidth().padding(horizontal = DEFAULT_PADDING_HALF), contentAlignment = Alignment.Center) {
    Column(
      Modifier
      .widthIn(max = 380.dp)
      .fillMaxWidth()
      .clip(shape)
      .background(Brush.verticalGradient(listOf(QryptanaMetal.titaniumMid, QryptanaMetal.depth, QryptanaMetal.void)))
      .border(1.dp, copperEdge, shape)
      .drawBehind {
        // engraved corner ticks
        val inset = 9.dp.toPx(); val len = 11.dp.toPx(); val w = 1.dp.toPx()
        val c = QryptanaMetal.copperMid.copy(alpha = 0.55f)
        fun tick(x: Float, y: Float, dx: Float, dy: Float) {
          drawLine(c, Offset(x, y), Offset(x + dx * len, y), w)
          drawLine(c, Offset(x, y), Offset(x, y + dy * len), w)
        }
        tick(inset, inset, 1f, 1f)
        tick(size.width - inset, inset, -1f, 1f)
        tick(inset, size.height - inset, 1f, -1f)
        tick(size.width - inset, size.height - inset, -1f, -1f)
      }
      .padding(horizontal = 20.dp, vertical = 18.dp),
           horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Image(painterResource(MR.images.qryptana_symbol), stringResource(MR.strings.qryptana_logo_description), Modifier.size(20.dp))
        Spacer(Modifier.width(10.dp))
        Text(
          title.uppercase(),
             color = QryptanaMetal.copperLight,
             fontFamily = rememberQryptanaSerif(),
             fontSize = 15.sp,
             letterSpacing = 2.4.sp,
        )
      }
      Spacer(Modifier.height(14.dp))
      Canvas(
        Modifier
        .fillMaxWidth()
        .aspectRatio(1f)
        .alpha(revealAlpha)
        .clip(RoundedCornerShape(10.dp))
        .border(1.dp, QryptanaMetal.copperShadow.copy(alpha = 0.9f), RoundedCornerShape(10.dp))
        .semantics {
          this.contentDescription = contentDescription
          role = Role.Button
        }
        .clickable(onClick = onClick)
      ) {
        // polished copper plate
        drawRect(
          Brush.linearGradient(
            0f to QryptanaMetal.copperSheen,
            0.30f to QryptanaMetal.copperLight,
            0.65f to QryptanaMetal.copperMid,
            1f to Color(0xFFB07038),
                               start = Offset.Zero,
                               end = Offset(size.width, size.height)
          )
        )
        // soft brushed sheen
        drawRect(
          Brush.linearGradient(
            0.30f to Color.Transparent,
            0.42f to Color.White.copy(alpha = 0.16f),
                               0.54f to Color.Transparent,
                               start = Offset.Zero,
                               end = Offset(size.width, size.height)
          )
        )
        // quiet zone stays copper; dark modules are multiplied on top
        val inset = (size.width * 0.085f).toInt()
        val side = (size.width.toInt() - inset * 2).coerceAtLeast(1)
        drawImage(
          image = qr,
          srcOffset = IntOffset.Zero,
          srcSize = IntSize(qr.width, qr.height),
                  dstOffset = IntOffset(inset, inset),
                  dstSize = IntSize(side, side),
                  filterQuality = FilterQuality.None,
                  blendMode = BlendMode.Multiply
        )
      }
      Spacer(Modifier.height(14.dp))
      Text(
        hint,
        color = QryptanaMetal.textSupport,
        fontSize = 12.sp,
        lineHeight = 17.sp,
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(horizontal = 6.dp)
      )
    }
  }
}

fun qrCodeBitmap(content: String, size: Int = 1024, errorLevel: QrCode.ErrorLevel): ImageBitmap {
  val qrCode = QrCodeEncoder().addAutomatic(content).setError(errorLevel).fixate()
  /** See [QrCodeGeneratorImage.initialize] and [FiducialImageEngine.configure] for size calculation */
  val numModules = QrCode.totalModules(qrCode.version)
  // Hide border on light themes to make it fit to the same place as camera in QRCodeScanner.
  // On dark themes better to show the border
  val borderModule = if (CurrentColors.value.colors.isLight) 0 else 1
  // val calculatedFinalWidth = (pixelsPerModule * numModules) + 2 * (borderModule * pixelsPerModule)
  // size = (x * numModules) + 2 * (borderModule * x)
  // size / x = numModules + 2 * borderModule
  // x = size / (numModules + 2 * borderModule)
  val pixelsPerModule = size / (numModules + 2 * borderModule)
  // + 1 to make it with better quality
  val renderer = QrCodeGeneratorImage(pixelsPerModule + 1)
  renderer.borderModule = borderModule
  renderer.render(qrCode)
  return renderer.gray.toImageBitmap().scale(size, size)
}

expect fun ImageBitmap.replaceColor(from: Int, to: Int): ImageBitmap

@Preview
@Composable
fun PreviewQRCode() {
  SimpleXTheme {
    QRCode(connReq = "https://simplex.chat/contact#/?v=1&smp=smp%3A%2F%2FPQUV2eL0t7OStZOoAsPEV2QYWt4-xilbakvGUGOItUo%3D%40smp6.simplex.im%2FK1rslx-m5bpXVIdMZg9NLUZ_8JBm8xTt%23MCowBQYDK2VuAyEALDeVe-sG8mRY22LsXlPgiwTNs9dbiLrNuA7f3ZMAJ2w%3D")
  }
}
