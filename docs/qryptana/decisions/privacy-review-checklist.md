# Privacy review checklist

Status: awaiting maintainer decision.
Owner: [ASSIGN OWNER]
Decision: [SELECT OPTION]
Decision date: [YYYY-MM-DD]
Closure evidence: [LINKS / COMMIT / SIGN-OFF]

Provenance: reconstructed qryptana-grafted; source release-readiness `2d5b1beff`; this document does not assert a completed 7.x port.

Evidence: root PRIVACY.md is a legal-review draft; docs/upstream/SIMPLEX_PRIVACY.md is the retained upstream operator policy embedded via src/Simplex/Chat/Operators.hs. docs/upstream/README.md explains that boundary. The remediation report records default link previews, operator names SimpleX Chat Ltd and InFlux Technologies Limited, local logging/sharing, and optional upstream GitHub checks. These are inspected source behavior, not an audited shipped binary or operator promises.

Options: (A) retain internal Labs draft and block public release; (B) approve a Qryptana policy after operator/data-flow verification; (C) change service scope and repeat review. Recommend A pending counsel and operator evidence.

Checklist for owner: inventory initial/default and optional endpoints including previews, relays, calls, update feed and support; capture network behavior on supported platforms; inspect logs, crash reports, backups, retention/deletion, encryption/key handling and export; identify who operates each endpoint and jurisdiction; confirm roles, lawful basis, disclosures and contact requirements with counsel. Review embedded upstream terms separately from Qryptana policy and verify translated links.

Unknown: [LEGAL ENTITY], [PRIVACY CONTACT], [ACTUAL OPERATIONS], [RETENTION], [JURISDICTIONS]. Do not infer user population either way.

Closure evidence: signed review record, endpoint inventory tied to implementation hash, traffic test results, approved operator disclosures and policy/contact text, translation review, and verified Haskell embedded-policy path. No claim of universal absence of telemetry follows from this source review.
