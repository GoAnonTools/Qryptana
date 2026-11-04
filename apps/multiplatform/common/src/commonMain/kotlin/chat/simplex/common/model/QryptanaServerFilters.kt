package chat.simplex.common.model

/**
 * Qryptana exposes only its own self-hosted relays. The bundled native core (and
 * databases created by earlier builds) can still contain the upstream SimpleX and
 * Flux preset operators and servers; these helpers keep them out of the UI.
 */
object QryptanaServerFilters {
  private val upstreamHosts = listOf("simplex.im", "simplexonflux.com", "simplex.chat")

  fun isUpstreamHost(host: String): Boolean {
    val h = host.lowercase()
    return upstreamHosts.any { h == it || h.endsWith(".$it") }
  }

  fun isUpstreamServer(server: UserServer): Boolean = isUpstreamHost(serverHost(server.server))

  fun isQryptanaOperator(operator: ServerOperator): Boolean =
    (listOfNotNull(operator.tradeName, operator.legalName) + operator.serverDomains)
      .any { it.contains("qryptana", ignoreCase = true) || it.contains("goanon", ignoreCase = true) }

  fun isUpstreamPresetOperator(operator: ServerOperator): Boolean {
    if (isQryptanaOperator(operator)) return false
    // Match only exact upstream identities/domains, never brand words in display names.
    return operator.serverDomains.any(::isUpstreamHost) ||
      operator.operatorTag == OperatorTag.SimpleX ||
      operator.operatorTag == OperatorTag.Flux
  }

  fun visibleOperators(operators: List<ServerOperator>): List<ServerOperator> =
    operators.filterNot(::isUpstreamPresetOperator)

  private fun serverHost(server: String): String {
    val afterAuth = server.substringAfter('@', server)
    val hostPort = afterAuth.substringBefore(',')
    return hostPort.substringBefore(':')
  }
}
