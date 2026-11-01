# Bundle and application ID migration

The current application identifiers remain unchanged for upgrade compatibility:

- Android `applicationId`: `chat.simplex.app` (including existing debug suffix behavior).
- Desktop macOS bundle ID: `chat.simplex.app`.

Distribution artwork uses the existing original `docs/qryptana/brand/qryptana-symbol.png`. The initial remediation renamed/re-encoded some already-Qryptana assets: the desktop PNG and Android legacy `icon.png` pixels were already present at `2121b84`, not newly designed in `ff5daf4`. Desktop ICO was generated and build references changed. macOS still points at PNG; that is an unverified placeholder, not a validated jpackage fallback. A real ICNS and macOS packaging validation remain required.

The corrective pass regenerates both adaptive foreground variants directly from the 1254×1254 original, not the small legacy icons. Each uses a 108 dp transparent canvas (108/162/216/324/432 px for mdpi through xxxhdpi), with the original scaled to 66 dp and centered. Existing source padding is preserved, keeping the visible Q inside launcher masks. The `icon_round_dark_blue` adaptive alias now references the adaptive foreground rather than the legacy round bitmap. IDs, legacy launcher pixels, background colors and notification artwork are unchanged by this correction.

Reproduction uses ImageMagick locally, for each density and both `icon_foreground.png` / `icon_dark_blue_foreground.png`:

```sh
magick docs/qryptana/brand/qryptana-symbol.png -resize 264x264 -gravity center -background none -extent 432x432 apps/multiplatform/android/src/main/res/mipmap-xxxhdpi/icon_foreground.png
```

Other density canvas/artwork sizes: mdpi 108/66, hdpi 162/99, xhdpi 216/132, xxhdpi 324/198. Alpha and dimensions were checked; light/dark circle and rounded-square raster mask simulations were inspected. These are not Android device screenshots. No connected Android device was available, and monochrome notification rendering still needs device review.

Changing identifiers in a future release would require an explicit migration plan covering installation upgrades, deep links, app links, notifications, store records, signing identities and user data. No identifier migration is performed here.

NEEDS HUMAN: decide whether and when a bundle/application ID migration is worth the upgrade and ecosystem cost, and provide native icon assets for each target platform.
