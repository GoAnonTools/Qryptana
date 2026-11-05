# Qryptana privacy statement (DRAFT: FOR LEGAL REVIEW)

This document describes the current repository implementation as inspected for the Qryptana remediation. It is not a legal notice, operator policy, or promise about services operated by Qryptana or any third party.

Qryptana is an early Labs-stage downstream fork of SimpleX Chat. The upstream project's policy is retained separately at [docs/upstream/SIMPLEX_PRIVACY.md](docs/upstream/SIMPLEX_PRIVACY.md); that document applies to its authors and operators, not automatically to Qryptana.

## Server operator and connection metadata

- Qryptana's preset SMP (message relay) and XFTP (file relay) servers are operated solely by **GoAnon Foundation**:
  - SMP (VPS1): `smp.qryptana.foundation:5224`, onion host `b544yscwxhrir2ilsy57dgwedr5vutl7zj525rio4dazsfka4f22k6yd.onion:5224`
  - XFTP (VPS2): `xftp2.goanon.foundation`, onion host `e6xroxqvdwcid42m54ck6bl4u3te45wu6x3ptaeupuowrgxxow7b6ayd.onion`
- The relays are on different hosts and different root domains (`qryptana.foundation` for SMP, `goanon.foundation` for XFTP) and are installed as separate server groups. This is a technical separation intended to limit cross-relay metadata visibility. It does **not** make them independent third parties: GoAnon Foundation operates both relays and could in principle correlate metadata observed at both.
- These are the application's default servers for new users. Onboarding installs them (`platform/QryptanaServers.kt`); the core preset definition used when the native core is rebuilt is in `src/Simplex/Chat/Operators/Presets.hs`. At the end of onboarding the app disables other preset operators so the default configuration uses the GoAnon Foundation-operated relays.
- **Connection metadata routes through these servers.** When the app connects to them, GoAnon Foundation, as the operator, can observe network and relay metadata. This can include the client's IP address, connection and disconnection times, TLS and transport information, the relay or queue identifiers used by the client, message and file sizes, and traffic timing and volume. This metadata is inherent in operating a relay. It is not message content: GoAnon Foundation does not receive message or file plaintext, contact names, profile contents, or a public user directory through the protocol. Messages and files are end-to-end encrypted between users.
- These observations are network-level. If the user routes connections through a configured SOCKS proxy or an onion service, the operator may see the proxy or onion address instead of the user's direct IP address, depending on configuration. The Maximum Privacy (Tor) switch configures the app to use Orbot and require onion hosts, as described below.
- Qryptana does not require a phone number, email address, or public user ID to use these relays.
- Users can add or change servers in Network & servers settings. Any third-party server a user adds is governed by that operator's policy, not this document.

## What the application currently does

### Messaging, servers and storage

- Messaging uses the inherited SimpleX protocol implementation. With the default configuration, messages are relayed through the GoAnon Foundation-operated SMP server at `smp.qryptana.foundation` and files through the GoAnon Foundation-operated XFTP server at `xftp2.goanon.foundation`. Server selection and roles can be changed in Network & servers settings.
- The application stores profiles, contacts and messages in its local database. The code retains the upstream SQLCipher-backed database encryption behavior; device configuration and user choices can affect how a database is protected.

### Maximum Privacy (Tor)

- The Network & servers screen has an "Enable Maximum Privacy (Tor)" switch (`views/usersettings/networkAndServers/NetworkAndServers.kt`). It requires Orbot to be running with a SOCKS proxy at `127.0.0.1:9050`.
- When enabled, the app routes its traffic through Orbot and requires onion hosts (`OnionHosts.REQUIRED`). Before enabling, the app checks every enabled SMP and XFTP server across all user profiles for an onion address. If any enabled server lacks one, the switch refuses to enable Tor and shows a warning instead of leaving those servers unreachable.
- When disabled, the app restores the proxy and onion settings that were active before Tor was enabled.
- Traffic for calls and links opened in an external browser does not go through the Tor proxy.
- Tor changes the network path that relays and other servers observe; it does not change message or file end-to-end encryption, and it does not prevent a relay operator from observing metadata about connections that reach its onion service.

### Link previews

- Link previews are enabled by default (`privacyLinkPreviews` in `model/SimpleXAPI.kt`), but the first preview fetch is blocked behind a consent alert (`privacyLinkPreviewsShowAlert`, also defaulting to true in `model/SimpleXAPI.kt`; the flow is in `views/chat/ComposeView.kt`). The app does not fetch a preview until the user responds to the alert. Choosing to enable previews records that choice and fetches the preview; choosing to disable them sets `privacyLinkPreviews` to false and does not fetch. Dismissing the alert cancels that preview without changing the setting, and the alert may appear again for a later link. After an explicit enable/disable choice, the alert is not shown again because `privacyLinkPreviewsShowAlert` is cleared. Users can change previews later in Privacy and security.
- When a preview is allowed, `views/helpers/LinkPreviews.kt` fetches the linked page and its preview image/icon, potentially from different hosts and through redirects. These requests disclose the requested URL and network connection information to those hosts. The code uses the configured SOCKS proxy when enabled; without one it connects directly. This is separate from message encryption.

### Calls

- Calls use WebRTC in a local WebView. Android loads `commonMain/resources/assets/www/android/call.html`, which loads the shipped `commonMain/resources/assets/www/call.js`; the app-side call handling is in `views/call/WebRTC.kt`, `views/call/CallManager.kt` and `model/SimpleXAPI.kt`.
- The call page's default ICE servers are `stuns:stun.simplex.im:443`, `stun:stun.simplex.im:443`, and `turns:turn.simplex.im:443?transport=tcp`. The TURN entry uses a hard-coded shared credential embedded in `commonMain/resources/assets/www/call.js`; this document intentionally does not reproduce the credential. These are third-party services under the `simplex.im` domain, not GoAnon-operated servers. They are used to discover network paths and, for TURN, to relay call traffic; they therefore receive connection metadata such as IP addresses, ports and timing.
- "Always use relay" is enabled by default (`webrtcPolicyRelay` in `model/SimpleXAPI.kt`). With it enabled, `call.js` sets the WebRTC ICE transport policy to `relay`, so call traffic uses the TURN server rather than direct peer-to-peer paths. Users can change this in call settings (`views/usersettings/CallSettings.kt`).
- Users can replace the default ICE server list with their own in call settings (`views/usersettings/RTCServers.kt`). When no custom list is configured, the app uses the defaults above; when a custom list is configured, `views/call/WebRTC.kt` passes it to the call page instead.

### SimpleX names

- Contact and channel profiles can contain SimpleX names. Qryptana hides the SimpleX-name registration and sales-promotion UI behind a disabled feature flag (`QryptanaFeatures.SIMPLEX_NAMES_ENABLED = false` in `platform/QryptanaFeatures.kt`, applied globally on Android and Desktop), but names already present in profiles are still displayed.
- Resolving a name depends on name-resolution services reached through the user's configured SMP servers. With the default configuration these are GoAnon-operated servers; if the user adds other servers, those operators may also be involved. Verification calls `resolveSimplexName` through the core (`src/Simplex/Chat/Library/Commands.hs`), and the app's UI notes that a server must support name resolution (`commonMain/moko-resources/base/strings.xml`). The app also shows a "Verify name" action (`views/chat/SimplexNameView.kt`).
- A resolution or verification request can disclose the name being checked and network metadata to the servers used. It does not change the end-to-end encryption of messages.

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

Qryptana does not currently publish a data-retention schedule, server-log retention commitments, a privacy contact, or legal commitments about GoAnon Foundation-operated relays. The SMP and XFTP addresses above are the current preset servers, both operated by GoAnon Foundation; this draft does not promise that they will never change, and it does not cover third-party servers a user adds. These items require human and legal review before any public release.

## Source and license

The source is distributed under AGPL-3.0. SimpleX Chat attribution and upstream documentation are retained in the repository. Qryptana is not affiliated with, endorsed by, sponsored by, or officially connected to the SimpleX Chat project.

## Review status

**DRAFT: FOR LEGAL REVIEW.** Confirm the actual deployment and operator model, support and privacy contacts, applicable jurisdictions, retention practices, and the final policy before publishing or distributing a Qryptana artifact. This revision (2026-11-05) describes the split SMP (`qryptana.foundation`) and XFTP (`goanon.foundation`) relays, states that GoAnon Foundation operates both, documents the Maximum Privacy (Tor) behavior, and retains the source-inspected details about link-preview consent, calls and ICE servers, SimpleX-name resolution, and Android notification/background behavior.
