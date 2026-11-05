package chat.simplex.common.platform

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.core.content.ContextCompat
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import java.net.InetSocketAddress
import java.net.Socket

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
    // TODO: pin Guardian Project's signing certificate SHA-256 in addition to the
    // package name. Until then, rely on strict loopback proxy validation so a
    // substituted Orbot package cannot redirect traffic off-device.
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
      // Android 14+ exposes the sender package. Reject anything that is not Orbot
      // so a malicious app cannot spoof its mode or proxy.
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
        val sender = sentFromPackage
        if (sender == null) {
          // Orbot did not identify itself. Do not wait for another broadcast:
          // fall back immediately to the default SOCKS endpoint.
          Log.w(TAG, "Orbot status broadcast has no sender identity; falling back to SOCKS 127.0.0.1:9050")
          if (!result.isCompleted) {
            result.complete(OrbotState(vpnMode = false, socksProxy = "127.0.0.1:9050"))
          }
          return
        }
        if (sender != ORBOT_PACKAGE) {
          Log.w(TAG, "Ignoring Orbot status broadcast from unexpected sender: $sender")
          return
        }
      }
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
  // RECEIVER_NOT_EXPORTED cannot receive broadcasts from another app, so it would
  // never see Orbot's status. Export the receiver and verify the sender above; on
  // older releases the loopback-pinned proxy validation below limits any spoofed
  // broadcast to a denial of service, never traffic redirection.
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

actual suspend fun isTorRoutingLive(vpnMode: Boolean, proxyHost: String, proxyPort: Int): Boolean =
  withContext(Dispatchers.IO) {
    if (vpnMode) hasVpnTransport() else isLoopbackHost(proxyHost) && canConnect(proxyHost, proxyPort)
  }

actual suspend fun isVpnTransportActive(): Boolean =
  withContext(Dispatchers.IO) { hasVpnTransport() }

actual fun openOrbot(): Boolean {
  val intent = androidAppContext.packageManager.getLaunchIntentForPackage(ORBOT_PACKAGE) ?: return false
  intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
  return try {
    androidAppContext.startActivity(intent)
    true
  } catch (_: Exception) {
    false
  }
}

private fun isLoopbackHost(host: String): Boolean =
  host.lowercase() in setOf("127.0.0.1", "::1", "localhost")

private fun hasVpnTransport(): Boolean {
  val cm = androidAppContext.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager ?: return false
  val network = cm.activeNetwork ?: return false
  val capabilities = cm.getNetworkCapabilities(network) ?: return false
  return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN)
}

private fun canConnect(host: String, port: Int): Boolean =
  try {
    Socket().use {
      it.connect(InetSocketAddress(host, port), 800)
      true
    }
  } catch (_: Exception) {
    false
  }
