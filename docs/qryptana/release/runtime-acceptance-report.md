# Linux runtime acceptance

Date: 2026-10-30. Outcome: **blocked**. The startup diagnostic is attributed; full Linux navigation acceptance is incomplete. No production code or artifact was changed, and no public-release readiness is claimed.

Worktree: `/home/theanarchitect/code/Qryptana-release-validation`. Branch: `qryptana-runtime-fixes`, created from clean `qryptana-release-validation` at `12e0f7f6b422259f15aaab5491cd6aaab09b7d99`. Existing branches/history and profiles preserved. Release-gate follow-up commit: `db32a3b23`. No source-fix commits: no Qryptana application defect was confirmed. Resolve the final report commit with `git log -1 --format=%H -- docs/qryptana/release/runtime-acceptance-report.md`; a separate local receipt is retained in the diagnostic directory.

## Diagnostic and process attribution

Durable local diagnostic directory: `/home/theanarchitect/code/Qryptana-release-artifacts/runtime-acceptance-20261030/`. Useful prior /tmp smoke log and input harness were preserved there, along with new logs, trace source and matching vendor source/issue metadata. Only non-secret selected evidence is committed under runtime-evidence/. No databases, profile settings/passphrases, identities, memory dumps or contacts were committed. Disposable profiles remain under /tmp/qryptana-runtime-* for diagnosis and are not distributable artifacts.

Reproduced startup in fresh isolated profiles, then an uninstrumented restart of the disposable UI profile. Stdout/stderr were captured separately. The same two-line diagnostic occurs before the JVM options message. Installed strace was unavailable; GDB 18.1 was available. Two GDB fork-follow attempts stopped at an exited package-probe grandchild (127), producing no usable stack; both logs retained, then that approach stopped.

A materially different diagnostic-only LD_PRELOAD interposer logged PID/executable, fork/exec/wait and pure-virtual/abort stacks. It forwards the real functions; it was never added to the distribution. The initial version could not resolve a locally scoped C++ handler via RTLD_NEXT and is excluded from accepted termination evidence. The corrected version resolves the existing versioned handler through its already loaded libstdc++ handle. The final trace also captures wait(NULL)'s otherwise discarded status and routes each fork child's stderr to a named file; those files are preserved and the error text remains visible. This separates child output rather than suppressing it. The independent uninstrumented run reproduces the original stderr unchanged.

| Process / output | Observed result |
|---|---|
| Launcher/JVM parent 1511228 | Survives helper failure; its stderr contains the JAVA_TOOL_OPTIONS line, not the child's termination text. Separate normal gsettings/echo JVM helper records retained locally. |
| Launcher child 1511229 | Emits the diagnostic in child-1511229.stderr, then aborts. Parent records raw wait status **134**, SIGABRT (6) with core flag. No core dump retained. |
| Grandchildren 1511230 / 1511231 | execvp probes `rpm` and `dpkg`; each wait status **32512**, exit **127**. Those packaging tools are unavailable on this host. |
| Uninstrumented UI parent 1512140 | Starts fresh isolated database/onboarding, screenshot captured; later deliberately closed with SIGTERM. |
| Uninstrumented restart 1513031 | Still running after 10 seconds; deliberately terminated, return code **143**. This is not a successful normal UI exit. |

The abort stack is `__cxa_pure_virtual -> libapplauncher.so+0x21368 (Logger::log) <- libapplauncher.so+0x8e4a (dcon) <- loader finalization/exit`. `addr2line -fiC` maps the call sites to `Log.cpp:129` and `LinuxLauncherLib.cpp:171`. Disassembly confirms the indirect log-appender call; the matching vendor source's unload destructor calls LOG_TRACE. `readelf -d` confirms dynamic libstdc++.so.6 linkage. The bundled runtime reports **21.0.13.0.2**.

Classification: **confirmed launcher helper crash**, consistent with a **JDK packaging-toolchain teardown defect**, rather than a Haskell/JNI crypto/core failure. Confidence is high for process attribution and the logger/destructor failure path. Exact vendor repair/backport is not locally verified. The official [JDK-8348560 issue](https://bugs.openjdk.org/browse/JDK-8348560) describes this signature in dynamically linked JDK 21 launchers; its retrieved metadata still marks it open with no fixed 21 release specified. Matching source was obtained from [Microsoft's pinned 21.0.13.0.2 source](https://github.com/microsoft/openjdk-jdk21u/blob/release/jdk-21.0.13.0.2_1/src/jdk.jpackage/linux/native/libapplauncher/LinuxLauncherLib.cpp). Source/issue records are preserved locally. The absent package probes trigger the fallback path; installing them is not established as a correct logger-lifetime repair.

This child failure is **not classified as harmless** because the UI survives. The helper's normal exit is replaced by an abort, so the native packaging acceptance gate remains failed. No stderr filtering, termination suppression, LD_PRELOAD runtime workaround, arbitrary native replacement, speculative dependency update or Haskell build was performed.

Required fix prerequisite: a provenance-verified corrected JDK packaging launcher/toolchain addressing the logger teardown issue, followed by repackaging and clean helper/application exit regression tests. The installed JDK is the reproducing version; no validated corrected alternative or complete native-JDK build setup/source checkout was supplied. Do not guess a binary patch or switch toolchains without verification. If a native source repair is pursued, retain the exact vendor base, reviewed logger/destructor fix, OpenJDK build configuration including C++ linkage, and checksums; rebuild jpackage's launcher through that toolchain and rerun the helper probe path. Existing Qryptana native libraries are not the repair target.

## Runtime acceptance matrix

Session inspection established KDE Wayland, WAYLAND_DISPLAY=wayland-1, DISPLAY=:0 (XWayland). No X11 focus operations were retried. Portal RemoteDesktop and compositor scripting were inspected. A temporary KWin script targeted only PID 1512140; a temporary kernel uinput device was created with installed headers/compiler. One compositor-aware pointer/scroll attempt produced no reliable navigation; automation stopped. The script was unloaded (isScriptLoaded=false) and input device destroyed. No persistent/global desktop configuration changed. Screenshot uses read-only capture of the known app's XWayland window, not X11 input/focus injection.

| Check | Status | Evidence / limit |
|---|---|---|
| Fresh isolated startup/native initialization | Passed | ui.stderr, initial live onboarding screenshot; configuration/data/temp paths isolated. |
| Startup helper termination | Failed | Confirmed SIGABRT from launcher child; native toolchain repair prerequisite above. |
| Branded onboarding BLACK/copper rendering | Passed, initial screen only | runtime-evidence/qryptana-runtime-wayland.png. Does not establish Appearance/theme selection behavior. |
| Disposable onboarding completion/main screen | Unperformed | Automation did not reliably activate actions. No local identity or external account/contact registered. |
| Settings and Appearance navigation | Unperformed | Neither screen opened in this run. |
| Forced BLACK independent of saved/system mode | Unperformed in UI | Existing meaningful ThemesTest remains supplemental evidence, not a screen pass. |
| Hidden null support contact | Unperformed in UI | Existing null value/conditional preserved; no runtime screen observed. |
| Disabled updater / clearly labelled upstream notes | Unperformed in UI | Persisted-preference tests/source unchanged; row not opened. |
| No upstream binary offered | Unperformed in UI | Existing empty-asset regression remains valid; no UI update interaction performed. |
| Scrolling, resizing, ordinary font scaling | Unperformed this run | Native scroll injection not reliable; earlier scroll screenshots remain historical evidence only. No resizing/scaling success inferred. |
| Clean normal UI shutdown | Unperformed | Controlled SIGTERM is not menu-close acceptance. |
| Restart of disposable initial profile | Passed, startup only | restart-result.json: alive after 10s, same isolated profile; no onboarding/main-screen restoration claimed. |

No external contacts, messages, calls, device installs, data clearing, real database access or account registrations. All test processes are closed. No continued focus retries or helper workarounds are prescribed as acceptance.

## Verification and artifacts

No implementation changes; reused the committed **35 tests, zero failures/errors/skips** and prior three successful Gradle checks at 3ad2abd2c. A targeted Git diff confirms app/build/CI files unchanged. No Gradle, Linux packaging, Android rebuild or dependency upgrade was run. Diagnostic C compilation used installed GCC, exit 0. GDB batch commands exited 0 but failed their diagnostic objective (no stack), not application acceptance. Final interposer logs show actual child statuses above; the uninstrumented restart driver exited 0 with recorded application termination 143. Temporary compositor/input/screenshot commands exited 0, which does not prove UI navigation.

Native provenance remains the verified upstream v7.0.3 inputs recorded in release-candidate-report.md and evidence/native-manifest.json. Runtime input hashes in runtime-evidence/runtime-input-hashes.json:

- launcher bin/qryptana: `7fe37e51ff3c05c9c34dcd7438ef9ae733537ad63bfd6f6af9c5d392684e41c4`.
- lib/libapplauncher.so: `aaa5504ada5e61d7d00a9d1e6093ae6ab3fa8a00879977f4f08efe74b5f4524e`.

No rebuilt outputs; no new artifact version/manifest is required. Previous files and their receipts are untouched at `/home/theanarchitect/code/Qryptana-release-artifacts/`:

| Existing artifact | SHA-256 |
|---|---|
| qryptana-7.0.3-linux-x86_64-local.tar.gz | cf299c5faf220b5039ee84e64d9616f078e717458bd7894be8d02da5785bad9f |
| android-foss-arm64-v8a-debug.apk | 40d1927698d460bce7c7395b46bb2701895a17ba6b4fa29f2f9d4f707e56ca5c |
| android-foss-armeabi-v7a-debug.apk | 173c97e424ba60b084613158bbb90d28314ab29889b35e06365da933d10e712d |

The desktop launcher finding does not change APK compatibility or signatures; no shared/Android source changed. APK device-runtime acceptance remains unperformed, not newly certified.

## Exact manual launch and remaining gates

Run this in a terminal in the graphical session. It creates a fresh disposable profile, prints its path, leaves stderr visible, and uses no diagnostic preload:

```sh
task_profile=$(mktemp -d /tmp/qryptana-manual.XXXXXX)
mkdir -p "$task_profile"/config "$task_profile"/data "$task_profile"/cache "$task_profile"/tmp "$task_profile"/prefs
printf 'Disposable profile: %s\n' "$task_profile"
env -u LD_PRELOAD   XDG_CONFIG_HOME="$task_profile/config"   XDG_DATA_HOME="$task_profile/data"   XDG_CACHE_HOME="$task_profile/cache"   JAVA_TOOL_OPTIONS="-Djava.io.tmpdir=$task_profile/tmp -Djava.util.prefs.userRoot=$task_profile/prefs"   /home/theanarchitect/code/Qryptana-release-validation/apps/multiplatform/release/main/app/qryptana/bin/qryptana
```

1. Complete only disposable local onboarding; do not add contacts, connect a remote account/device, send messages or make calls. Record main-screen screenshot. If an external operation is required, stop and record that gate.
2. Open Settings and Appearance; capture BLACK/copper finish, theme explanation and behavior with alternate saved/system mode. Check scrolling and ordinary window sizes/font scale; record clipping or exceptions.
3. Open Help/Support and updater settings: null chat contact hidden, checks disabled, opt-in text explicitly upstream notes, no upstream binary offered. Do not enable network checks or download upstream binaries for this acceptance test.
4. Close normally through UI; record terminal exit status, stderr and any helper exit. Relaunch only the env command with the same task_profile variable (do not rerun mktemp), verify disposable profile resumes, then close normally again.
5. Keep launcher helper crash as a failed gate until corrected-toolchain artifacts pass the same probe path without abort. Record hashes of any future versioned replacement; preserve this candidate.

Remaining exact gates: validated corrected JDK launcher/repackage; manual Linux navigation, sizing/scaling and normal shutdown/restart checklist; authorized clean ARM physical-device testing for APKs (none performed); translations/store text review with English fallback still incomplete; maintainer platform/owner/distribution choices and typed contacts if support is ever enabled; counsel/rights-owner privacy/operator/trademark/assets/source-distribution decisions. Existing bundle IDs, bundled video, desktop BLACK/copper, stock Android palettes and disabled publishing/update/support defaults remain unchanged. No new decisions or legal approval invented.

Final working tree is clean after this report commit. Ignored build/native inputs and /tmp disposable diagnostics remain intentionally retained; no test app or temporary compositor script/input device remains active. Nothing pushed, deployed, published, signed or uploaded.
