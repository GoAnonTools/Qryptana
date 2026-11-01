# ADR 0001: Qryptana palette scope

Status: amended 2026-10-31; the earlier desktop-only decision is superseded

## Decision

Qryptana copper/titanium palettes apply to desktop and Android. Desktop remains a single forced black-titanium finish, regardless of saved or system theme preferences. Android retains its existing system/user theme-selection behavior and custom-theme overlay path; it now supplies coherent Qryptana Light, Dark, SimpleX and Black base presets.

Android semantic roles are explicit: warm copper action text, a darker copper filled/selected control tone, muted readable secondary text, titanium structural borders, charcoal/warm surfaces, and separate error/success/warning colours. Invitation cards use neutral titanium surfaces rather than saturated blue gradients. Smart Invite keeps a conventional near-black-on-white QR and moves copper to surrounding action/identity accents.

## Context

The remediation originally restored upstream Android palettes because the earlier partial rebrand left mixed visual languages. The maintainer has now authorized the complete Android copper rollout, including semantic roles, representative component work, measured contrast and focused visual evidence. This authorization supersedes the temporary stock-palette constraint without changing desktop behavior.

## Alternatives considered

The rejected alternatives are forcing desktop BLACK onto Android, collapsing the four named Android presets into one palette, or globally replacing every `SimplexBlue` reference. Those approaches would discard system/user choice, erase useful preset distinctions, or risk changing protocol-adjacent/shared UI without semantic review.

## Consequences

`ThemeDefaults` continues to own platform-dependent base-theme selection and palette construction without initializing `ChatController` or `CurrentColors`. `ThemeManager` still uses that selector, including saved theme, system theme, named presets, wallpapers and explicit app/user/chat overrides. Desktop BLACK selection is unchanged.

`ThemesTest` now checks role mapping, preset distinctness, selection behavior, unchanged desktop values, and calculated contrast rather than obsolete upstream colour literals. Enabled normal text targets at least 4.5:1 against its adjacent background; enabled structural controls/indicators target at least 3:1. Disabled controls may use lower contrast because their unavailable state is conveyed by semantics and control state as well as colour.

Existing wallpapers and custom theme behavior are intentionally unchanged. Error, warning and success remain distinct. QR payload, generation, correction level, quiet zone and logo size are unchanged. Physical Android device validation remains required; local previews and APK compilation are not a substitute for device rendering, scanning and accessibility checks.
