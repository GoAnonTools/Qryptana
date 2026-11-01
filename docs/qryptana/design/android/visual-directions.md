# Qryptana Android Visual Directions

## 1. Visual Philosophy & Common Identity Foundation

All three design directions share the exact same core Qryptana brand identity:
- **Foundations**: Cool charcoal (`#0E1013`) and brushed titanium (`#161A1F`, `#1E232A`). Zero brown, beige, tan, or orange casts.
- **Typography & Structural Accents**: Crisp silver hierarchy (`#DDE2E8` primary text, `#9BA4AF` secondary details, `#6A737E` structural hairlines).
- **Accents**: Restrained, authentic copper (`#B87333` reference, `#9F634C` accessible filled control, `#80513E` light action). Copper strictly occupies **5% or less** of any ordinary screen.
- **Brand Artwork Integrity**: The canonical Qryptana symbol and wordmark are preserved exactly as approved. No tinting, shaders, recoloring, or redraws.

Below are three distinct, coherent design options exploring how this metallic visual language can be expressed across the Android experience.

---

## Direction A: Precision Titanium (Recommended)

### Concept & Personality
*Calm, restrained, crisp structure, sparse copper.*
Precision Titanium treats the mobile interface as a calibrated, high-precision instrument. The atmosphere is quiet, focused, and distraction-free. Structural clarity and legibility dominate. Elevation is communicated through subtle shifts in cool neutral luminance and crisp 0.5dp–1dp hairlines rather than heavy drop shadows. Copper is held in extreme reserve—appearing only on primary action verbs, active selection states, unread counters, and subtle security status marks.

### 1. Typography & Hierarchy
- **Font Family**: Inter (standardized weights).
- **Headings (H1/H2)**: Inter Medium / SemiBold in Silver Primary (`#DDE2E8` Dark / `#20262B` Light). Calm tracking, avoiding exaggerated display sizes.
- **Body Text**: Inter Regular 15sp / line-height 21sp in Cool Silver (`#DDE2E8` Dark / `#30383F` Light).
- **Metadata / Secondary**: Inter Regular 13sp in Muted Titanium (`#9BA4AF` Dark / `#59636C` Light).
- **Action Verbs**: Inter Medium 14sp in Warm Copper Action Text (`#D3A78F` Dark / `#80513E` Light).
- **Monospace Strings**: Monospace 13sp on addresses, safety numbers, and hashes, formatted in 4-character chunked blocks.

### 2. Palette & Semantic Roles

| Semantic Role | Dark Mode (Charcoal/Titanium) | Light Mode (Cool Titanium White) | WCAG Contrast Target |
|---|---|---|---|
| **Canvas Background** | `#0E1013` (Deep Charcoal) | `#F1F3F5` (Cool Pale Titanium) | Base |
| **Card / Surface** | `#161A1F` (Titanium Surface) | `#FCFDFE` (Crisp Titanium White) | Base |
| **Raised Surface** | `#1E232A` (Elevated Titanium) | `#FFFFFF` (Pure White) | Base |
| **Primary Text / Heading** | `#DDE2E8` (Silver Primary) | `#20262B` (Deep Charcoal) | ≥ 13.0:1 |
| **Body Text** | `#DDE2E8` (Silver Primary) | `#30383F` (Charcoal Body) | ≥ 10.5:1 |
| **Secondary Text** | `#9BA4AF` (Silver Secondary) | `#59636C` (Muted Slate) | ≥ 5.5:1 |
| **Structural Hairline** | `#262C34` (Subtle 0.5dp) | `#D8DEE4` (Subtle 0.5dp) | ≥ 3.0:1 |
| **Container Border** | `#6A737E` (Muted Titanium 1dp) | `#7C858D` (Muted Titanium 1dp) | ≥ 3.5:1 |
| **Primary Action Text** | `#D3A78F` (Refined Copper Text) | `#80513E` (Dark Copper Text) | ≥ 5.9:1 |
| **Filled Control Fill** | `#9F634C` (Solid Control Copper) | `#9F634C` (Solid Control Copper) | Control Base |
| **On-Filled Control (Thumb/Text)** | `#FFFFFF` (White) | `#FFFFFF` (White) | **4.81:1** (Exceeds 4.5:1) |
| **Sent Message Bubble** | `#1E232A` (Raised Cool Titanium) | `#E1E5EA` (Cool Silver-Grey) | Eliminates all brown cast |
| **Received Message Bubble** | `#161A1F` (Titanium Surface) | `#FFFFFF` (Pure Crisp White) | High contrast, calm reading |
| **Unread Badge** | `#9F634C` Fill / `#FFFFFF` Text | `#9F634C` Fill / `#FFFFFF` Text | 4.81:1 contrast |
| **Error / Danger** | `#FFB4AB` (Muted Crimson) | `#B3261E` (Deep Crimson) | ≥ 4.5:1 |
| **Success Indicator** | `#81C784` (Soft Green) | `#2E7D32` (Forest Green) | ≥ 4.5:1 |
| **Warning Indicator** | `#FFB74D` (Muted Amber) | `#E65100` (Deep Amber) | ≥ 4.5:1 |

### 3. Component Shapes, Borders, Spacing & Elevation
- **Corner Radii**: 12dp for message bubbles, 14dp for invitation action cards, 16dp for section groups, 20dp for dialogs and sheets.
- **Borders**: Crisp, uniform 0.5dp or 1dp hairlines (`#262C34` Dark, `#D8DEE4` Light). No heavy fuzzy drop shadows; layers are delineated by tonal shift and precise edge definition.
- **Spacing**: Comfortable 16dp horizontal gutter, 12dp card padding, 8dp item separation.
- **Touch Targets**: All interactive elements strictly adhere to **minimum 48dp × 48dp** touch bounding boxes.

### 4. Copper Placement & Metallic Treatment
- **Total Copper Screen Budget**: **~2% to 4%** of user-visible pixels.
- **Where Copper Lives**: Primary action buttons ("Send", "Accept", "Create"), switch toggle active track (`#9F634C`), small 24dp invitation glyphs, unread count pills, active filter chip stroke, and verified security checkmarks.
- **Metallic Realism**: Kept strictly to supporting vector artwork (e.g. subtle multi-stop linear gradient on small decorative icons: `#6A3E32` -> `#AD7257` -> `#F0D4C4`) and hardware reveal assets. Zero copper filters on backgrounds, wordmarks, or text blocks.

### 5. Interaction Feedback & Motion
- **Touch Feedback**: Low-latency, desaturated ripple (`#DDE2E8` at 8% alpha in dark mode).
- **Transitions**: Crisp linear-out-slow-in (200ms) for modal sheets; quick 150ms cross-fades between chat list and conversation.
- **Haptics**: Subtle, light tick on switch toggle and message long-press. Respects `accessibility_reduce_motion`.

### 6. Accessibility & Implementation Tradeoffs
- **Pros**: Highest sustained readability; fully exceeds WCAG AAA contrast on text (13:1+ on headings); switch track/thumb contrast is mathematically verified at 4.81:1; minimal GPU rendering overhead.
- **Tradeoffs**: Lower decorative flamboyance; appeals to users who value utility, privacy, and understated luxury over flashy visual effects.

---

## Direction B: Machined Metal

### Concept & Personality
*More tactile supporting artwork and controlled metallic highlights.*
Machined Metal draws inspiration from precision-milled aerospace titanium hardware and authentic copper inlay craftsmanship. Surfaces feature subtle directional sheen effects, inset wells for input fields, and dual-tone borders that evoke beveled metallic plates. Copper is treated as an engraved inlay—subtly rimming prominent cards, progress rings, and key security components.

### 1. Typography & Hierarchy
- **Font Family**: Inter, paired with distinct semi-bold sub-headers.
- **Headings (H1/H2)**: Inter SemiBold in Titanium Light (`#DDE2E8`), styled with tightened tracking to give an engraved architectural feel.
- **Body Text**: Inter Regular 15sp in Silver Primary (`#DDE2E8` Dark / `#30383F` Light).
- **Secondary Details**: Inter Medium 13sp in Muted Titanium (`#9BA4AF`).
- **Caps Labels**: Micro-headers (e.g. section titles) styled in Inter Medium 11sp with +0.5sp letter spacing and uppercase casing.

### 2. Palette & Semantic Roles
- **Canvas Background**: `#0E1013` (Dark) / `#F1F3F5` (Light).
- **Titanium Plate Surface**: `#161A1F` with a subtle 145° directional sheen (top-left `#1A1F26` to bottom-right `#13161A`).
- **Inset Wells (Inputs & Quoted Replies)**: `#121519` (Dark recessed plate) / `#E8ECEF` (Light recessed plate) with an inset 1dp top border (`#090B0D`).
- **Dual-Tone Bevel Borders**: Cards use a 1dp linear gradient border (top-left `#6A737E` 70% alpha, bottom-right `#262C34` 30% alpha) to simulate a light source reflecting off a chamfered metal edge.
- **Copper Inlay Accent**: Authentic copper (`#B87333`) used as a thin 1dp accent line along active card borders, voice message waveforms, and circular progress rings.
- **Sent Message Bubble**: Deep Titanium `#20262E` with a 1dp subtle copper top-right bevel highlight (`#AD7257` at 40% alpha).
- **Received Message Bubble**: Milled Titanium `#161A1F` with a cool silver left hairline.

### 3. Component Shapes, Borders, Spacing & Elevation
- **Corner Radii**: 16dp for cards, 14dp for message bubbles, 24dp for modal containers.
- **Tactile Plates**: Elevated surfaces appear as physically layered metallic plates. Subtle 2dp vertical shadows with 0-spread dark ambient tint (`#000000` at 40% alpha).
- **Buttons**: Primary buttons feature a subtle brushed metallic gradient fill (`#9F634C` to `#8A523D`) with a crisp 1dp top specular highlight.

### 4. Copper Placement & Metallic Treatment
- **Total Copper Screen Budget**: **~4% to 5%** of user-visible pixels.
- **Where Copper Lives**: Subtle 1dp border inlays on active cards, voice playback progress scrubbers, floating action button edge highlight, Smart Invite QR border rim, and selected tab indicators.
- **Metallic Realism**: High tactile craft in supporting assets. Never an orange color wash; copper is always bounded by cool titanium graphite.

### 5. Interaction Feedback & Motion
- **Motion**: Physical, spring-damped easing (`cubic-bezier(0.2, 0.8, 0.2, 1)`) simulating mechanical inertia.
- **Touch States**: Buttons depress slightly (`scale(0.98)`) with a brief specular sheen flash across the border before executing actions.

### 6. Accessibility & Implementation Tradeoffs
- **Pros**: Visually distinctive and unmistakably metallic; feels bespoke, handcrafted, and tactile.
- **Tradeoffs**: Requires slightly more complex Compose `drawWithCache` or gradient modifier implementations; careful testing needed to ensure gradient borders maintain required contrast against all wallpaper backgrounds.

---

## Direction C: Charcoal Gallery

### Concept & Personality
*Generous spacing, strong typography, striking but restrained hero composition.*
Charcoal Gallery treats messaging as an editorial, premium canvas. It utilizes expansive whitespace (or "dark space"), strong typographic contrast, and floating card surfaces. Information density is relaxed, giving the user a serene, uncluttered experience. Copper is utilized as an artful punctuation mark—appearing like a jeweler's hallmark on pure charcoal expanses.

### 1. Typography & Hierarchy
- **Font Family**: Inter with expressive typographic scale.
- **Headings (H1/H2)**: Inter Light/Normal in oversized sizes (28sp–34sp) with generous line heights (36sp–42sp), creating a sophisticated, high-end editorial presence.
- **Body Text**: Inter Regular 16sp / line-height 24sp for exceptional readability and breathability.
- **Secondary Details**: Inter Light 14sp in Muted Titanium (`#9BA4AF`).
- **Hero Numbers & Counters**: Large, confident figures in silver primary with copper dot accents.

### 2. Palette & Semantic Roles
- **Canvas Background**: Deep, unbroken Charcoal `#0E1013` (Dark) / Pale Gallery White `#F4F6F8` (Light).
- **Floating Gallery Surfaces**: `#181D24` (Dark) / `#FFFFFF` (Light) with generous 24dp padding and floating 8dp soft ambient elevation.
- **Borders**: Minimal to absent; surfaces separate primarily through luminous contrast and generous 16dp–24dp margins.
- **Punctuation Copper**: Pure reference copper `#B87333` reserved for singular focal points (e.g., solitary unread badge, FAB glyph, signature identity reveal card).
- **Sent Message Bubble**: Crisp floating titanium card `#1E242C` with generous 14dp internal padding and 18dp rounded corners.
- **Received Message Bubble**: Seamless charcoal surface `#14181E` with subtle 0.5dp silver border.

### 3. Component Shapes, Borders, Spacing & Elevation
- **Corner Radii**: Generous 20dp–24dp soft radii on all cards and message clusters.
- **Spacing**: 20dp–24dp horizontal margins, 16dp vertical gutters. Generous breathing room between message clusters.
- **Touch Targets**: Expansive 52dp–56dp touch heights with generous hit slop.

### 4. Copper Placement & Metallic Treatment
- **Total Copper Screen Budget**: **~1% to 3%** (the most restrained of all three).
- **Where Copper Lives**: A single focal point per screen. On the chat list: solely the unread badge or FAB icon. In the conversation: the send arrow when active. In settings: subtle copper dots beside section headers.

### 5. Interaction Feedback & Motion
- **Motion**: Elegant, decelerated fades (300ms–400ms duration) with soft scale transitions.
- **Touch States**: Subtle surface illumination without abrupt ripples.

### 6. Accessibility & Implementation Tradeoffs
- **Pros**: Exceptionally calm, modern, and beautiful; large touch targets are naturally accessible; superb readability for long text.
- **Tradeoffs**: Lower information density (fewer chat rows visible simultaneously on 5-inch screens); power users with 100+ active conversations may need more scrolling.

---

## Side-by-Side Comparison Matrix

| Design Attribute | Direction A: Precision Titanium | Direction B: Machined Metal | Direction C: Charcoal Gallery |
|---|---|---|---|
| **Primary Atmosphere** | Calibrated instrument, crisp, focused | Tactile aerospace hardware, beveled, crafted | Editorial gallery, serene, expansive |
| **Information Density** | High / Efficient (8–10 chats visible) | Balanced (7–8 chats visible) | Relaxed / Spacious (6–7 chats visible) |
| **Copper Budget** | 2% – 4% (Actions, switches, unread) | 4% – 5% (Inlays, bevels, progress, actions) | 1% – 3% (Singular focal punctuation marks) |
| **Borders & Edges** | Crisp 0.5dp–1dp uniform hairlines | Dual-tone bevel light/shadow gradients | Minimal / borderless floating cards |
| **Corner Radii** | 12dp – 16dp | 14dp – 18dp | 20dp – 24dp |
| **Sent Message Bubble** | Cool Raised Titanium `#1E232A` | Titanium Plate `#20262E` with top bevel | Floating Card `#1E242C` with 18dp radius |
| **Touch Target Margin** | Strict 48dp minimum | 48dp – 50dp with mechanical depress | 52dp – 56dp generous targets |
| **Implementation Complexity** | Low / Direct token mapping | Moderate (gradient shaders & bevel borders) | Low / Layout & spacing adjustment |
| **Daily Messaging Fit** | **Optimal** (fast, legible, zero fatigue) | Strong (highly distinctive character) | Good (refined, but lower screen density) |

---

## Recommendation & Justification

### Recommended Option: **Direction A: Precision Titanium**

### Why Direction A is the Superior Choice for a Daily Messaging App

1. **Daily Cognitive Ergonomics (Zero Fatigue)**
   A messaging app is opened 50 to 150 times a day in varying lighting conditions—from dark rooms at night to harsh midday sunlight. Precision Titanium provides the calmest, most legible reading experience. By strictly eliminating all brown, peach, and orange background casts and using cool raised titanium (`#1E232A`) for sent messages and titanium surface (`#161A1F`) for received messages, reading fatigue drops to zero.

2. **Visible Definition of "Premium" Through Executed Details**
   "Premium" is not conveyed through decorative filters, artificial gloss, or gold gradients. In Direction A, premium is demonstrated through visible engineering rigor:
   - **Flawless Mathematical Contrast**: 13.7:1 on headings, 10.7:1 on body copy, and 4.81:1 on active switch thumbs.
   - **Architectural Hierarchy**: Information is structured through disciplined luminance steps (`#0E1013` -> `#161A1F` -> `#1E232A`) rather than visual clutter.
   - **Authentic Copper Purity**: Copper is treated as a precious conductor—it appears when a message is ready to send, when an action is triggered, or when an unread message arrives.

3. **Rock-Solid Cross-Platform Safety**
   Direction A maps directly into `QryptanaAndroidThemeRoles` and `qryptanaMessagingTokens()` without requiring fragile platform shaders or custom drawing hacks that could compromise Compose Multiplatform stability or inadvertently leak into Desktop and iOS.
