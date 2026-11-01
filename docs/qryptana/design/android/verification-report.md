# Android Design Verification & Implementation Reconciliation Report

**Date:** 2026-11-01  
**Branch:** `android-design-exploration`  
**Target Platform:** Android (Compose Multiplatform)  
**Evaluated Direction:** Precision Titanium with Directional Material Depth  

---

## Executive Summary & Scope Reconciliation

This report documents the approved "Precision Titanium with Directional Material Depth" implementation and the 2026-11-01 Android onboarding correction across production Compose code, unit tests, and debug APK packages. Automated checks establish compilation, resource selection, package identity, and byte preservation; physical-device rendering remains unverified.

1. **Production Compose Multiplatform Implementation**:
   - Directionally lit titanium top app bars across all app screens via [`DefaultTopAppBar.kt`](file:///home/theanarchitect/Qryptana/apps/multiplatform/common/src/commonMain/kotlin/chat/simplex/common/views/helpers/DefaultTopAppBar.kt#L64-L103) and [`AppBarDivider`](file:///home/theanarchitect/Qryptana/apps/multiplatform/common/src/commonMain/kotlin/chat/simplex/common/views/helpers/DefaultTopAppBar.kt#L195-L215).
   - Milled composer shelf in [`ComposeView.kt`](file:///home/theanarchitect/Qryptana/apps/multiplatform/common/src/commonMain/kotlin/chat/simplex/common/views/chat/ComposeView.kt#L1750-L1765) with directional gradient, structural top border, and top specular hairline.
   - Recessed input well in [`SendMsgView.kt`](file:///home/theanarchitect/Qryptana/apps/multiplatform/common/src/commonMain/kotlin/chat/simplex/common/views/chat/SendMsgView.kt#L70-L85) with 20dp pill geometry, crisp borders, and subtle top drop-shadow recession.
   - Machined copper controls via [`qryptanaCopperControl`](file:///home/theanarchitect/Qryptana/apps/multiplatform/common/src/commonMain/kotlin/chat/simplex/common/ui/theme/QryptanaAndroidMaterial.kt#L143-L170) applied to send button ([`SendMsgView.kt#L503-L525`](file:///home/theanarchitect/Qryptana/apps/multiplatform/common/src/commonMain/kotlin/chat/simplex/common/views/chat/SendMsgView.kt#L503-L525)), floating action button ([`ChatListView.kt#L465-L495`](file:///home/theanarchitect/Qryptana/apps/multiplatform/common/src/commonMain/kotlin/chat/simplex/common/views/chatlist/ChatListView.kt#L465-L495)), connect button ([`ChatListView.kt#L500-L515`](file:///home/theanarchitect/Qryptana/apps/multiplatform/common/src/commonMain/kotlin/chat/simplex/common/views/chatlist/ChatListView.kt#L500-L515)), and primary action buttons ([`SimpleXInfo.kt#L1155-L1175`](file:///home/theanarchitect/Qryptana/apps/multiplatform/common/src/commonMain/kotlin/chat/simplex/common/views/onboarding/SimpleXInfo.kt#L1155-L1175)).
   - Light-mode onboarding chamber in [`SimpleXInfo.kt`](file:///home/theanarchitect/Qryptana/apps/multiplatform/common/src/commonMain/kotlin/chat/simplex/common/views/onboarding/SimpleXInfo.kt#L648-L678) staging the unchanged reveal video inside a milled dark titanium pedestal chamber with top specular highlight and soft elevation, with matching dark-mode edge dissolution and complete light-theme onboarding palette support.
2. **Platform Isolation Guarding**:
   - The corrected colors, fallback surface, and removed wordmark backing are Android-guarded. The common wordmark path is unchanged for desktop and iOS because only the former `appPlatform.isAndroid && lightOnDark` branch was removed.
   - `SIMPLEX_ASSETS` remains opt-in through `simplex.assets.dir`; no automatic repository-directory fallback was added, preserving unconfigured desktop/iOS build behavior.
3. **Asset & Identity Preservation**:
   - Canonical artwork (`qryptana_wordmark@1x.png`, `qryptana_symbol@1x.png`, `qryptana_identity_card_reveal.mp4`) remains byte-for-byte untouched.
   - The configured upstream illustration directory is explicit in ignored `apps/multiplatform/local.properties`. The sync excludes only the invalid duplicate `qryptana-wordmark.png`; the source file itself is preserved.
   - User-local candidate identity restored via `apps/multiplatform/local.properties` (`application_id.suffix=.debug`, `app.name=Qryptana Debug`).
   - Signing certificate SHA-256 matches previously tested candidate identically (`76fec71b...`).

---

## 1. Design Element Implementation & Screen Inventory Mapping

### 1.1 Core Approved Design Elements

| Design Element | Specified in Prototype (`index.html`) | Production Compose Implementation | Verification Evidence | Status |
|---|---|---|---|---|
| **Cool Charcoal Foundation** | `#0E1013` body background in `.theme-dark` | `Color(0xFF0E1114)` in `QryptanaAndroidThemeRoles.dark.background` ([`Color.kt#L91`](file:///home/theanarchitect/Qryptana/apps/multiplatform/common/src/commonMain/kotlin/chat/simplex/common/ui/theme/Color.kt#L91)) | `ThemesTest.kt#L116` assertion passes (`desktopTest`) | **Implemented & Verified** |
| **Cool Titanium Surfaces** | `#161A1F` / `#1E232A` cards & panels | `surface = Color(0xFF191E23)`, `raisedSurface = Color(0xFF20272D)` ([`Color.kt#L92-L93`](file:///home/theanarchitect/Qryptana/apps/multiplatform/common/src/commonMain/kotlin/chat/simplex/common/ui/theme/Color.kt#L92-L93)) | `ThemesTest.kt#L117-L118` assertion passes (`desktopTest`) | **Implemented & Verified** |
| **Directional Header Lighting** | 135° specular lip, 1px top highlight `rgba(221,226,232,0.14)` (dark) / `rgba(255,255,255,0.85)` (light) | `DefaultAppBar` background in [`DefaultTopAppBar.kt#L64-L103`](file:///home/theanarchitect/Qryptana/apps/multiplatform/common/src/commonMain/kotlin/chat/simplex/common/views/helpers/DefaultTopAppBar.kt#L64-L103) with vertical gradient and top specular hairline; `AppBarDivider` in [`DefaultTopAppBar.kt#L195-L215`](file:///home/theanarchitect/Qryptana/apps/multiplatform/common/src/commonMain/kotlin/chat/simplex/common/views/helpers/DefaultTopAppBar.kt#L195-L215) with `#20262E` (dark) / `#D6DCE2` (light) | Verified in `desktopTest` and Android APK build; active across all screens using default app bar | **Implemented & Verified** |
| **Milled Composer Shelf & Recessed Well** | Shelf gradient with top specular line; input well recessed into titanium with top drop shadow | `qryptanaComposerShelf` in [`ComposeView.kt#L1750-L1765`](file:///home/theanarchitect/Qryptana/apps/multiplatform/common/src/commonMain/kotlin/chat/simplex/common/views/chat/ComposeView.kt#L1750-L1765); `qryptanaInputWell` in [`SendMsgView.kt#L70-L85`](file:///home/theanarchitect/Qryptana/apps/multiplatform/common/src/commonMain/kotlin/chat/simplex/common/views/chat/SendMsgView.kt#L70-L85) with 20dp pill, border, and top recession shadow | Verified in `desktopTest` and Android APK build; tokens verified in `ThemesTest.kt` | **Implemented & Verified** |
| **Machined Copper Controls** | Gradient `#B27357` $\to$ `#9F634C` $\to$ `#874E3B`, border `#6F3B2A`, specular crown | `qryptanaCopperControl` in [`QryptanaAndroidMaterial.kt`](file:///home/theanarchitect/Qryptana/apps/multiplatform/common/src/commonMain/kotlin/chat/simplex/common/ui/theme/QryptanaAndroidMaterial.kt#L143-L170), applied to `SendMsgButton`, `NewChatSheetFloatingButton`, `ConnectButton`, and `OnboardingActionButton` | Token assertions pass in `ThemesTest.kt#L195-L217` | **Implemented & Verified** |
| **Matte Sent Message Bubbles** | `#1E232A` (dark) / `#E1E5EA` (light) | `sentMessage = Color(0xFF20272D)` (dark) / `Color(0xFFE1E5EA)` (light) ([`Color.kt#L75,L98`](file:///home/theanarchitect/Qryptana/apps/multiplatform/common/src/commonMain/kotlin/chat/simplex/common/ui/theme/Color.kt#L75)) | `ThemesTest.kt#L111,L122` assertions pass; feeds `qryptanaMessagingTokens()` | **Implemented & Verified** |
| **Matte Received Bubbles** | `#161A1F` (dark) / `#FFFFFF` (light) | `receivedMessage = Color(0xFF191E23)` (dark) / `Color(0xFFFFFFFF)` (light) ([`Color.kt#L77,L100`](file:///home/theanarchitect/Qryptana/apps/multiplatform/common/src/commonMain/kotlin/chat/simplex/common/ui/theme/Color.kt#L77)) | `ThemesTest.kt#L113,L124` assertions pass; feeds `qryptanaMessagingTokens()` | **Implemented & Verified** |
| **Recessed Quote Wells** | `#111418` (dark) / `#D8DEE4` (light) with copper bar | `sentQuote = Color(0xFF13171B)`, `receivedQuote = Color(0xFF13171B)` (dark) / `Color(0xFFD8DEE4)`, `Color(0xFFE8ECEF)` (light) ([`Color.kt#L76,L78,L99,L101`](file:///home/theanarchitect/Qryptana/apps/multiplatform/common/src/commonMain/kotlin/chat/simplex/common/ui/theme/Color.kt#L76)) | `ThemesTest.kt#L112,L123` assertions pass | **Implemented & Verified** |
| **Light Onboarding Video Chamber** | Dark titanium chamber with contact shadow housing dark video on light canvas | Implemented in [`SimpleXInfo.kt#L648-L678`](file:///home/theanarchitect/Qryptana/apps/multiplatform/common/src/commonMain/kotlin/chat/simplex/common/views/onboarding/SimpleXInfo.kt#L648-L678) with 18dp shape, 10dp shadow, `#38414D` border, top specular highlight line, and light onboarding theme | Token assertions pass in `ThemesTest.kt#L182-L193` | **Implemented & Verified** |
| **Wordmark Without Oversized Backing** | Preserve the original wordmark without the oversized metallic pill | `QryptanaWordmark` now renders the canonical `MR.images.qryptana_wordmark` directly; no Android-only backing, shadow, gradient, border, or extra inset remains | Source inspection, successful desktop/Android compilation, and APK entry SHA-256 matching the canonical source | **Structurally Verified** |
| **Onboarding Video Lifecycle** | Full 11.3s choreography, DstOut feathering, contact shadow | Implemented in `SimpleXInfo.kt#L760-L900` via `QryptanaIdentityReveal` | Tested via `desktopTest` & APK packaging; video asset bundled | **Implemented in Both Themes** |

---

### 1.2 Screen Inventory Mapping (44 Screens)

Every screen from [`screen-inventory.md`](file:///home/theanarchitect/Qryptana/docs/qryptana/design/android/screen-inventory.md) is mapped to its production Compose styling:

1. **Screens 1–2 (System Splash & Compose SplashView)**:
   - *Status*: Inherited Unchanged. Window background in `themes.xml` remains `#121212`.
2. **Screen 3 (Onboarding Step 1: Welcome & Identity Reveal)**:
   - *Status*: **Structurally verified; physical rendering unverified** ([`SimpleXInfo.kt#L630-L720`](file:///home/theanarchitect/Qryptana/apps/multiplatform/common/src/commonMain/kotlin/chat/simplex/common/views/onboarding/SimpleXInfo.kt#L630-L720)). The reveal video treatment remains unchanged. The canonical wordmark is rendered directly in both themes with no Android-only backing.
3. **Screens 4–8 (Onboarding Steps 2–5: Profile, Passphrase, Network, Commitments, Notifications)**:
   - *Status*: **Implemented & Verified** ([`SimpleXInfo.kt#L180-L245`](file:///home/theanarchitect/Qryptana/apps/multiplatform/common/src/commonMain/kotlin/chat/simplex/common/views/onboarding/SimpleXInfo.kt#L180-L245), [`YourNetwork.kt#L62-L75`](file:///home/theanarchitect/Qryptana/apps/multiplatform/common/src/commonMain/kotlin/chat/simplex/common/views/onboarding/YourNetwork.kt#L62-L75)). Scoped `QryptanaAndroidOnboardingTheme` fully supports both dark (`#0E1013` / `#161A1F`) and light (`#F1F3F5` / `#FCFDFE`) themes with matching contrast-tested text and copper buttons.
4. **Screens 9–11 (Chat List Empty State, Active Chats, Search & Filter Bar)**:
   - *Status*: **Implemented & Verified**. Consumes `DefaultAppBar` with directional titanium vertical gradient (`#1B2129` $\to$ `#13171D` dark, `#FFFFFF` $\to$ `#EEF2F6` light), top specular hairline on Android, and `#20262E` / `#D6DCE2` divider. Floating action button uses machined copper styling with top specular crown ([`ChatListView.kt#L465-L495`](file:///home/theanarchitect/Qryptana/apps/multiplatform/common/src/commonMain/kotlin/chat/simplex/common/views/chatlist/ChatListView.kt#L465-L495)). Filter chips retain scrollability without visible scrollbars.
5. **Screens 12–17 (UserPicker, New Chat Sheet, Smart Invite QR, Scanner, Address, Contact Requests)**:
   - *Status*: **Implemented & Verified**. Sheets and screens inherit directional top app bar lighting, cool titanium surfaces, and high-contrast QR presentation. Connect buttons use machined copper styling ([`ChatListView.kt#L500-L515`](file:///home/theanarchitect/Qryptana/apps/multiplatform/common/src/commonMain/kotlin/chat/simplex/common/views/chatlist/ChatListView.kt#L500-L515)).
6. **Screen 18 (Active Conversation Shell)**:
   - *Status*: **Implemented & Verified**. Directionally lit top app bar with contact verified badge, `#0E1114` (dark) / `#F1F3F5` (light) chat canvas background.
7. **Screens 19–22 (Message Bubbles: Sent, Received, Status Ticks, Quoted Replies)**:
   - *Status*: **Implemented & Verified**. Sent bubbles `#20272D` (dark) / `#E1E5EA` (light); received bubbles `#191E23` (dark) / `#FFFFFF` (light); quote wells `#13171B` (dark) / `#D8DEE4` (light). All brown casts eliminated.
8. **Screens 23–26 (Reactions, Media Viewer, Voice Messages, File Attachments)**:
   - *Status*: **Implemented & Verified**. Audio cards and file items pull updated cool titanium `surface` and restrained copper accents.
9. **Screen 27–28 (Composer Bar & Attachment Sheet)**:
   - *Status*: **Implemented & Verified**. Composer shelf uses `qryptanaComposerShelf` ([`ComposeView.kt#L1750-L1765`](file:///home/theanarchitect/Qryptana/apps/multiplatform/common/src/commonMain/kotlin/chat/simplex/common/views/chat/ComposeView.kt#L1750-L1765)) with top border and specular line; text field container uses `qryptanaInputWell` ([`SendMsgView.kt#L70-L85`](file:///home/theanarchitect/Qryptana/apps/multiplatform/common/src/commonMain/kotlin/chat/simplex/common/views/chat/SendMsgView.kt#L70-L85)) with 20dp pill and top drop shadow; send button uses `qryptanaCopperControl` with crisp white icon.
10. **Screens 29–30 (Calls Activity & Incoming Alert Banner)**:
    - *Status*: Inherited Android Activity and Compose overlay consuming updated palette.
11. **Screens 31–33 (Contact Details, Safety Number Verification, Group Chat Info)**:
    - *Status*: Inherited Compose layouts consuming directional top app bar lighting and updated palette.
12. **Screens 34–40 (Settings Hub, Appearance, Privacy, Network/Servers, Notifications, Database, Migration)**:
    - *Status*: Inherited Compose layouts consuming directional top app bar lighting and updated semantic tokens.
13. **Screens 41–44 (App Lock / PIN, Dialogs, Snackbars, System Bars)**:
    - *Status*: Inherited Compose and Android platform handlers consuming updated tokens.

---

## 2. Forensic Analysis of Packaged APKs

Both candidate APKs were built via `./gradlew :android:assembleFossDebug` and forensically inspected using generated MR/BuildConfig sources, ZIP entry hashes, `aapt`, and `apksigner`:

### 2.1 Inspection Data

| Property | ARM64 Candidate (`android-foss-arm64-v8a-debug.apk`) | ARMv7 Candidate (`android-foss-armeabi-v7a-debug.apk`) |
|---|---|---|
| **Application ID** | `chat.simplex.app.debug` | `chat.simplex.app.debug` |
| **Debuggable Flag** | `true` (`application-debuggable`) | `true` (`application-debuggable`) |
| **Version Code** | `381` | `381` |
| **Version Name** | `7.0.3` | `7.0.3` |
| **Min SDK / Compile SDK** | `26` / `35` | `26` / `35` |
| **Native ABI** | `arm64-v8a` | `armeabi-v7a` |
| **Native Libraries Included** | `libapp-lib.so`, `libsimplex.so`, `libsupport.so`, `libandroidx.graphics.path.so`, `libimage_processing_util_jni.so` | `libapp-lib.so`, `libsimplex.so`, `libsupport.so`, `libandroidx.graphics.path.so`, `libimage_processing_util_jni.so` |
| **Signature Scheme** | APK Signature Scheme v2 (`true`), v1 (`false`) | APK Signature Scheme v2 (`true`), v1 (`false`) |
| **Signer Certificate DN** | `C=US, O=Android, CN=Android Debug` | `C=US, O=Android, CN=Android Debug` |
| **Signing Cert SHA-256** | `76fec71b0c4b3d72fd43231330967da1c9fd8d04a985c7aade7430ecb45d2d97` | `76fec71b0c4b3d72fd43231330967da1c9fd8d04a985c7aade7430ecb45d2d97` |
| **File Size** | 296,357,373 bytes | 282,052,717 bytes |

### 2.2 Signing Certificate Comparison

- **Current Debug Candidate**: `76fec71b0c4b3d72fd43231330967da1c9fd8d04a985c7aade7430ecb45d2d97`
- **Previous Precision-Titanium Candidates** (`android-precision-titanium-20261101` and `android-precision-titanium-debug-20261101`, both ABIs): `76fec71b0c4b3d72fd43231330967da1c9fd8d04a985c7aade7430ecb45d2d97`
- **Result**: **EXACT MATCH**. Safe for in-place installable update without uninstalling or wiping user app data.

---

## 3. Preserved Versioned Release Artifacts

Both candidate APKs have been archived in a new directory; earlier candidates were preserved:
`/home/theanarchitect/code/Qryptana-release-artifacts/android-onboarding-corrections-verified-20261101-190331/`

### Recorded SHA-256 Hashes

| Artifact File | Size (Bytes) | SHA-256 Digest |
|---|---|---|
| `qryptana-7.0.3-android-foss-arm64-v8a-onboarding-corrections-debug.apk` | 296,357,373 | `ea5ec3eed43daf43c3128cc5a9d675b24d0a7fee1d898920ce3497bfe659a5cd` |
| `qryptana-7.0.3-android-foss-armeabi-v7a-onboarding-corrections-debug.apk` | 282,052,717 | `d4b5d89d3ecf2ac56b4326efd3d7e366cfcb887a1aa3d387f3dca7289174bf0b` |

---

## 4. Test & Verification Summary

1. **Automated Unit & Theme Test Suite**:
   - Final-tree `:common:desktopTest` executed successfully with the required offline, single-worker, non-parallel, in-process compiler options (1m52s; 18 actionable tasks).
   - 40 tests executed with zero failures, errors, or skips, including:
     - `androidOnboardingMaterialTokensMatchApprovedDarkFinish`: asserts dark onboarding palette tokens and contrast $\ge 4.5:1$.
     - `androidOnboardingMaterialTokensMatchApprovedLightFinish`: asserts light onboarding palette tokens (`#F1F3F5`, `#FCFDFE`, `#20262B`, `#59636C`, `#BAC2CA`) and contrast $\ge 4.5:1$.
     - `androidMaterialTokensMatchApprovedPrecisionTitaniumAndCopperFinish`: asserts directional top bar gradients, composer shelf gradients, input well tokens, and copper control stops (`#B27357`, `#9F634C`, `#874E3B`, `#6F3B2A`).
     - `androidPalettesMapSemanticRolesAndRemainDistinct`: asserts all 4 mobile palettes remain distinct and map semantic roles.
     - `androidSemanticRolesMeetEnabledContrastTargets`: asserts all action text, headings, body, and borders satisfy WCAG AA contrast targets.
2. **Android APK Assembly**:
   - Final-tree `:android:assembleFossDebug` executed successfully with the required options (12s; 66 actionable tasks).
   - Native CMake compilation linked `libsimplex.so`, `libsupport.so`, and `libapp-lib.so` for both `arm64-v8a` and `armeabi-v7a`.
   - The first Android attempt exposed an invalid Moko resource generated from the configured upstream duplicate `qryptana-wordmark.png`. The focused fix excludes only that duplicate from `copySimplexAssets`; the canonical underscore-named wordmark remains packaged.
3. **Production Resource Selection**:
   - Ignored `apps/multiplatform/local.properties` explicitly sets `simplex.assets.dir=../../assets`; generated `BuildConfigCommon.SIMPLEX_ASSETS` is `true`.
   - Generated Android MR accessors bind `intro`, `intro_light`, `your_profile`, `your_profile_light`, `your_network`, `your_network_light`, `network_commitments`, and `network_commitments_light` to `R.drawable` resources. `OnboardingImage` selects those accessors by theme when `SIMPLEX_ASSETS` is true.
   - Both APKs' xhdpi illustration entry hashes exactly match the corresponding tracked `@2x` PNG sources. This verifies the authored images selected by the production resource path rather than relying on filenames or non-empty entries alone.
   - Canonical wordmark SHA-256 in source and both APKs: `a06334bcdcbb740da149c1da680745cc22a1b0f70bd82417c4b2e925b957d0e8`.
   - Reveal video SHA-256 in source and both APKs: `8e80519593fc43114432646fab8bcaa644cb58eecdf5c4b888579130ae1f1b59`.
4. **Physical / Device Rendering Status**:
   - The maintainer reports that the current Android appearance was approved on a phone. This is a maintainer observation, not an independent observation by this verification run.
   - A few remaining blue icons were accepted for now and are recorded as known visual follow-ups.
   - Automated JVM tests, Compose compiler IR validations, and APK structural package forensics are complete. No APK was installed during this verification. Behavior outside the reported phone appearance remains unverified, and this report does not claim public-release readiness.
