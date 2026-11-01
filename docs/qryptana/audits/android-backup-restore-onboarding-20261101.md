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
