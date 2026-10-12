package chat.simplex.common.views.onboarding

import androidx.compose.animation.core.*
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.text.TextLayoutResult
import dev.icerock.moko.resources.compose.painterResource
import dev.icerock.moko.resources.compose.stringResource
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
import kotlin.math.ceil
import kotlin.math.floor

@Composable
fun SimpleXInfo(chatModel: ChatModel, onboarding: Boolean = true) {
  if (onboarding) {
    if (appPlatform.isDesktop) {
      SimpleXInfoDesktop(chatModel)
    } else {
      CompositionLocalProvider(LocalAppBarHandler provides rememberAppBarHandler()) {
        ModalView({}, showClose = false, showAppBar = false) {
          SimpleXInfoLayout(
            user = chatModel.currentUser.value,
            onboardingStage = chatModel.controller.appPrefs.onboardingStage
          )
        }
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
 */
@Composable
private fun QryptanaOnboardingLayout(
  user: User?,
  onboardingStage: SharedPreference<OnboardingStage>
) {
  val gradient = if (isInDarkTheme()) {
    Brush.verticalGradient(listOf(QryptanaDarkBackground, Color(0xFF24201E)))
  } else {
    Brush.verticalGradient(listOf(Color(0xFFF8F1E8), Color(0xFFEFE2D2)))
  }
  Box(
    Modifier
      .fillMaxSize()
      .background(gradient)
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
      QryptanaWelcomeContent(user, onboardingStage, Modifier.widthIn(max = 560.dp).fillMaxWidth())
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

@Composable
private fun QryptanaWelcomeContent(
  user: User?,
  onboardingStage: SharedPreference<OnboardingStage>?,
  modifier: Modifier = Modifier
) {
  Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {
    InvitationPreviewCard()
    Spacer(Modifier.height(24.dp))
    QryptanaWordmark(Modifier.widthIn(max = 280.dp).fillMaxWidth(0.78f))
    Spacer(Modifier.height(14.dp))
    Text(
      stringResource(MR.strings.onboarding_be_free),
      style = MaterialTheme.typography.h1,
      fontWeight = FontWeight.Bold,
      textAlign = TextAlign.Center,
      lineHeight = 42.sp,
      color = QryptanaDarkText
    )
    Spacer(Modifier.height(12.dp))
    Text(
      stringResource(MR.strings.onboarding_private_and_secure),
      style = MaterialTheme.typography.h3,
      color = QryptanaDarkSecondary,
      fontWeight = FontWeight.Medium,
      lineHeight = 25.sp,
      textAlign = TextAlign.Center
    )
    Text(
      stringResource(MR.strings.onboarding_first_network),
      style = MaterialTheme.typography.body2,
      color = QryptanaDarkSecondary.copy(alpha = 0.82f),
      textAlign = TextAlign.Center,
      lineHeight = 20.sp,
      modifier = Modifier.padding(top = 8.dp)
    )
    Spacer(Modifier.height(26.dp))
    if (onboardingStage != null) {
      OnboardingActionButton(user, onboardingStage)
      TextButtonBelowOnboardingButton(stringResource(MR.strings.how_it_works), icon = painterResource(MR.images.ic_info), onClick = {
        ModalManager.fullscreen.showModal { HowItWorks(user, onboardingStage) }
      })
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
  Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(horizontal = 24.dp)) {
    Box(
      Modifier
        .widthIn(max = 360.dp)
        .fillMaxWidth(0.88f)
        .height(188.dp)
        .offset(y = floatOffset.dp)
        .shadow(26.dp, cardShape, clip = false)
        .clip(cardShape)
        .background(
          Brush.linearGradient(
            colors = listOf(QryptanaDarkCard, QryptanaDarkSurface, Color(0xFF312A25)),
            start = Offset.Zero,
            end = Offset(420f, 240f)
          )
        )
        .border(1.dp, QryptanaCopper.copy(alpha = 0.82f), cardShape)
        .padding(horizontal = 28.dp, vertical = 22.dp)
    ) {
      Box(Modifier.fillMaxSize()) {
        Box(
          Modifier
            .align(Alignment.TopEnd)
            .size(78.dp)
            .background(
              Brush.radialGradient(listOf(QryptanaCopper.copy(alpha = 0.18f), Color.Transparent)),
              CircleShape
            )
        )
        Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
              painter = painterResource(MR.images.qryptana_symbol),
              contentDescription = stringResource(MR.strings.qryptana_logo_description),
              contentScale = ContentScale.Fit,
              modifier = Modifier.size(54.dp)
            )
            Spacer(Modifier.width(16.dp))
            Column {
              Text(
                stringResource(MR.strings.onboarding_invitation_card_title),
                color = QryptanaDarkText,
                style = MaterialTheme.typography.h3,
                fontWeight = FontWeight.Medium
              )
              Text(
                stringResource(MR.strings.onboarding_invitation_card_subtitle),
                color = QryptanaDarkSecondary,
                style = MaterialTheme.typography.body2,
                modifier = Modifier.padding(top = 3.dp)
              )
            }
          }
          Text(
            "A private way to connect",
            color = QryptanaCopperLight.copy(alpha = 0.88f),
            style = MaterialTheme.typography.body2,
            fontWeight = FontWeight.Medium
          )
        }
      }
    }
  }
}

@Composable
fun QryptanaWordmark(modifier: Modifier = Modifier) {
  Image(
    painter = painterResource(MR.images.qryptana_wordmark),
    contentDescription = stringResource(MR.strings.qryptana_logo_description),
    contentScale = ContentScale.Fit,
    modifier = modifier
      .padding(bottom = DEFAULT_PADDING_HALF)
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
  iconColor: Color = Color.White,
  secondary: Boolean = false,
  onclick: (() -> Unit)?
) {
  val shape = RoundedCornerShape(50)
  Box(
    modifier
      .widthIn(min = 280.dp, max = 360.dp)
      .height(56.dp)
      .clip(shape)
      .then(
        if (secondary) Modifier.border(1.dp, QryptanaCopper.copy(alpha = 0.8f), shape)
        else Modifier
          .shadow(12.dp, shape, clip = false)
          .background(Brush.horizontalGradient(listOf(QryptanaCopperDark, QryptanaCopperLight)))
      )
      .clickable(enabled = enabled) {
        onclick?.invoke()
        if (onboarding != null) appPrefs.onboardingStage.set(onboarding)
      }
      .padding(horizontal = 24.dp),
    contentAlignment = Alignment.Center
  ) {
    Row(verticalAlignment = Alignment.CenterVertically) {
      if (icon != null) {
        Icon(icon, stringResource(labelId), Modifier.padding(end = DEFAULT_PADDING_HALF), tint = if (secondary) QryptanaCopperLight else Color.White)
      }
      Text(
        stringResource(labelId),
        style = MaterialTheme.typography.h2,
        color = if (secondary) QryptanaCopperLight else QryptanaDarkText,
        fontSize = 17.sp,
        fontWeight = FontWeight.Medium
      )
    }
  }
}

@Composable
fun TextButtonBelowOnboardingButton(text: String, onClick: (() -> Unit)?, icon: Painter? = null) {
  val state = getKeyboardState()
  val enabled = onClick != null
  val topPadding by animateDpAsState(if (appPlatform.isAndroid && state.value == KeyboardState.Opened) 0.dp else 7.5.dp)
  val bottomPadding by animateDpAsState(if (appPlatform.isAndroid && state.value == KeyboardState.Opened) 0.dp else 7.5.dp)
  if ((appPlatform.isAndroid && state.value == KeyboardState.Closed) || topPadding > 0.dp) {
    TextButton({ onClick?.invoke() }, Modifier.padding(top = topPadding, bottom = bottomPadding).clip(CircleShape), enabled = enabled) {
      if (icon != null) {
        Icon(icon, null, tint = MaterialTheme.colors.primary)
        Spacer(Modifier.width(4.dp))
      }
      Text(
        text,
        Modifier.padding(vertical = 5.dp),
        color = if (enabled) MaterialTheme.colors.primary else MaterialTheme.colors.secondary,
        fontWeight = FontWeight.Medium,
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
