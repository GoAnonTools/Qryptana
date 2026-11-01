# Android backup, restore, and deletion UX verification

Status: **verification passed; integration pending**

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

## Final verification continuation (2026-11-01)

The available-memory check reported 6.33 GiB available RAM; swap had 511 MiB total with virtually all in use. No Gradle/Kotlin build process was competing, and no process was terminated. The requested task-scoped 3 GiB heap was used with one worker, no parallelism, no daemon reuse, and in-process Kotlin compilation. No clean task was run and no permanent memory configuration changed.

- `:common:desktopTest` — passed, 41 tests across 12 XML suites, 0 failures/errors. This includes `MokoResourcesDesktopTest.generatedStringBundleAndWordmarkLoadAtRuntime[desktop]`, which resolves the restore string and decodes the canonical wordmark through the desktop runtime resource API. The test needed its image accessor corrected from the obsolete `getImage()` call to MOKO 0.27.1's `image` property; that focused test correction is commit `cfc1f6d19`.
- `:common:compileDebugKotlinAndroid` — passed.
- `:android:assembleFossDebug` — passed for ARM64 and ARMv7. Existing resource-format and Android API warnings remain non-fatal.
- SIMPLEX assets are opt-in through ignored `apps/multiplatform/local.properties` (`simplex.assets.dir=../../assets`). Gradle's production resource source points at `assets/multiplatform/resources/MR/images`; it verifies the directory and copies those local assets. No upstream assets were downloaded or newly enabled. The production onboarding UI selects the themed `intro`, `create_profile`, `your_profile`, `your_network`, and `network_commitments` accessors. Both APKs contain both light/dark variants of the ten source `@2x` illustration PNGs, byte-for-byte matching their tracked sources.
- Both APKs contain the canonical `qryptana_wordmark` selected by `SimpleXInfo`; the packaged wordmark hash matches the source (`a06334bcdcbb740da149c1da680745cc22a1b0f70bd82417c4b2e925b957d0e8`). The reveal video remains at its original resource path and its APK bytes match source (`8e80519593fc43114432646fab8bcaa644cb58eecdf5c4b888579130ae1f1b59`). Android compiled resources include the Android-specific restore, backup, export/import, password, and deletion strings.
- Both APKs report `chat.simplex.app.debug`, each contains only its expected ABI (`arm64-v8a` or `armeabi-v7a`), and `apksigner` verifies v2 signatures. Both signer certificate SHA-256 fingerprints are `76fec71b0c4b3d72fd43231330967da1c9fd8d04a985c7aade7430ecb45d2d97`, matching the prior backup/restore candidates.

Archived (outside the repository) in `/home/theanarchitect/code/Qryptana-release-artifacts/android-restore-moko-volume-fix-20261101-v1/`:

| ABI | APK | SHA-256 |
| --- | --- | --- |
| ARM64 | `qryptana-7.0.3-android-foss-arm64-v8a-restore-moko-debug.apk` | `9a3bebcef65078bdb3af8454f53d8879b5d008ca148a537069dc370aeb010e71` |
| ARMv7 | `qryptana-7.0.3-android-foss-armeabi-v7a-restore-moko-debug.apk` | `629b51b65e92c58e0465dbab8d4e7b512b3e482d520fda125e2a6b04c05e8961` |

No APK was installed. Restore/import cancellation, profile selection, and physical rendering are not established by these automated checks. The maintainer previously approved the phone appearance; several remaining blue icons are accepted visual follow-ups. Translations beyond English remain pending. The resource smoke test validates desktop runtime lookup, but iOS was not built or tested. No release/publication readiness is claimed.
