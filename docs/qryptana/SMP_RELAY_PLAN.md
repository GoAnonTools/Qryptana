# Qryptana SMP Relay Plan

Qryptana's first infrastructure milestone is to run a self-hosted SMP relay.

This relay is part of the Qryptana/GoAnon direction, but it remains compatible with the upstream SimpleX-derived architecture unless later documented otherwise.

## Purpose

The first Qryptana VPS is not for hosting a full web app.

Its first purpose is to run private communication infrastructure, starting with an SMP relay.

The relay will help Qryptana move toward:

- self-hosted infrastructure;
- reduced dependency on default public relays;
- clearer GoAnon/Qryptana operational control;
- future store-free and identity-light onboarding;
- a foundation for later Qryptana Web/PWA research.

## Current priority

Start with SMP relay only.

XFTP/file transfer infrastructure should be evaluated later.

## Why SMP first

SMP is the message relay layer used for asynchronous message delivery.

Starting with SMP first is safer because:

- it is the core relay path;
- it is simpler than deploying every infrastructure component at once;
- it allows testing client-to-relay connectivity early;
- it avoids mixing messaging, file transfer, push, web hosting, and branding concerns too soon.

## XFTP later

XFTP/file transfer should not be part of the first VPS milestone unless required for testing.

Reasons to delay XFTP:

- larger files increase bandwidth/storage considerations;
- more moving parts;
- more operational monitoring;
- more abuse considerations;
- more public-facing responsibility.

## Starter VPS requirements

Recommended first VPS:

- Ubuntu 24.04 LTS or Debian 12;
- 2 vCPU;
- 4 GB RAM;
- 60 GB SSD/NVMe;
- static IPv4;
- root SSH access;
- Docker allowed;
- firewall control;
- ability to open required relay ports;
- preferably Iceland or another privacy-respecting jurisdiction.

## Nice to have

- IPv6 support;
- snapshot backups;
- reverse DNS control;
- DDoS protection;
- simple provider firewall;
- clear abuse policy;
- no forced managed stack.

## Domain plan

Preferred domain structure:

- `qryptana.pro` for the public product page;
- `relay.qryptana.pro` for the first SMP relay;
- optional later: `xftp.qryptana.pro` for file transfer;
- optional later: `status.qryptana.pro` for infrastructure status.

The first relay should use a dedicated subdomain rather than the root domain.

## DNS plan

Initial DNS records:

- `A relay.qryptana.pro -> VPS IPv4`
- optional: `AAAA relay.qryptana.pro -> VPS IPv6`

Do not proxy the relay through CDN services unless the protocol and ports explicitly support it.

## Security baseline

Before exposing the relay publicly:

- update the system;
- create SSH key-only access;
- disable password SSH login;
- configure firewall;
- allow only required ports;
- install fail2ban or equivalent if useful;
- keep system packages updated;
- document every open port;
- avoid installing unrelated services.

## Operational principle

The first relay should be boring, simple, and auditable.

Avoid unnecessary dashboards, panels, analytics, trackers, or complex automation.

## What we need to test first

The first technical tests should confirm:

1. the server starts correctly;
2. required ports are reachable;
3. a client can connect to the relay;
4. messages can be relayed through it;
5. logs do not expose unnecessary user content;
6. restart behavior is understood;
7. backup/restore requirements are understood;
8. update procedure is documented.

## What not to claim yet

Until tested and documented, Qryptana should not publicly claim:

- production-ready relay infrastructure;
- perfect anonymity;
- no metadata at all;
- audited infrastructure;
- full independence from upstream SimpleX components;
- full Web/PWA messaging support;
- XFTP support;
- native mobile release targets.

## Public wording for early stage

Recommended wording:

"Qryptana is currently in Labs-stage development. The first infrastructure milestone is a self-hosted SMP relay used to test Qryptana-controlled messaging infrastructure."

Avoid wording like:

"Qryptana is fully anonymous."
"Qryptana has no metadata."
"Qryptana is production-ready."
"Qryptana replaces SimpleX."

## First VPS checklist

Before buying/configuring:

- choose VPS provider;
- confirm Docker is allowed;
- confirm root SSH is available;
- confirm static IPv4;
- confirm firewall/port control;
- confirm bandwidth limits;
- confirm acceptable use policy;
- decide subdomain;
- prepare SSH key;
- prepare local notes for server access;
- review upstream SimpleX SMP server documentation before installation.

## Initial deployment sequence

1. Buy VPS.
2. Point `relay.qryptana.pro` to the VPS IP.
3. Harden SSH.
4. Update server packages.
5. Configure firewall.
6. Install required runtime/deployment tooling.
7. Install/configure SMP relay.
8. Start relay.
9. Test port reachability.
10. Test client connection.
11. Document working configuration.
12. Only then consider XFTP or Web/PWA integration.

## Relationship to Web/PWA

The SMP relay is infrastructure.

It does not automatically make Qryptana a web app.

The Web/PWA direction should be researched separately after the relay path works.

Possible future architectures include:

- browser-only limited client;
- local bridge plus web UI;
- server-assisted web gateway;
- web companion product first.

Each option has different privacy and trust implications.

## Current conclusion

The first VPS should be used to run and understand a Qryptana-controlled SMP relay.

Keep the milestone narrow.

Do not deploy everything at once.

Do not overbrand or overclaim before testing.
