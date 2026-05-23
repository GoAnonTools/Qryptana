# Qryptana Web/PWA Feasibility

Qryptana's preferred long-term product direction is Web/PWA-first where technically and security-wise realistic.

The goal is to reduce dependency on native app stores, phone-number identity, email identity, and platform-controlled distribution channels.

## Current position

Qryptana is currently a downstream fork derived from SimpleX Chat.

The inherited SimpleX structure includes native/mobile/desktop components, shared Kotlin code, Haskell/native core components, server tooling, documentation, and protocol-related material.

This repository is not currently a simple browser-first web application.

## Desired Qryptana direction

Qryptana should move toward:

- store-free access;
- identity-light onboarding;
- invite-based communication;
- self-hosted relay/server infrastructure;
- mobile-friendly browser access where possible;
- honest privacy wording;
- no claims of perfect anonymity or perfect security.

## Browser/PWA constraints

A browser-only private messenger has important limitations:

- browser storage can be cleared or weakened by user/device/browser policies;
- background execution is limited, especially on mobile;
- receiving messages while the PWA is closed is difficult without push or a native helper;
- notifications may require browser push infrastructure;
- long-running network connections may be suspended by mobile browsers;
- file handling is sandboxed;
- secure local key storage is weaker than native secure storage;
- multi-device behavior must be carefully designed;
- server-assisted web access can change the trust model.

## Possible technical paths

### Path 1 — Browser-only limited client

A pure PWA client could be explored, but it may have major constraints around storage, background receiving, notifications, and native cryptographic/client integration.

This path should not be assumed viable until researched.

### Path 2 — Local bridge plus web UI

A local Qryptana desktop/CLI process could handle the secure client/core responsibilities while a browser UI connects locally.

This may preserve stronger local control while still giving a web-style interface.

Tradeoff: it is not a pure web app and requires local installation.

### Path 3 — Server-assisted web gateway

A server-side gateway could make web access easier, but it may weaken the trust model depending on where keys, sessions, message queues, and decrypted content live.

This path requires extreme caution and honest documentation.

### Path 4 — Qryptana web companion first

The first web product may be a companion product rather than a full chat client:

- Qryptana project page;
- relay/server setup guide;
- invite explanation;
- download/instructions page;
- account-free onboarding education;
- status and trust documentation.

This path is safer for early public launch.

## Recommended practical roadmap

1. Preserve the upstream-derived structure safely.
2. Run a self-hosted SMP relay first.
3. Understand the SimpleX CLI/local client path.
4. Document how Qryptana can use self-hosted infrastructure.
5. Research whether Web/PWA can safely interact with the client/core.
6. Decide between browser-only, local-bridge, or companion-web architecture.
7. Only then build the public Qryptana Web/PWA.

## Non-goals for now

Qryptana should not currently claim:

- that it is a complete browser-native SimpleX replacement;
- that it avoids all metadata;
- that it provides perfect anonymity;
- that it has production-ready Web/PWA messaging;
- that iOS or Android are current Qryptana release targets.

## Current conclusion

Qryptana should remain Web/PWA-oriented, but the safe first technical milestone is self-hosted relay infrastructure and understanding the local client/CLI path.

The web product should be designed after the trust model is clear.
