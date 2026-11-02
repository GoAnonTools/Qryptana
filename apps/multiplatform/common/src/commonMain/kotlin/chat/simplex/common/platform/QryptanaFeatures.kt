package chat.simplex.common.platform

/**
 * Compile-time switches for Qryptana builds.
 *
 * Keep [SIMPLEX_NAMES_ENABLED] off: SimpleX-name registration, its sales countdown, and the
 * simplex.domains links are third-party promotion that Qryptana users should not see.
 */
object QryptanaFeatures {
  const val SIMPLEX_NAMES_ENABLED = false

  /**
   * The Android finishing pass hides this UI unconditionally; desktop keeps its upstream behavior.
   * Even if the flag is enabled for a future Android release, desktop remains unchanged.
   */
  val simplexNamesVisible: Boolean
    get() = SIMPLEX_NAMES_ENABLED && appPlatform.isAndroid
}
