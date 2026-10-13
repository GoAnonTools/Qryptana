package chat.simplex.common.views.onboarding

import androidx.compose.animation.core.*
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import dev.icerock.moko.resources.compose.painterResource
import dev.icerock.moko.resources.compose.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import chat.simplex.common.BuildConfigCommon
import chat.simplex.common.model.*
import chat.simplex.common.model.ChatController.appPrefs
import chat.simplex.common.platform.*
import chat.simplex.common.ui.theme.*
import chat.simplex.common.views.helpers.*
import chat.simplex.common.views.newchat.darkStops
import chat.simplex.common.views.newchat.gradientPoints
import chat.simplex.common.views.newchat.lightStops
import chat.simplex.common.views.migration.MigrateToDeviceView
import chat.simplex.common.views.migration.MigrationToState
import chat.simplex.res.MR
import dev.icerock.moko.resources.StringResource
import kotlinx.coroutines.delay
import kotlin.math.ceil
import kotlin.math.floor

@Composable
fun SimpleXInfo(chatModel: ChatModel, onboarding: Boolean = true) {
  if (onboarding) {
    if (appPlatform.isDesktop) {
      SimpleXInfoDesktop(chatModel)
    } else {
      // Visual-only change from the original: no longer wrapped in
      // ModalView({}, showClose = false, showAppBar = false). That wrapper's
      // own surface background and default padding/elevation were fighting
      // the full-bleed premium background, which is the "wrapper not
      // bypassed" bug. Everything else about this branch — no migration
      // check, same CompositionLocalProvider — is unchanged from before.
      CompositionLocalProvider(LocalAppBarHandler provides rememberAppBarHandler()) {
        QryptanaOnboardingLayout(
          user = chatModel.currentUser.value,
          onboardingStage = chatModel.controller.appPrefs.onboardingStage
        )
      }
    }
  } else {
    SimpleXInfoLayout(
      user = chatModel.currentUser.value,
      onboardingStage = null
    )
  }
}

@Composable
private fun SimpleXInfoDesktop(chatModel: ChatModel) {
  val user = chatModel.currentUser.value
  val onboardingStage = chatModel.controller.appPrefs.onboardingStage
  CompositionLocalProvider(LocalAppBarHandler provides rememberAppBarHandler()) {
    QryptanaOnboardingLayout(user, onboardingStage)
  }
  LaunchedEffect(Unit) {
    if (chatModel.migrationState.value != null && !ModalManager.fullscreen.hasModalsOpen()) {
      ModalManager.fullscreen.showCustomModal(animated = false) { close -> MigrateToDeviceView(close) }
    }
  }
}

/**
 * The desktop first-run screen is intentionally a single composition. It does
 * not use the legacy onboarding modal or illustration/content containers.
 * Mobile now shares the same unwrapped composition (see SimpleXInfo above)
 * but keeps its own original behavior otherwise — in particular it still has
 * no migration-check effect, exactly as before.
 */
/**
 * Screen-scoped premium palette for the Qryptana first-launch experience.
 *
 * Deliberately NOT sourced from the app-wide QryptanaCopper/QryptanaDark*
 * theme tokens: those resolve to a flat, saturated orange elsewhere in the
 * app, which is correct for small UI accents but reads as "bright orange
 * UI" at the scale of a hero card or primary button. Real metal is layered
 * and desaturated — these values model that directly for this screen only.
 */
private object QryptanaMetal {
  val void = Color(0xFF0B0B0A)
  val depth = Color(0xFF131211)
  val cardHigh = Color(0xFF232019)
  val cardMid = Color(0xFF1C1915)
  val cardLow = Color(0xFF18150F)

  // Titanium: the dominant surface material for controls. Neutral graphite
  // greys, not warm brown — copper stays reserved for thin edges/engraving.
  val titaniumHigh = Color(0xFF34322F)
  val titaniumMid = Color(0xFF23221F)
  val titaniumLow = Color(0xFF151413)
  val titaniumBorder = Color(0xFF45423F)

  // Neutral studio light — the reflection colour. Never tinted orange.
  val studio = Color(0xFF5A5651)
  val studioSoft = Color(0xFF3A3733)

  // Cool titanium light: used only for the ambient glow behind the wordmark,
  // so the metal reads as lit by a cool source (not a warm/orange wash).
  val titaniumGlow = Color(0xFF8A96A6)

  // Copper: accent only — engraving and reflection, never a filled surface.
  val copperShadow = Color(0xFF6B4828)
  val copperCore = Color(0xFFA8692D)
  val copperMid = Color(0xFFC4844A)
  val copperLight = Color(0xFFD9A878)
  val copperSheen = Color(0xFFEBC9A4)

  val textBright = Color(0xFFEDE8E2)
  val textHero = Color(0xFFF4F0EA)
  val textSupport = Color(0xFFB9B1A9)
  val textSoft = Color(0xFF9A9088)
  val textTertiary = Color(0xFF8F8780)
  val textQuiet = Color(0xFF746C65)
}

@Composable
private fun QryptanaOnboardingLayout(
  user: User?,
  onboardingStage: SharedPreference<OnboardingStage>
) {
  // Layered graphite: a long, gentle vertical falloff with a lifted middle
  // band so the stage behind the wordmark has somewhere to catch light.
  val gradient = Brush.verticalGradient(
    colorStops = arrayOf(
      0.0f to QryptanaMetal.void,
      0.18f to Color(0xFF12110F),
                         0.46f to QryptanaMetal.depth,
                         0.78f to Color(0xFF181715),
                         1.0f to Color(0xFF0F0E0D)
    )
  )
  Box(
    Modifier
    .fillMaxSize()
    .background(gradient)
    .drawBehind {
      // Key light: a wide, neutral studio glow above the wordmark. Not
      // coloured — this is what lets brushed metal read as metal.
      drawRect(
        Brush.radialGradient(
          colors = listOf(QryptanaMetal.studioSoft.copy(alpha = 0.30f), Color.Transparent),
                             center = Offset(size.width / 2f, size.height * 0.20f),
                             radius = size.minDimension * 0.78f
        )
      )
      // Brushed titanium: an anisotropic diagonal sheen across the whole
      // surface, so the light isn't uniform (uniform = flat black).
      drawRect(
        Brush.linearGradient(
          colors = listOf(
            Color.Transparent,
            QryptanaMetal.studioSoft.copy(alpha = 0.10f),
                          Color.Transparent,
                          QryptanaMetal.studioSoft.copy(alpha = 0.05f),
                          Color.Transparent
          ),
          start = Offset(0f, 0f),
                             end = Offset(size.width, size.height)
        )
      )
      // Light falloff: dark corners, so the centre reads as lit hardware.
      // (The faint copper environment tint that used to sit before this
      // was removed: it only contributed a warm-brown wash.)
      drawRect(
        Brush.radialGradient(
          colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.50f)),
                             center = Offset(size.width / 2f, size.height * 0.42f),
                             radius = size.maxDimension * 0.76f
        )
      )
      // Brushed grain: ultra-fine horizontal lines, barely there. Felt more
      // than seen — it is what removes the "flat digital black" look.
      val step = 3.dp.toPx()
      var y = 0f
      var i = 0
      while (y < size.height) {
        if (i % 2 == 0) {
          drawLine(
            color = Color.White.copy(alpha = 0.011f),
                   start = Offset(0f, y),
                   end = Offset(size.width, y),
                   strokeWidth = 1f
          )
        }
        y += step
        i++
      }
    }
    .systemBarsPadding(),
      contentAlignment = Alignment.Center
  ) {
    Column(
      Modifier
      .fillMaxWidth()
      .verticalScroll(rememberScrollState())
      .padding(horizontal = DEFAULT_ONBOARDING_HORIZONTAL_PADDING, vertical = DEFAULT_PADDING),
           horizontalAlignment = Alignment.CenterHorizontally,
           verticalArrangement = Arrangement.Center
    ) {
      QryptanaWelcomeContent(user, onboardingStage, Modifier.widthIn(max = 620.dp).fillMaxWidth())
    }
  }
}

@Composable
fun SimpleXInfoLayout(
  user: User?,
  onboardingStage: SharedPreference<OnboardingStage>?
) {
  val topBar = onboardingStage == null && !appPrefs.oneHandUI.state.value
  val gradient = if (isInDarkTheme()) {
    Brush.verticalGradient(listOf(QryptanaDarkBackground, Color(0xFF24201E)))
  } else {
    Brush.verticalGradient(listOf(Color(0xFFF8F1E8), Color(0xFFEFE2D2)))
  }
  Box(Modifier.fillMaxSize().background(gradient).systemBarsPadding()) {
    Column(
      Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal = DEFAULT_ONBOARDING_HORIZONTAL_PADDING),
           horizontalAlignment = Alignment.CenterHorizontally,
           verticalArrangement = Arrangement.Center
    ) {
      if (topBar) Spacer(Modifier.height(AppBarHeight * fontSizeSqrtMultiplier))
        QryptanaWelcomeContent(user, onboardingStage, Modifier.widthIn(max = 560.dp).fillMaxWidth())
        if (topBar) Spacer(Modifier.height(DEFAULT_PADDING))
    }
  }
  LaunchedEffect(Unit) {
    if (chatModel.migrationState.value != null && !ModalManager.fullscreen.hasModalsOpen()) {
      ModalManager.fullscreen.showCustomModal(animated = false) { close -> MigrateToDeviceView(close) }
    }
  }
}

/**
 * One-shot, slow fade-in. Opacity only — no movement, no scale — so the
 * screen reads as "introducing itself" rather than "loading".
 */
@Composable
private fun rememberReveal(delayMs: Int, durationMs: Int): Animatable<Float, AnimationVector1D> {
  val reveal = remember { Animatable(0f) }
  LaunchedEffect(Unit) {
    delay(delayMs.toLong())
    reveal.animateTo(1f, tween(durationMs, easing = LinearOutSlowInEasing))
  }
  return reveal
}

/**
 * The signature word of the hero title. Letters resolve one after another
 * (staggered opacity across the word, plus a blur that clears as it
 * completes) — a progressive reveal, not a typing effect: every glyph keeps
 * its layout slot from the first frame, so nothing jumps. Each letter's tone
 * moves from copper-light toward a paler sheen across the word. The final
 * state is fully static: no blur modifier, no animation running.
 */
@Composable
private fun QryptanaSignatureWord(text: String, delayMs: Int) {
  val progress = remember { Animatable(0f) }
  LaunchedEffect(Unit) {
    delay(delayMs.toLong())
    progress.animateTo(1f, tween(1500, easing = LinearEasing))
  }
  val p = progress.value
  val n = text.length.coerceAtLeast(1)
  val annotated = buildAnnotatedString {
    text.forEachIndexed { i, ch ->
      val pos = if (n > 1) i.toFloat() / (n - 1) else 0f
      val start = pos * 0.55f
      val a = FastOutSlowInEasing.transform(((p - start) / 0.45f).coerceIn(0f, 1f))
      val tone = lerp(QryptanaMetal.copperLight, QryptanaMetal.copperSheen, pos * 0.55f)
      withStyle(SpanStyle(color = tone.copy(alpha = a))) { append(ch) }
    }
  }
  Text(
    annotated,
    Modifier.then(
      if (p < 1f) Modifier.blur((1f - p) * 5.dp, BlurredEdgeTreatment.Unbounded) else Modifier
    ),
    style = MaterialTheme.typography.h1,
    fontFamily = FontFamily.Serif,
    fontStyle = FontStyle.Italic,
    fontWeight = FontWeight.Normal,
    fontSize = 38.sp,
    letterSpacing = 1.sp,
    lineHeight = 48.sp,
    textAlign = TextAlign.Center
  )
}

/**
 * Launch statement. Line one is refined, bright serif light and fades in
 * softly. Line two is the signature word: italic serif, copper-toned,
 * revealed letter by letter after a short pause, visually separated from
 * line one by extra space and a larger size.
 */
@Composable
private fun QryptanaHeroTitle(text: String) {
  val lines = if ('\n' in text) {
    text.split('\n', limit = 2)
  } else {
    // Fallback if the resource has no explicit line break.
    text.split(", ", limit = 2).let { if (it.size == 2) listOf(it[0] + ",", it[1]) else it }
  }.map { it.trim() }
  val first = rememberReveal(delayMs = 300, durationMs = 800)
  Column(horizontalAlignment = Alignment.CenterHorizontally) {
    Text(
      lines.firstOrNull().orEmpty(),
         Modifier.graphicsLayer { alpha = first.value },
         style = MaterialTheme.typography.h1,
         fontFamily = FontFamily.Serif,
         fontWeight = FontWeight.Light,
         fontSize = 31.sp,
         letterSpacing = 0.5.sp,
         lineHeight = 40.sp,
         textAlign = TextAlign.Center,
         color = QryptanaMetal.textHero
    )
    if (lines.size > 1) {
      Spacer(Modifier.height(8.dp))
      QryptanaSignatureWord(lines[1], delayMs = 1000)
    }
  }
}

@Composable
private fun QryptanaWelcomeContent(
  user: User?,
  onboardingStage: SharedPreference<OnboardingStage>?,
  modifier: Modifier = Modifier
) {
  val logoReveal = rememberReveal(delayMs = 0, durationMs = 1100)
  val supportReveal = rememberReveal(delayMs = 1500, durationMs = 900)
  val actionsReveal = rememberReveal(delayMs = 1750, durationMs = 900)
  Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {
    // 1. Wordmark: a floating metal object in darkness. No divider lines —
    // just a cool titanium pool of light behind the letters (an elliptical
    // wash plus a tighter core) so the dark metallic letterforms separate
    // from the background. The asset is untouched and untinted, so the
    // copper reflection on the Q is exactly as authored.
    Box(
      Modifier
      .widthIn(max = 320.dp)
      .fillMaxWidth(0.84f)
      .graphicsLayer { alpha = logoReveal.value }
      .drawBehind {
        val c = Offset(size.width / 2f, size.height / 2f)
        withTransform({
          scale(1f, 0.5f, pivot = c)
        }) {
          drawCircle(
            brush = Brush.radialGradient(
              colors = listOf(
                QryptanaMetal.titaniumGlow.copy(alpha = 0.30f),
                              QryptanaMetal.titaniumGlow.copy(alpha = 0.10f),
                              Color.Transparent
              ),
              center = c,
              radius = size.width * 0.62f
            ),
            radius = size.width * 0.62f,
            center = c
          )
        }
        // Tight core directly behind the letterforms.
        drawCircle(
          brush = Brush.radialGradient(
            colors = listOf(QryptanaMetal.titaniumGlow.copy(alpha = 0.13f), Color.Transparent),
                                       center = c,
                                       radius = size.width * 0.28f
          ),
          radius = size.width * 0.28f,
          center = c
        )
      }
      .padding(vertical = 20.dp),
        contentAlignment = Alignment.Center
    ) {
      QryptanaWordmark(Modifier.fillMaxWidth(), lightOnDark = true)
    }
    Spacer(Modifier.height(46.dp))

    // 2. Identity / invitation object (placeholder, unchanged design).
    InvitationPreviewCard()
    Spacer(Modifier.height(58.dp))

    // 3. Launch statement — staged reveal.
    QryptanaHeroTitle(stringResource(MR.strings.onboarding_be_free))
    Spacer(Modifier.height(26.dp))

    // Supporting copy fades in after the statement has landed.
    Column(
      Modifier.graphicsLayer { alpha = supportReveal.value },
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      // A single engraved copper hairline: a quiet editorial divider.
      Box(
        Modifier
        .width(36.dp)
        .height(1.dp)
        .background(
          Brush.horizontalGradient(
            listOf(Color.Transparent, QryptanaMetal.copperMid.copy(alpha = 0.7f), Color.Transparent)
          )
        )
      )
      Spacer(Modifier.height(24.dp))
      Text(
        stringResource(MR.strings.onboarding_private_and_secure),
           Modifier.widthIn(max = 480.dp),
           style = MaterialTheme.typography.h3,
           color = QryptanaMetal.textBright.copy(alpha = 0.92f),
           fontWeight = FontWeight.Normal,
           fontSize = 18.sp,
           letterSpacing = 0.1.sp,
           lineHeight = 28.sp,
           textAlign = TextAlign.Center
      )
      // The privacy promise: readable at normal desktop size, warm grey,
      // never white — said quietly but clearly. Regular weight (not Light)
      // so the thin strokes don't dissolve on near-black.
      Text(
        stringResource(MR.strings.onboarding_first_network),
           Modifier.widthIn(max = 460.dp).padding(top = 14.dp),
           style = MaterialTheme.typography.body2,
           color = QryptanaMetal.textSupport,
           fontWeight = FontWeight.Normal,
           fontSize = 16.sp,
           letterSpacing = 0.1.sp,
           textAlign = TextAlign.Center,
           lineHeight = 25.sp
      )
    }
    Spacer(Modifier.height(46.dp))

    // 4. Actions.
    if (onboardingStage != null) {
      Column(
        Modifier.graphicsLayer { alpha = actionsReveal.value },
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        OnboardingActionButton(user, onboardingStage)
        TextButtonBelowOnboardingButton(
          stringResource(MR.strings.how_it_works),
                                        onClick = { ModalManager.fullscreen.showModal { HowItWorks(user, onboardingStage) } },
                                        color = QryptanaMetal.textTertiary
        )
      }
    }
  }
}

@Composable
private fun InvitationPreviewCard() {
  val transition = rememberInfiniteTransition()
  val floatOffset by transition.animateFloat(
    initialValue = -3f,
    targetValue = 3f,
    animationSpec = infiniteRepeatable(
      animation = tween(3600, easing = FastOutSlowInEasing),
                                       repeatMode = RepeatMode.Reverse
    )
  )
  val cardShape = RoundedCornerShape(24.dp)
  Box(
    contentAlignment = Alignment.Center,
    modifier = Modifier
    .padding(horizontal = 24.dp)
    .drawBehind {
      // Halo behind the card: reduced to a trace so it never reads as an
      // orange glow.
      drawCircle(
        brush = Brush.radialGradient(
          colors = listOf(QryptanaMetal.copperCore.copy(alpha = 0.045f), Color.Transparent),
                                     center = Offset(size.width / 2f, size.height / 2f),
                                     radius = size.maxDimension * 0.76f
        ),
        radius = size.maxDimension * 0.76f,
        center = Offset(size.width / 2f, size.height / 2f)
      )
    }
  ) {
    Box(
      Modifier
      .widthIn(max = 480.dp)
      .fillMaxWidth(0.96f)
      .height(268.dp)
      .offset(y = floatOffset.dp)
      .shadow(34.dp, cardShape, clip = false)
      .clip(cardShape)
      .background(
        // A three-stop diagonal gradient, not a flat fill: this is what
        // reads as a real graphite surface catching light from one corner
        // rather than a flat-colored card.
        Brush.linearGradient(
          colors = listOf(QryptanaMetal.cardHigh, QryptanaMetal.cardMid, QryptanaMetal.cardLow),
                             start = Offset.Zero,
                             end = Offset(420f, 240f)
        )
      )
      .border(
        1.dp,
        // The edge catches light like real metal: brightest where the
        // light source is, fading to nothing — not a uniform stroke.
        Brush.linearGradient(
          colors = listOf(
            QryptanaMetal.copperMid.copy(alpha = 0.75f),
                          QryptanaMetal.copperMid.copy(alpha = 0.15f),
                          Color.Transparent
          ),
          start = Offset.Zero,
          end = Offset(420f, 300f)
        ),
        cardShape
      )
      .padding(horizontal = 38.dp, vertical = 34.dp)
    ) {
      Box(Modifier.fillMaxSize()) {
        Box(
          Modifier
          .align(Alignment.TopEnd)
          .size(78.dp)
          .background(
            Brush.radialGradient(listOf(QryptanaMetal.copperCore.copy(alpha = 0.16f), Color.Transparent)),
                      CircleShape
          )
        )
        Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
              painter = painterResource(MR.images.qryptana_symbol),
                  contentDescription = stringResource(MR.strings.qryptana_logo_description),
                  contentScale = ContentScale.Fit,
                  modifier = Modifier.size(74.dp)
            )
            Spacer(Modifier.width(20.dp))
            Column {
              Text(
                stringResource(MR.strings.onboarding_invitation_card_title),
                   color = QryptanaMetal.textBright,
                   style = MaterialTheme.typography.h3,
                   fontSize = 21.sp,
                   fontWeight = FontWeight.Medium
              )
              Text(
                stringResource(MR.strings.onboarding_invitation_card_subtitle),
                   color = QryptanaMetal.textSoft,
                   style = MaterialTheme.typography.body2,
                   modifier = Modifier.padding(top = 3.dp)
              )
            }
          }
          Text(
            "A private way to connect",
            color = QryptanaMetal.copperLight.copy(alpha = 0.9f),
               style = MaterialTheme.typography.body2,
               fontWeight = FontWeight.Medium
          )
        }
      }
    }
  }
}

@Composable
fun QryptanaWordmark(modifier: Modifier = Modifier, lightOnDark: Boolean = false) {
  // Single asset, no runtime recolor: qryptana_wordmark.png carries its own
  // metallic bevel/sheen shading on the dark letterforms and reads correctly
  // against the charcoal background as-is. A BlendMode.SrcIn tint here would
  // flatten that shading (and the copper Q) to one flat color, which is the
  // bug this used to have. `lightOnDark` is kept in the signature for API
  // compatibility with existing call sites but no longer changes rendering.
  Image(
    painter = painterResource(MR.images.qryptana_wordmark),
        contentDescription = stringResource(MR.strings.qryptana_logo_description),
        contentScale = ContentScale.Fit,
        modifier = modifier.padding(bottom = DEFAULT_PADDING_HALF)
  )
}

@Composable
fun QryptanaSymbol() {
  Image(
    painter = painterResource(MR.images.qryptana_symbol),
        contentDescription = stringResource(MR.strings.qryptana_logo_description),
        contentScale = ContentScale.Fit,
        modifier = Modifier.size(120.dp)
  )
}

@Composable
expect fun OnboardingActionButton(user: User?, onboardingStage: SharedPreference<OnboardingStage>, onclick: (() -> Unit)? = null)

@Composable
fun OnboardingActionButton(
  modifier: Modifier = Modifier,
  labelId: StringResource,
  onboarding: OnboardingStage?,
  enabled: Boolean = true,
  icon: Painter? = null,
  iconColor: Color = Color.White, // kept for API compatibility; unused, matching the original implementation
  secondary: Boolean = false,
  onclick: (() -> Unit)?
) {
  // Refined rounded shape, not a full pill — a pill reads as generic app
  // chrome at this size; a shallower radius reads as a deliberately designed
  // control.
  val shape = RoundedCornerShape(14.dp)
  val interactionSource = remember { MutableInteractionSource() }
  val pressed by interactionSource.collectIsPressedAsState()
  val pressScale by animateFloatAsState(if (pressed) 0.985f else 1f, animationSpec = tween(120))

  Box(
    modifier
    .widthIn(min = 300.dp, max = 390.dp)
    .height(56.dp)
    .graphicsLayer { scaleX = pressScale; scaleY = pressScale }
    .then(
      if (secondary) {
        // Deliberately quiet: no drop shadow, no copper, a faint translucent
        // titanium body and a low-contrast neutral hairline. It reads as
        // available, never as competing with the primary action.
        Modifier
        .clip(shape)
        .background(
          Brush.verticalGradient(
            colors = listOf(
              QryptanaMetal.titaniumHigh.copy(alpha = if (pressed) 0.30f else 0.16f),
                            QryptanaMetal.titaniumLow.copy(alpha = 0.10f)
            )
          )
        )
        .border(
          1.dp,
          Brush.verticalGradient(
            colors = listOf(
              QryptanaMetal.titaniumBorder.copy(alpha = if (pressed) 0.55f else 0.38f),
                            QryptanaMetal.titaniumBorder.copy(alpha = 0.20f)
            )
          ),
          shape
        )
      } else {
        Modifier
        .shadow(if (pressed) 5.dp else 18.dp, shape, clip = false)
        .clip(shape)
        .background(
          // Titanium body: lit from above, falling into shadow.
          Brush.verticalGradient(
            colors = listOf(
              QryptanaMetal.titaniumHigh,
              QryptanaMetal.titaniumMid,
              QryptanaMetal.titaniumLow
            )
          )
        )
        // Copper edge reflection: the third-brightest thing on screen.
        // Clearly visible on the lit top edge, falling off along the
        // sides — an engraved edge catching light, not a filled surface.
        .border(
          1.dp,
          Brush.verticalGradient(
            colors = listOf(
              QryptanaMetal.copperLight.copy(alpha = if (pressed) 0.70f else 0.88f),
                            QryptanaMetal.copperMid.copy(alpha = 0.30f),
                            QryptanaMetal.copperShadow.copy(alpha = 0.16f)
            )
          ),
          shape
        )
        .drawBehind {
          // Machined top highlight: a neutral sliver of studio light.
          drawLine(
            brush = Brush.horizontalGradient(
              listOf(Color.Transparent, QryptanaMetal.studio.copy(alpha = 0.70f), Color.Transparent)
            ),
            start = Offset(size.width * 0.10f, 2f),
                   end = Offset(size.width * 0.90f, 2f),
                   strokeWidth = 1.5f
          )
          // Soft inner shade along the bottom edge for physical depth.
          drawLine(
            brush = Brush.horizontalGradient(
              listOf(Color.Transparent, Color.Black.copy(alpha = 0.45f), Color.Transparent)
            ),
            start = Offset(size.width * 0.06f, size.height - 2f),
                   end = Offset(size.width * 0.94f, size.height - 2f),
                   strokeWidth = 2f
          )
        }
      }
    )
    .clickable(enabled = enabled, interactionSource = interactionSource, indication = null) {
      onclick?.invoke()
      if (onboarding != null) appPrefs.onboardingStage.set(onboarding)
    }
    .padding(horizontal = 24.dp),
      contentAlignment = Alignment.Center
  ) {
    Row(verticalAlignment = Alignment.CenterVertically) {
      if (icon != null) {
        Icon(
          icon,
          stringResource(labelId),
             Modifier.padding(end = DEFAULT_PADDING_HALF),
             // Copper reserved for this one small detail — an engraved accent,
             // not a filled surface — on both button variants.
             tint = QryptanaMetal.copperLight
        )
      }
      Text(
        stringResource(labelId),
           style = MaterialTheme.typography.h2,
           // Primary: full-bright titanium text. Secondary: noticeably dimmer.
           color = if (secondary) QryptanaMetal.textBright.copy(alpha = 0.68f) else QryptanaMetal.textHero,
           fontSize = 15.sp,
           letterSpacing = 0.9.sp,
           fontWeight = if (secondary) FontWeight.Normal else FontWeight.Medium
      )
    }
  }
}

@Composable
fun TextButtonBelowOnboardingButton(
  text: String,
  onClick: (() -> Unit)?,
                                    icon: Painter? = null,
                                    // Optional override so the onboarding screen can keep this link neutral
                                    // (graphite/silver) instead of the app-wide primary. Unspecified = original.
                                    color: Color = Color.Unspecified
) {
  val state = getKeyboardState()
  val enabled = onClick != null
  val topPadding by animateDpAsState(if (appPlatform.isAndroid && state.value == KeyboardState.Opened) 0.dp else 7.5.dp)
  val bottomPadding by animateDpAsState(if (appPlatform.isAndroid && state.value == KeyboardState.Opened) 0.dp else 7.5.dp)
  if ((appPlatform.isAndroid && state.value == KeyboardState.Closed) || topPadding > 0.dp) {
    val tint = if (color != Color.Unspecified) color else MaterialTheme.colors.primary
    TextButton({ onClick?.invoke() }, Modifier.padding(top = topPadding, bottom = bottomPadding).clip(CircleShape), enabled = enabled) {
      if (icon != null) {
        Icon(icon, null, tint = tint)
        Spacer(Modifier.width(4.dp))
      }
      Text(
        text,
        Modifier.padding(vertical = 5.dp),
           color = if (enabled) tint else MaterialTheme.colors.secondary,
           fontWeight = FontWeight.Medium,
           letterSpacing = if (color != Color.Unspecified) 0.6.sp else TextUnit.Unspecified,
           textAlign = TextAlign.Center
      )
    }
  } else {
    // Hide from view when keyboard is open and move the view down
    Spacer(Modifier.height(DEFAULT_PADDING * 2))
  }
}

@Composable
fun OnboardingInformationButton(
  text: String,
  onClick: () -> Unit,
) {
  Box(
    modifier = Modifier
    .clip(CircleShape)
    .clickable { onClick() }
  ) {
    Row(Modifier.padding(8.dp), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
      Icon(
        painterResource(MR.images.ic_info),
           null,
           tint = MaterialTheme.colors.primary
      )
      // https://issuetracker.google.com/issues/206039942#comment32
      var textLayoutResult: TextLayoutResult? by remember { mutableStateOf(null) }
      Text(
        text,
        Modifier
        .layout { measurable, constraints ->
          val placeable = measurable.measure(constraints)
          val newTextLayoutResult = textLayoutResult

          if (newTextLayoutResult == null || newTextLayoutResult.lineCount == 0) {
            // Default behavior if there is no text or the text layout is not measured yet
            layout(placeable.width, placeable.height) {
              placeable.placeRelative(0, 0)
            }
          } else {
            val minX = (0 until newTextLayoutResult.lineCount).minOf(newTextLayoutResult::getLineLeft)
            val maxX = (0 until newTextLayoutResult.lineCount).maxOf(newTextLayoutResult::getLineRight)

            layout(ceil(maxX - minX).toInt(), placeable.height) {
              placeable.place(-floor(minX).toInt(), 0)
            }
          }
        },
        onTextLayout = {
          textLayoutResult = it
        },
        style = MaterialTheme.typography.button,
        fontWeight = FontWeight.Medium,
        color = MaterialTheme.colors.primary
      )
    }
  }
}

@Preview/*(
  uiMode = Configuration.UI_MODE_NIGHT_YES,
showBackground = true,
name = "Dark Mode"
)*/
@Composable
fun PreviewSimpleXInfo() {
  SimpleXTheme {
    SimpleXInfoLayout(
      user = null,
      onboardingStage = null
    )
  }
}
