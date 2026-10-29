# Qryptana privacy statement (DRAFT: FOR LEGAL REVIEW)

This document describes the current repository implementation as inspected for the Qryptana remediation. It is not a legal notice, operator policy, or promise about services operated by Qryptana or any third party.

Qryptana is an early Labs-stage downstream fork of SimpleX Chat. The upstream operator policy is retained separately at [docs/upstream/SIMPLEX_PRIVACY.md](docs/upstream/SIMPLEX_PRIVACY.md); that document applies to its authors and operators, not automatically to Qryptana.

## What the application currently does

- Messaging uses the inherited SimpleX protocol implementation and its configured relay/server behavior. The repository does not establish that Qryptana operates those servers.
- The application stores profiles, contacts and messages in its local database. The code retains the upstream SQLCipher-backed database encryption behavior; device configuration and user choices can affect how a database is protected.
- Crash handling observed in the inspected code is local logging. No Qryptana telemetry or crash-reporting service is documented here; this statement is limited to the inspected repository and is not a guarantee about every dependency or future build.
- The desktop updater is disabled by default. If a user enables it, the current code checks the upstream SimpleX GitHub release feed for release information, honors the configured SOCKS proxy, and intentionally offers no upstream binary as a Qryptana update.

## Information we do not claim

Qryptana does not currently publish verified operator contacts, a support address, a data-retention schedule, a list of Qryptana-operated servers, or legal commitments about third-party relay operators. These items require human and legal review before any public release.

## Source and license

The source is distributed under AGPL-3.0. SimpleX Chat attribution and upstream documentation are retained in the repository. Qryptana is not affiliated with, endorsed by, sponsored by, or officially connected to the SimpleX Chat project.

## Review status

**DRAFT: FOR LEGAL REVIEW.** Confirm the actual deployment and operator model, support and privacy contacts, applicable jurisdictions, retention practices, and the final policy before publishing or distributing a Qryptana artifact.
