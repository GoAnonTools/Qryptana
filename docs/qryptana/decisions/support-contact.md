# Support contact

Status: awaiting maintainer decision.
Owner: [ASSIGN OWNER]
Decision: [SELECT OPTION]
Decision date: [YYYY-MM-DD]
Closure evidence: [LINKS / COMMIT / SIGN-OFF]

Provenance: reconstructed qryptana-grafted; source release-readiness `2d5b1beff`; this document does not assert a completed 7.x port.

Evidence: SettingsView.kt defines qryptanaSupportUri: String? = null; Settings and ChatHelpView hide chat-contact entries while null. Existing email and store-rating entries were removed. Source actions use the Qryptana repository.

Options: (A) keep support hidden for Labs; (B) provision a maintained SimpleX chat-contact URI; (C) add separately typed email/web support with dedicated handlers. Recommend A until ownership and a tested destination exist. A chat-contact URI cannot be replaced by an email or website string.

Required inputs: [SUPPORT OWNER], [VERIFIED CHAT-CONTACT URI, if selected], response scope/hours, abuse handling and key/device recovery process. No address is assumed.

Closure evidence: owner approval; destination verification from a clean installation; tests for null hiding and correct URI handling; reviewed labels/translations; privacy treatment of support messages. Source links must remain distinct from support. Do not enable a destination merely to close this document.
