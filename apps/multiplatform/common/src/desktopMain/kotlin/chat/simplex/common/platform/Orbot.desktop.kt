package chat.simplex.common.platform

actual fun isOrbotInstalled(): Boolean = false

actual suspend fun startOrbotAndAwaitStatus(timeoutMs: Long): OrbotState? = null
