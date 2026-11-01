# Android onboarding restore action audit and correction

**Date:** 2026-11-01
**Branch:** `android-restore-fix`

## Root cause and correction

The Android onboarding action in `SimpleXInfo.android.kt` was wired to `OnboardingStage.LinkAMobile`. That stage renders `LinkAMobileView`, which generates a QR code to add/link another mobile device to the current device. It does not import an existing identity or database. The label `qryptana_link_device` further confirms that this is device linking, not restore. The actual backup/transfer entry point already exists on Android from the create-profile screen: `MigrateButton` initializes `MigrationToState.PasteOrScanLink` and opens `MigrateToDeviceView`.

The correction removes only the misleading secondary action from the Android onboarding landing screen. It leaves the primary create-profile action and its approved styling intact. It does not alter desktop's `LinkAMobile` stage, existing profiles, databases, encryption, passphrases, logo, video, or animation lifecycle.

## Supported Android paths and safeguards

- **Restore from an exported database archive:** On the create-profile screen, choose **Migrate** > **Chat archive** and select the exported `.zip`. The existing import path imports the whole database and starts chat on success. It is not a per-profile merge. Use the passphrase belonging to the archive when prompted.
- **Transfer from another device:** On the source device use **Settings > Database passphrase & export > Migrate to another device** to create a transfer link. On the destination, choose **Migrate**, scan/paste the link, review/apply the network settings, and continue through archive transfer and database migration. This transfers the database; it is distinct from companion-device linking.
- **Link a companion device:** The `LinkAMobile` QR flow is for linking a second device to an existing remote host/session. It remains available through its intended app flow; it is not presented as first-run identity restore.
- **Import prerequisites:** An archive must already be available to the Android file chooser, or a transfer link/source device must be available. Database migration may require the old database passphrase. Android exposes the archive chooser and QR scanner directly from `MigrateToDeviceView`.
- **Existing profile safety:** Import is an explicit user-selected operation in the migration modal; this change does not launch it automatically or replace/create a profile on the landing screen. The import implementation is for the full chat database, so users should treat it as replacement/transfer and keep a separate backup. No existing database or profile was accessed in this verification.
- **Cancel/back:** `MigrateToDeviceView` allows close/back from the initial chooser, link entry/download/failure, and archive-import-failed states. Back is disabled during archive import, database initialization, passphrase, migration, and migration confirmation. On permitted close, `cleanUpOnBack` stops an active transfer receiver, removes migration temporary files/state, and specially resets a failed archive import when no original database exists. This correction adds no new route or back-stack state.

## Checks and package evidence

- Repository began clean on `main` at `b6f4d66e5`; the new branch was created from that tip. No applicable `AGENTS.md` was present.
- `:common:desktopTest` passed (18 Gradle tasks; test task was up to date).
- `:android:assembleFossDebug` passed (66 actionable tasks; both ABI splits).
- Local ignored `apps/multiplatform/local.properties` still specifies `application_id.suffix=.debug`; it was not modified.
- Both APK manifests report `chat.simplex.app.debug`; ARM64 contains only `arm64-v8a`, ARMv7 only `armeabi-v7a`. Both verify with APK Signature Scheme v2 and certificate SHA-256 `76fec71b0c4b3d72fd43231330967da1c9fd8d04a985c7aade7430ecb45d2d97`, matching the previous onboarding-corrections candidates.
- APKs are archived under `/home/theanarchitect/code/Qryptana-release-artifacts/android-restore-fix-20261101/`:

| ABI | APK | SHA-256 |
|---|---|---|
| ARM64 | `qryptana-7.0.3-android-foss-arm64-v8a-restore-fix-debug.apk` | `3a701aa1268c0ce788b814c2da0371082f140e1326c1ae997b7626f1f70a205a` |
| ARMv7 | `qryptana-7.0.3-android-foss-armeabi-v7a-restore-fix-debug.apk` | `89bab5b794e410575b0f4d5ad2105339550711dc4a521cd460a2dd09afd9adb4` |

## Manual device checklist — not performed here

1. On a fresh Android install, confirm the landing screen offers **Create my identity** and no action that implies restoring via device linking.
2. Choose **Create my identity**, proceed to the profile form, then choose **Migrate**. Verify the chooser offers archive and transfer/link options.
3. Cancel at the initial chooser; verify onboarding returns to profile creation without changing data.
4. Reopen Migrate and cancel/back from an available pre-import stage; verify temporary transfer state is discarded and profile creation remains available.
5. With a disposable test database only, import a known archive and verify expected passphrase prompt, whole-database behavior, and successful restart/start-chat guidance.
6. Separately test source-device transfer and destination scan/paste, including cancellation before and after link acquisition.
7. Open the intended companion-device linking flow separately and verify its QR is for adding a device, not restoring a database.
8. Repeat relevant cancel/back cases with an existing disposable profile and verify its database and passphrase remain intact unless the user explicitly confirms archive import/transfer.

No APK was installed and no physical-device behavior was observed in this verification. The checklist remains outstanding.
