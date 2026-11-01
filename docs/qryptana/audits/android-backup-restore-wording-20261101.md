# Android backup and restore wording verification

Date: 2026-11-01
Branch: `android-restore-fix`
Source commit: `37bdd9ecf` (`feat(android): simplify backup and restore wording`)

## Confirmed behavior and wording scope

The Android password protects the local profile database. `DatabaseEncryptionView` calls the database encryption flow, and Android requires the password to open encrypted profile data. Archive export calls `apiExportArchive(config)` without a user/profile argument; the API specification says the archive contains the chat and agent databases. The Android export therefore includes all profiles, rather than only the selected profile. The new explanatory copy describes both local protection and the need to restore an exported backup.

The regular import path in `DatabaseView.importArchive` calls `apiDeleteStorage()` before `apiImportArchive(config)`. The warning now says that all current profiles and chats will be replaced. The Android file import under **Migrate > Restore from a backup file** uses the same confirmation. Cancelling or dismissing that confirmation does not call the import callback. The separate link based device transfer remains a transfer flow, with distinct transfer and password wording. Companion device linking, archive formats, filenames, encryption, password handling, and import/export APIs are unchanged.

The shared database views select the new IDs only when `appPlatform.isAndroid`; desktop and iOS continue to select their prior strings. The Android resource build generated the IDs and the packaged APK exposes them. No non-English translation files were edited.

## Checks performed

Using the requested JDK 21 and Android SDK with `--offline --no-daemon --max-workers=1 --no-parallel`, `-Xmx1536m`, `MaxMetaspaceSize=512m`, and in-process Kotlin compilation:

- `:common:compileDebugKotlinAndroid` — passed on the final source.
- `:android:assembleFossDebug` — passed on the final source, producing ARM64 and ARMv7 APKs.
- `git diff --check` — passed.
- `aapt dump resources` — confirmed Android-specific labels, warning, and password description resource IDs in the ARM64 APK.
- `aapt dump badging` — both APKs report `chat.simplex.app.debug`.
- `unzip -Z1` — ARM64 APK contains `lib/arm64-v8a/`; ARMv7 APK contains `lib/armeabi-v7a/`.
- `apksigner verify --verbose --print-certs` — both APKs verify with v2 signatures. Certificate SHA-256 `76fec71b0c4b3d72fd43231330967da1c9fd8d04a985c7aade7430ecb45d2d97` matches both previous `android-restore-fix-20261101` candidates.

The build emitted existing Android SDK XML version and non-positional format warnings for unrelated strings. They did not fail resource processing or packaging. A first compilation attempt during this change caught a missing footer import; that was fixed and the final compile and assemble both passed.

These checks establish resource generation, source compilation, package metadata, ABI contents, and signing. They do not establish the phone rendering or user journey. No APK was installed and no profile, database, backup, or real user data was opened or changed.

## Archived candidates

Directory: `/home/theanarchitect/code/Qryptana-release-artifacts/android-backup-restore-wording-20261101-v1/`

| ABI | APK | SHA-256 |
|---|---|---|
| ARM64 | `qryptana-7.0.3-android-foss-arm64-v8a-backup-restore-wording-debug.apk` | `a230e2abe17346b5801c5e55cc859c69b41764337fc31dfff67b8d194efc1d56` |
| ARMv7 | `qryptana-7.0.3-android-foss-armeabi-v7a-backup-restore-wording-debug.apk` | `785f05a82129bce9cf075d48b5216267ad28bb7faaf671a957a5839b3dc1b6c1` |

Both preserve the `.debug` application ID suffix and match the prior debug signing certificate. Earlier artifact directories were preserved.

## Phone checklist still required

Run only with disposable test profiles and backups, not real profile data:

1. Open **Settings > Backup & restore** and confirm the profile password description says it protects local profiles and chats and is required to restore an exported backup.
2. With at least two disposable profiles present, export **Profiles & chats**. Confirm both profiles are included and the archive can be saved.
3. Select an archive for import. Confirm the title, full replacement warning, **Import and replace**, and **Cancel** wording. Cancel and use Android Back separately; verify both leave existing profiles unchanged.
4. Confirm import only with disposable data. Verify all existing profiles are replaced and the password belonging to the backup is accepted.
5. Repeat the cancel, Back, and confirm checks from **Migrate > Restore from a backup file**. Verify the separate device-link transfer path remains distinct.
6. Check the wording in both Android themes and confirm onboarding artwork, logo, video, and animation remain unchanged.

No phone checklist item has been observed or passed in this verification.

## English strings for translation follow-up

New IDs in `apps/multiplatform/common/src/commonMain/resources/MR/base/strings.xml` (English source values only; translate in the normal localization workflow):

`android_backup_restore`, `android_restore_backup_file`, `android_export_profiles_chats`, `android_import_profiles_chats`, `android_profile_protection_password`, `android_profile_protection_password_description`, `android_export_all_profiles_description`, `android_import_replace_title`, `android_import_replace_message`, `android_import_replace_confirm`, `android_cancel`, `android_exported_title`, `android_exported_save`, `android_exported_not_all_files`, `android_imported_title`, `android_restart_to_use_imported`, `android_error_exporting`, `android_error_importing`, `android_error_deleting_for_import`, `android_error_saving_backup`, `android_wrong_profile_password`, `android_saved_profile_password_mismatch`, `android_stop_to_backup_restore`, `android_export_password_title`, `android_export_password_message`, `android_current_profile_password`, `android_new_profile_password`, `android_confirm_profile_password`, `android_enter_profile_password`, `android_open_profiles_and_chats`, `android_setup_profile_password`, `android_update_profile_password`, `android_password_needed_to_open`, `android_importing_profiles_chats`, `android_save_password_and_open`, `android_encrypt_profiles_question`, `android_profiles_will_be_encrypted`, `android_profiles_not_password_protected`, `android_password_saved_securely_in_keystore`, `android_change_profile_password_question`, `android_profile_password_will_be_updated`, `android_enter_correct_current_password`, `android_random_profile_password`, `android_profile_password_not_recoverable`, `android_save_password_in_keystore`, `android_password_saved_in_keystore`, `android_password_saved_after_restart`, `android_password_needed_each_start`, `android_random_password_can_change`, `android_store_password_securely`, `android_store_password_securely_or_lose_access`, `android_save_password`, `android_update_password`, `android_remove_password`, `android_remove_password_from_keystore`, `android_verify_profile_password`, `android_verify_password`, `android_remember_password_for_transfer`.
