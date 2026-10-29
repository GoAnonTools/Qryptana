# Qryptana audit remediation report

Remediation branch: `release-readiness`

Start commit: `2121b84ec8b1c11ea6deb04ca7fab42c12a6ee3b`

Audited commit: `2121b84ec8b1c11ea6deb04ca7fab42c12a6ee3b`

Implementation commit tested: `2be2c94` (`fix(H-05): avoid unsafe theme test initialization`)

The later commits `96b768f` and this report commit are documentation-only and did not require repeating the full Gradle checks.

## Finding register

| ID | Status | Evidence and implementation |
|---|---|---|
| C-01 | Done | Main-triggered Qryptana CI was added; inherited build/web workflows now handle `main`; upstream reproduction is dispatch-only. Commit `c6480a8`. GitHub Actions was not run here. |
| C-02 | Done | `qryptanaSupportUri` defaults to `null`; developer-contact entries are hidden and the SimpleX team URI is no longer used in those paths. Commit `24afebc`. Real support routing remains a human decision. |
| H-01 | Done | Updater default was already disabled and empty asset selection was preserved. The upstream feed URL is centralized with a TODO, upstream-only copy is explicit, and tests cover empty assets/default channel. Commit `e479a66`. |
| H-02 | Done | Android en-US title, short description and full description now identify Qryptana and preserve truthful upstream/AGPL attribution. No other Android metadata locales were present. Commit `4d66b6d`. |
| H-03 | Partial | Upstream policy moved to `docs/upstream/SIMPLEX_PRIVACY.md`; root `PRIVACY.md` is a verified-behavior draft and Haskell embedding paths were repaired. Legal review and contacts remain. Commits `cf84d56`, `4d66b6d`. |
| H-04 | Partial | Verified upstream mobile palettes were restored; Qryptana palettes are desktop-only. Android device validation remains. Commit `51ca731`. |
| H-05 | Partial | Desktop remains forced Black and Appearance explains the single finish; saved preferences are ignored on desktop. A direct ThemeManager test hit an existing initialization cycle and was removed; code compile and existing tests pass. Commits `51ca731`, `2be2c94`. |
| H-06 | Out of scope | No Haskell core build or desktop packaging was attempted, per scope and host constraints. |
| H-07 | Blocked | Upstream remote was added, but tag fetch made no progress; no tag or temporary merge worktree could be selected/created. Commit `96b768f`; details in `docs/qryptana/audits/upstream-trial-merge.md`. |
| M-01 | Partial | Base operator wording was clarified while retaining SimpleX Chat Ltd and Flux; translated values were not hand-translated. Commit `4d0ab60`; follow-up is documented. |
| M-02 | Partial | Desktop and Android assets were derived from `qryptana-symbol.png`; IDs remain `chat.simplex.app`. Native ICNS tooling was unavailable, so macOS uses the derived PNG fallback. Commit `ff5daf4`. |
| M-03 | Done | Real SimpleX operator metadata retains its verified website; `dummyOperatorInfo` no longer advertises it and empty links are omitted. Commit `4d0ab60`. |
| M-04 | Done | Root `.gitattributes` added with LF policy. Controlled renormalization found unrelated pre-existing CRLF-only churn, which was excluded. Commit `7b820e9`. |
| M-05 | Partial | Direct/generated resource inspection found Inter is referenced by `Type.desktop.kt`; deletion was reverted to avoid a build regression. Video delivery remains a human decision. Commits `2565a6b`, `6a7f472`. |
| M-06 | Done | Example debug app name is `Qryptana Debug`. Commit `3e9e08a`. |
| M-07 | Done | Rebrand plan reflects actual history and current theme/icon decisions; ADR 0001 records scope. Commit `ec09fd3`. |
| M-08 | Done | `SECURITY.md` documents the upstream TLS test fixture, its test-only role and non-production status without printing key contents. Commit `ba6419b`. |
| L-01 | Out of scope | Plain-language network copy pass deferred by the plan. |
| L-02 | Partial | No formatter was installed/configured and no repository-wide formatting was run; only touched-file readability was preserved. |
| L-03 | Done | Core `6.5.3.0` versus app `6.5.2` mapping documented without changing versions. Commit `a968b51`. |
| L-04 | Out of scope | Typography expansion deferred by the plan. |
| L-05 | Partial | Updater asset/default tests were added. Theme regression testing is limited by the existing initialization cycle; onboarding JVM smoke coverage was not practical. Commit `e479a66`. |

## Verification

Baseline was reused under the supplied verified-setup override at audited main `2121b84`:

- `./gradlew :common:compileKotlinDesktop` — BUILD SUCCESSFUL.
- `./gradlew :common:desktopTest` — BUILD SUCCESSFUL, 14/14 tests in baseline XML reports.
- `./gradlew :android:compileDebugKotlin` — BUILD SUCCESSFUL.

Final verification was run sequentially from `apps/multiplatform` with one worker, no daemon, no parallel execution, the 1536 MB Gradle heap, 512 MB metaspace and in-process Kotlin compiler:

- `./gradlew :common:compileKotlinDesktop --no-daemon --max-workers=1 --no-parallel -Dorg.gradle.jvmargs="-Xmx1536m -XX:MaxMetaspaceSize=512m -Dfile.encoding=UTF-8" -Pkotlin.compiler.execution.strategy=in-process` — BUILD SUCCESSFUL, tested at `2be2c94`.
- `./gradlew :common:desktopTest --no-daemon --max-workers=1 --no-parallel -Dorg.gradle.jvmargs="-Xmx1536m -XX:MaxMetaspaceSize=512m -Dfile.encoding=UTF-8" -Pkotlin.compiler.execution.strategy=in-process` — BUILD SUCCESSFUL, 16/16 tests; XML reports show zero failures/errors, tested at `2be2c94`.
- `./gradlew :android:compileDebugKotlin --no-daemon --max-workers=1 --no-parallel -Dorg.gradle.jvmargs="-Xmx1536m -XX:MaxMetaspaceSize=512m -Dfile.encoding=UTF-8" -Pkotlin.compiler.execution.strategy=in-process` — BUILD SUCCESSFUL, warnings only, tested at `2be2c94`.

Unavailable or intentionally skipped: GitHub Actions execution, Haskell core build, desktop packaging/runtime, Android APK/instrumented/device validation, iOS targets, native ICNS generation and the upstream trial merge. The Android compile emitted the environment’s SDK XML-version warning and inherited deprecation warnings; these did not fail the check.

## NEEDS HUMAN

- Provide and verify a real Qryptana support address.
- Complete legal review of the draft privacy statement and supply verified legal/security contacts.
- Review translated store/operator locale copy.
- Decide onboarding-video delivery.
- Provide native icon formats, especially ICNS, and validate launcher/notification artwork.
- Decide whether to migrate bundle/application IDs and define upgrade/deep-link/store consequences.
- Perform Android device theme/icon validation.
- Complete trademark clearance.
- Run the isolated H-07 6.5.x trial merge in a network-enabled environment.

Passing repository checks do not establish public-release readiness. No release, packaging, signing, deployment, push or merge was performed.
