package chat.simplex.common.views.onboarding

import chat.simplex.common.platform.QryptanaServersException
import chat.simplex.common.views.helpers.AlertManager
import chat.simplex.common.views.helpers.generalGetString
import chat.simplex.res.MR
import chat.simplex.res.qryptana_servers_setup_failed_desc
import chat.simplex.res.qryptana_servers_setup_failed_title

/**
 * Shows a blocking error when Qryptana's default servers cannot be installed.
 * Callers must stop onboarding after this; it must never fail silently.
 */
internal fun showQryptanaServersSetupError(error: Throwable) {
  // setUserServers already shows its own alert for API failures.
  if (error is QryptanaServersException && error.alertAlreadyShown) return
  val detail = (error as? QryptanaServersException)?.message ?: error.message
  val text = generalGetString(MR.strings.qryptana_servers_setup_failed_desc) +
    if (!detail.isNullOrBlank()) "\n\n$detail" else ""
  AlertManager.shared.showAlertMsg(
    generalGetString(MR.strings.qryptana_servers_setup_failed_title),
    text
  )
}
