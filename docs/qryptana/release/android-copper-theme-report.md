# Android titanium and polished copper implementation report

Date: 2026-11-01
Branch: `qryptana-runtime-fixes`
Source commits: `8fe9e08c0bfcf71d560003ba9a844193ec93ec18`, `a410c3bcc30cd1b1189fc4dfb1d7be321256995a`

## Result

Android Light/Dark now use the approved cool neutral hierarchy: Light background `#F1F3F5`, card `#FCFDFE`, heading `#20262B`, body `#30383F`, secondary `#59636C`; Dark background `#0E1114`, card `#191E23`, raised surface `#20272D`, heading/body `#EEF1F3`, secondary `#9AA5AE`, neutral icon `#AEB8C0`. Android BLACK is a deeper neutral variant. The named SimpleX preset remains distinct as a titanium dark variant. Saved/system theme choice and custom overlays remain active. Desktop palette and component branches were not changed.

Copper is limited to small invitation glyphs and selected/action states. The supplied metallic colors are `#6A3E32`, `#AD7257`, and `#F0D4C4`; the solid filled-control color was darkened to `#9F634C` so white enabled thumbs/text reach 4.81:1, retaining a clear lighter thumb and distinct unchecked/disabled tracks. No copper heading or warm background was introduced. The switch unchecked track/thumb are neutral grey. Disabled track contrast is lower by design and state is additionally conveyed through enabled semantics.

Invitations now render as compact horizontal action cards with a copper glyph, wrapping title and existing subtitle, and neutral chevron. A first render showed the root filling the entire available height; the follow-up commit applies `heightIn(min = 100.dp)` and the final renders show compact cards. Smart Invite remains a flat, cool surface with one understated border and white QR panel. QR payload, quiet zone, error correction and logo sizing are unchanged.

## Contrast measurements

Ratios use WCAG relative luminance on the actual adjacent backgrounds, based on 8-bit sRGB role values. Targets: enabled normal text 4.5:1; essential indicators 3:1. Light values: heading/background 13.74:1, body/background 10.71:1, secondary/background 5.51:1, action/background 5.97:1, white/control fill 4.81:1, neutral icon/card 4.76:1, border/card 3.68:1. Dark values: heading/background 16.69:1, body/background 16.69:1, secondary/background 7.55:1, action/background 8.74:1, neutral icon/card 8.32:1, border/card 3.46:1, white/control fill 4.81:1. The checked track and foreground pair exceed 3:1; disabled controls are excluded from enabled-state thresholds and have lower contrast by design. The metallic decorative gradient is not used for text and is not asserted as an accessible text color. A test initially measured white on the original filled copper at 3.95:1; it was adjusted to `#9F634C`, then the contrast suite passed.

## Rendered previews

Eight images are in [android-copper-theme-evidence](android-copper-theme-evidence/): Settings Light/Dark, Privacy Light/Dark, Invitations Light/Dark, Smart Invite Light/Dark. They were captured from actual shared Compose components using local Compose Desktop with the Android palette factories, isolated temporary home/config/data/cache paths, and the installed `spectacle` capture utility. Invitation and Smart Invite explicitly render their Android style paths. Final invitation screenshots include the restored subtitles. These are rendered preview images, not generated mockups.

Visual inspection confirms neutral headings, cool grey backgrounds, restrained copper controls/glyphs, distinct checked/unchecked/disabled switches, compact invitation cards, and wrapping Smart Invite copy. No clipping appeared at 1.3x font scale in the narrow Privacy, Invitations or Smart Invite views.

Limitations: all eight previews run on Compose Desktop. In the Settings preview, shared app-bar typography and icon defaults follow desktop behavior: the title is unusually large and row glyphs appear copper. Android's runtime branch uses neutral icon roles; the Settings screenshot therefore does not visually verify those Android-specific details. Privacy and other shared Compose components show the Android palette but cannot reproduce Android system bars, insets, native font metrics or rendering. Android rendering was unavailable: `adb devices` failed with `/bin/bash: adb: command not found`, and no emulator/device route was available in this environment. The approved mockup files were not present as local workspace inputs, so visual review compared against the explicit token hierarchy and component requirements in the task. Physical-device rendering, accessibility services, touch states and QR scanning remain unverified; no physical device was installed. The Settings preview is retained for traceability and must not be read as evidence of Android Settings rendering.

## Checks and artifacts

| Check | Result |
| --- | --- |
| `:common:desktopTest` | Passed, 37 tests, 0 failures/errors; includes role mapping, exact approved tokens, contrast and unchanged desktop palette assertions. |
| `:common:compileDebugKotlinAndroid :android:compileFossDebugKotlin` | Passed as part of the final sequential build. |
| `:android:assembleFossDebug` | Passed; one Gradle worker, ARM64 and ARMv7 splits. |
| `apksigner verify --verbose` | Both APKs verified with APK Signature Scheme v2; Android debug signer. |

The source-to-artifact commit is `a410c3bcc30cd1b1189fc4dfb1d7be321256995a` (including palette commit `8fe9e08c0bfcf71d560003ba9a844193ec93ec18`). APKs are debug-signed local candidates, not production-signed or published.

| ABI | Path | SHA-256 |
| --- | --- | --- |
| ARM64 | `/home/theanarchitect/code/Qryptana-release-artifacts/android-titanium-copper-20261101/qryptana-7.0.3-android-foss-arm64-v8a-titanium-copper-debug.apk` | `88dbd5af99fd03c08d10e73623a00e9ead5541a31dc0c1cae721337ae60a4c7a` |
| ARMv7 | `/home/theanarchitect/code/Qryptana-release-artifacts/android-titanium-copper-20261101/qryptana-7.0.3-android-foss-armeabi-v7a-titanium-copper-debug.apk` | `7d11049fd040d525696cbf5a60509118dbe4295b7d6caa0f86ff51db08bf4210` |

Physical device checklist: check Light/Dark/BLACK/SimpleX/system/custom selection; inspect 1.3x and default text sizes; check touch and TalkBack state; verify invitation navigation and subtitles; scan Smart Invite and address codes against the expected payload; verify onboarding reveal/fallback and navigation.
