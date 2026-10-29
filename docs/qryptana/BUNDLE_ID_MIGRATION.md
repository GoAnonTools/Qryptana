# Bundle and application ID migration

The current application identifiers remain unchanged for upgrade compatibility:

- Android `applicationId`: `chat.simplex.app` (including existing debug suffix behavior).
- Desktop macOS bundle ID: `chat.simplex.app`.

This remediation replaces distribution and launcher artwork using the existing original `docs/qryptana/brand/qryptana-symbol.png`. Desktop PNG and ICO assets were generated locally and wired into the distribution build. Native ICNS generation tooling was unavailable, so macOS currently uses the derived PNG and needs a native ICNS pass before release packaging.

Changing identifiers in a future release would require an explicit migration plan covering installation upgrades, deep links, app links, notifications, store records, signing identities and user data. No identifier migration is performed here.

NEEDS HUMAN: decide whether and when a bundle/application ID migration is worth the upgrade and ecosystem cost, and provide native icon assets for each target platform.
