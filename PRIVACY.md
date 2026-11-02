# Qryptana privacy statement (DRAFT: FOR LEGAL REVIEW)

This document describes the current repository implementation as inspected for the Qryptana remediation. It is not a legal notice, operator policy, or promise about services operated by Qryptana or any third party.

Qryptana is an early Labs-stage downstream fork of SimpleX Chat. The upstream operator policy is retained separately at [docs/upstream/SIMPLEX_PRIVACY.md](docs/upstream/SIMPLEX_PRIVACY.md); that document applies to its authors and operators, not automatically to Qryptana.

## What the application currently does

### Messaging, servers and storage

- Messaging uses the inherited SimpleX protocol implementation. Preset operators are SimpleX Chat Ltd (`simplex.im`) and InFlux Technologies Limited, trading as Flux (`simplexonflux.com`), as defined in `src/Simplex/Chat/Operators/Presets.hs`. Server selection and roles depend on configuration and accepted operator conditions. These are third parties, not verified Qryptana-operated servers. Their retained [policy and conditions](docs/upstream/SIMPLEX_PRIVACY.md) are not Qryptana promises.
- The application stores profiles, contacts and messages in its local database. The code retains the upstream SQLCipher-backed database encryption behavior; device configuration and user choices can affect how a database is protected.

### Link previews

- Link previews are enabled by default (`privacyLinkPreviews` in `model/SimpleXAPI.kt`), but the first preview fetch is blocked behind a consent alert (`privacyLinkPreviewsShowAlert`, also defaulting to true in `model/SimpleXAPI.kt`; the flow is in `views/chat/ComposeView.kt`). The app does not fetch a preview until the user responds to the alert. Choosing to enable previews records that choice and fetches the preview; choosing to disable them sets `privacyLinkPreviews` to false and does not fetch. Dismissing the alert cancels that preview without changing the setting, and the alert may appear again for a later link. After an explicit enable/disable choice, the alert is not shown again because `privacyLinkPreviewsShowAlert` is cleared. Users can change previews later in Privacy and security.
- When a preview is allowed, `views/helpers/LinkPreviews.kt` fetches the linked page and its preview image/icon, potentially from different hosts and through redirects. These requests disclose the requested URL and network connection information to those hosts. The code uses the configured SOCKS proxy when enabled; without one it connects directly. This is separate from message encryption.

### Calls

- Calls use WebRTC in a local WebView. Android loads `commonMain/resources/assets/www/android/call.html`, which loads the shipped `commonMain/resources/assets/www/call.js`; the app-side call handling is in `views/call/WebRTC.kt`, `views/call/CallManager.kt` and `model/SimpleXAPI.kt`.
- The call page's default ICE servers are `stuns:stun.simplex.im:443`, `stun:stun.simplex.im:443`, and `turns:turn.simplex.im:443?transport=tcp`. The TURN entry uses the hard-coded shared username `private2` and credential `Hxuq2QxUjnhj96Zq2r4HjqHRj` (`commonMain/resources/assets/www/call.js`). These are third-party services under the `simplex.im` domain, not Qryptana-operated servers. They are used to discover network paths and, for TURN, to relay call traffic; they therefore receive connection metadata such as IP addresses, ports and timing.
- "Always use relay" is enabled by default (`webrtcPolicyRelay` in `model/SimpleXAPI.kt`). With it enabled, `call.js` sets the WebRTC ICE transport policy to `relay`, so call traffic uses the TURN server rather than direct peer-to-peer paths. Users can change this in call settings (`views/usersettings/CallSettings.kt`).
- Users can replace the default ICE server list with their own in call settings (`views/usersettings/RTCServers.kt`). When no custom list is configured, the app uses the SimpleX defaults above; when a custom list is configured, `views/call/WebRTC.kt` passes it to the call page instead.

### SimpleX names

- Contact and channel profiles can contain SimpleX names. The Android build hides the SimpleX-name registration and sales-promotion UI behind a disabled feature flag (`QryptanaFeatures.SIMPLEX_NAMES_ENABLED = false` in `platform/QryptanaFeatures.kt`, scoped to Android), but names already present in profiles are still displayed.
- Resolving a name depends on SimpleX name-resolution services reached through the user's configured SimpleX messaging servers. Verification calls `resolveSimplexName` through the core (`src/Simplex/Chat/Library/Commands.hs`), and the app's UI notes that a server must support name resolution (`commonMain/moko-resources/base/strings.xml`). Those servers are not operated by Qryptana. The app also shows a "Verify name" action (`views/chat/SimplexNameView.kt`); automatic verification when opening a chat is optional and off by default (`privacyVerifySimplexNames` in `model/SimpleXAPI.kt`).
- A resolution or verification request can disclose the name being checked and network metadata to the configured servers. It does not change the end-to-end encryption of messages.

### Android notifications and background service

- For notifications, the Android app does not use push-notification providers. The default mode is "Instant notifications" (`NotificationsMode.SERVICE` in `model/SimpleXAPI.kt`), which runs a foreground service (`android/src/main/java/chat/simplex/app/SimplexService.kt`) with a persistent low-importance "Qryptana service / Receiving messages…" notification. Android requires this visible notification while the service runs. The app's UI warns that this mode uses more battery because the app always runs in the background.
- "Periodic notifications" (`NotificationsMode.PERIODIC`) instead schedules background work that checks for messages approximately every 10 minutes and runs briefly (`android/src/main/java/chat/simplex/app/MessagesFetcherWorker.kt`). The app's UI describes this as using a few percent of battery per day and notes that calls or urgent messages may be missed. "Off" uses no background service and shows notifications only while the app is running.
- On Android 12 and later, instant-notification mode expects the user to exempt the app from battery optimization; periodic mode expects this on Android 6 and later (`androidMain/kotlin/chat/simplex/common/helpers/Extensions.kt`). If the system restricts background execution or the app is battery-optimized, the app prompts the user to set "App battery usage" to "Unrestricted" and may disable the background service or periodic checks (`android/src/main/java/chat/simplex/app/SimplexService.kt`). On Android 13 and later, the app also requires the system notification permission; the service is not started when notifications are disabled in the system (`android/src/main/java/chat/simplex/app/SimplexService.kt`, `android/src/main/java/chat/simplex/app/model/NtfManager.android.kt`).
- When allowed, the service is marked `START_STICKY`, can restart after boot, app updates or being killed, and may be restarted by periodic WorkManager jobs (`android/src/main/AndroidManifest.xml`, `android/src/main/java/chat/simplex/app/SimplexService.kt`, `android/src/main/java/chat/simplex/app/SimplexApp.kt`). For message delivery, the service intentionally does not hold a permanent wake lock; the implementation relies on the battery-optimization exemption and the upstream comments describe acquiring wake locks around network events (`android/src/main/java/chat/simplex/app/SimplexService.kt`). Message notifications show a sender name and message text or a generic preview according to the user's notification preview setting (`android/src/main/java/chat/simplex/app/model/NtfManager.android.kt`).

### Diagnostics and updates

- Inspected crash handlers log exceptions to Android's local logcat or desktop standard output (`platform/UI.android.kt`, `UI.desktop.kt`, `Log.android.kt`, `Log.desktop.kt`). Android also offers a user-initiated share action for crash text. Logs and shared diagnostic text can contain sensitive information. The inspected multiplatform Gradle dependencies and handlers do not configure a dedicated analytics/crash-upload SDK; this is not an exhaustive dependency audit or a promise of no network endpoints, OS diagnostics, or future changes.
- The desktop release-note checker is disabled by default. If a user enables it, `views/helpers/AppUpdater.kt` requests the upstream SimpleX GitHub release feed at `api.github.com`, immediately and periodically, and intentionally offers no upstream binary as a Qryptana update. The HTTP client uses a configured SOCKS host/port when valid; otherwise it connects directly. SOCKS authentication/stream isolation is not configured by this updater, so this draft does not promise equivalent proxy behavior across all app requests. Opening release notes also opens an external link with the browser's own network settings.

The paths above are under `apps/multiplatform/common/src/` unless prefixed with `android/`, which refers to `apps/multiplatform/android/src/main/`, or shown as `src/`, which refers to the Haskell core at the repository root. Database encryption dependencies are declared in `simplex-chat.cabal`. This is source inspection, not a network-traffic audit or verification of a distributed binary.

## Information we do not claim

Qryptana does not currently publish verified operator contacts, a support address, a data-retention schedule, a list of Qryptana-operated servers, or legal commitments about third-party relay operators. These items require human and legal review before any public release.

## Source and license

The source is distributed under AGPL-3.0. SimpleX Chat attribution and upstream documentation are retained in the repository. Qryptana is not affiliated with, endorsed by, sponsored by, or officially connected to the SimpleX Chat project.

## Review status

**DRAFT: FOR LEGAL REVIEW.** Confirm the actual deployment and operator model, support and privacy contacts, applicable jurisdictions, retention practices, and the final policy before publishing or distributing a Qryptana artifact. This revision (2026-11-02) adds source-inspected details about link-preview consent, calls and ICE servers, SimpleX-name resolution, and Android notification/background behavior.
