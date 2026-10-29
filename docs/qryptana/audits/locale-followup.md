# Locale and operator-copy follow-up

M-01 was addressed in the base locale only: the operator choice now says that users can choose between servers operated by SimpleX Chat Ltd and servers operated by Flux. These are real operator names retained for accurate network configuration; the wording no longer presents the choice as a Qryptana-operated service.

The same key remains in translated locale files and was not hand-translated. Translators should review the corresponding `onboarding_network_operators_simplex_flux_agreement` value in every locale and preserve the operator names accurately.

M-03 was addressed by retaining the verified SimpleX website on the real `OperatorTag.SimpleX` operator record while removing the upstream website from `dummyOperatorInfo`. The fallback type requires a website string, so it is now empty and the operator-info screen omits an empty website link.

The `dummyOperatorInfo` fallback is used by the operator-info display path and by the Flux illustration in `WhatsNewView.kt` when a matching operator record is unavailable. The normal server-operator screens display the real operator metadata from `operatorsInfo` or the server record.

NEEDS HUMAN: review translations and confirm the final operator descriptions and legal presentation before release.
