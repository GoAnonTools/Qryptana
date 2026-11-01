# Qryptana privacy statement (DRAFT: FOR LEGAL REVIEW)

This document describes the current repository implementation as inspected for the Qryptana remediation. It is not a legal notice, operator policy, or promise about services operated by Qryptana or any third party.

Qryptana is an early Labs-stage downstream fork of SimpleX Chat. The upstream operator policy is retained separately at [docs/upstream/SIMPLEX_PRIVACY.md](docs/upstream/SIMPLEX_PRIVACY.md); that document applies to its authors and operators, not automatically to Qryptana.

## What the application currently does

- Messaging uses the inherited SimpleX protocol implementation. Preset operators are SimpleX Chat Ltd (`simplex.im`) and InFlux Technologies Limited, trading as Flux (`simplexonflux.com`), as defined in `src/Simplex/Chat/Operators/Presets.hs`. Server selection and roles depend on configuration and accepted operator conditions. These are third parties, not verified Qryptana-operated servers. Their retained [policy and conditions](docs/upstream/SIMPLEX_PRIVACY.md) are not Qryptana promises.
- The application stores profiles, contacts and messages in its local database. The code retains the upstream SQLCipher-backed database encryption behavior; device configuration and user choices can affect how a database is protected.
- Link previews are enabled by default (`privacyLinkPreviews` in `SimpleXAPI.kt`). When a preview is requested, `views/helpers/LinkPreviews.kt` fetches the linked page and its preview image/icon, potentially from different hosts and through redirects. These requests disclose the requested URL and network connection information to those hosts. The code uses the configured SOCKS proxy when enabled; without one it connects directly. This is separate from message encryption. Users can disable previews in Privacy and security.
- Inspected crash handlers log exceptions to Android's local logcat or desktop standard output (`platform/UI.android.kt`, `UI.desktop.kt`, `Log.android.kt`, `Log.desktop.kt`). Android also offers a user-initiated share action for crash text. Logs and shared diagnostic text can contain sensitive information. The inspected multiplatform Gradle dependencies and handlers do not configure a dedicated analytics/crash-upload SDK; this is not an exhaustive dependency audit or a promise of no network endpoints, OS diagnostics, or future changes.
- The desktop release-note checker is disabled by default. If a user enables it, `views/helpers/AppUpdater.kt` requests the upstream SimpleX GitHub release feed at `api.github.com`, immediately and periodically, and intentionally offers no upstream binary as a Qryptana update. The HTTP client uses a configured SOCKS host/port when valid; otherwise it connects directly. SOCKS authentication/stream isolation is not configured by this updater, so this draft does not promise equivalent proxy behavior across all app requests. Opening release notes also opens an external link with the browser's own network settings.

The paths above are under `apps/multiplatform/common/src/` (common, Android or desktop source sets). Database encryption dependencies are declared in `simplex-chat.cabal`. This is source inspection, not a network-traffic audit or verification of a distributed binary.

## Information we do not claim

Qryptana does not currently publish verified operator contacts, a support address, a data-retention schedule, a list of Qryptana-operated servers, or legal commitments about third-party relay operators. These items require human and legal review before any public release.

## Source and license

The source is distributed under AGPL-3.0. SimpleX Chat attribution and upstream documentation are retained in the repository. Qryptana is not affiliated with, endorsed by, sponsored by, or officially connected to the SimpleX Chat project.

## Review status

**DRAFT: FOR LEGAL REVIEW.** Confirm the actual deployment and operator model, support and privacy contacts, applicable jurisdictions, retention practices, and the final policy before publishing or distributing a Qryptana artifact.
