package chat.simplex.common.platform

/**
 * Compile-time switches for Qryptana builds.
 *
 * Keep [SIMPLEX_NAMES_ENABLED] off: SimpleX-name registration, its sales countdown, and the
 * simplex.domains links are third-party promotion. Qryptana hides this promotion globally,
 * on both Android and Desktop.
 */
object QryptanaFeatures {
  const val SIMPLEX_NAMES_ENABLED = false

  /** Hides third-party SimpleX-name promotion on every Qryptana platform. */
  val simplexNamesVisible: Boolean
    get() = SIMPLEX_NAMES_ENABLED
}
