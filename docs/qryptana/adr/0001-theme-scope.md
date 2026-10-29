# ADR 0001: Desktop-only Qryptana palette scope

Status: accepted for the current remediation

## Decision

Qryptana copper/titanium palettes apply to desktop only. Desktop remains a single forced black-titanium finish, regardless of saved or system theme preferences. The Appearance screen explains this decision. Mobile restores the verified upstream Light, Dark, SimpleX and Black palette values until a complete Qryptana mobile theme set is designed and validated.

## Context

The shared palette definitions had been changed globally while the desktop Black palette alone was platform-conditional. That left Android with mixed Qryptana and upstream visual languages. The desktop force-lock was already behaviorally intentional but was not explained in the UI.

## Alternatives considered

The alternative is to complete the Qryptana palette rollout on Android, including named themes, launcher/notification validation and device testing, then re-enable a consistent cross-platform theme system. That is deferred because it requires product and device decisions beyond this remediation.

## Consequences

Desktop tests can assert the forced Black selection. Android device validation remains required. Saved theme preferences are still retained for compatibility but are ignored by the desktop selector until a future theme decision changes the behavior.
