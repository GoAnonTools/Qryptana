# Local release candidate report

Later Linux candidate: [toolchain-acceptance-report.md](toolchain-acceptance-report.md) records the separately archived Temurin build that resolves the launcher helper abort. Original artifacts and this historical report are preserved; full UI and release gates remain open.

Date: 2026-10-30. Outcome: local Linux runnable distribution and debug Android APKs produced; full runtime acceptance remains incomplete. Nothing published, uploaded, signed with production keys, installed on a physical device or pushed.

Durable worktree: `/home/theanarchitect/code/Qryptana-release-validation`, branch `qryptana-release-validation`, created from verified migrated checkpoint `97dd441a10217aaf82531e002122409593e958f5`. Application/build source is unchanged from that checkpoint and the tested implementation `3ad2abd2c1dc19a57871cdca3fabe4b86e123165`. The preceding release-gate documentation commit is `dd46149d8`. The final report commit is the branch-tip documentation checkpoint printed in the final handoff and recorded in `/home/theanarchitect/code/Qryptana-release-artifacts/FINAL_CHECKPOINT.txt`; resolve it with `git log -1 --format=%H -- docs/qryptana/release/release-candidate-report.md`.

## Durability and preserved work

Actual starting state: release-readiness clean at 2d5b1beff; qryptana-grafted clean at 6e3cf9b7c; qryptana-7x clean at 97dd441a1. No branch tips or existing files were reset, rewritten, deleted or overwritten. No applicable AGENTS.md found. Root upstream/attribution guidance, migration report, remediation report and existing decisions were consulted; no repeat migration or broad audit performed.

`git worktree move /tmp/qryptana-grafted /home/theanarchitect/code/Qryptana-grafted` failed with Invalid cross-device link (tmpfs to disk). Moving qryptana-7x failed because Git refuses moves of worktrees containing submodules. No plain filesystem move attempted. Both registered /tmp worktrees remain intact. Safe alternative implemented: a complete new durable worktree at the same migrated commit, with the pinned submodule initialized. All task commits are also preserved by refs in the durable repository's .git directory. The new worktree plus durable artifact directory secure this task without removing originals. Future cleanup/moves need a separately reviewed worktree recreation strategy, not mv.

## Changes and baseline

No source fixes or dependency updates were needed. Changes are the evidence/report files and updated release-gate document only. Ignored local native inputs and local.properties supply the matching binaries and supported debug configuration; they are not committed. Android debug application ID is `chat.simplex.app.debug` via the existing supported `.debug` suffix; production ID remains `chat.simplex.app`. Desktop bundle/package settings, themes, disabled updater, empty upstream-asset selection, null support and manual-only website publishing were not altered.

Validated existing committed final-results.json and all 11 test XML files: **35 tests, zero failures/errors/skips**. Git diff from tested 3ad2abd2c to migrated 97dd441a1 has no application or CI change. These unchanged tests were reused rather than rerun. New packaging runs compiled desktop entry/JNI and Android code/resources/native bridge. No implementation change was made, so the conditional requirement to rerun all three final Gradle checks was not triggered. No test was removed or suppressed.

## Native prerequisites and provenance

Host: Linux x86_64, installed GHC 9.6.3, Cabal 3.10.1.0, GCC, CMake, patchelf, JDK 21 and SDK. Initial RAM: 15149 MiB total, 6755 MiB available, swap already full. Disk: 415 GiB available on /home. Builds used one worker sequentially; heap not increased. Native build was unnecessary because exact official release inputs were available; no resource-intensive Haskell build started. No system tools installed or SDK licenses accepted.

Target remains upstream v7.0.3 at `24614e24a7bfda24fe3e8ff4abf30239f2686a37`, core source version 7.0.0.12. Official asset metadata retrieved from https://api.github.com/repos/simplex-chat/simplex-chat/releases/tags/v7.0.3 is preserved as upstream-release-metadata.json in the artifact directory. Downloads use that release's HTTPS GitHub URLs and passed its SHA-256 asset digests:

- simplex-desktop-ubuntu-24_04-x86_64.deb: `67fa2919fc1bc0e034a9f014e8d4b2ba9072be2056a44436c230fc4a6fa3d839`.
- simplex-aarch64.apk: `d535e5a8986d4e11b9abd5656a3c0e1be5a08e06b00a7b20d6b7587ed0c3457f`.
- simplex-armv7a.apk: `3a89d2f825ce1a30ff51b30487c1b0b58de6eba03426d1500112b7417e659fdc`.

Linux libraries and bundled VLC were extracted from the official deb using ar/bsdtar, without installing or running upstream SimpleX. Android libsimplex.so and libsupport.so were extracted for arm64-v8a/armeabi-v7a. Required Linux dynamic dependencies resolve with ldd. JNI C sources match the selected upstream commit exactly; the only downstream Haskell integration change is the policy path. Retained policy and target policy both have blob `5713f8e13463a15755e386726ec3656fa3bed190`. No protocol/crypto modification or arbitrary old native binary substitution. NanoHTTPD is the pinned efb2ebf85a2b06f7c508aba9eaad5377e3a01e81 submodule.

Per-file native hashes/byte sizes are in evidence/native-manifest.json (including the rebuilt Linux JNI bridge and runtime dependencies). Provenance establishes origin/integrity against official release metadata; independent binary reproduction and upstream signing-key verification were not performed. No claim that the libraries were rebuilt from source here. A source rebuild route is scripts/desktop/build-lib-linux.sh with GHC 9.6.3 and cabal.project pinned dependencies; Android uses scripts/android/build-android.sh and flake.nix pinned toolchain. Review those scripts/environment before running; do not use moving latest CI downloads from download-libs.sh as a substitute for target provenance.

## Build commands and results

From apps/multiplatform, each command used command-scoped ANDROID_HOME and ANDROID_SDK_ROOT=/home/theanarchitect/Android/Sdk and the following options:

```
--no-daemon --max-workers=1 --no-parallel
-Dorg.gradle.jvmargs="-Xmx1536m -XX:MaxMetaspaceSize=512m -Dfile.encoding=UTF-8"
-Pkotlin.compiler.execution.strategy=in-process
```

| Command | Result |
|---|---|
| ./gradlew :desktop:tasks --all | Exit 0; verified createDistributable task exists. |
| ./gradlew :desktop:createDistributable | Exit 0, 2m1s, 25 tasks; CMake JNI, desktop Kotlin and self-contained app image built. |
| ./gradlew :android:assembleFossDebug | Exit 0, 4m23s, 65 tasks; both supported ABI APKs built, debug signed. |
| SDK build-tools/36.0.0/apksigner verify --verbose (each APK) | Exit 0; APK v2 scheme verified, one signer, no production-signing claim. |
| SDK build-tools/36.0.0/aapt dump badging (ARM64 APK) | Exit 0; chat.simplex.app.debug, 7.0.3 / 381, min SDK 26, compile SDK 35. |

The initially assumed build-tools/35.0.0 inspection path was absent; corrected to installed 36.0.0. No software installation or build retry needed. Full build/verification logs are in evidence and the durable artifact directory. Linux installer task was not attempted: dpkg-deb is absent. The runnable app image satisfies local Linux distribution scope; .deb installation and system integration are not verified.

## Local artifacts

Directory: `/home/theanarchitect/code/Qryptana-release-artifacts/` (outside tracked source).

| Artifact | Bytes | SHA-256 |
|---|---|---|
| qryptana-7.0.3-linux-x86_64-local.tar.gz | 388669580 | `cf299c5faf220b5039ee84e64d9616f078e717458bd7894be8d02da5785bad9f` |
| android-foss-arm64-v8a-debug.apk | 275322182 | `40d1927698d460bce7c7395b46bb2701895a17ba6b4fa29f2f9d4f707e56ca5c` |
| android-foss-armeabi-v7a-debug.apk | 261017526 | `173c97e424ba60b084613158bbb90d28314ab29889b35e06365da933d10e712d` |

Linux unpacked app image: `/home/theanarchitect/code/Qryptana-release-validation/apps/multiplatform/release/main/app/qryptana`; launcher `bin/qryptana`. Archive includes the self-contained runtime/resources. Native and original download manifests, logs, notices and receipts are alongside artifacts. No artifact uploaded. Debug APKs are not production releases.

## Runtime smoke results

Linux launcher ran with XDG_CONFIG_HOME, XDG_DATA_HOME, XDG_CACHE_HOME under `/tmp/qryptana-rc-profile/`, and JAVA_TOOL_OPTIONS set java.io.tmpdir and java.util.prefs.userRoot inside that same isolated root. Existing user config/database was not opened or migrated. Local first-run encrypted database/settings initialized. No external account/contact created and no messages sent; onboarding remained at its initial step.

Verified: native loading and first-run initialization; live Qryptana window; copper/titanium branded onboarding; identity-card artwork/video surface rendered; scrolling exposes Create my identity / Restore my identity. Screenshots: evidence/qryptana-rc-onboarding.png and evidence/qryptana-rc-scroll.png. The initial apparent clipping was scroll position, not a demonstrated layout defect.

Unverified: completion of onboarding, Settings, Appearance, runtime null-support visibility and updater UI/default behavior. Pointer and keyboard approaches did not reliably navigate; X11 reported the window minimized between tool calls, including a BadMatch focus error. Automation stopped rather than treating clicks as successful. Existing meaningful theme/preference tests plus source safeguards remain valid, but do not close these runtime gates. No production workaround or stubs introduced.

Startup log contains “pure virtual method called / terminate called without an active exception” before the JVM options line. The app nevertheless initialized/rendered and remained running. Its cause is unresolved and must be reproduced/traced before full runtime acceptance; it was not hidden or declared harmless. Test PID 1500589 was deliberately closed with SIGTERM (exit 143), not reported as a crash. The temporary profile is retained for diagnostics, outside Git; it is not a real user identity and should not be distributed.

## Remaining gates and next actions

| Gate | Category | Next concrete action |
|---|---|---|
| Full onboarding/Settings/Appearance/support/updater smoke and launcher diagnostic | Technically blocked in this automation environment | Run the archive manually with isolated XDG/temp paths, finish disposable local onboarding, capture each screen and trace/reproduce startup diagnostic. Do not use an existing chat profile. |
| Android runtime/install/rendering/notifications | Requires physical-device testing | Obtain explicit installation authorization and use checklist below on clean ARM test hardware. |
| Linux distro installer integration | Technically blocked by missing dpkg-deb | Build/install a .deb in a prepared packaging environment; app image remains runnable for local testing. |
| Native binary independent reproduction/ABI regression across OSes | Unperformed | Rebuild pinned source with documented toolchain, compare exported ABI and test database/backup compatibility. Current verification is Linux runtime startup plus APK linkage/build, not all core APIs. |
| Privacy/operator/contact and name/artwork/source-distribution review | Requires legal review | Actual operators/rights owners and counsel complete existing privacy/trademark decisions; no legal approval inferred. |
| Locale/store copy and English fallbacks | Requires maintainer input | Assign reviewers to locale-followup.md and obtain reviewed translations. No hand translation performed. |
| Distribution/signing scope, owner assignments and existing install inventory | Requires maintainer input | Record platform/release owners and distribution scope; preserve IDs/current conservative defaults until a separate migration decision. |
| Video accessibility/performance and macOS ICNS/iOS/Windows | Unperformed / scope decision | Measure on selected target platforms; assign owners before claiming their support. Video remains bundled and its rights/accessibility decision remains open. |

Smallest useful maintainer questions (not needed to produce these local builds): who owns the intended release platforms and can authorize a clean ARM device install; who owns the outstanding translation and legal reviews; and should support remain hidden for the intended test scope or is a verified typed contact now available? Existing decisions already specify desktop BLACK/copper, stock Android palettes, retained IDs, bundled video and disabled publishing/updates; these are not asked again. No signing material or contact was invented.

Prioritized physical-device checklist (no device commands were executed beyond enumeration):

1. Obtain explicit authorization; record device/OS/ABI and confirm no existing chat.simplex.app.debug installation/data would be replaced. Keep production app/data untouched; do not uninstall/clear data as a test shortcut.
2. Verify SHA-256 and apksigner result; select matching ABI APK. After authorization and a clean test target only: `adb -s SERIAL install /home/theanarchitect/code/Qryptana-release-artifacts/android-foss-arm64-v8a-debug.apk` (use ARMv7 file for that ABI). Do not add -r without a separately approved replacement/data plan.
3. Launch `adb -s SERIAL shell monkey -p chat.simplex.app.debug -c android.intent.category.LAUNCHER 1`; capture logcat and screenshots. Complete only disposable local onboarding; no contacts/accounts/messages required.
4. Check Light/Dark/SimpleX/Black stock palettes, Settings/Appearance, hidden unconfigured support, disabled updater/defaults, no upstream binary offered; review text/layout/English fallback and accessibility/font scale.
5. Check launcher/adaptive/notification icons, offline launch, restart, lifecycle/background behavior, video/reduced-motion and local encrypted export/import on disposable data. Require separate approval before network messaging/call/contact testing.

`adb devices` found no device. The available Medium_Phone_API_37.0 AVD is x86_64, while repository native builds support ARM64/ARMv7; /dev/kvm is unavailable. It was not launched or modified. Physical-device, Haskell-source build, protocol/security testing, packaged Linux installer, traffic audit, other-OS and remote CI verification remain unperformed. Compilation alone is not runtime correctness or public-release readiness.

Final state: clean validation worktree after this documentation commit; ignored native inputs/build outputs/local.properties intentionally retained. Both original temporary worktrees and all existing branches remain. Artifacts and evidence are durable; diagnostic profile remains in /tmp and the test process is closed. No release, upload, push or public installation performed.
