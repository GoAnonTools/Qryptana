package chat.simplex.common.views.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.*
import androidx.compose.ui.input.key.*
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import dev.icerock.moko.resources.compose.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import chat.simplex.common.model.*
import chat.simplex.common.platform.*
import chat.simplex.common.ui.theme.*
import chat.simplex.common.views.database.*
import chat.simplex.common.views.helpers.*
import chat.simplex.res.MR
import kotlinx.coroutines.delay

@Composable
fun SetupDatabasePassphrase(m: ChatModel) {
  val progressIndicator = remember { mutableStateOf(false) }
  val prefs = m.controller.appPrefs
  val initialRandomDBPassphrase = remember { mutableStateOf(prefs.initialRandomDBPassphrase.get()) }
  // Do not do rememberSaveable on current key to prevent saving it on disk in clear text
  val currentKey = remember { mutableStateOf(if (initialRandomDBPassphrase.value) DatabaseUtils.ksDatabasePassword.get() ?: "" else "") }
  val newKey = rememberSaveable { mutableStateOf("") }
  val confirmNewKey = rememberSaveable { mutableStateOf("") }
  fun nextStep() {
    if (appPlatform.isAndroid || chatModel.currentUser.value != null) {
      m.controller.appPrefs.onboardingStage.set(OnboardingStage.Step3_ChooseServerOperators)
    } else {
      m.controller.appPrefs.onboardingStage.set(OnboardingStage.LinkAMobile)
    }
  }
  SetupDatabasePassphraseLayout(
    currentKey,
    newKey,
    confirmNewKey,
    progressIndicator,
    onConfirmEncrypt = {
      withLongRunningApi {
        if (m.chatRunning.value == true) {
          // Stop chat if it's started before doing anything
          stopChatAsync(m)
        }
        prefs.storeDBPassphrase.set(false)

        val newKeyValue = newKey.value
        val success = encryptDatabase(
          currentKey = currentKey,
          newKey = newKey,
          confirmNewKey = confirmNewKey,
          initialRandomDBPassphrase = mutableStateOf(true),
                                      useKeychain = mutableStateOf(false),
                                      storedKey = mutableStateOf(true),
                                      progressIndicator = progressIndicator,
                                      migration = false
        )
        if (success) {
          startChat(newKeyValue)
          nextStep()
        } else {
          // Rollback in case of it is finished with error in order to allow to repeat the process again
          prefs.storeDBPassphrase.set(true)
        }
      }
    },
    nextStep = ::nextStep,
  )

  if (progressIndicator.value) {
    ProgressIndicator()
  }

  DisposableEffect(Unit) {
    onDispose {
      if (m.chatRunning.value != true) {
        withBGApi {
          val user = chatController.apiGetActiveUser(null)
          if (user != null) {
            m.controller.startChat(user)
          }
        }
      }
    }
  }
}

@Composable
private fun SetupDatabasePassphraseLayout(
  currentKey: MutableState<String>,
  newKey: MutableState<String>,
  confirmNewKey: MutableState<String>,
  progressIndicator: MutableState<Boolean>,
  onConfirmEncrypt: () -> Unit,
                                          nextStep: () -> Unit,
) {
  val qryptanaDesktop = appPlatform.isDesktop
  val screenBackground = if (qryptanaDesktop) QryptanaMetal.void else Color.Unspecified
  val primaryText = if (qryptanaDesktop) QryptanaMetal.textHero else MaterialTheme.colors.onBackground
  val secondaryText = if (qryptanaDesktop) QryptanaMetal.textSupport else MaterialTheme.colors.secondary
  // Slow, staggered, opacity-only reveal (desktop) — same language as the other Qryptana pages.
  val markReveal = rememberReveal(delayMs = 0, durationMs = 800)
  val titleReveal = rememberReveal(delayMs = 150, durationMs = 800)
  val bodyReveal = rememberReveal(delayMs = 450, durationMs = 900)
  val formReveal = rememberReveal(delayMs = 700, durationMs = 900)

  // Shared by both layouts: the screen content itself is unchanged, only its surface differs.
  val content: @Composable ColumnScope.() -> Unit = {
    if (qryptanaDesktop) {
      Box(Modifier.graphicsLayer { alpha = markReveal.value }) { QryptanaSymbol(size = 84.dp) }
      Spacer(Modifier.height(30.dp))
      Column(Modifier.graphicsLayer { alpha = titleReveal.value }, horizontalAlignment = Alignment.CenterHorizontally) {
        QryptanaSerifTitle(stringResource(MR.strings.onboarding_protect_app), sizeSp = 36)
        Spacer(Modifier.height(18.dp))
        QryptanaHairline()
      }
      Spacer(Modifier.height(26.dp))
    } else {
      AppBarTitle(stringResource(MR.strings.setup_database_passphrase), overrideTitleColor = primaryText, withPadding = true, textAlign = TextAlign.Center)
    }

    val onClickUpdate = {
      // Don't do things concurrently. Shouldn't be here concurrently, just in case
      if (!progressIndicator.value) {
        encryptDatabaseAlert(onConfirmEncrypt)
      }
    }
    val disabled = currentKey.value == newKey.value ||
    newKey.value != confirmNewKey.value ||
    newKey.value.isEmpty() ||
    !validKey(currentKey.value) ||
    !validKey(newKey.value) ||
    progressIndicator.value

    Column(Modifier.widthIn(max = 600.dp).fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
      val textStyle = MaterialTheme.typography.body1.copy(color = secondaryText, lineHeight = if (qryptanaDesktop) 25.sp else 24.sp, fontSize = if (qryptanaDesktop) 16.sp else MaterialTheme.typography.body1.fontSize, letterSpacing = if (qryptanaDesktop) 0.1.sp else androidx.compose.ui.unit.TextUnit.Unspecified)
      Column(
        Modifier.then(if (qryptanaDesktop) Modifier.graphicsLayer { alpha = bodyReveal.value } else Modifier),
             horizontalAlignment = Alignment.CenterHorizontally
      ) {
        ReadableText(MR.strings.you_have_to_enter_passphrase_every_time, TextAlign.Center, padding = PaddingValues(), style = textStyle )
        Spacer(Modifier.height(DEFAULT_PADDING))
        ReadableText(MR.strings.impossible_to_recover_passphrase, TextAlign.Center, padding = PaddingValues(), style = textStyle)
      }
      Spacer(Modifier.height(if (qryptanaDesktop) 34.dp else DEFAULT_PADDING))

      val focusRequester = remember { FocusRequester() }
      val focusManager = LocalFocusManager.current
      LaunchedEffect(Unit) {
        delay(100L)
        focusRequester.requestFocus()
      }
      Column(Modifier.then(if (qryptanaDesktop) Modifier.graphicsLayer { alpha = formReveal.value } else Modifier)) {
        PassphraseField(
          newKey,
          generalGetString(if (qryptanaDesktop) MR.strings.onboarding_secret_phrase else MR.strings.new_passphrase),
                        modifier = Modifier
                        .padding(horizontal = DEFAULT_PADDING)
                        .focusRequester(focusRequester)
                        .onPreviewKeyEvent {
                          if ((it.key == Key.Enter || it.key == Key.NumPadEnter) && it.type == KeyEventType.KeyUp) {
                            focusManager.moveFocus(FocusDirection.Down)
                            true
                          } else {
                            false
                          }
                        },
                        showStrength = true,
                        isValid = ::validKey,
                        backgroundColor = if (qryptanaDesktop) Color(0xFF121110) else Color.Unspecified,
                        textColor = primaryText,
                        placeholderColor = if (qryptanaDesktop) QryptanaMetal.textQuiet else secondaryText,
                        focusedIndicatorColor = if (qryptanaDesktop) QryptanaMetal.copperLight else Color.Unspecified,
                        unfocusedIndicatorColor = if (qryptanaDesktop) QryptanaMetal.titaniumBorder else Color.Unspecified,
                        visibilityIconColor = if (qryptanaDesktop) QryptanaMetal.textTertiary else Color.Unspecified,
                        titanium = qryptanaDesktop,
                        keyboardActions = KeyboardActions(onNext = { defaultKeyboardAction(ImeAction.Next) }),
        )

        if (qryptanaDesktop) Spacer(Modifier.height(14.dp))
          PassphraseField(
            confirmNewKey,
            generalGetString(if (qryptanaDesktop) MR.strings.onboarding_confirm_secret_phrase else MR.strings.confirm_new_passphrase),
                          modifier = Modifier
                          .padding(horizontal = DEFAULT_PADDING)
                          .onPreviewKeyEvent {
                            if (!disabled && (it.key == Key.Enter || it.key == Key.NumPadEnter) && it.type == KeyEventType.KeyUp) {
                              onClickUpdate()
                              true
                            } else {
                              false
                            }
                          },
                          isValid = { confirmNewKey.value == "" || newKey.value == confirmNewKey.value },
                          backgroundColor = if (qryptanaDesktop) Color(0xFF121110) else Color.Unspecified,
                          textColor = primaryText,
                          placeholderColor = if (qryptanaDesktop) QryptanaMetal.textQuiet else secondaryText,
                          focusedIndicatorColor = if (qryptanaDesktop) QryptanaMetal.copperLight else Color.Unspecified,
                          unfocusedIndicatorColor = if (qryptanaDesktop) QryptanaMetal.titaniumBorder else Color.Unspecified,
                          visibilityIconColor = if (qryptanaDesktop) QryptanaMetal.textTertiary else Color.Unspecified,
                          titanium = qryptanaDesktop,
                          keyboardActions = KeyboardActions(onDone = { defaultKeyboardAction(ImeAction.Done) }),
          )
      }
    }
    Spacer(Modifier.height(if (qryptanaDesktop) 34.dp else DEFAULT_PADDING))

    Column(
      Modifier.widthIn(max = if (appPlatform.isAndroid) 450.dp else 1000.dp).then(if (qryptanaDesktop) Modifier.graphicsLayer { alpha = formReveal.value } else Modifier),
           horizontalAlignment = Alignment.CenterHorizontally
    ) {
      SetPassphraseButton(disabled, onClickUpdate, qryptanaDesktop)
      SkipButton(progressIndicator.value, color = if (qryptanaDesktop) QryptanaMetal.textTertiary else Color.Unspecified, qryptanaDesktop = qryptanaDesktop) {
        randomPassphraseAlert {
          chatModel.desktopOnboardingRandomPassword.value = true
          nextStep()
        }
      }
    }
  }

  CompositionLocalProvider(LocalAppBarHandler provides rememberAppBarHandler()) {
    ModalView(
      {},
      showClose = false,
      background = screenBackground,
      appBarBackground = if (qryptanaDesktop) QryptanaMetal.void else Color.Unspecified,
      appBarContentColor = if (qryptanaDesktop) QryptanaMetal.textBright else Color.Unspecified,
      appBarNavigationTint = if (qryptanaDesktop) QryptanaMetal.copperLight else Color.Unspecified
    ) {
      if (qryptanaDesktop) {
        Box(Modifier.fillMaxSize().qryptanaBackdrop(), contentAlignment = Alignment.Center) {
          Column(
            Modifier.fillMaxWidth().verticalScroll(rememberScrollState()).padding(horizontal = DEFAULT_PADDING, vertical = 24.dp),
                 horizontalAlignment = Alignment.CenterHorizontally
          ) { content() }
        }
      } else {
        ColumnWithScrollBar(
          Modifier.themedBackground(bgLayerSize = LocalAppBarHandler.current?.backgroundGraphicsLayerSize, bgLayer = LocalAppBarHandler.current?.backgroundGraphicsLayer)
          .padding(horizontal = DEFAULT_PADDING),
                            horizontalAlignment = Alignment.CenterHorizontally,
        ) { content() }
      }
    }
  }
}

@Composable
private fun SetPassphraseButton(disabled: Boolean, onClick: () -> Unit, qryptanaDesktop: Boolean) {
  OnboardingActionButton(
    if (appPlatform.isAndroid) Modifier.padding(horizontal = DEFAULT_PADDING).fillMaxWidth() else Modifier.widthIn(min = 300.dp),
      labelId = if (qryptanaDesktop) MR.strings.onboarding_create_secret_phrase else MR.strings.set_database_passphrase,
                         onboarding = null,
                         onclick = onClick,
                         enabled =  !disabled
  )
}

@Composable
private fun SkipButton(disabled: Boolean, color: Color = Color.Unspecified, qryptanaDesktop: Boolean, onClick: () -> Unit) {
  TextButtonBelowOnboardingButton(stringResource(if (qryptanaDesktop) MR.strings.onboarding_generate_secure_phrase else MR.strings.use_random_passphrase), onClick = if (disabled) null else onClick, color = color)
}

@Composable
private fun ProgressIndicator() {
  Box(
    Modifier.fillMaxSize(),
      contentAlignment = Alignment.Center
  ) {
    CircularProgressIndicator(
      Modifier
      .padding(horizontal = 2.dp)
      .size(30.dp),
                              color = MaterialTheme.colors.secondary,
                              strokeWidth = 3.dp
    )
  }
}

private suspend fun startChat(key: String?) {
  val m = ChatModel
  initChatController(key)
  m.chatDbChanged.value = false
  m.chatRunning.value = true
}

private fun randomPassphraseAlert(onConfirm: () -> Unit) {
  AlertManager.shared.showAlertDialog(
    title = generalGetString(MR.strings.use_random_passphrase),
                                      text = generalGetString(MR.strings.you_can_change_it_later),
                                      confirmText = generalGetString(MR.strings.ok),
                                      onConfirm = onConfirm,
  )
}
