# HANDOFF: Android UX/Terminology Finishing Pass

## Current State
- **Repo:** Qryptana (Android-only focus)
- **Last commit:** 3ad852f2671c909619ad45f89fd15c97ddc55354 (Task 4)
- **Status:** Tasks 1–4 complete and locally verified; Task 5 (optional) pending. CI has not been re-run on these commits yet.
- **Agent:** Claude Code (DeepSeek Flash backend)

## Strict Rules (DO NOT VIOLATE)
1. **NO changes to business logic, protocol constants, or cryptographic code.**
2. **NO changes to signing configs, `applicationId`, or native library build scripts.**
3. **Only modify:** UI strings, documentation files, CI workflows, feature flags, and localization files.
4. **Preserve all placeholders and format specifiers** in strings.

## Tasks to Complete (from Re-audit Report)

### 1. Hide SimpleX Names / Crowdfunding Promo (F2) — DONE
- **Status:** Complete in commit `168557587` (2026-11-02).
- **Files:** `UserAddressView.kt`, `GroupChatInfoView.kt`, `SetSimplexNameView.kt`, plus new `platform/QryptanaFeatures.kt`
- **Action:** Gate the "Get SimpleX name (BETA)" entry points behind a feature flag that is **off by default**.
- **Implementation:**
  - Added `QryptanaFeatures.SIMPLEX_NAMES_ENABLED = false`; `QryptanaFeatures.simplexNamesVisible` now mirrors the flag directly, so the third-party SimpleX-name promotion is hidden globally on both Android and Desktop. A post-audit correction removed the earlier Android-only `appPlatform.isAndroid` condition.
  - `UserAddressView.kt`: hides the "Your SimpleX name" / "Get SimpleX name (BETA)" section and its divider, including the `SetSimplexDomainView` launcher.
  - `GroupChatInfoView.kt`: hides the channel SimpleX-name section and defensively gates the `setSimplexName` modal handler.
  - `SetSimplexNameView.kt`: hides the simplex.domains "register test name" link, the sales countdown section, and the `simplex_name_sales_footer` / simplex.domains link. Editing/removal controls remain for any pre-existing name.
  - Existing-name display and verification badges (`SimplexNameView`, `ContactSimplexNameView`, `GroupSimplexNameView`) are intentionally untouched.
- **Verification:** `:common:compileKotlinDesktop`, `:android:compileFossDebugKotlin`, and `:common:desktopTest` all passed (`BUILD SUCCESSFUL`) on 2026-11-02. Grep confirmed every `get_simplex_name_beta`, `simplex_name_sales`, `simplex.domains`, and `SetSimplexDomainView(` reference is inside a gated block.
- **Goal:** Qryptana users should not see third-party commercial promotion.

### 2. Update PRIVACY.md (F3) — DONE
- **Status:** Complete in commit `8dd62938b` (2026-11-02).
- **Action:** Rewrite the document to accurately reflect:
  - Link previews are blocked behind a consent alert (code is safer than doc).
  - Calls use `stun.simplex.im` / `turn.simplex.im` with a shared TURN credential.
  - SimpleX-names resolution is used.
  - Foreground-service/battery behavior for notifications.
- **Implemented:**
  - Link previews: documents the default-on preference plus the consent alert (`privacyLinkPreviewsShowAlert` defaults true), enable/disable/dismiss behavior, and what the eventual fetch discloses.
  - Calls: documents the WebRTC WebView, the default `stuns`/`stun`/`turns` servers at `*.simplex.im`, the presence of a hard-coded shared TURN credential embedded in `call.js` (the document deliberately does not reproduce the credential), the default relay-only policy, and user ICE overrides.
  - SimpleX names: documents that resolution depends on the configured SimpleX servers, that manual verification is available and automatic verification is off by default, and that the Task 1 flag hides registration while existing names still resolve/display.
  - Android notifications: documents the default foreground service and persistent notification, periodic WorkManager checks, battery-optimization exemption requirements, notification permission, restart behavior, and the absence of a permanent wake lock.
- **Verification:** Markdown heading/trailing-whitespace review passed and all referenced repository paths exist. No markdown linter is installed in the environment.
- **Goal:** Legal accuracy and transparency.

### 3. Add CI Guard for Production Assets (R3 residual) — DONE
- **Status:** Complete in commit `27075d5fe` (2026-11-02).
- **Action:** Add a check to the CI workflow that fails if the build uses `assets/default` instead of production assets.
- **Context:** The last CI failure exposed that CI doesn't build with production assets.
- **Implemented:**
  - `.github/workflows/qryptana-ci.yml` now sets `PRODUCTION_ASSETS_DIR: ../../assets` and passes `-Psimplex.assets.dir="${PRODUCTION_ASSETS_DIR}"` to the desktop compile, desktop test, and Android compile invocations, so CI uses the tracked production assets instead of falling back to `assets/default`.
  - Added a pre-build step that fails early if `assets/multiplatform/resources/MR/images` or the production-only `intro@2x.png` is missing.
  - Added a post-build step that fails unless `BuildConfigCommon.SIMPLEX_ASSETS` is `true`, the expected production-derived outputs exist, and the copied and generated `intro@2x.png` plus the canonical `qryptana_wordmark` match their source bytes.
  - No Gradle or build-script changes were made.
- **Verification:** Locally ran `:common:compileKotlinDesktop`, `:common:desktopTest`, and `:android:compileFossDebugKotlin` with `-Psimplex.assets.dir=../../assets`; all reported `BUILD SUCCESSFUL`. The exact post-build verification block passed. The workflow parses as valid YAML; `actionlint` is not installed.

### 4. Translate New Android Strings (Low/Medium) — DONE
- **Status:** Complete in commit `3ad852f26` (2026-11-02).
- **Action:** Translate the ~57 new `android_*` strings into German, French, Spanish, and Russian.
- **Fallback:** Accepted for this phase: the remaining 34 locale directories (the audit estimated ~33) use the English fallback for these new keys; only de/fr/es/ru were translated.
- **Implemented:**
  - Added all 57 keys to `moko-resources/de/strings.xml`, `fr/strings.xml`, `es/strings.xml`, and `ru/strings.xml`.
  - Reused the established vocabulary: DE *Profil/Chat-Profil, Datenbank, Passwort, Sicherung, Gerät, Android-Keystore*; FR *profil, base de données, mot de passe, sauvegarde, appareil, Keystore d'Android*; ES *perfil, base de datos, contraseña, copia de seguridad, dispositivo, Android Keystore*; RU *профиль, база данных, пароль, резервная копия, устройство, Android Keystore*.
  - Preserved the `\n\n` escape in the delete-all warning, the ellipsis characters, the `<b>` markup in the non-recoverable-password warning, and all other formatting. This batch contains no `%` format specifiers.
  - Kept the security warnings explicit: the password cannot be recovered or changed if lost, import replaces current profiles and chats, backup export must happen before deletion, and stopped-chat limitations remain stated.
- **Verification:** XML well-formedness, key-completeness, duplicate-key, and formatting-invariant checks passed for all four locales. Generated Android resources contain 57 `android_*` entries per locale. `:common:generateMRcommonMain`, `:common:compileKotlinDesktop`, `:common:desktopTest`, and `:android:compileFossDebugKotlin` all passed with `-Psimplex.assets.dir=../../assets` on the final tree.

### 5. (Optional) Improve Android CI (F4)
- **Action:** Add `assembleDebug` and `lint` steps to the CI pipeline.
- **Context:** Current CI only compiles and runs desktop JVM tests.

## Follow-up TODOs
- [ ] Extend the production-asset CI guard to cover launcher icons and APK-level packaged-asset checks, in addition to the current generated-resource verification.

## Out of Scope (Human Maintainer Required)
- **R1:** Package ID / Signing (`chat.simplex.app` → new ID).
- **R2:** Rebuilding native core (`libsimplex.so`) from source.
- **On-device testing:** Restore flow, MOKO fonts, TalkBack.

## Definition of Done
- All tasks above completed and committed.
- CI passes.
- A summary of changes is written back to this HANDOFF.md for the next session.
