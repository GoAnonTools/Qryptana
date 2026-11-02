# Bundle id migration

Status: decided (Android)
Owner: Qryptana maintainer
Decision: Option B — adopt a new Android applicationId, `com.goanon.qryptana`, for the self-hosted Qryptana sideload build. Debug builds append `.debug`. The desktop macOS bundle ID remains `chat.simplex.app`. Qryptana is not distributed through Google Play; release APKs are self-hosted on a VPS for direct download and sideload/update.
Decision date: 2026-11-02
Closure evidence: Android `applicationId` updated in `apps/multiplatform/android/build.gradle.kts`; release signing is configured from ignored `apps/multiplatform/local.properties` (`Qryptana.storeFile`, `Qryptana.storePassword`, `Qryptana.keyAlias`, `Qryptana.keyPassword`); the FileProvider authority derives from `${applicationId}`; the debug build is verified locally. Deep-link host filters for `simplex.chat` and `*.simplex.im` remain in place for QR/deep-link compatibility.

Provenance: reconstructed qryptana-grafted; source release-readiness `2d5b1beff`; this document does not assert a completed 7.x port.

Evidence: docs/qryptana/BUNDLE_ID_MIGRATION.md; the Android applicationId is now `com.goanon.qryptana` (`com.goanon.qryptana.debug` for debug builds via the existing suffix). The desktop macOS bundle ID remains `chat.simplex.app`. Because this is a new Android package, there is no in-place upgrade path from `chat.simplex.app`; users must install the new package and use export/import for existing data. Distribution is self-hosted via VPS direct download; no Google Play listing, Play App Signing, or store-record migration is involved.

Options: (A) retain current identifiers temporarily with distribution restrictions documented; (B) adopt new identifiers as a separate app with explicit data transfer — SELECTED for Android; (C) design a platform-specific migration after signing/store ownership is established. Desktop remains on option A.

Required decisions: Android owner: Qryptana maintainer (self-hosted). Desktop owner: unchanged for this phase. Signing/store ownership evidence: maintainer-held release keystore referenced only through ignored `local.properties`; the VPS serves the signed APK. Proposed IDs: Android `com.goanon.qryptana`; desktop `chat.simplex.app`. Existing install inventory: the new Android package has no public install base under this ID; migration is manual export/import.

Closure evidence: local debug build and release signing verification; deep-link/app-link/QR scanning retained; notification and on-device update/sideload checks; signing-key custody and backup plan; user communication documenting the new package, fresh-install requirement, and manual export/import. ICNS/macOS packaging and desktop identifier changes remain separate gates.
