# Retained upstream operator policy

[SIMPLEX_PRIVACY.md](SIMPLEX_PRIVACY.md) is the inherited **SimpleX Chat operators' privacy policy and conditions of use**, not a Qryptana privacy policy or commitment. Qryptana's separate statement is a [draft for legal review](../../PRIVACY.md).

The retained file is byte-identical to `PRIVACY.md` at audited commit `2121b84`. Its text and front matter are deliberately unchanged: `src/Simplex/Chat/Operators.hs` embeds it (stripping front matter) as the in-app operator usage conditions, and `simplex-chat.cabal` includes it as an extra source file. Adding a Qryptana disclaimer inside that document would alter the conditions presented for third-party operators without changing their recorded conditions identity. This README provides the boundary instead. No operator terms or protocol defaults were changed.

The embed path and Cabal inclusion have been checked statically, but the Haskell core was not built in this remediation. Website publication is also unverified and remains manual: the inherited website tooling copies both `docs/` and the root privacy draft, and the upstream file retains `/privacy/index.html`. Do not deploy that site as a reviewed Qryptana policy; reconcile its routes, upstream branding and draft/legal status before publication.
