# Qryptana audit remediation report

Corrective verification: 30 October 2026. Branch: `release-readiness`.

- Audited/original remediation start: `2121b84ec8b1c11ea6deb04ca7fab42c12a6ee3b`.
- Corrective-pass start: `74e068471d83ba88c9c648374be5e9dc87adf42c` (18 existing remediation commits, clean tree).
- Final implementation tested: `887567e939311ebfdb9d9b0df295969397731712`.
- This final report is a separate documentation-only commit after that tested implementation. No code/resource changes followed the final checks.

All feasible in-scope repository corrections are implemented and locally verified, subject to the explicit limitations below. **This is not public-release readiness.** Existing commits were preserved; no reset, rebase, squash, history rewrite, push, deployment, publication, PR, Haskell core build, desktop packaging or 7.x rebase occurred. The isolated 6.5.x merge attempt did not change this branch.

The complete plan, run instructions and independent review in `/home/theanarchitect/qryptana-run/` were read and findings checked against current code. No applicable `AGENTS.md` was found; root `UPSTREAM.md` supplied the repository's upstream/branding guidance. The plan's `docs/qryptana/UPSTREAM.md` path does not exist.

## Finding register

Statuses describe repository work, not permission to release. Partial items identify remaining human decisions or unavailable validation.

| ID | Status | Evidence and implementation commits |
|---|---|---|
| C-01 | Done | Qryptana CI handles push/PR to `main`, JDK 21/Android SDK and all three Gradle tasks (`c6480a824`). Corrections `a7f665657` and `feca6e68e` make inherited Haskell/build, website deployment and reproduction workflows dispatch-only with explanations; add read-only permissions/45-minute timeout to Qryptana CI; gate the inherited CLA job to the upstream repository. YAML and trigger assertions passed. GitHub Actions was not executed. |
| C-02 | Partial | `24afebc82` introduced the null support URI. `d5f896709` routes contribute/star/source actions to the verified Qryptana repository, removes the inherited Play rating and email entry, and labels source access without claiming a terminal release. The URI is explicitly a SimpleX chat-contact link, not mailto/website. Settings and chat-help contact entries stay hidden while null. A verified support destination remains NEEDS HUMAN. |
| H-01 | Done | `e479a662d` retained disabled defaults/empty binary assets and named the upstream feed. `e5c2ab8c5` removes automatic opt-in prompting, labels Settings/channel choices as upstream notes using new safe-fallback keys, and tests the actual persisted preference: default/invalid disabled, explicit stable/beta/disabled persistence, and no rewrite on reads. The empty asset-selection regression remains. No Qryptana update channel is claimed. |
| H-02 | Done | `4d66b6d6f` corrected en-US title/descriptions; `3810bc058` adds truthful default Labs changelog copy. Only en-US Android store metadata exists. No translated store titles needed replacement; new translated release material requires human review. |
| H-03 | Partial | `4d66b6d6f`/`cf84d561e` moved the upstream policy and repaired Haskell embed/Cabal paths. `4a0fd05b7` corrects the draft's operator names, default link previews/network requests, local logs/user sharing and optional GitHub/proxy behavior, repairs privacy links/security routing, and adds `docs/upstream/README.md`. The retained operator policy is byte-identical to audited `PRIVACY.md`; its boundary is outside the embedded terms. Legal/contact review, traffic/binary audit and Haskell build validation remain unavailable or out of scope. |
| H-04 | Partial | `51ca731d0` missed the copper `SimplexBlue` alias. `b7186d594` restores Light/Dark primary, variant and title to upstream `#0088FF`; all four mobile base palettes match verified `423c8c0` values. Literal regression expectations caught the defect before correction. Android compiles and Compose palette previews were inspected; Android device validation remains. No wallpaper changes or Android copper rollout. |
| H-05 | Done | `52a2f45ca` isolates `ThemeDefaults` from controller/global initialization; runtime ThemeManager and palette globals consume it. `b7186d594` tests desktop BLACK across saved/system choices and contrasting mobile preferences, and moves the explanation to main Appearance. Desktop copper/titanium and saved preference storage are preserved. Full Settings/Appearance runtime capture remains unverified as detailed below. |
| H-06 | Out of scope | Native artifacts, Haskell core build and desktop packaging were expressly excluded. Compiling desktop-entry Kotlin with the native build task excluded does not verify a runnable/packaged app. |
| H-07 | Partial | `df0a99c0b` supersedes `96b768f09`: full v6.5.6 fetch succeeded, and the isolated trial exited 128 because the imported snapshot and upstream have unrelated histories. No conflicts were produced because merging never started. See [trial evidence](upstream-trial-merge.md). Choosing an integration strategy remains NEEDS HUMAN; no forced merge or 7.x work. |
| M-01 | Partial | `4d0ab6077` retains real SimpleX Chat Ltd/Flux names with neutral base operator wording. `e5c2ab8c5` expands [locale follow-up](locale-followup.md): 23 operator translations, 22 legacy updater descriptions and 38 non-base locale files. No hand translations; new upstream/source/theme keys fall back to English. Translation review remains. |
| M-02 | Partial | `ff5daf412` changed distribution references/ICO and re-encoded some pre-existing Qryptana pixels; it did not newly design all assets. `5588661d3` regenerates both adaptive foreground variants from the original Qryptana symbol at 108/162/216/324/432 px and fixes the round-dark alias. Dimensions/alpha, mask simulations and Android resource/Kotlin compilation passed. Legacy launcher pixels, notification artwork and IDs are unchanged by the correction. ICNS/macOS packaging and Android device/notification validation remain; PNG is an unverified placeholder, not a certified ICNS fallback. |
| M-03 | Done | `4d0ab6077` preserves the real SimpleX operator website, removes it from `dummyOperatorInfo`, and omits empty website links. The dummy appears in fallback operator-information presentation and the WhatsNew Flux illustration when no matching operator is available; normal screens use real metadata. |
| M-04 | Done | `7b820e9e5` only added policy; its completion claim was premature. `887567e93` adds `*.bat text eol=crlf` and actually runs `git add --renormalize .` from a clean controlled tree. Five existing text blobs change only CRLF to LF: gradlew.bat, three Haskell dependency licenses and a `.jpg`-named SVG. Staged `--ignore-cr-at-eol` diff showed only the attribute addition; byte comparison preserved all existing text. No indexed CRLF files remain. |
| M-05 | Already resolved | The six Inter fonts are used by both `Type.android.kt` and `Type.desktop.kt` through generated MR resources, so the condition for deletion is false. `2565a6b53` deleted them and `6a7f4722f` restored them before this pass. Their bytes, OFL license, other fonts and onboarding video are retained. History is preserved; video delivery remains a human decision. |
| M-06 | Done | `3e9e08afe` sets `local.properties.example` to `Qryptana Debug`. No machine-specific SDK/memory settings were committed. |
| M-07 | Done | `ec09fd36a` and corrective `a6ad0e93c` distinguish historical branding from current acceptance evidence, correct asset/theme completion claims, and keep deferred work explicit. ADR corrected in `b7186d594`; `006ea4d..2121b84` remains historical context only. |
| M-08 | Done | `127ec2ff7` corrects `ba6419b39`: fixture is unencrypted PKCS#8 PEM, used as SMP/XFTP test-server credential, not TLS client/OpenSSH material. OpenSSL validation and `ChatClient.hs` references confirm it. Local provenance is initial snapshot `423c8c0`, not a claim about its generation. No key was removed, regenerated or printed. |
| L-01 | Out of scope | Network plain-language copy pass deferred by the plan. |
| L-02 | Done | `0d2b07a08` normalizes only touched desktop `AppUpdater.kt` using installed Vim Kotlin indentation. Comparison verified indentation plus one argument line wrap only; update behavior/asset guard unchanged. No repository-wide formatting or formatter installation. Final compile/tests pass. |
| L-03 | Done | `a968b51ef` documents app `6.5.2` versus core `6.5.3.0`; `a6ad0e93c` corrects release-checklist blockers. No version change. |
| L-04 | Out of scope | Typography expansion deferred by the plan. |
| L-05 | Partial | Existing 14 baseline tests remain, plus two updater tests from `e479a662d` (persisted preference strengthened in `e5c2ab8c5`) and two meaningful theme tests from `b7186d594`: 18 total. Pure defaults avoid the earlier initialization cycle. Real onboarding smoke coverage is not established by pure JVM tests; native/window/controller initialization and unavailable runnable application prevent that claim. No required coverage was removed. |

## Verification at the final implementation

Environment: existing JDK 21, approximately 15 GiB RAM, existing SDK `/home/theanarchitect/Android/Sdk`. No system software, SDK licenses or credentials were requested. Commands ran sequentially from `apps/multiplatform`, with this environment and the supplied conservative Gradle options:

```sh
export ANDROID_HOME=/home/theanarchitect/Android/Sdk
export ANDROID_SDK_ROOT=/home/theanarchitect/Android/Sdk

./gradlew :common:compileKotlinDesktop --no-daemon --max-workers=1 --no-parallel -Dorg.gradle.jvmargs="-Xmx1536m -XX:MaxMetaspaceSize=512m -Dfile.encoding=UTF-8" -Pkotlin.compiler.execution.strategy=in-process
./gradlew :common:desktopTest --rerun --no-daemon --max-workers=1 --no-parallel -Dorg.gradle.jvmargs="-Xmx1536m -XX:MaxMetaspaceSize=512m -Dfile.encoding=UTF-8" -Pkotlin.compiler.execution.strategy=in-process
./gradlew :android:compileDebugKotlin --no-daemon --max-workers=1 --no-parallel -Dorg.gradle.jvmargs="-Xmx1536m -XX:MaxMetaspaceSize=512m -Dfile.encoding=UTF-8" -Pkotlin.compiler.execution.strategy=in-process
```

All three ran at `887567e939311ebfdb9d9b0df295969397731712` with a clean working tree:

| Check | Result | Detail |
|---|---|---|
| `:common:compileKotlinDesktop` | Exit 0, BUILD SUCCESSFUL | 1m 26s; 10 actionable tasks, 3 executed/7 up-to-date. |
| `:common:desktopTest --rerun` | Exit 0, BUILD SUCCESSFUL | 19s; 17 actionable tasks, 5 executed/12 up-to-date. Test task explicitly rerun. XML: **18 tests, 0 failures, 0 errors, 0 skipped**. |
| `:android:compileDebugKotlin` | Exit 0, BUILD SUCCESSFUL | 10s; 33 actionable tasks, 1 executed/32 up-to-date. Android Kotlin/resources were up-to-date from the successful corrective compilation (28s, 10 executed/23 up-to-date); only desktop-only formatting and documentation/normalization followed. |

Final XML breakdown: ThemesTest 3; AppUpdaterTest 2; ChatItemsMergerTest 2; ProviderForGalleryTest 2; SemVerTest 2; SingleInstanceTest 2; EmojiPickerTest 5. Baseline coverage was not reduced. Existing deprecation/SDK XML-version warnings were nonfatal, not suppressed or claimed fixed.

Local evidence is retained in ignored `.amp/in/artifacts/checks/`: final logs, final-results.txt, seven test XML files, baseline/refactor/regression/render logs and the trial/fetch evidence. These are local review artifacts, not release artifacts or tracked source. The repository report contains the results independently of those files.

## Baseline, regression and supplemental checks

- The supplied audited-main baseline at `2121b84` recorded all three tasks successful with 14 tests. At the resumed `74e0684` baseline, the combined three-task invocation exited 0 (10s, tasks up-to-date), and existing XML contained 16 tests with no failures/errors/skips. After the behavior-preserving theme refactor, desktop tests executed successfully before changing palette behavior.
- Before fixing the copper alias, the new theme tests ran against the defective implementation: **3 tests, 1 failure**, `mobilePalettesMatchUpstreamBeforeRebrand`, Gradle exit 1 (`theme-before.log`). The same kept regression passes after correction. Other baseline tests were retained.
- Corrective `:common:desktopTest :common:compileDebugKotlinAndroid :desktop:compileKotlinJvm -x :desktop:cmakeBuildAndCopy` with the same environment/options exited 0 (3m 6s, 25 actionable tasks, 13 executed/12 up-to-date). This compiled the changed desktop entry point while deliberately excluding native building; it does not establish runtime/packaging readiness.
- YAML parsing/assertions passed for dispatch-only inherited workflows, Qryptana main push/PR triggers, read-only permissions, timeout and the upstream-only CLA gate. No remote CI, website build or deployment was triggered. The inherited site's privacy-route collision/branding risk remains a publication blocker, not a claimed tested failure.
- Static checks passed for retained policy byte identity and embed/Cabal paths; all 14 relative privacy-policy/draft links in changed Markdown resolved. Locale XML parsing confirmed 38 non-base files, 23 operator-copy occurrences and 22 legacy updater descriptions. An initial scratch link-count assumption was corrected after inspecting the actual links; no product change was made to satisfy that assumption.
- Support routing/no-auto-prompt source checks passed. These are static checks, not exercised UI navigation or live GitHub traffic tests. Six Inter font bytes, OFL, attribution and the onboarding video match audited baseline. `git diff --check` passed. No messaging protocol, crypto, preset server, app ID, wallpaper or version changes were made by this corrective pass.
- `openssl pkey -in tests/fixtures/tls/server.key -check -noout` exited 0 (`Key is valid`), without emitting key contents. Git ancestry confirms `74e0684` remains an ancestor of the final implementation.

## Visual evidence and unavailable verification

Real Compose palette-component captures were inspected: `.amp/in/artifacts/desktop-black.png`, `mobile-light.png`, `mobile-dark.png`, `mobile-simplex.png`, `mobile-black.png`. They show the desktop copper/black finish and upstream mobile palette components, not complete app screens or Android device output. The temporary render harness initially lacked Skiko native runtime (exit 1); supplying the already-cached native runtime locally allowed rendering without new repository dependencies. All 18 permanent tests passed in that run; the extra scratch render test made 19 successful tests.

Full Settings/Appearance rendering did not succeed in the isolated harness: first no `AppBarHandler` composition provider, then uninitialized desktop `windowState` (20 tests including two scratch checks, one scratch failure, exit 1). This is a harness/application-initialization limitation, not evidence that those screens are correct or a demonstrated product regression. Further retries stopped. The scratch test and local Gradle init file were removed; they were never required regression coverage. Final 18-test verification uses no render harness or added runtime dependency. Full UI navigation, appearance text layout and onboarding smoke validation remain unavailable.

Both adaptive foreground variants have alpha spanning 0–1 and density-correct dimensions. Light/dark circle and rounded-square mask simulations in `.amp/in/artifacts/adaptive-icons.png` were inspected without clipping/halos. These raster simulations do not verify Android launcher/notification rendering. `adb devices` listed no connected device.

Not executed: GitHub Actions, packaged desktop runtime, Haskell core, Android APK/device/instrumented tests, iOS, native ICNS generation/macOS packaging, or network-traffic audit. No required tests were deleted or disabled to obtain the final pass.

## History and integration limitations

The independent review's verified historical defects remain in history as requested: `4d66b6d6f` moved the policy before `cf84d561e` repaired the embed; `2565a6b53` deleted required fonts until `6a7f4722f` restored them; `2be2c94be` removed the earlier initialization-failing theme test. This pass does not certify every intermediate commit. It adds corrections and reports evidence for the final implementation instead of dropping, squashing or rewriting those commits.

The verified upstream remote advertised v6.5.6 (`59fce95d3cd08897b4ef742447b785cf2e56c7ce`) as the latest plain stable v6.5 patch. Full tag fetch succeeded. Trial source was `127ec2ff706340b0b3351dad1d6abcab2f405dbe` in a separate throwaway worktree with hooks disabled; uncommitted changes were not transferred. Merge exited 128, `refusing to merge unrelated histories`, and merge-base found no ancestor. The imported root is `423c8c094aab585f52a47fe3956cf3b51559ab88`. Temporary branch/worktree were removed; no forced unrelated-history merge was attempted. This verifies the ancestry blocker, not compatibility of the final corrections with upstream.

## NEEDS HUMAN before release

- Supply verified Qryptana support, security and privacy contacts and their URI types; approve disclosure handling. No details were invented.
- Legally review the privacy draft, actual operators/deployment, retention practices and documentation boundaries. Retained upstream terms are not Qryptana commitments.
- Review all listed operator/update/support/theme translations and translated upstream documentation. Supply reviewed non-English store metadata if those locales will be published; only en-US exists now.
- Decide onboarding-video delivery; the existing video and font licenses stay intact.
- Provide a genuine native ICNS and validate macOS packaging. Review icon artwork and Android adaptive/legacy/notification behavior on devices.
- Validate Android Light/Dark/SimpleX/Black screens and desktop Settings/Appearance/onboarding on runnable applications. Android copper remains deferred; wallpapers were deliberately not changed.
- Decide any future bundle/application-ID migration with upgrade, data, deep-link and store implications; no IDs changed here.
- Complete trademark clearance and other release/legal decisions.
- Decide whether/how to publish a Qryptana website or enable inherited Haskell workflows and a Qryptana contribution policy. Review manual workflows before running them; local YAML checks are not CI success.
- Choose an upstream integration strategy for the imported snapshot. The old network blocker is resolved; merely retrying the same merge will not resolve unrelated ancestry. No 7.x rebase is authorized.

The repository corrections and final report are local commits only. The final delivery check must show a clean working tree; ignored local screenshots/logs are intentionally retained for review. Passing these checks does not authorize release, deployment, publication or pushing.
