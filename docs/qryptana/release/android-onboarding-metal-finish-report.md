# Android onboarding titanium and copper finish

> **Superseded; not approved.** This report documents the rejected candidate from source commit `0f0fe367f0e71741a11cd3363d305cb0279c402f` and evidence commit `7b80a0b55ce2360ab6305a7975e9990892903672`. Corrective commit `a19a8e08c233846a2c6c1340660faae1ea7da80c` reverses the implementation. The APKs and screenshots below are historical candidate artifacts, not approved release artifacts.

## Change and scope

Source commit: `0f0fe367f0e71741a11cd3363d305cb0279c402f` (`style(android): refine onboarding titanium copper artwork`).

The Android onboarding treatment is scoped to the welcome, first profile, network, and operator commitment pages. It uses the requested tokens: Background `#0E1013`, Surface `#161A1F`, SurfaceRaised `#1E232A`, TitaniumLight `#DDE2E8`, TitaniumMedium `#9BA4AF`, TitaniumDark `#6A737E`, and Copper `#B87333`. Android page backgrounds are cool, solid surfaces; titles/body text use titanium; copper is limited to small action details and the hero material. The nested Android theme leaves the app-wide theme, saved/system selection, custom themes, and light appearance elsewhere untouched. Desktop styling and its original reveal/sweep remain unchanged.

The hero shader uses only those palette stops. It shades the alpha of the existing Qryptana wordmark artwork in a static upper-left-to-lower-right pass with a titanium key highlight, dark reflection, copper body/return band, narrow titanium specular, and dark edge. The artwork's geometry and transparent mask are retained. There is no screen or panel gradient, glow, shimmer, or video tint. The source hero files are `qryptana_symbol@1x.png` and `qryptana_wordmark@1x.png`; their packaged Android resources appear in both APKs. The reveal remains `assets/videos/qryptana_identity_card_reveal.mp4`, unchanged.

Relative luminance ratios calculated from the sRGB tokens:

| Foreground | Background | Contrast |
|---|---|---:|
| TitaniumLight | Background / Surface / SurfaceRaised | 14.62 / 13.41 / 12.13:1 |
| TitaniumMedium | Background / Surface / SurfaceRaised | 7.55 / 6.93 / 6.26:1 |
| TitaniumDark | Background / Surface / SurfaceRaised | 3.96 / 3.63 / 3.29:1 |
| Copper | Background / Surface / SurfaceRaised | 5.02 / 4.61 / 4.17:1 |

TitaniumDark and Copper are used as artwork reflections, small glyphs, borders, or state/action details, not ordinary paragraph text. The decorative hero gradient is not being presented as text-contrast evidence.

When `SIMPLEX_ASSETS=false`, the upstream onboarding illustration source files for intro, profile, network and commitments are empty 24×24 SVG placeholders (175 bytes each). The Android fallback leaves those art slots unillustrated rather than adding substitute symbols or SimpleX art. The original Qryptana symbol/wordmark remain available as separately packaged Qryptana resources. No asset or artwork was replaced.

## Visual evidence and limits

The images are local Compose Desktop-hosted renders of the actual wordmark painter, Android palette theme and metallic modifier, not Android captures:

- [Welcome screen preview, desktop host](android-onboarding-metal-evidence/welcome-normal-desktop-hosted.png)
- [Hero wordmark close-up, desktop host](android-onboarding-metal-evidence/hero-closeup-desktop-hosted.png)

The metallic finish was visually inspected at close range: its specular highlight is clipped to the original logo, the copper band remains visibly reflective rather than flat, and the titanium/dark reflections preserve opaque substance. The standalone welcome preview shows the cool background, titanium heading, original wordmark geometry and restrained logo-area copper.

I could not produce trustworthy full-screen renders of the name, network and operator commitment layouts in this environment. The local Compose preview task launched, but the available screenshot command captured the workstation's unrelated foreground instead of its disposable preview window; that capture was discarded. Therefore those full layouts have not received visual acceptance here. The fallback illustration slots are empty because their original optional upstream SVGs are empty in this build. No production screenshot protection was changed, no real profile was read, and no physical device was used. Physical-device visual acceptance remains outstanding.

## Verification and artifacts

Build used the already verified Eclipse Temurin 21.0.13+9 under `/home/theanarchitect/.local/share/qryptana-toolchains/jdk-21.0.13+9`, installed Android SDK, offline Gradle 8.12, one worker and in-process Kotlin compilation.

| Check | Result |
|---|---|
| `:common:desktopTest` | Passed |
| `:common:compileDebugKotlinAndroid` | Passed |
| `:android:compileFossDebugKotlin` | Passed |
| `:android:assembleFossDebug` | Passed; ARM64 and ARMv7 debug splits |
| `apksigner verify --verbose` | Both verify with APK v2, one debug signer |
| Packaged reveal video | Present at the Android resource path in both; embedded SHA-256 matches source `8e80519593fc43114432646fab8bcaa644cb58eecdf5c4b888579130ae1f1b59` |
| `git diff --check` | Passed before source commit |

The artifact build is mapped to source commit `0f0fe367f0e71741a11cd3363d305cb0279c402f`. Outputs are preserved in `/home/theanarchitect/code/Qryptana-release-artifacts/android-onboarding-metal-20261101/`:

| ABI | APK | SHA-256 |
|---|---|---|
| ARM64 | `qryptana-7.0.3-android-foss-arm64-v8a-onboarding-metal-debug.apk` | `0664dc4d4f4a4b629471a3478dee83952f8dfddf19dc37f06f3815204cdd17e7` |
| ARMv7 | `qryptana-7.0.3-android-foss-armeabi-v7a-onboarding-metal-debug.apk` | `111c371e1e540a897563034da4b08bcb9d673e8af53039f9273896218b329038` |

The debug APKs retain package `chat.simplex.app.debug`, version 7.0.3 (381), min SDK 26 and target SDK 35. Prior artifacts were left intact.

## Physical-device acceptance

On a disposable Android test install, inspect the welcome wordmark at normal scale and close up; verify the static metallic treatment follows the original Qryptana mark, then review Choose your name, Your network and operator/network commitments in the dark onboarding treatment. Confirm titles/body remain titanium, action details remain restrained copper, and the original video reveal/navigation still work. This report does not claim those runtime observations were performed.
