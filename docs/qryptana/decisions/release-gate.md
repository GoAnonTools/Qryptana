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
