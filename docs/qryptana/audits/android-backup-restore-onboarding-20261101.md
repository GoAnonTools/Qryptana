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
