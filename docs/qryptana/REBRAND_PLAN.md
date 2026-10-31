# Qryptana Rebrand Plan

Qryptana is a downstream fork derived from SimpleX Chat.

This plan defines the safe order for rebranding work.

## Phase 1 — Documentation and compliance

Status: repository identity established; privacy/legal review remains open

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

Status: original artwork present; platform formats and device validation incomplete

- Create original Qryptana logo and icons.
- Do not reuse SimpleX logos, illustrations, app icons, or visual identity.
- Locate all image/icon references before replacing assets.
- Replace visible branding assets only after confirming build impact.

The repository contains original Qryptana symbol and wordmark assets from pre-remediation history (`f156294` and earlier). The initial remediation changed distribution references/ICO and re-encoded some existing pixels; it did not create all launcher artwork anew. The corrective pass regenerates density-correct adaptive foregrounds from that original symbol. Bundle/application IDs remain unchanged. ICNS generation, macOS packaging and Android launcher/notification device validation remain follow-up work; see [asset and ID status](BUNDLE_ID_MIGRATION.md).

## Phase 3 — User-facing text

Status: in progress

- Change only visible product text that clearly represents the app to users.
- Avoid changing protocol names, package names, code namespaces, or technical references blindly.
- Keep SimpleX references where they are required for attribution, upstream compatibility, or protocol explanation.

Base-locale operator wording, store metadata, support routing and privacy-document boundaries have corrective review coverage. The independent review found incomplete routing, palette and completion claims in the initial remediation. See the [remediation report](audits/remediation-report.md) rather than treating this plan as release certification. Translated operator/update/support/theme strings remain a [documented follow-up](audits/locale-followup.md); they are not hand-translated here.

## Phase 4 — Release target isolation

Status: partially complete

- Keep iOS and Android source trees as upstream reference for now.
- Do not publish iOS or Android as Qryptana release targets.
- Remove upstream download links from Qryptana marketing.
- Define Qryptana-supported platforms explicitly before release.

The current decision keeps desktop copper/titanium and forced BLACK while authorizing a complete semantic copper/titanium rollout on Android. Android continues to honor system/user selection, named Light/Dark/SimpleX/Black presets and custom overrides; wallpapers are unchanged. See [ADR 0001](adr/0001-theme-scope.md). The earlier stock-palette remediation and historical range `006ea4d..2121b84` remain history, not the current design acceptance rule.

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
