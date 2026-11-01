# Linux packaging toolchain acceptance

Later implementation/candidates: [investment-removal-report.md](investment-removal-report.md) removes active investment promotion and rebuilds affected Linux/Android outputs with this verified toolchain. The archive below remains preserved; the later report records new paths/hashes and focused disposable verification without accessing the manual real-contact profile.

Date: 2026-10-30. **The launcher helper crash is resolved in a new local candidate. Full Linux UI acceptance and public release remain on hold.** This follow-up supersedes the corrected-toolchain prerequisite and limited shutdown/navigation evidence in runtime-acceptance-report.md; that report remains the historical diagnosis of the original candidate.

Branch: `qryptana-runtime-fixes`. Starting checkpoint: `2be173fd9e7a007d078be8fd03742fb0f8ddb349`. No application, dependency, protocol, JNI, Android, CI or production build configuration changed. This commit records a verified alternative packaging toolchain, local build instructions and selected non-secret evidence. Resolve its commit with `git log -1 --format=%H -- docs/qryptana/release/toolchain-acceptance-report.md`.

## Toolchain provenance and regression

The official [Adoptium installation documentation](https://adoptium.net/en-GB/installation/ci-scripts) identifies its API and GitHub release downloads. Retrieved official API metadata is retained in toolchain-evidence/temurin21-api-metadata.json. Selected release: **Eclipse Temurin 21.0.13+9-LTS**, Linux x64 HotSpot JDK. Downloaded the exact HTTPS URL in that metadata, verified both its 207858352-byte size and SHA-256 before extraction:

`d7f5d5cf865a2902b70ff549b97586a7d3f234e016f041ab447ea740c7d66d1c`

Archive and installation are under `/home/theanarchitect/.local/share/qryptana-toolchains/`; JDK home is `jdk-21.0.13+9`. No system JDK, shell startup file or global Java default changed. Verification is against official HTTPS metadata, not independent reproduction or GPG-signature verification.

The existing [JDK-8348560](https://bugs.openjdk.org/browse/JDK-8348560) reproduction specifically describes dynamic C++ linkage. Temurin's generated libapplauncher.so has no dynamic libstdc++ dependency: readelf lists libdl, libm, libc and the loader. This is a verified alternative build configuration avoiding the defect; no claim is made that JDK-8348560 was fixed upstream or backported into this release.

Packaged the same minimal LauncherProbe.java with the original system JDK and Temurin, using java.base only. Both launchers followed the same unavailable rpm/dpkg probe path. Each vendor received three plain and three diagnostic runs:

| Check | Original system JDK | Temurin |
|---|---|---|
| Three plain probe runs | Each prints the pure-virtual/termination diagnostic; parent exit 0 hides child failure | Each prints only QRYPTANA_LAUNCHER_PROBE_OK; stderr empty, parent exit 0 |
| Three diagnostic probe runs | Each helper raw wait status 134, SIGABRT; probes exit 127 | Each helper raw wait status 0; probes still exit 127 |
| Repackaged Qryptana diagnostic run | Historical original candidate: helper abort | Parent 1521352 waits for helper 1521353 with raw status 0; rpm/dpkg grandchildren 1521354/1521355 exit 127 |

Selected per-process logs and all probe result records are retained. Committed Gradle log copies normalize trailing whitespace; raw local logs are preserved in the artifact directory. Diagnostic-only LD_PRELOAD forwards real functions and separates child stderr into named files; it is never shipped. Plain Qryptana runs independently show no pure-virtual or termination diagnostic. No missing package probes were hidden or made artificially successful.

## Packaging and verification

Used the existing one-worker, 1536 MiB heap Gradle configuration, offline dependencies, and a separate output directory. The first sandboxed invocation failed because Gradle could not open its local daemon socket; automatic approval review authorized the local build. Network download likewise used approval review after sandbox DNS failed. No approval rejection occurred.

The first successful package had the new launcher but reused the old cached runtime image. Runtime release-file inspection caught this before acceptance. A local init script declares the selected JDK's release metadata as an input to createRuntimeImage, so that task regenerates the bundled runtime. An initial eager task lookup failed during configuration; the retained final script uses lazy task matching. The preliminary hybrid candidate was not archived as the accepted artifact. Its disposable startup was deliberately terminated (143), excluded from the successful exit evidence below.

Final build: **createDistributable passes in 19s**, 25 tasks, 5 executed. Bundled runtime release now reports 21.0.13 and the installed JDK reports Temurin 21.0.13+9. Reproduction from apps/multiplatform:

```sh
env JAVA_HOME=/home/theanarchitect/.local/share/qryptana-toolchains/jdk-21.0.13+9 \
  ANDROID_HOME=/home/theanarchitect/Android/Sdk \
  ANDROID_SDK_ROOT=/home/theanarchitect/Android/Sdk \
  ./gradlew :desktop:createDistributable --offline \
  --init-script ../../docs/qryptana/release/toolchain-evidence/packaging.gradle \
  --no-daemon --max-workers=1 --no-parallel \
  '-Dorg.gradle.jvmargs=-Xmx1536m -XX:MaxMetaspaceSize=512m -Dfile.encoding=UTF-8' \
  -Pkotlin.compiler.execution.strategy=in-process
```

The init script intentionally uses the separate artifact output path recorded below. It is a local reproduction aid, not a change to project or production toolchain policy. Run existing `:common:desktopTest --rerun` with the same JDK/environment/worker/memory flags, omitting the init script: **35 tests, zero failures/errors/skips**, 16s. XML and full log are retained. Existing opt-in-marker compiler warnings remain. Android outputs were not rebuilt: Android source and shared source are unchanged.

A full file-hash comparison with the original image finds **975 identical files**, including application jars, JNI, Haskell core, bundled VLC and other native libraries. Changes outside the JVM are the verified packaging launcher, jpackage metadata/config and the stripped Linux Skiko jar's generated archive/name. The old/new stripped jars contain identical entries with identical entry hashes; only ZIP metadata differs, and the classpath follows the new name. No dependency version changed. New package-manifest.json records every file's hash and size.

## Isolated UI and lifecycle evidence

A temporary authenticated **rootful Xwayland :98** server, with TCP disabled, allowed XTest input inside its own server. No focus/input commands targeted the user's existing Xwayland display. It used a private cookie under /tmp and no persistent compositor settings. Screenshots capture only the disposable Qryptana window. This differs from the previous unreliable input attempts against the user's Wayland-managed window; it does not establish native Wayland compositor integration.

Fresh XDG config/data/cache, JVM temp and Java preferences were isolated under `/tmp/qryptana-temurin-c6z9xm3n`. Created only a disposable local profile and encrypted its local database with a test-only phrase. No real profile, external account, contact, message, call, device connection or update check was used. This was not a network-traffic audit; no claim of zero background traffic is made.

| Runtime check | Result |
|---|---|
| Fresh startup, native loading, BLACK/copper onboarding/video | Passed |
| Scroll to Create my identity; choose local name; encrypt local database | Passed |
| Private-network and operator-commitments screens | Rendered and navigated; operator terms **not accepted** |
| Initial 1376x768 and resized 1000x700 commitments layout | Passed for those screens/sizes; action/text visible, no observed clipping |
| Normal first shutdown | WM_DELETE_WINDOW delivered to application close handler; exit **0**, no signals used |
| Restart same disposable encrypted database | Unlock screen shown; test phrase unlocks it and restores the saved operator-commitments stage |
| Normal restart shutdown | Same application close event; exit **0** |
| Diagnostic fresh-profile shutdown | Same normal close; parent exit **0**, helper status **0** |
| Main chat screen, Settings, Appearance, support/updater rows | Unperformed; completing onboarding requires operator privacy-policy/conditions acceptance |
| Alternate saved/system theme, ordinary font scaling | Unperformed in UI; existing unit tests supplement but do not close these gates |

The next onboarding button explicitly says Accept and invokes acceptConditions for selected operators before completion. It was left untouched; no acceptance or legal sign-off was invented and no preference/database edit bypassed it. An authorized human must review that step before completing the remaining UI checklist. Existing tests still establish forced desktop BLACK, disabled update preferences and empty upstream asset selection; unvisited screens are not runtime-certified.

All three accepted app processes exited normally. The preliminary hybrid test was terminated deliberately. The temporary Xwayland server was stopped after confirming the recorded app PIDs were gone. Disposable profiles remain only under /tmp; no databases, Java preferences, cookies, identities or test phrases are included in committed evidence or the package.

## New local artifact and remaining gates

New image: `/home/theanarchitect/code/Qryptana-release-artifacts/toolchain-regression-20261030/qryptana-temurin/main/app/qryptana`.

New archive: `/home/theanarchitect/code/Qryptana-release-artifacts/qryptana-7.0.3-linux-x86_64-temurin21-local.tar.gz`.

- Bytes: **388974500**.
- SHA-256: `c148a7d683de018126e362a83aebbce2b930739545d89b7e4f7ff0ea9665bac4`.
- Launcher SHA-256: `b6c11c88fd797451e6c81f075fef8d4d044a1187eb91698ecf932914f57e4654`.
- libapplauncher.so SHA-256: `336fe5e8ce1e8b36e0f8287f265f9456f52e776d48041f427e275c45c350e494`.
- Archive stream verified against the package manifest for **1017 regular files**; contains the app image only.

Original Linux archive/image, APKs, checksums and receipts are preserved. New artifact receipt is toolchain-evidence/artifacts-temurin21.json, also retained with full local diagnostics under the artifact directory. Existing upstream native provenance remains the verified v7.0.3 inputs from release-candidate-report.md. No independently rebuilt Haskell core, protocol/security certification or physical-device acceptance is claimed.

Remaining gates: reviewed operator-terms decision then main-screen/Settings/Appearance/support/updater checks; font scaling and broader desktop integration; distro installer integration (dpkg-deb absent); authorized clean ARM-device tests; translations and English-fallback review; maintainer owners/distribution decisions; privacy/operator/trademark/assets/source-distribution review. Public release remains HOLD. Nothing pushed, published, uploaded, deployed, signed with production keys or installed on a physical device.
