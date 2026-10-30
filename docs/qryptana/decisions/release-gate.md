# Release gate

Status: awaiting maintainer decision.
Owner: [ASSIGN OWNER]
Decision: [SELECT OPTION]
Decision date: [YYYY-MM-DD]
Closure evidence: [LINKS / COMMIT / SIGN-OFF]

Provenance: reconstructed qryptana-grafted; source release-readiness `2d5b1beff`; this document does not assert a completed 7.x port.

Decision today: HOLD public release. Compilation alone is insufficient.

Options: (A) keep local development checkpoint; (B) internal unsigned test distribution after runtime validation; (C) public signed release after all closure evidence. Recommend A until the port and prerequisite checks are verified.

Technical gates: exact ancestry/tree-equivalence evidence; stable-target metadata/tag hash; completed merge with reviewed integration conflicts; all three required Gradle checks and test counts at the final implementation hash; upstream protocol source/dependency provenance; compatible native core/JNI libraries; Android device, desktop runtime, packaging, ICNS and notifications; upgrade/import/export and rollback validation. No production code may hide missing native dependencies.

Behavior gates: desktop forced BLACK copper/titanium; selected upstream stock mobile palettes; disabled update preference and no upstream binaries; null support hiding; retained IDs; dispatch-only inherited website deployment; unchanged intended wallpaper; original assets/licenses. Review new upstream crowdfunding/news/store/support UI for truthful product boundaries.

Decision gates: support-contact, privacy-review-checklist, bundle-id-migration, onboarding-video, trademark-and-naming; locale/store translations; signing custody and platform/distribution scope; CI execution. Population/install history remains unknown.

Closure evidence: [RELEASE OWNER] records final source/target/implementation hashes, logs/test counts, runtime matrix, reviewed legal decisions, artifact provenance/checksums and rollback plan. Future publication should use reviewed branches and a maintainer-controlled PR, then reproducible signed artifacts with source availability; none is authorized or performed by this task.


## Local candidate verification — 2026-10-30

Decision remains HOLD for public release. Local Linux distribution and debug Android APKs now exist; this supersedes the earlier absence of native/package evidence, without closing legal or device gates. Evidence: ../release/release-candidate-report.md and ../release/evidence/.

| Category | Actual evidence / unresolved gate | Next concrete action |
|---|---|---|
| Technically verified | Durable qryptana-release-validation from 97dd441a1; committed 35-test XML baseline validated, no app-source changes since tested 3ad2abd2c. Linux createDistributable and Android assembleFossDebug both pass with matching verified upstream native libraries. APK v2 signatures verify. | Retain hashes/native manifest with any internal test copy. |
| Technically verified | Linux JNI loads, isolated database/profile initializes, branded onboarding and scrollable action buttons render. Original IDs/palettes/licenses/updater/support safeguards remain unchanged. | Complete full desktop navigation smoke manually; do not infer coverage of unvisited screens. |
| Technically blocked | Automated Settings/Appearance/support navigation did not succeed reliably with the available X11/window-minimization automation. A native-launcher diagnostic says “pure virtual method called”; the app nevertheless remained running until deliberately terminated. No crash-free/runtime-complete claim. | Manually reproduce in the isolated profile, record Settings/Appearance/updater/support screenshots; trace launcher warning before accepting runtime gate. |
| Technically blocked | No .deb built: dpkg-deb packaging tool is absent. A self-contained Linux app image is archived instead. Matching native inputs are official release binaries, not independently rebuilt in this task. | Use the app image for local tests; build distro installer in a prepared packaging environment and perform independent source rebuild if required by release policy. |
| Requires physical-device testing | ARM64/ARMv7 debug APKs built and signatures verified; no connected device. Only x86_64 AVD exists and /dev/kvm is unavailable. No install/uninstall/data-clear performed. | Obtain explicit physical-device installation authorization and use the prioritized checklist in the candidate report. |
| Requires maintainer input | Assign release/platform/translation owners and distribution scope; supply typed contact details only if support is to be enabled. Existing conservative choices remain: IDs unchanged, video bundled, Android copper deferred, updates/support/publishing disabled. | Complete outstanding fields in the existing five decision documents; no need to re-decide these current defaults for local testing. |
| Requires legal review | Privacy/operator policy, contacts, asset/name rights, trademark clearance, distribution/source obligations and upstream crowdfunding presentation remain unapproved. User/install population unknown. | Counsel and actual operator/rights owners supply written review and approved copy. |
| Requires maintainer input | Locale and store translations remain unreviewed; English fallback is not completed localization. | Assign reviewers to locale-followup.md and obtain reviewed translations before selected-locale publication. |

No production keys or production identity generated; no push, upload, deployment or publication. Debug signing is only for isolated local testing. Native-binary download provenance is documented separately from upstream-binary updater safeguards: those safeguards were not relaxed.


## Linux runtime attribution follow-up — 2026-10-30

Evidence: ../release/runtime-acceptance-report.md and ../release/runtime-evidence/. The previously unresolved startup diagnostic is now attributed with high confidence to a pre-JVM jpackage launcher child: raw wait status 134 (SIGABRT with core flag), logger called from libapplauncher.so unload destructor. Its rpm/dpkg probes exit 127. The parent JVM is a separate surviving process; this does not make the child abort harmless. The dynamically linked JDK 21 launcher matches JDK-8348560. A corrected verified JDK packaging toolchain/launcher is required before closing this technical gate; no arbitrary native replacement or diagnostic suppression was made.

Full Linux acceptance remains BLOCKED. Uninstrumented startup and an initial-profile restart were observed; onboarding completion, Settings/Appearance, runtime support/updater, resize/font scaling and normal UI shutdown remain UNPERFORMED. KDE Wayland was inspected first; one temporary compositor/uinput approach did not establish reliable navigation and was stopped. No further X11 focus retries. Use the exact isolated manual command/checklist in the runtime report. The temporary compositor script and virtual input device were removed; no global desktop configuration changed.

No production source/build/native artifact changed. Existing 35 passing tests and APK build/signature evidence remain applicable; no builds were repeated. Device/legal/privacy/trademark/translation/installer gates remain as listed above. This update supersedes the earlier unresolved-attribution description, not the remaining runtime gates.
