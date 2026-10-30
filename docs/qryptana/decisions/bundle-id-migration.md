# Bundle id migration

Status: awaiting maintainer decision.
Owner: [ASSIGN OWNER]
Decision: [SELECT OPTION]
Decision date: [YYYY-MM-DD]
Closure evidence: [LINKS / COMMIT / SIGN-OFF]

Provenance: reconstructed qryptana-grafted; source release-readiness `2d5b1beff`; this document does not assert a completed 7.x port.

Evidence: docs/qryptana/BUNDLE_ID_MIGRATION.md; Android applicationId and macOS bundle ID remain chat.simplex.app. Identity continuity can affect installation collisions, signing and upgrade/data behavior. Existing public user/install population is unknown.

Options: (A) retain current identifiers temporarily with distribution restrictions documented; (B) adopt new identifiers as a separate app with explicit data transfer; (C) design a platform-specific migration after signing/store ownership is established. Recommend no ID change in this port.

Required decisions: [ANDROID OWNER], [DESKTOP OWNER], [SIGNING/STORE OWNERSHIP EVIDENCE], [PROPOSED IDS], [EXISTING INSTALL INVENTORY]. Do not invent signing keys or assume upstream signing compatibility.

Closure evidence: counsel/platform review of identifier use; upgrade/co-install/deep-link/app-link/notification tests; encrypted export/import and rollback tests; signing custody plan; user communication appropriate to measured install history; explicit support window. ICNS/macOS packaging and Android notification/device validation remain separate gates.
