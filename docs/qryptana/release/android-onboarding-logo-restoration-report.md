# Android onboarding logo restoration

Source commit tested: `d6de5ce6d` (`fix(android): restore unfiltered onboarding logo`). The original rejected implementation remains in history; corrective commit `a19a8e08c` reverses it, and this follow-up restores the approved titanium palette/layout portions while leaving the logo material effect out.

The Android onboarding pages retain the cool titanium roles and restrained copper action accents. The welcome hero draws the original Qryptana wordmark image directly; the identity card uses the original symbol asset. No Android tint, color filter, shader, logo gradient overlay, or redraw is applied. The Android-only wordmark sweep is disabled; desktop retains its existing artwork and sweep. The identity reveal video, timing, fallback, lifecycle handling and onboarding navigation are unchanged. Invitation screens and their QR payload/rendering are unchanged by this correction.

## Verification

Using the verified Temurin 21.0.13+9 at `/home/theanarchitect/.local/share/qryptana-toolchains/jdk-21.0.13+9`, offline Gradle 8.12, one worker and in-process Kotlin compilation:

| Check | Result |
|---|---|
| `:common:desktopTest` | Passed |
| `:common:compileDebugKotlinAndroid` | Passed |
| `:android:compileFossDebugKotlin` | Passed |
| `:android:assembleFossDebug` | Passed; ARM64 and ARMv7 debug APKs |
| APK signatures | Both verify with APK v2 and one debug signer |

The build emitted the existing Android SDK XML version warning and existing Kotlin deprecation/opt-in warnings. No physical device or user profile was used.

## APKs

Source-to-artifact mapping: `d6de5ce6d`.

| ABI | Artifact | SHA-256 |
|---|---|---|
| ARM64 | `/home/theanarchitect/code/Qryptana-release-artifacts/android-restored-titanium-onboarding-20261101/qryptana-7.0.3-android-foss-arm64-v8a-restored-titanium-debug.apk` | `3a8866290747fe291e1f1e15b3e089e552c0d1d0bf2152c15681f2dabf28a488` |
| ARMv7 | `/home/theanarchitect/code/Qryptana-release-artifacts/android-restored-titanium-onboarding-20261101/qryptana-7.0.3-android-foss-armeabi-v7a-restored-titanium-debug.apk` | `09258033291213b2fe013d6eca19ed6a040e789e9ac623efae292630f2725eae` |

The APKs remain debug-signed local candidates. Earlier APK directories remain preserved; the `android-onboarding-metal-20261101` set is superseded and unapproved.
