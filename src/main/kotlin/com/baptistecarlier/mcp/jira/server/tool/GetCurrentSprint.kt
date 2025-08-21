package com.baptistecarlier.mcp.jira.server.tool

import com.baptistecarlier.jira.client.JiraClient
import com.baptistecarlier.mcp.jira.server.key.callToolResultError
import io.modelcontextprotocol.kotlin.sdk.CallToolResult
import io.modelcontextprotocol.kotlin.sdk.TextContent
import io.modelcontextprotocol.kotlin.sdk.server.Server
import kotlinx.serialization.json.Json

internal fun Server.getCurrentSprint(
    jiraClient: JiraClient,
    boardId: String,
) {
    addTool(
        name = "get-jira-current-sprint",
        description = "Returns the current sprint from Jira.",
    ) {

        val activeSprints = runCatching {
            jiraClient.getActiveSprints(boardId).values
        }

        val sprints = activeSprints.getOrElse {
            return@addTool callToolResultError("Error retrieving active sprints: ${it.message}")
        }
        if (sprints.isNullOrEmpty()) {
            return@addTool callToolResultError("No active sprints found.")
        }
        // Assuming we want the first active sprint
        val activeSprint = sprints.firstOrNull()
            ?: return@addTool callToolResultError("No active sprints found.")

        CallToolResult(content = listOf(TextContent(Json.encodeToString(activeSprint))))
    }

}
