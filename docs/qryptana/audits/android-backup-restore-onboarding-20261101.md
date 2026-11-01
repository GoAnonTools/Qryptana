# Android backup, restore, and deletion UX verification

Status: **blocked; not integrated**

## Changes prepared

- Added a “Restore from backup” action on Android's first welcome screen. It opens the existing archive-only importer and does not enter the companion-device link flow or create a temporary profile. The importer asks for a backup file and then requires its existing replacement confirmation before deleting current storage. Cancelling the picker or confirmation leaves storage untouched. After import, the existing initialization and encrypted-profile password flow applies; the restored active profile opens and other profiles remain available through profile selection.
- Updated Android database-management labels and the deletion confirmation. The confirmation retains the existing additional warning about contacts, messages, files, media, and saved remote-device data.
- Updated the settings import explanation to say the backup replaces current profiles and chats and profile names need not match.
- Kept technical database diagnostics and desktop/iOS resources unchanged.

## Verification blocker

`git diff --check` passes. Repeated runs of `:common:desktopTest` stop in `:common:compileKotlinDesktop` with Kotlin's generated `chat.simplex.res.MR$strings.<clinit>` exceeding the JVM method-size limit (`MethodTooLargeException`). One intervening attempt exposed an unresolved resource reference; that reference was corrected, and the next attempt again reached the method-size failure. Compacting the new Android resource identifiers and removing the duplicate Android “Cancel” resource did not resolve it.

The required sequence therefore stopped at its first task. `:common:compileDebugKotlinAndroid` and `:android:assembleFossDebug` were not run. No APK was produced, inspected, or archived; package ID, ABI, signature, signing-certificate match, and logo/video packaging remain unverified for these changes. No install or real profile access occurred.

No merge or push was attempted. The physical-device restore and deletion flows remain unverified. The new/changed English Android strings need translation follow-up; no other language was edited. Manual phone checks still needed: cancel/back before choosing a backup, cancel/back at replacement confirmation, restore an encrypted backup, select among restored profiles, and confirm the deletion warning and full deletion scope.

## Generator follow-up

The six strings introduced for the welcome restore and deletion UI have no existing resource with matching wording and meaning. The delete button and confirm action already share `android_delete_profiles_chats_action`; the duplicate “Cancel” Android ID was removed in favor of the exact shared `cancel_verb`. Reusing other strings would change the approved copy or misstate the action/result. The current base resource file contains one fewer `<string>` entry than at `9c1893dbf`, yet fresh generation followed by desktop compilation still reports the oversized initializer.

The pinned MOKO generator is 0.23.0. Its local plugin extension exposes package, class name, source set, visibility, and iOS localization settings, but no accessor batching/splitting option. The upstream 0.27.0 release notes document batching specifically to prevent this JVM method-size failure. That release also changes generated accessors and the resource source directory convention, so adopting it would be a larger MOKO migration with cross-platform source/import changes. It was not applied speculatively. No checks beyond the failed desktop compilation were run, and the branch was not merged or pushed.

## MOKO migration follow-up

Backup branch: `backup/android-restore-fix-pre-moko-20261101-aac67c58d` at `aac67c58dded9c6ba79a1f460281980bd1fa3b6e`.

The project toolchain meets MOKO's documented minimums: Gradle 8.12, Kotlin 2.1.20, AGP 8.7.0, Compose 1.8.2, and iOS deployment target 15.0. Resources and all 21 locale variants were moved unchanged to `commonMain/moko-resources`; source files now import only the generated accessors they use. SIMPLEX's external image directory is included through MOKO's documented `resourcesSourceSets`, and the old explicit 0.23 Android generated-source directory was removed.

The requested 0.27.0 build generated Kotlin declarations whose line break before a delegated `lazy` lambda failed compilation. The official 0.27.1 release fixes that exact generator output issue, so generator and runtime dependencies were consistently moved to 0.27.1, with no toolchain upgrade. See the [0.27.0 batching and accessor migration notes](https://github.com/icerockdev/moko-resources/releases) and [0.27.1 generated `lazy` formatting fix](https://github.com/icerockdev/moko-resources/releases).

Fresh `:common:generateMRcommonMain` passed. `:common:desktopTest` compiled generated batched accessors after import/font migration but stopped on the pre-existing `CIVideoView.kt` reference to `MR.images.ic_volume_off_filled`, which has no corresponding resource in the repository or generated resource set. `:common:compileDebugKotlinAndroid` first exposed the stale generated-source path, which was removed; the rerun reached that same missing image accessor. No icon was substituted or fabricated. A desktop runtime smoke test for the generated English string bundle and canonical wordmark was added, but could not run because desktop compilation is blocked.

Consequently APK assembly, packaged string/artwork/video inspection, APK signing/ABI/certificate checks, artifact archiving, and GitHub integration were not run. Resource generation confirms the original wordmark and onboarding illustration accessors exist, and the video assets remain at their original non-MOKO resource path; APK packaging is unverified. Desktop and Android source sets were attempted; iOS was not built or runtime-tested. Translation files were moved byte-for-byte, with no translated text edits. No real profiles were accessed, and no APK was installed.

## Missing muted-volume reference follow-up

The `ic_volume_off_filled` use in `CIVideoView.kt` was inside a commented-out block and had no matching resource. Upstream v7.0.3 has neither that import nor the commented block ([upstream source](https://github.com/simplex-chat/simplex-chat/blob/v7.0.3/apps/multiplatform/common/src/commonMain/kotlin/chat/simplex/common/views/chat/item/CIVideoView.kt)). The dead block and unused import were removed without changing active video behavior. Source commit: `f049d89c5`.

The subsequent `:common:desktopTest` and `:common:compileDebugKotlinAndroid` runs both stopped because the Gradle daemon thrashed its configured 1.5 GiB heap during compilation. The heap was not increased. Tests and the desktop runtime smoke test did not execute, and APK assembly was not run. No APKs were produced or archived. Merge, push, and CI verification remain blocked until the checks pass under the required memory settings.
