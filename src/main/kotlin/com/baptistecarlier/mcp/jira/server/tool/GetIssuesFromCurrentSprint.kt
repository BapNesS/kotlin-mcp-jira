package com.baptistecarlier.mcp.jira.server.tool

import com.baptistecarlier.jira.client.JiraClient
import com.baptistecarlier.mcp.jira.server.key.callToolResultError
import io.modelcontextprotocol.kotlin.sdk.CallToolResult
import io.modelcontextprotocol.kotlin.sdk.TextContent
import io.modelcontextprotocol.kotlin.sdk.server.Server
import kotlinx.serialization.json.Json

/**
 * Tool to retrieve issues from the current sprint in a Jira board.
 * TODO Retrieve all pages. So far, only the first page is retrieved.
 */
internal fun Server.getIssuesFromCurrentSprint(
    jiraClient: JiraClient,
    boardId: String,
) {
    addTool(
        name = "get-issues-detail-from-current-sprint",
        description = "Get details of all issues from the current sprint.",
    ) {

        val response = runCatching {
            jiraClient.getActiveSprints(boardId)
                .values
                ?.firstOrNull()
                ?.id
                ?.toString()
                ?.let { activeSprintId ->
                    jiraClient.getIssuesFromSprint(boardId, activeSprintId)
                }
                ?: return@addTool callToolResultError("No active sprint found.")
        }

        val issues = response.getOrElse {
            return@addTool callToolResultError("Error retrieving issues from current sprint.")
        }

        if (issues.values.isNullOrEmpty()) return@addTool CallToolResult(content = listOf(TextContent("No issues found in the current sprint.")))

        CallToolResult(content = listOf(TextContent(Json.encodeToString(issues.values))))
    }

}