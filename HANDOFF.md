# HANDOFF: Android UX/Terminology Finishing Pass

## Current State
- **Repo:** Qryptana (Android-only focus)
- **Last code commit:** 1685575870391c5fd448380a6b9e5db0658634fd (Task 1 / F2)
- **Status:** Task 1 complete and locally verified; Tasks 2–5 pending. CI has not been re-run on this commit yet.
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
  - Added `QryptanaFeatures.SIMPLEX_NAMES_ENABLED = false`; the UI check is `QryptanaFeatures.simplexNamesVisible` (`SIMPLEX_NAMES_ENABLED && appPlatform.isAndroid`), so this Android pass leaves desktop behavior unchanged.
  - `UserAddressView.kt`: hides the "Your SimpleX name" / "Get SimpleX name (BETA)" section and its divider, including the `SetSimplexDomainView` launcher.
  - `GroupChatInfoView.kt`: hides the channel SimpleX-name section and defensively gates the `setSimplexName` modal handler.
  - `SetSimplexNameView.kt`: hides the simplex.domains "register test name" link, the sales countdown section, and the `simplex_name_sales_footer` / simplex.domains link. Editing/removal controls remain for any pre-existing name.
  - Existing-name display and verification badges (`SimplexNameView`, `ContactSimplexNameView`, `GroupSimplexNameView`) are intentionally untouched.
- **Verification:** `:common:compileKotlinDesktop`, `:android:compileFossDebugKotlin`, and `:common:desktopTest` all passed (`BUILD SUCCESSFUL`) on 2026-11-02. Grep confirmed every `get_simplex_name_beta`, `simplex_name_sales`, `simplex.domains`, and `SetSimplexDomainView(` reference is inside a gated block.
- **Goal:** Qryptana users should not see third-party commercial promotion.

### 2. Update PRIVACY.md (F3)
- **Action:** Rewrite the document to accurately reflect:
  - Link previews are blocked behind a consent alert (code is safer than doc).
  - Calls use `stun.simplex.im` / `turn.simplex.im` with a shared TURN credential.
  - SimpleX-names resolution is used.
  - Foreground-service/battery behavior for notifications.
- **Goal:** Legal accuracy and transparency.

### 3. Add CI Guard for Production Assets (R3 residual)
- **Action:** Add a check to the CI workflow that fails if the build uses `assets/default` instead of production assets.
- **Context:** The last CI failure exposed that CI doesn't build with production assets.

### 4. Translate New Android Strings (Low/Medium)
- **Action:** Translate the ~57 new `android_*` strings into German, French, Spanish, and Russian.
- **Fallback:** If full translation is not feasible, document the English fallback as accepted in the handoff.

### 5. (Optional) Improve Android CI (F4)
- **Action:** Add `assembleDebug` and `lint` steps to the CI pipeline.
- **Context:** Current CI only compiles and runs desktop JVM tests.

## Out of Scope (Human Maintainer Required)
- **R1:** Package ID / Signing (`chat.simplex.app` → new ID).
- **R2:** Rebuilding native core (`libsimplex.so`) from source.
- **On-device testing:** Restore flow, MOKO fonts, TalkBack.

## Definition of Done
- All tasks above completed and committed.
- CI passes.
- A summary of changes is written back to this HANDOFF.md for the next session.
