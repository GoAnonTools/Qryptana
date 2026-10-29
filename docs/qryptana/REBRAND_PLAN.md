# Qryptana Rebrand Plan

Qryptana is a downstream fork derived from SimpleX Chat.

This plan defines the safe order for rebranding work.

## Phase 1 — Documentation and compliance

Status: complete for repository identity and documentation boundaries

- Add Qryptana scope documentation.
- Add upstream attribution.
- Add upstream relationship documentation.
- Add platform status documentation.
- Add upstream documentation status documentation.
- Clarify README so visitors understand which parts are Qryptana-specific and which parts are retained upstream reference.

Phase 1 is limited to documentation and compliance. It does not rename code
packages, protocol identifiers, storage paths, native targets, or application
resources.

## Phase 2 — Branding assets

Status: substantially complete for the current desktop/Labs scope

- Create original Qryptana logo and icons.
- Do not reuse SimpleX logos, illustrations, app icons, or visual identity.
- Locate all image/icon references before replacing assets.
- Replace visible branding assets only after confirming build impact.

The repository now contains original Qryptana symbol and wordmark assets. Desktop and Android distribution artwork is derived from the original Qryptana symbol; bundle/application IDs remain upstream-compatible by decision. Native ICNS tooling and a future identifier migration remain follow-up work.

## Phase 3 — User-facing text

Status: in progress

- Change only visible product text that clearly represents the app to users.
- Avoid changing protocol names, package names, code namespaces, or technical references blindly.
- Keep SimpleX references where they are required for attribution, upstream compatibility, or protocol explanation.

Base-locale operator wording, store metadata, support routing and privacy-document boundaries have been reviewed. Translated operator strings remain a documented follow-up and are not hand-translated in this remediation.

## Phase 4 — Release target isolation

Status: partially complete

- Keep iOS and Android source trees as upstream reference for now.
- Do not publish iOS or Android as Qryptana release targets.
- Remove upstream download links from Qryptana marketing.
- Define Qryptana-supported platforms explicitly before release.

The current decision is desktop-first Labs scope: Qryptana copper/titanium palettes apply to desktop, desktop uses a forced black-titanium finish, and mobile retains verified upstream palettes pending a deliberate rollout decision. See [ADR 0001](adr/0001-theme-scope.md).

## Phase 5 — Self-hosted server path

Status: not started

- Start with SMP relay research and deployment.
- Document VPS requirements.
- Document relay setup.
- Evaluate XFTP/file transfer later.
- Prefer simple, auditable deployment over complex automation at the beginning.

## Phase 6 — Public Qryptana release material

Status: not started

- Create Qryptana-specific product page.
- Create Qryptana-specific install instructions.
- Create Qryptana-specific release notes.
- Publish modified source as required by AGPL-3.0.
- Keep upstream attribution visible.
