package chat.simplex.common.views.onboarding

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import chat.simplex.common.BuildConfigCommon
import chat.simplex.common.model.*
import chat.simplex.common.model.ChatController.appPrefs
import chat.simplex.common.platform.*
import chat.simplex.common.ui.theme.*
import chat.simplex.common.views.helpers.*
import chat.simplex.common.views.newchat.darkStops
import chat.simplex.common.views.newchat.gradientPoints
import chat.simplex.common.views.newchat.lightStops
import chat.simplex.common.views.usersettings.changeNotificationsMode
import chat.simplex.res.MR
import dev.icerock.moko.resources.compose.painterResource
import dev.icerock.moko.resources.compose.stringResource

internal object OnboardingSharedState {
  var selectedOperatorIds: Set<Long> = emptySet()
}

@Composable
fun YourNetworkView(chatModel: ChatModel) {
  LaunchedEffect(Unit) {
    prepareChatBeforeFinishingOnboarding()
  }

  val serverOperators = remember { derivedStateOf { chatModel.conditions.value.serverOperators } }
  val selectedOperatorIds = remember {
    mutableStateOf(serverOperators.value.filter { it.enabled }.map { it.operatorId }.toSet())
  }

  LaunchedEffect(selectedOperatorIds.value) {
    OnboardingSharedState.selectedOperatorIds = selectedOperatorIds.value
  }

  val notificationMode = rememberSaveable { mutableStateOf(NotificationsMode.default) }

  if (appPlatform.isDesktop) {
    YourNetworkDesktop(serverOperators, selectedOperatorIds)
  } else {
    CompositionLocalProvider(LocalAppBarHandler provides rememberAppBarHandler()) {
      ModalView({}, showClose = false, showAppBar = false) {
        OnboardingShrinkingLayout(
          modifier = Modifier.fillMaxSize().themedBackground(bgLayerSize = LocalAppBarHandler.current?.backgroundGraphicsLayerSize, bgLayer = LocalAppBarHandler.current?.backgroundGraphicsLayer)
          .systemBarsPadding()
          .padding(horizontal = DEFAULT_ONBOARDING_HORIZONTAL_PADDING),
                                  topPadding = DEFAULT_PADDING,
                                  image = {
                                    Column(Modifier.padding(vertical = DEFAULT_PADDING_HALF), horizontalAlignment = Alignment.CenterHorizontally) {
                                      OnboardingImage(
                                        MR.images.your_network, MR.images.your_network_light, MR.images.ic_dns,
                                        modifier = Modifier.padding(horizontal = DEFAULT_ONBOARDING_HORIZONTAL_PADDING).fillMaxWidth()
                                      )
                                    }
                                  },
                                  content = {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                      Text(
                                        stringResource(MR.strings.onboarding_your_network),
                                           style = MaterialTheme.typography.h1,
                                           fontWeight = FontWeight.Bold,
                                           textAlign = TextAlign.Center,
                                           lineHeight = 42.sp,
                                           modifier = Modifier.padding(top = DEFAULT_PADDING_HALF)
                                      )
                                      Text(
                                        stringResource(MR.strings.onboarding_network_routers_cannot_know),
                                           style = MaterialTheme.typography.h3,
                                           fontWeight = FontWeight.Medium,
                                           color = MaterialTheme.colors.secondary,
                                           lineHeight = 25.sp,
                                           textAlign = TextAlign.Center,
                                           modifier = Modifier.padding(top = 14.dp)
                                      )
                                      Column(
                                        Modifier.padding(top = DEFAULT_PADDING_HALF),
                                             horizontalAlignment = Alignment.Start,
                                             verticalArrangement = Arrangement.spacedBy(4.dp)
                                      ) {
                                        ConfigureRoutersButton(serverOperators, selectedOperatorIds) {
                                          ModalManager.fullscreen.showCustomModal { close ->
                                            ChooseServerOperators(serverOperators, selectedOperatorIds, close)
                                          }
                                        }
                                        ConfigureNotificationsButton(notificationMode) {
                                          ModalManager.fullscreen.showModalCloseable { close ->
                                            SetNotificationsMode(notificationMode, close)
                                          }
                                        }
                                      }
                                    }
                                  },
                                  button = {
                                    Column(
                                      Modifier.widthIn(max = 450.dp).padding(bottom = DEFAULT_PADDING * 2),
                                           horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                      OnboardingActionButton(
                                        modifier = Modifier.padding(horizontal = DEFAULT_ONBOARDING_HORIZONTAL_PADDING).fillMaxWidth(),
                                                             labelId = MR.strings.onboarding_network_operators_continue,
                                                             onboarding = null,
                                                             onclick = {
                                                               changeNotificationsMode(notificationMode.value, chatModel)
                                                               appPrefs.onboardingStage.set(OnboardingStage.Step4_NetworkCommitments)
                                                             }
                                      )
                                    }
                                  }
        )
      }
    }
  }
}

@Composable
private fun YourNetworkDesktop(
  serverOperators: State<List<ServerOperator>>,
  selectedOperatorIds: MutableState<Set<Long>>
) {
  // Same slow, staggered, opacity-only reveal as the other Qryptana onboarding pages.
  val markReveal = rememberReveal(delayMs = 0, durationMs = 900)
  val titleReveal = rememberReveal(delayMs = 200, durationMs = 900)
  val bodyReveal = rememberReveal(delayMs = 500, durationMs = 900)
  val actionsReveal = rememberReveal(delayMs = 750, durationMs = 900)
  CompositionLocalProvider(LocalAppBarHandler provides rememberAppBarHandler()) {
    ModalView({}, showClose = false, background = QryptanaMetal.void, appBarBackground = QryptanaMetal.void, appBarContentColor = QryptanaMetal.textBright, appBarNavigationTint = QryptanaMetal.copperLight) {
      Box(Modifier.fillMaxSize().qryptanaBackdrop(), contentAlignment = Alignment.Center) {
        Column(
          Modifier.fillMaxWidth().verticalScroll(rememberScrollState()).padding(horizontal = DEFAULT_PADDING, vertical = DEFAULT_PADDING),
               horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Column(Modifier.widthIn(max = 520.dp).fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Box(Modifier.graphicsLayer { alpha = markReveal.value }) { QryptanaSymbol(size = 84.dp) }
            Spacer(Modifier.height(30.dp))
            Column(Modifier.graphicsLayer { alpha = titleReveal.value }, horizontalAlignment = Alignment.CenterHorizontally) {
              QryptanaSerifTitle(stringResource(MR.strings.onboarding_private_network), sizeSp = 36)
              Spacer(Modifier.height(18.dp))
              QryptanaHairline()
            }
            Spacer(Modifier.height(24.dp))
            Text(
              stringResource(MR.strings.onboarding_connections_stay_private),
                 Modifier.graphicsLayer { alpha = bodyReveal.value }.widthIn(max = 440.dp),
                 style = MaterialTheme.typography.body2,
                 color = QryptanaMetal.textSupport,
                 fontSize = 16.sp,
                 letterSpacing = 0.1.sp,
                 lineHeight = 25.sp,
                 textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(36.dp))
            Column(
              Modifier.graphicsLayer { alpha = actionsReveal.value }.widthIn(max = 390.dp),
                   horizontalAlignment = Alignment.CenterHorizontally
            ) {
              ConfigureRoutersButton(serverOperators, selectedOperatorIds, titanium = true) {
                ModalManager.fullscreen.showCustomModal(forceAnimated = true) { close ->
                  ChooseServerOperators(serverOperators, selectedOperatorIds, close)
                }
              }
              Spacer(Modifier.height(24.dp))
              OnboardingActionButton(
                Modifier.fillMaxWidth(),
                                     labelId = MR.strings.onboarding_network_operators_continue,
                                     onboarding = null,
                                     onclick = { appPrefs.onboardingStage.set(OnboardingStage.Step4_NetworkCommitments) }
              )
            }
          }
        }
      }
    }
  }
}

@Composable
private fun ConfigureRoutersButton(serverOperators: State<List<ServerOperator>>, selectedOperatorIds: State<Set<Long>>, color: Color = MaterialTheme.colors.primary, titanium: Boolean = false, onClick: () -> Unit) {
  val shape = RoundedCornerShape(14.dp)
  Box(
    modifier = (
      if (titanium) {
        // A quiet titanium control (the secondary button's material): the operators are
        // shown as small badges, lit when selected and greyed back when not.
        Modifier
        .fillMaxWidth()
        .clip(shape)
        .background(Brush.verticalGradient(listOf(QryptanaMetal.titaniumHigh.copy(alpha = 0.24f), QryptanaMetal.titaniumLow.copy(alpha = 0.10f))))
        .border(1.dp, Brush.verticalGradient(listOf(QryptanaMetal.titaniumBorder.copy(alpha = 0.62f), QryptanaMetal.titaniumBorder.copy(alpha = 0.32f))), shape)
      } else Modifier.clip(CircleShape)
    ).clickable { onClick() },
      contentAlignment = Alignment.Center
  ) {
    Row(
      Modifier.padding(if (titanium) PaddingValues(horizontal = 20.dp, vertical = 16.dp) else PaddingValues(8.dp)),
        horizontalArrangement = Arrangement.spacedBy(if (titanium) 12.dp else 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        stringResource(if (appPlatform.isDesktop) MR.strings.onboarding_choose_private_routing else MR.strings.onboarding_configure_routers),
           style = MaterialTheme.typography.button,
           fontWeight = if (titanium) FontWeight.Normal else FontWeight.Medium,
           fontSize = if (titanium) 15.sp else MaterialTheme.typography.button.fontSize,
           letterSpacing = if (titanium) 0.6.sp else TextUnit.Unspecified,
           color = if (titanium) QryptanaMetal.textBright.copy(alpha = 0.86f) else color
      )
      serverOperators.value.forEach { op ->
        val selected = selectedOperatorIds.value.contains(op.operatorId)
        Image(
          painterResource(op.logo),
              contentDescription = null,
              modifier = Modifier.size(22.dp).then(if (titanium && !selected) Modifier.graphicsLayer { alpha = 0.55f } else Modifier),
              colorFilter = if (selected) null else ColorFilter.colorMatrix(ColorMatrix().apply {
                setToSaturation(0f)
              })
        )
      }
    }
  }
}

@Composable
private fun ConfigureNotificationsButton(notificationMode: State<NotificationsMode>, onClick: () -> Unit) {
  val icon = when (notificationMode.value) {
    NotificationsMode.SERVICE -> MR.images.ic_bolt
    NotificationsMode.PERIODIC -> MR.images.ic_timer
    NotificationsMode.OFF -> MR.images.ic_bolt_off
  }
  Box(
    modifier = Modifier
    .clip(CircleShape)
    .clickable { onClick() }
  ) {
    Row(Modifier.padding(8.dp), horizontalArrangement = Arrangement.spacedBy(4.dp), verticalAlignment = Alignment.CenterVertically) {
      Text(
        stringResource(MR.strings.onboarding_configure_notifications),
           style = MaterialTheme.typography.button,
           fontWeight = FontWeight.Medium,
           color = MaterialTheme.colors.primary
      )
      Icon(
        painterResource(icon),
           contentDescription = null,
           tint = MaterialTheme.colors.primary
      )
    }
  }
}
