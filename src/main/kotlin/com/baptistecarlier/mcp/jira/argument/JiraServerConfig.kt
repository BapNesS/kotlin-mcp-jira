package com.baptistecarlier.mcp.jira.argument

internal data class JiraServerConfig(
    val boardId: String,
    val baseUrl: String,
    val mail: String,
    val apiKey: String,
) {
    companion object {
        private fun get(
            data: List<Pair<String, String>>,
            paramName: String
        ) = data.find { it.first == paramName }?.second.orEmpty()

        fun fromArgs(args: Array<String>): JiraServerConfig {
            val params = args
                .filter { it.startsWith("-P") && it.contains(":") }
                .map {
                    val (key, value) = it.removePrefix("-P").split(":", limit = 2)
                    key to value
                }

            val boardId = get(params, "boardId")
            val baseUrl = get(params, "baseUrl")
            val mail = get(params, "mail")
            val apiKey = get(params, "apiKey")

            return JiraServerConfig(boardId, baseUrl, mail, apiKey)
        }
    }
}



