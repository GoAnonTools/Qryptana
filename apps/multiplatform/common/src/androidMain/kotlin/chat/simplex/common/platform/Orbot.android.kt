package chat.simplex.common.platform

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull

private const val ORBOT_PACKAGE = "org.torproject.android"
private const val ACTION_START = "org.torproject.android.intent.action.START"
private const val ACTION_STATUS = "org.torproject.android.intent.action.STATUS"
private const val EXTRA_PACKAGE_NAME = "org.torproject.android.intent.extra.PACKAGE_NAME"
private const val EXTRA_STATUS = "org.torproject.android.intent.extra.STATUS"
private const val EXTRA_SOCKS_PROXY = "org.torproject.android.intent.extra.SOCKS_PROXY"
private const val EXTRA_VPN = "org.torproject.android.intent.extra.VPN"
private const val STATUS_ON = "on"

actual fun isOrbotInstalled(): Boolean =
  try {
    androidAppContext.packageManager.getPackageInfo(ORBOT_PACKAGE, 0)
    true
  } catch (_: PackageManager.NameNotFoundException) {
    false
  }

actual suspend fun startOrbotAndAwaitStatus(timeoutMs: Long): OrbotState? = withContext(Dispatchers.Main) {
  val context = androidAppContext
  val result = CompletableDeferred<OrbotState>()
  val receiver = object : BroadcastReceiver() {
    override fun onReceive(ctx: Context?, intent: Intent?) {
      if (intent?.action != ACTION_STATUS) return
      if (intent.getStringExtra(EXTRA_STATUS) != STATUS_ON) return
      val vpn = if (intent.hasExtra(EXTRA_VPN)) intent.getBooleanExtra(EXTRA_VPN, false) else null
      if (!result.isCompleted) {
        result.complete(
          OrbotState(
            vpnMode = vpn,
            socksProxy = intent.getStringExtra(EXTRA_SOCKS_PROXY),
          )
        )
      }
    }
  }
  ContextCompat.registerReceiver(
    context,
    receiver,
    IntentFilter(ACTION_STATUS),
    ContextCompat.RECEIVER_EXPORTED,
  )
  try {
    context.sendBroadcast(
      Intent(ACTION_START)
        .setPackage(ORBOT_PACKAGE)
        .putExtra(EXTRA_PACKAGE_NAME, context.packageName)
    )
    withTimeoutOrNull(timeoutMs) { result.await() }
  } finally {
    runCatching { context.unregisterReceiver(receiver) }
  }
}
