package com.baptistecarlier.mcp.jira.server.tool

import com.baptistecarlier.jira.client.JiraClient
import com.baptistecarlier.jira.client.ensureStatusIsOK
import com.baptistecarlier.mcp.jira.server.key.callToolResultError
import com.baptistecarlier.mcp.jira.server.key.comment
import com.baptistecarlier.mcp.jira.server.key.errorMissingParameter
import com.baptistecarlier.mcp.jira.server.key.getString
import com.baptistecarlier.mcp.jira.server.key.issueIdOrKey
import com.baptistecarlier.mcp.jira.server.key.putJsonObject
import io.modelcontextprotocol.kotlin.sdk.CallToolResult
import io.modelcontextprotocol.kotlin.sdk.TextContent
import io.modelcontextprotocol.kotlin.sdk.Tool
import io.modelcontextprotocol.kotlin.sdk.server.Server
import kotlinx.serialization.json.buildJsonObject

internal fun Server.addCommentForIssue(
    jiraClient: JiraClient,
) {
    val inputSchema = Tool.Input(
        properties = buildJsonObject {
            putJsonObject(issueIdOrKey)
            putJsonObject(comment)
        },
        required = listOf(issueIdOrKey.key, comment.key)
    )

    addTool(
        name = "add-comment-for-issue",
        description = "Add a comment to a Jira issue by its ID or Key.",
        inputSchema = inputSchema
    ) { input ->
        val issueIdOrKey = input.arguments[issueIdOrKey.key]?.getString()
            ?: return@addTool errorMissingParameter(issueIdOrKey)
        val comment = input.arguments[comment.key].getString()
            ?: return@addTool errorMissingParameter(comment)

        val result = runCatching {
            jiraClient.addCommentToIssue(issueIdOrKey = issueIdOrKey, comment = comment)
                .ensureStatusIsOK("Error adding comment.")
        }
        result.fold(
            onSuccess = {
                CallToolResult(listOf(TextContent("Comment successfully added to ticket $issueIdOrKey.")))
            },
            onFailure = { callToolResultError("Error adding comment: ${it.message}") }
        )
    }
}
