# Android onboarding identity reveal

Date: 2026-10-31. Implementation commit: `0240806288c85c1ea5b31a2a700fa0ea509a4025` on `qryptana-runtime-fixes`.

## Implementation

The first onboarding screen no longer initializes the identity reveal as already settled on Android. Android now enters the same reveal sequence as desktop, using the existing `VideoPlayer`/ExoPlayer implementation with sound disabled. The supporting copy and actions remain hidden until playback reaches 8.8 seconds, the player reports a failure, or the existing 12.5-second safety timeout expires.

The player and URI are remembered, and playback is launched with `LaunchedEffect(player)`, so ordinary recomposition does not restart the clip. Android changes this onboarding player's repeat mode to `REPEAT_MODE_OFF`; other gallery players retain their existing behavior. `DisposableEffect` still stops and releases the player when the screen leaves composition, and Android's existing `MainActivity.onStop()` continues to stop all players when the app backgrounds.

Resource/player construction failure shows the existing static invitation card immediately. Asynchronous decoder failure switches to that card and releases the delayed onboarding content. Reduced motion is respected through Android's global animator-duration scale: when animations are disabled, the static card and onboarding content are available without waiting. Desktop retains motion and its existing classpath playback path.

The authored video was moved, without byte changes, to the common Android asset source set at `assets/videos/qryptana_identity_card_reveal.mp4`. Desktop's resource-copy task now reads the same source file and still exposes it as `/videos/qryptana_identity_card_reveal.mp4`. The source and every packaged copy have SHA-256 `8e80519593fc43114432646fab8bcaa644cb58eecdf5c4b888579130ae1f1b59`. `ffprobe` identifies one H.264 video stream, 1280x714, 11.291667 seconds, with no audio stream.

No onboarding navigation, palettes, protocol/crypto behavior, updater safeguards, package IDs or production signing changed. No installed/manual profile or user data was accessed or modified.

## Verification

Using `/home/theanarchitect/.local/share/qryptana-toolchains/jdk-21.0.13+9`, the installed Android SDK, offline dependencies, and the standard constrained Gradle settings:

```text
:common:desktopTest :android:assembleFossDebug
--offline --no-daemon --max-workers=1 --no-parallel
-Dorg.gradle.jvmargs=-Xmx1536m -XX:MaxMetaspaceSize=512m -Dfile.encoding=UTF-8
-Pkotlin.compiler.execution.strategy=in-process
```

The final run completed successfully in 3m23s (77 actionable tasks: 16 executed, 61 up-to-date). Desktop compilation and all 35 desktop tests passed with zero failures, errors or skips. Both Android ARM variants compiled and assembled. Existing project deprecation/opt-in and SDK XML compatibility warnings remained; no test was suppressed.

Direct archive inspection verified both APKs contain the 10,509,268-byte video at the exact path opened by Android, and `unzip -p | sha256sum` matched the source hash for both. Build-tools 36.0.0 `apksigner verify --verbose` passed for both APKs using the local Android debug certificate and APK Signature Scheme v2. `aapt dump badging` retained `chat.simplex.app.debug`, version 7.0.3 / 381, minimum SDK 26, target/compile SDK 35, and the expected single ABI per split.

No device was attached (`adb devices -l` returned an empty list). The only installed AVD is x86_64, while these candidates are ARM-only, and `/dev/kvm` is unavailable. Compilation, resource resolution, byte identity, player wiring and candidate generation are verified; actual rendered playback and reveal timing still require confirmation on the user's physical Android device. No app/profile data was cleared and no APK was installed.

## New local artifacts

Directory: `/home/theanarchitect/code/Qryptana-release-artifacts/android-identity-reveal-20261031/`.

| File | Bytes | SHA-256 |
|---|---:|---|
| `qryptana-7.0.3-android-foss-arm64-v8a-identity-reveal-debug.apk` | 286741169 | `b0c8138381ef8793bf027200bec6a209109d1d25e8cfec52c3d6dd5d47fe66e6` |
| `qryptana-7.0.3-android-foss-armeabi-v7a-identity-reveal-debug.apk` | 272436513 | `cb7f3fda8689233a67f9648caf7d5b4d310901c3ee7083f746105e992663399d` |

Previous release artifacts remain in place. No push, publication, upload, deployment or production signing occurred.
