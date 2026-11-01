# Qryptana Android Visual Proposals • Refined Direction & Onboarding Prototype

This directory hosts the visual proposals and interactive prototypes for the Qryptana Android redesign, presenting **Precision Titanium with Directional Material Depth** and the complete **Onboarding Sequence with Video Reveal Integration**.

---

## 1. Standalone Interactive Prototype

The standalone interactive prototype viewer is located at:
👉 **[`docs/qryptana/design/android/proposals/index.html`](index.html)**

Open `docs/qryptana/design/android/proposals/index.html` in any modern web browser (Firefox, Chrome, Edge, Safari).

### Direct View Hash Links
- **Welcome & Onboarding Sequence**: [`index.html#onboard`](index.html#onboard)
- **Settled Plaque & Revealed Copy State**: [`index.html#settled`](index.html#settled)
- **Reduced Motion & Failure Fallback State**: [`index.html#reduced-motion`](index.html#reduced-motion)
- **Complete Conversation & Pinned Composer**: [`index.html#conv`](index.html#conv)
- **Chat List & Milled Search Filter**: [`index.html#list`](index.html#list)
- **Material Anatomy & Lighting Close-ups**: [`index.html#closeups`](index.html#closeups)

---

## 2. Onboarding Video Reveal Implementation & Specification

The onboarding experience embeds the authored reveal animation (`qryptana_identity_card_reveal.mp4`) with complete Compose runtime parity:

### Video Asset Integrity
- **Source Asset**: `apps/multiplatform/common/src/commonMain/resources/assets/videos/qryptana_identity_card_reveal.mp4`
- **Resolution**: `1280 x 714` (exact aspect ratio `1.7927:1`)
- **Encoding**: H.264 High Profile, progressive, 24.0 fps, duration 11.291s (271 frames), no audio stream.
- **Visual Handling**: Preserved at full authored aspect ratio (`object-fit: contain`). Zero cropping, stretching, recoloring, tinting, or video shader modifications.

### Perimeter Pixel Edge Analysis
Edge pixel sampling of the first (`frame_start.png`), intermediate (`frame_mid1.png`, `frame_mid2.png`), and settled (`frame_end.png`) frames using ImageMagick confirmed the video's dark perimeter lighting:
- **Top Edge**: `#1B1917` (top center) easing to `#141210` (corners).
- **Bottom Edge**: `#171513` (bottom center) easing to `#141210` (corners).
- **Side Edges**: `#141210` to `#151311`.

### Dual-Theme Integration Architecture
1. **Dark Mode Foundation (`#0E1013` / `#141210`)**:
   - The immediate surrounding stage matches `#141210` with a subtle radial falloff matching the video lighting center.
   - Applies an edge-feathered mask mirroring the exact Compose `SimpleXInfo.kt` DstOut gradient: 17% horizontal fade (`fx = width * 0.17f`) and 14% vertical fade (`fy = height * 0.14f`).
   - The video perimeter dissolves into the screen foundation without visible borders, card edges, or rectangular seams.
2. **Light Mode Foundation (`#F1F3F5`)**:
   - Rather than forcing artificial transparency onto an opaque dark MP4, the prototype mounts the video inside an **intentional dark titanium hero pedestal / chamber**.
   - Built with a milled 1px titanium bezel (`#38414D`), top specular hairline (`inset 0 1px 0 rgba(221, 226, 232, 0.22)`), and elevation drop shadow (`0 10px 24px rgba(0, 0, 0, 0.12)`).
   - Features a subtle "SECURE IDENTITY PLAQUE" micro-badge with an authentic copper anchor dot.
   - Honors the video's dark physical key and rim lighting as a tactile hardware object resting gracefully on the light chassis.

### Staged Sequence Timeline & Controls
The prototype includes an interactive sequence control deck above the side-by-side devices:
- **0.0s — Initial Poster State**: First frame poster (`frame_start.png`) displayed; audio strictly muted; title, supporting copy, and buttons hidden.
- **0.0s to 8.8s — Active Reveal Playback**: Smooth 24fps video playback showing the physical identity plaque sliding and settling into place.
- **8.8s (`QRYPTANA_TEXT_REVEAL_AT_MS`) — Settle & Plaque Rest**:
  - The plaque comes to rest.
  - A soft contact shadow ellipse renders under the plaque at 89.5% height (matching Compose `SimpleXInfo.kt` lines 767–785).
  - Headline statement (*"Private conversations,\nreimagined"*) rises and fades in.
- **10.0s (`supportReveal`)**: Engraved copper hairline divider (36dp × 1dp) and supporting body copy fade in smoothly.
- **11.2s (`actionsReveal`)**: Machined copper CTA button (*"Create my identity"*) and secondary action (*"How it works"*) fade in and become interactive.
- **11.29s — Held Rest State**: The final settled frame is held indefinitely with zero abrupt background switches or black flashes.
- **12.5s Safety Net (`QRYPTANA_TEXT_REVEAL_TIMEOUT_MS`)**: If playback stalls, the safety net triggers automatically to ensure copy is never trapped invisible.
- **Reduced Motion / Playback Failure Fallback**: Bypasses video playback entirely, immediately mounting the static identity card and presenting the settled copy and CTA buttons without delay.

### Android Lifecycle & Playback Parity
- **Visibility & Navigation Handling**: Audio is muted (`muted` attribute). Video pauses when navigating to other screens or switching browser tabs (`document.visibilitychange`), and safely resumes when returning to onboarding.
- **Scrubbing & Review Bar**: Dragging the timeline slider scrubs synchronously across both dark and light devices, immediately adjusting reveal opacity and contact shadow state based on playhead position.

---

## 3. Canonical Reviewed Copy & Attribution Integrity

All marketing language conforms strictly to upstream reviewed strings from `MR.strings` (in `apps/multiplatform/common/src/commonMain/resources/MR/base/strings.xml`):
- **Hero Title**: *"Private conversations,\nreimagined"* (`MR.strings.onboarding_be_free`)
- **Divider**: 36dp × 1dp machined copper hairline (`#B87333` / `#80513E`)
- **Subhead**: *"Connect with the people you choose, privately and freely."* (`MR.strings.onboarding_private_and_secure`)
- **Body**: *"Your conversations stay yours, with no phone number or email required."* (`MR.strings.onboarding_first_network`)
- **Primary Action**: *"Create my identity"* (`MR.strings.get_started`)
- **Secondary Action**: *"How it works"* (`MR.strings.how_it_works`)

> [!IMPORTANT]
> **Zero Unreviewed Claims**: Unreviewed phrases like *"without servers"*, *"without identity"*, and *"quantum-resistant ready"* were explicitly eliminated. The copy accurately honors decentralized SMP relays and anonymous cryptographic key distribution.

---

## 4. Conversation & Chat List Presentation

- **Scrollbar Elimination**: Persistent desktop browser scrollbars inside phone viewports are fully suppressed via CSS (`scrollbar-width: none; -ms-overflow-style: none; ::-webkit-scrollbar { display: none; }`) while preserving natural wheel and touch scrolling.
- **Complete Composer Visibility**: The conversation screen pins the full milled titanium composer shelf above the system gesture bar at the bottom of the phone. The text field, mic button, attachment button, and machined copper send button remain 100% visible and unclipped.
- **Matte Bubbles**: Sent bubbles use Raised Titanium (`#1E232A` dark / `#E1E5EA` light) and received bubbles use Surface Titanium (`#161A1F` dark / `#FFFFFF` light) with zero gloss or glare, ensuring 12.1:1 contrast.
- **Quoted Conductor**: Quoted reply plates use an authentic copper wire conductor (`#B87333`) to anchor context.

---

## 5. Verification & Scope Guardrails

- [x] **NO Application Code Modified**: Kotlin/Compose production code in `apps/` remains completely untouched.
- [x] **Local Branch**: Active on `android-design-exploration`.
- [x] **Zero APK Builds**: No APK compilation or packaging tasks were run.
- [x] **Artwork Integrity**: Canonical brand assets (`qryptana-wordmark.png`, `qryptana-symbol.png`, `qryptana_identity_card_reveal.mp4`) are preserved unmodified.
- [x] **Browser vs Android Distinction**: Browser prototype verified via Firefox headless snapshots (`1366x1372`) to confirm zero rendering errors, correct layout containment, and responsive controls before any future Compose implementation.
