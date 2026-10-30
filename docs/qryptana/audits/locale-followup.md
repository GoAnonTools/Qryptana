# Locale and operator-copy follow-up

M-01 was addressed in the base locale only: the operator choice now says that users can choose between servers operated by SimpleX Chat Ltd and servers operated by Flux. These are real operator names retained for accurate network configuration; the wording no longer presents the choice as a Qryptana-operated service.

The same key remains in translated locale files and was not hand-translated. Translators should review the corresponding `onboarding_network_operators_simplex_flux_agreement` value in every locale and preserve the operator names accurately.

M-03 was addressed by retaining the verified SimpleX website on the real `OperatorTag.SimpleX` operator record while removing the upstream website from `dummyOperatorInfo`. The fallback type requires a website string, so it is now empty and the operator-info screen omits an empty website link.

The `dummyOperatorInfo` fallback is used by the operator-info display path and by the Flux illustration in `WhatsNewView.kt` when a matching operator record is unavailable. The normal server-operator screens display the real operator metadata from `operatorsInfo` or the server record.

NEEDS HUMAN: review translations and confirm the final operator descriptions and legal presentation before release.

## Corrective review: updater, support and theme strings

Resource paths below are relative to `apps/multiplatform/common/src/commonMain/resources/MR/`. No translations were invented or deleted to hide stale copy.

| Keys | Affected files / action |
|---|---|
| `onboarding_network_operators_simplex_flux_agreement` | `ar/strings.xml`, `bg/strings.xml`, `ca/strings.xml`, `cs/strings.xml`, `de/strings.xml`, `el/strings.xml`, `es/strings.xml`, `fa/strings.xml`, `fr/strings.xml`, `hu/strings.xml`, `in/strings.xml`, `it/strings.xml`, `ja/strings.xml`, `lv/strings.xml`, `nl/strings.xml`, `pl/strings.xml`, `pt-rBR/strings.xml`, `ro/strings.xml`, `ru/strings.xml`, `tr/strings.xml`, `uk/strings.xml`, `vi/strings.xml`, `zh-rCN/strings.xml`: review neutral operator choice; retain SimpleX Chat Ltd / Flux names. |
| `app_check_for_updates_notice_desc` | The same list **except `ja`**: 22 legacy translations still describe release notifications. The automatic prompt and its helper were removed, so these are no longer displayed by that path. Do not reuse them as Qryptana update claims. |
| `app_check_for_updates`, `app_check_for_updates_stable`, `app_check_for_updates_beta`, `app_check_for_updates_notice_title` | Legacy translations also occur in partial locales `da`, `hr`, `iw`, `ko`, `ku`, `nb-rNO`, `zh-rTW` (not every key in each file). The settings row/channel options now use new upstream-specific keys instead of reusing misleading legacy translations. |
| `qryptana_upstream_release_notes`, `qryptana_upstream_stable`, `qryptana_upstream_beta`, `qryptana_upstream_notes_description`, `app_check_for_updates_upstream_title`, `app_check_for_updates_upstream_only` | Base English explicitly identifies upstream SimpleX notes and no Qryptana binary. All non-base locales need reviewed translations; missing keys fall back to English. |
| `qryptana_source_code`, `qryptana_desktop_theme_description` | Base-only safe wording: source repository, not an unverified terminal release; desktop single finish and deferred Android copper. Translate in all non-base locales. |
| `chat_with_the_founder`, `you_can_connect_to_simplex_chat_founder`, `send_us_an_email`, `install_simplex_chat_for_terminal`, `rate_the_app` | Legacy copy remains. Chat contact entries stay hidden while `qryptanaSupportUri` is null; email/store-rating entries were removed, and the terminal-install row now says source code. Revisit contact wording only after a real destination and its URI type are verified. |

All non-base resource files to include in the new-key translation pass (each is `<locale>/strings.xml`): `ar`, `bg`, `bn`, `ca`, `cs`, `da`, `de`, `el`, `es`, `fa`, `fi`, `fr`, `hi`, `hr`, `hu`, `in`, `it`, `iw`, `ja`, `ko`, `ku`, `lt`, `lv`, `ml`, `nb-rNO`, `nl`, `pl`, `pt`, `pt-rBR`, `ro`, `ru`, `sv`, `th`, `tr`, `uk`, `vi`, `zh-rCN`, `zh-rTW`.

## Store and documentation follow-up

Only `fastlane/metadata/android/en-US/` exists. Its title, descriptions and default changelog are Qryptana Labs copy, not a public-release announcement. Other store locales must be supplied/reviewed before publication; there are no non-English store titles here to replace.

The privacy-link targets in `docs/lang/{cs,fr,pl}/README.md` were repaired without translating their bodies. Those are upstream reference documents: their operator promises and SimpleX security contacts are not Qryptana commitments. Review translated documentation boundaries and support wording before using them as Qryptana release material. Website publication remains gated on a separate human decision and privacy/legal review.
