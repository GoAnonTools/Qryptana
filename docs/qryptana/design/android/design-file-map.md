# Qryptana Android Design File Map

This map documents all discovered design-, styling-, resource-, and UI-related files across the Qryptana repository. Files are grouped by architectural role. Each entry details what the file controls, which screens consume it, its cross-platform impact boundary (Android vs. Desktop vs. iOS), and its maintenance disposition (Reuse, Adjust, or Leave Untouched).

---

## 1. Theme, Color & Semantic Palette Definitions

These files define the fundamental design tokens, palettes, color roles, and theme resolution cascade.

| File Path | What It Controls | Consuming Screens | Cross-Platform Impact | Disposition |
|---|---|---|---|---|
| `apps/multiplatform/common/src/commonMain/kotlin/chat/simplex/common/ui/theme/Color.kt` | Direct palette colors, `QryptanaAndroidColorRoles`, `QryptanaAndroidThemeRoles` (Light, Dark, Simplex, Black), legacy compatibility constants (`SimplexBlue`, `HighOrLowlight`). | Consumed app-wide by all screens on Android and Desktop. | **High Impact**: Shared file. Desktop uses explicit constants (`QryptanaDarkBackground`, `QryptanaMetal`). Android uses `QryptanaAndroidThemeRoles`. Changes must preserve desktop constants. | **Adjust**: Update Android sent/received bubble colors to eliminate warm brown tones (`#3A2B24`, `#F1DFD3`) in favor of cool titanium. Keep desktop constants untouched. |
| `apps/multiplatform/common/src/commonMain/kotlin/chat/simplex/common/ui/theme/Theme.kt` | `ThemeDefaults`, Material `Colors` and `AppColors` construction, `DefaultTheme` enum, `SimpleXTheme` and `SimpleXThemeOverride` composables, global `CurrentColors` state flow. | Root UI wrapper for entire app (`AppScreen`, `SimpleXInfo`, `SettingsView`, `ChatView`). | **High Impact**: Core theme engine for both Android and Desktop. Platform branching exists in `ThemeDefaults` (`if (isDesktop) ... else ...`). | **Adjust**: Ensure Android branch maps refined titanium and authentic copper tokens into Material and `AppColors` without touching desktop branches. |
| `apps/multiplatform/common/src/commonMain/kotlin/chat/simplex/common/ui/theme/ThemeManager.kt` | Singleton object resolving the 4-level theme cascade: per-chat > per-user > app settings > preset defaults. Manages persistence, YAML export/import, and system bar updates. | Consumed whenever user changes theme, wallpaper, or chat-specific colors. | **Shared Engine**: Manages state for both Android and Desktop. Uses `platform.androidSetStatusAndNavigationBarAppearance()`. | **Leave Untouched**: Resolution logic is stable and sound. Token values feeding it are adjusted via `Color.kt` and `Theme.kt`. |
| `apps/multiplatform/common/src/commonMain/kotlin/chat/simplex/common/ui/theme/QryptanaMessagingTokens.kt` | Scoped messaging tokens (`QryptanaMessagingTokens`), row padding, message corner radii, input surfaces, borders, and selection states. | Chat list rows (`ChatListView`), chat bubbles (`ChatItemView`), message text (`TextItemView`), composer (`SendMsgView`). | **Platform-Branched**: Explicitly delegates to `qryptanaDesktopTokens()` on Desktop and `qryptanaThemeTokens()` on Android. | **Adjust**: Refine Android tokens (`qryptanaThemeTokens`) to enforce cool titanium surfaces, 14dp bubble radii, and 48dp minimum control heights. |
| `apps/multiplatform/common/src/androidMain/kotlin/chat/simplex/common/ui/theme/Theme.android.kt` | Android-specific `actual fun isSystemInDarkTheme()` delegating to `androidx.compose.foundation.isSystemInDarkTheme()`. | All composables observing system night mode. | Android-specific only. | **Reuse / Leave Untouched**: Standard Android implementation. |
| `apps/multiplatform/common/src/desktopMain/kotlin/chat/simplex/common/ui/theme/Theme.desktop.kt` | Desktop-specific `actual fun isSystemInDarkTheme()` via `OsThemeDetector`. | Desktop shell only. | Desktop only. | **Leave Untouched**: Zero Android impact. |
| `apps/multiplatform/common/src/commonMain/kotlin/chat/simplex/common/views/onboarding/SimpleXInfo.kt` (lines 127–177) | `QryptanaMetal` (desktop/internal metallic palette) and `QryptanaAndroidOnboardingPalette` (scoped dark onboarding tokens: `#0E1013`, `#161A1F`, `#1E232A`, `#DDE2E8`, `#9BA4AF`, `#6A737E`, `#B87333`). | Onboarding wizard (`SimpleXInfo`, `CreateFirstProfile`, `YourNetwork`, `OnboardingConditionsView`). | Onboarding only; desktop uses `QryptanaMetal`. | **Reuse**: Already holds the approved dark titanium/copper tokens. Serve as reference for app-wide palette. |

---

## 2. Typography, Shapes, Spacing & Elevation

Files controlling font families, text styles, elevation, corner radii, and padding constants.

| File Path | What It Controls | Consuming Screens | Cross-Platform Impact | Disposition |
|---|---|---|---|---|
| `apps/multiplatform/common/src/commonMain/kotlin/chat/simplex/common/ui/theme/Type.kt` | Compose `Typography` definitions: h1 (33.5sp bold), h2 (24sp), h3 (18.5sp), h4 (17.5sp), body1 (16sp), body2 (14sp), button (16sp), caption (18sp) using `Inter` font family. | App-wide typography across all shared views. | Shared Compose: Affects both Desktop and Android text styles. | **Adjust**: Guard or define Android-specific scaling and line heights (e.g. caption at 18sp is unusually large for standard Android captions; button at 16sp needs vertical centering alignment). |
| `apps/multiplatform/common/src/androidMain/kotlin/chat/simplex/common/ui/theme/Type.android.kt` | Android font loading actuals for `Inter` font family from resources. | Android text rendering. | Android-specific only. | **Reuse**: Ensures high-fidelity Inter rendering on Android. |
| `apps/multiplatform/common/src/commonMain/kotlin/chat/simplex/common/ui/theme/Shape.kt` | Compose `Shapes` definition: `small = 4.dp`, `medium = 4.dp`, `large = 0.dp`. | Material components (`Card`, `Surface`, `Button`). | Shared Compose. | **Adjust**: Upgrade Android shape tokens: `small = 8.dp` (buttons/chips), `medium = 16.dp` (cards/sheets), `large = 24.dp` (bottom sheets/dialogs) to establish a modern, tactile feel. |
| `apps/multiplatform/common/src/commonMain/kotlin/chat/simplex/common/ui/theme/Theme.kt` (lines 649–663) | Global spacing constants: `DEFAULT_PADDING` (20.dp), `DEFAULT_PADDING_HALF` (10.dp), `DEFAULT_BOTTOM_PADDING` (48.dp), `DEFAULT_MIN_SECTION_ITEM_HEIGHT` (50.dp). | All sections, cards, and modal dialogs. | Shared Compose constants. | **Reuse / Augment**: Add explicit `TOUCH_TARGET_MIN = 48.dp` for interactive controls. |

---

## 3. Reusable UI Components & Navigation Shells

Foundational interactive elements used across multiple application flows.

| File Path | What It Controls | Consuming Screens | Cross-Platform Impact | Disposition |
|---|---|---|---|---|
| `apps/multiplatform/common/src/commonMain/kotlin/chat/simplex/common/App.kt` | `AppScreen()`, `MainScreen()`, `AndroidScreen()` (2-column sliding layout), `AndroidWrapInCallLayout`, `DesktopScreen()`. Manages window insets, display cutouts, and onboarding stage dispatch. | Root presentation shell for Android and Desktop. | **High Impact**: Top-level entry. Android branch (`AndroidScreen`) is isolated via `if (appPlatform.isAndroid)`. | **Adjust**: Refine Android sliding transition specs, eliminate seam flash, and ensure cutout padding matches titanium surface color. |
| `apps/multiplatform/common/src/commonMain/kotlin/chat/simplex/common/views/helpers/DefaultTopAppBar.kt` | `DefaultAppBar` composable, translucent blurred header backgrounds, title text, back button, and search integration. | Top of ChatList, ChatView, SettingsView, and secondary sub-views. | Shared Compose. | **Adjust**: Ensure top bar background matches titanium surface (`#161A1F` dark / `#FCFDFE` light) with 0.5dp bottom border (`#2A3038`), replacing legacy blurred mix. |
| `apps/multiplatform/common/src/commonMain/kotlin/chat/simplex/common/views/helpers/SimpleButton.kt` | `SimpleButton`, `SimpleButtonDecorated`, `SimpleButtonIconEnded`, `SimpleButtonFrame`. | Dialogs, auth unlock, onboarding, empty states, settings. | Shared Compose. | **Adjust**: Enforce minimum touch target height `Modifier.defaultMinSize(minHeight = 48.dp, minWidth = 48.dp)` for Android. Keep desktop compact layout. |
| `apps/multiplatform/common/src/commonMain/kotlin/chat/simplex/common/views/helpers/DefaultSwitch.kt` | `DefaultSwitch` with platform-specific thumb and track colors (`androidColors` vs `legacyColors`). | All settings toggles, notification options, privacy controls. | Platform-branched: Already has `if (appPlatform.isAndroid) androidColors`. | **Reuse**: Already tuned to approved copper/titanium switch colors with 4.81:1 contrast. |
| `apps/multiplatform/common/src/commonMain/kotlin/chat/simplex/common/views/helpers/Section.kt` | `SectionView`, `SectionItemView`, `CardColumnLayout`, `SectionDividerSpaced`, card corner radii (`SectionCardShape = 16.dp`). | Settings, contact info, group info, database views. | Shared Compose. | **Adjust**: Set divider lines to cool titanium hairline (`#1E232A`), ensure row item heights are at least 48dp, and eliminate warm tint on card backgrounds. |
| `apps/multiplatform/common/src/commonMain/kotlin/chat/simplex/common/views/helpers/ModalView.kt` | `ModalManager`, bottom sheet container, modal dialogs, sliding modal wrappers. | All sub-screens, sheets, and popups across the app. | Shared Compose. On Android, all modals collapse to full-screen or bottom sheets. | **Adjust**: Standardize Android sheet corners to 24dp top-rounded titanium surface with prominent drag handle and dark scrim (`#0E1013` at 70% alpha). |
| `apps/multiplatform/common/src/commonMain/kotlin/chat/simplex/common/views/helpers/SearchTextField.kt` | Custom search text field with clear button and placeholder text. | ChatList search, contact search, emoji search, settings search. | Shared Compose. | **Adjust**: Style field with recessed titanium surface (`#161A1F`), subtle 1dp border (`#6A737E`), silver placeholder text, and 48dp clear button target. |
| `apps/multiplatform/common/src/commonMain/kotlin/chat/simplex/common/views/helpers/TextEditor.kt` | Multi-line text input field, markdown shortcuts, character counters. | Composer (`SendMsgView`), profile name editing, group description editing. | Shared Compose. | **Adjust**: Enhance focused border highlight with authentic copper hairline (`#B87333`). |
| `apps/multiplatform/common/src/commonMain/kotlin/chat/simplex/common/views/helpers/AlertManager.kt` | App-wide alert and confirmation dialog system (`showAlertDialog`, `showAlertMsg`). | Destructive action confirmations, connection alerts, error prompts. | Shared Compose. | **Adjust**: Apply titanium dialog surface (`#1E232A`), distinct primary copper vs crimson destructive actions. |

---

## 4. Brand Assets, Logos, Icons & Artwork

Visual assets discovered in the repository that define brand identity and iconography.

| File Path | Description & Role | Consumed By | Status & Canonical Decision | Disposition |
|---|---|---|---|---|
| `docs/qryptana/brand/qryptana-symbol.png` | **Canonical Approved Brand Symbol**: 1254×1254 RGBA PNG. Sha256: `880afecba0ad90d10fdcdbbbe97aadc2f8e2dcd3636322d796945e811154d70f`. | Canonical source for all app icons and profile defaults. | **Canonical Approved Symbol Artwork**. Unshaded, un-tinted, authentic master asset. | **Preserve exactly**. Do not recolor, redraw, or apply shaders. |
| `apps/multiplatform/common/src/commonMain/resources/MR/images/qryptana_symbol@1x.png` | Packaged app resource for the Qryptana symbol. Exactly identical sha256 (`880afecb...`) to canonical brand asset. | Onboarding identity card, Smart Invite header, default avatar, profile headers. | Canonical packaged symbol. | **Preserve exactly**. |
| `docs/qryptana/brand/qryptana-wordmark.png` | **Canonical Approved Brand Wordmark**: 1672×941 RGBA PNG. Sha256: `9eec8dc235de5f0a846348603164f8e024537692568d11af0ed479108ef54d06`. | Canonical source for brand header typography and welcome screen. | **Canonical Approved Wordmark Artwork**. | **Preserve exactly**. Do not recolor, redraw, or apply shaders. |
| `apps/multiplatform/common/src/commonMain/resources/MR/images/qryptana_wordmark@1x.png` | Packaged app resource for the Qryptana wordmark (1672×941, optimized PNG encoding, 379KB). | Welcome screen hero in onboarding (`SimpleXInfo.kt`). | Canonical packaged wordmark. Rendered directly via `Image()` without shader filter. | **Preserve exactly**. |
| `assets/videos/qryptana_identity_card_reveal.mp4` | 3D rendered metallic identity card reveal video (1080p MP4). | Android and Desktop onboarding step 1 identity animation. | High-quality metallic render showcasing copper card edge and titanium lighting. | **Reuse as canonical video asset**. Ensure static fallback renders gracefully if playback fails. |
| `apps/multiplatform/android/src/main/res/drawable-v24/ic_launcher_foreground.xml` | Upstream green Android robot vector drawable (108×108dp). | Android launcher adaptive icon foreground fallback. | **Obsolete / Defective**: Still contains generic Android robot head. | **Adjust**: Replace with vector path or bitmap referencing canonical Qryptana symbol. |
| `apps/multiplatform/android/src/main/res/values/icon_background.xml` | Android launcher adaptive icon background color (`#F7F0E6`, warm beige). | Android launcher icon background. | **Obsolete**: Violates cool neutral direction. | **Adjust**: Change color value to Cool Titanium `#161A1F`. |
| `apps/multiplatform/android/src/main/res/values/icon_dark_blue_background.xml` | Android dark icon background color (`#171615`, warm charcoal). | Android launcher dark blue icon alias. | Outdated warm charcoal. | **Adjust**: Align with Charcoal `#0E1013`. |
| `apps/multiplatform/android/src/main/res/mipmap-*/icon_foreground.png` | Pre-rendered raster icons for Android launcher mipmaps (hdpi, mdpi, xhdpi, xxhdpi, xxxhdpi). | Android launcher icon on home screen and app drawer. | Contains upstream SimpleX symbol icon. | **Adjust**: Generate mipmaps using canonical Qryptana symbol. |
| `apps/multiplatform/common/src/commonMain/resources/MR/images/ic_*.svg` | Comprehensive collection of ~150 UI icons (chevron, lock, call, mic, videocam, check, database, etc.). | Consumed app-wide by all lists, headers, buttons, and status indicators. | Shared vector assets. Tinted dynamically in Compose via `tint = MaterialTheme.colors.*`. | **Reuse**: Retain SVG vectors; update tinting roles to titanium (`#9BA4AF`) and copper (`#B87333`). |

---

## 5. Android Host & System-Level Configuration Files

Files configuring the Android host activity, services, system bars, and Android OS integration.

| File Path | What It Controls | Consuming Screens | Cross-Platform Impact | Disposition |
|---|---|---|---|---|
| `apps/multiplatform/android/src/main/java/chat/simplex/app/MainActivity.kt` | Android Activity lifecycle, window insets, `enableEdgeToEdge()`, `FLAG_SECURE` screen privacy, system bar appearance calls. | Hosts entire Compose UI on Android. | Android-specific host activity. | **Adjust**: Ensure `enableEdgeToEdge()` works smoothly with titanium status bar backgrounds. |
| `apps/multiplatform/android/src/main/java/chat/simplex/app/SimplexApp.kt` | Application class. Defines runtime `PlatformInterface` implementation including `androidSetStatusAndNavigationBarAppearance()`. | Drives OS-level status bar / navigation bar contrast and light/dark mode icon switching. | Android-specific application class. | **Adjust**: Refine status bar color assignment to ensure navigation bar stays transparent and status bar icons contrast correctly against titanium top bar. |
| `apps/multiplatform/android/src/main/res/values/themes.xml` | Android application style `Theme.SimpleX`. Parent: `Theme.AppCompat.DayNight.NoActionBar`. | Android OS window decoration prior to Compose rendering. | Android-specific theme XML. | **Adjust**: Change window background to `#0E1013` (dark) and `#F1F3F5` (light) to avoid startup color flash. |
| `apps/multiplatform/android/src/main/res/values-night/themes.xml` | Android night style. Sets `android:statusBarColor` and `android:windowBackground` (`#121212`). | Android night mode window decor. | Android-specific theme XML. | **Adjust**: Update `window_background_dark` to exact Charcoal foundation `#0E1013`. |
| `apps/multiplatform/android/src/main/res/values/colors.xml` | XML color values: `black`, `white`, `window_background_dark` (`#121212`). | Android window styles. | Android-specific. | **Adjust**: Define `charcoal_background` (`#0E1013`) and `titanium_surface` (`#161A1F`). |
| `apps/multiplatform/android/src/main/java/chat/simplex/app/views/call/CallActivity.kt` | Standalone Activity for ongoing audio/video calls with lock screen wake and picture-in-picture. | Android full-screen call UI. | Android-specific call activity. | **Adjust**: Apply titanium call canvas styling and 56dp action buttons. |

---

## 6. Functional Files Hosting User-Visible UI

These functional files manage business logic, models, or state while hosting key user-visible Compose elements.

| File Path | What It Controls | Consuming Screens | Cross-Platform Impact | Disposition |
|---|---|---|---|---|
| `apps/multiplatform/common/src/commonMain/kotlin/chat/simplex/common/views/chat/ChatView.kt` | Core chat view logic: message list lazy column, scrolling state, date headers, message selection, context menu. | Conversation view for all direct chats and groups. | Shared Compose: Core conversation view. | **Leave functional logic untouched; Adjust styling tokens**: Bind bubbles and dates to `qryptanaMessagingTokens()`. |
| `apps/multiplatform/common/src/commonMain/kotlin/chat/simplex/common/views/chat/SendMsgView.kt` | Message input state, attachments button, voice recording gesture, command menu, send button trigger. | Bottom of all conversation screens. | Shared Compose with Android IME handling. | **Leave functional logic untouched; Adjust layout spacing**: Ensure 48dp touch targets on buttons. |
| `apps/multiplatform/common/src/commonMain/kotlin/chat/simplex/common/views/chat/ComposeView.kt` | Message drafting, editing previous message, quoted reply banner, file upload previews. | Active conversation composer area. | Shared Compose. | **Adjust styling**: Ensure quote banner uses copper accent stroke and cool recessed titanium fill. |
| `apps/multiplatform/common/src/commonMain/kotlin/chat/simplex/common/views/newchat/QRCode.kt` | QR code generation, error correction levels, logo embedding, and `QryptanaAndroidSmartInvite`. | Smart Invite, contact sharing, group links, WebRTC pairing. | Shared Compose with Android specific branch. | **Reuse `QryptanaAndroidSmartInvite`**: High-contrast, clean titanium card. Remove/guard legacy cyan-purple sweep fallback. |
| `apps/multiplatform/common/src/commonMain/kotlin/chat/simplex/common/views/chatlist/ChatListView.kt` | Chat list state, conversation filtering, tag chips, swipe actions, unread count collectors. | Home screen of the app. | Shared Compose with Android back handling. | **Adjust styling**: Eliminate bright blue unread badges in favor of copper badge (`#9F634C`). |

---

## 7. Previews, Tests & Verification Artifacts

Files providing visual previews, automated assertions, and contrast verification.

| File Path | Role & Coverage | Output / Assertions | Disposition |
|---|---|---|---|
| `apps/multiplatform/common/src/commonTest/kotlin/chat/simplex/app/ThemesTest.kt` | Automated unit test suite verifying token matching, desktop BLACK locking, Android preset distinctness, and WCAG contrast thresholds. | Tests: `androidOnboardingMaterialTokensMatchApprovedDarkFinish`, `desktopAlwaysSelectsBlackButMobileHonorsPreferences`, `androidPalettesMapSemanticRolesAndRemainDistinct`, `androidSemanticRolesMeetEnabledContrastTargets`. | **Maintain & Update**: Expand contrast assertions when updating sent/received message bubble colors. |
| `docs/qryptana/release/android-copper-theme-evidence/` | Directory containing 8 desktop-hosted PNG renders of Android settings, privacy, invitations, and smart invite screens, plus `contrast-results.json`. | Rendered evidence of previous palette rollouts. Shows settings, invitations, and smart invite in Light and Dark mode. | **Reference Artifacts**: Consult to verify baseline contrast measurements and visual progression. |
| `docs/qryptana/release/android-onboarding-metal-evidence/` | Renders of earlier rejected metallic logo shader treatment (`welcome-normal-desktop-hosted.png`, `hero-closeup-desktop-hosted.png`). | Historical negative evidence of what NOT to do (unapproved logo shaders). | **Reference Only (Superseded)**. |
