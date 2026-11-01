# Qryptana release checklist

## Verified version mapping

At the audited snapshot and remediation start, the application and core intentionally report different upstream-derived versions:

- Multiplatform Android and desktop `version_name`: `6.5.2` (`apps/multiplatform/gradle.properties`).
- Haskell core package version: `6.5.3.0` (`simplex-chat.cabal`).

This is a documented mapping, not a claim that the components are independently released. Version numbers were not changed by this remediation.

## Before any public release

- Complete the Qryptana support address and legal/privacy review.
- Validate Android palettes, launcher/notification icons and the desktop distributions on target devices.
- Provide native ICNS artwork and validate macOS packaging; the PNG reference is an unverified placeholder, not an accepted fallback.
- Decide bundle/application ID migration, video delivery and translated metadata.
- Establish a verified Qryptana release feed and signing/update process before enabling the updater.
- Run all three repository verification tasks and record the exact tested implementation commit.
- Review the manual inherited website/Haskell workflows before any execution, and decide Qryptana contribution policy; the SimpleX CLA job is gated off for this fork.
- Choose a future upstream integration strategy: the isolated v6.5.6 trial found unrelated histories, not a clean merge.
