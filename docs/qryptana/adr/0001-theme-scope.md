# ADR 0001: Desktop-only Qryptana palette scope

Status: accepted for the current remediation

## Decision

Qryptana copper/titanium palettes apply to desktop only. Desktop remains a single forced black-titanium finish, regardless of saved or system theme preferences. The Appearance screen explains this decision. Mobile restores the verified upstream Light, Dark, SimpleX and Black palette values until a complete Qryptana mobile theme set is designed and validated.

## Context

The shared palette definitions had been changed globally while the desktop Black palette alone was platform-conditional. That left Android with mixed Qryptana and upstream visual languages. The desktop force-lock was already behaviorally intentional but was not explained in the UI.

## Alternatives considered

The alternative is to complete the Qryptana palette rollout on Android, including named themes, launcher/notification validation and device testing, then re-enable a consistent cross-platform theme system. That is deferred because it requires product and device decisions beyond this remediation.

## Consequences

`ThemeDefaults` owns the platform-dependent base-theme selection and palette construction without initializing `ChatController` or `CurrentColors`. `ThemeManager` uses that selector, and the runtime palette values use the same factories. `ThemesTest` checks desktop BLACK for every saved base/system choice, contrasting mobile preference behavior, and compares all four mobile material/app palettes with literal values verified from `423c8c0`. The Light/Dark primary, variant and title use the upstream `#0088FF`, not the compatibility `SimplexBlue` alias (which is copper).

The earlier `51ca731` restoration was incomplete because of that alias, and `2be2c94` removed the direct ThemeManager test after an initialization-cycle failure. The isolated defaults now make meaningful coverage possible without removing baseline tests. The explanation is shown in the main Appearance themes section, not only Customize colors.

Android device validation remains required. This decision covers base palettes, not a full restoration of every mobile screen or shared branding helper. Saved theme preferences and explicit per-user/per-chat overrides are retained; desktop base-mode selection ignores saved/system mode without rewriting those preferences. Existing wallpaper behavior is intentionally unchanged in this correction. Completing Android copper remains deferred.
