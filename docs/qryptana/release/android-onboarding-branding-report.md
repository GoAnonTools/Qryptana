# Android onboarding branding correction

Date: 2026-11-01
Branch: `qryptana-runtime-fixes`
Source commit: `3becb8d084c1542716b598e168ef0b3ab2daf714`

## Scope and implementation

Removed the remaining Android blue cast from the Qryptana welcome wordmark lighting and the shared onboarding illustration fallback used by welcome/profile, Your network, operator selection and network commitments. Android now draws the illustration wash from the active theme background/surface roles and uses the active semantic primary copper role for the welcome wordmark sheen. Desktop keeps its prior gradient, glow and sweep colors. The existing source images, operator logos, text, state distinctions, navigation, reveal/video handling, and terms/acceptance were left untouched.

The Android onboarding action, selected operator outline/check, and operator control icons already resolve through `MaterialTheme.colors.primary`; the Android theme maps that role to its selected copper action token. The legacy `SimplexBlue` compatibility alias resolves to `QryptanaCopper`; `UpstreamSimplexBlue` is declared but has no call sites in the affected screens. No global blue replacement was made.

This candidate's generated `BuildConfigCommon.SIMPLEX_ASSETS` is `false`. Its default onboarding illustration files are transparent placeholders, so the visible illustration field comes from the Compose fallback gradient and icon. When the optional upstream illustration bundle is enabled, the existing artwork branch remains unchanged; those external assets were not altered. Functional error/status colors and operator-provided logos remain unchanged.

## Checks and artifacts

One sequential, one-worker Gradle invocation passed:

`:common:desktopTest :common:compileDebugKotlinAndroid :android:compileFossDebugKotlin :android:assembleFossDebug`

Desktop tests passed; Android common/library and app compilation passed; ARM64 and ARMv7 FOSS debug APK assembly passed. Both APKs verify with APK Signature Scheme v2 and one debug signer. The build used offline Gradle 8.12 and Temurin 21.0.13+9. Android's SDK emitted its existing XML-version warning; compilation also emitted existing deprecation/opt-in warnings.

| ABI | Path | SHA-256 |
| --- | --- | --- |
| ARM64 | `/home/theanarchitect/code/Qryptana-release-artifacts/android-onboarding-branding-20261101/qryptana-7.0.3-android-foss-arm64-v8a-onboarding-copper-debug.apk` | `f17994c3c9f91a84a044772a049abb62fb67634aa0aeed65a0fed340ae9743af` |
| ARMv7 | `/home/theanarchitect/code/Qryptana-release-artifacts/android-onboarding-branding-20261101/qryptana-7.0.3-android-foss-armeabi-v7a-onboarding-copper-debug.apk` | `3d20d31d938a1169ec550c64bf12c5983b4b388b7d7cbe19201afc2e55728d9b` |

No screenshot was captured and screenshot protection was not bypassed. Android device visual confirmation was not performed; `adb` is unavailable in this environment. APKs are debug-signed local candidates and were not installed, published or pushed.
