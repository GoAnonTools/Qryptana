# Onboarding video

Status: awaiting maintainer decision.
Owner: [ASSIGN OWNER]
Decision: [SELECT OPTION]
Decision date: [YYYY-MM-DD]
Closure evidence: [LINKS / COMMIT / SIGN-OFF]

Provenance: reconstructed qryptana-grafted; source release-readiness `2d5b1beff`; this document does not assert a completed 7.x port.

Evidence: common/src/commonMain/resources/MR/videos/qryptana_identity_card_reveal.mp4 is referenced by SimpleXInfo.kt. The tracked artifact is 10,509,268 bytes (about 10.02 MiB). This is source asset size, not its compressed contribution to any shipped package. No packages were built for this document; package overhead/compression and startup/runtime memory were not measured.

Options: (A) keep bundled/offline video; (B) replace with an approved static original Qryptana asset; (C) optional download with privacy/cache/offline handling. Recommend A for the current port; assess B using device accessibility/performance evidence. C adds network behavior requiring privacy review.

Inputs: [PRODUCT OWNER], [ARTWORK/VIDEO RIGHTS RECORD], [SUPPORTED DEVICE BUDGET], [ACCESSIBILITY REQUIREMENTS]. Existing provenance must be retained; no unrelated redesign is authorized here.

Closure evidence: approved delivery choice; actual package size delta from permitted release builds, playback and reduced-motion/accessibility tests, offline first-run behavior, device resource measurements and fallback screenshots. Record tool/platform/implementation hashes for any measurement.
