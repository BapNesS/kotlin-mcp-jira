package com.baptistecarlier.mcp.jira.server.tool

import com.baptistecarlier.jira.client.JiraClient
import com.baptistecarlier.mcp.jira.server.key.callToolResultError
import com.baptistecarlier.mcp.jira.server.key.errorMissingParameter
import com.baptistecarlier.mcp.jira.server.key.getString
import com.baptistecarlier.mcp.jira.server.key.issueIdOrKey
import com.baptistecarlier.mcp.jira.server.key.putJsonObject
import io.modelcontextprotocol.kotlin.sdk.CallToolResult
import io.modelcontextprotocol.kotlin.sdk.TextContent
import io.modelcontextprotocol.kotlin.sdk.Tool
import io.modelcontextprotocol.kotlin.sdk.server.Server
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonObject

internal fun Server.getIssueTransitions(
    jiraClient: JiraClient,
) {
    val inputSchema = Tool.Input(
        properties = buildJsonObject {
            putJsonObject(issueIdOrKey)
        },
        required = listOf(issueIdOrKey.key)
    )

    addTool(
        name = "get-issue-transitions",
        description = "Get the list of possible transitions for a Jira issue.",
        inputSchema = inputSchema
    ) { input ->
        val issueIdOrKey = input.arguments[issueIdOrKey.key]?.getString()
            ?: return@addTool errorMissingParameter(issueIdOrKey)

        val transitionsResult = runCatching {
            jiraClient.getIssueTransitions(issueIdOrKey)
        }
        val transitions = transitionsResult.getOrElse {
            return@addTool callToolResultError("Erreur lors de la récupération des transitions : ${it.message}")
        }
        CallToolResult(
            content = listOf(TextContent("Transitions possibles : ${Json.encodeToString(transitions)}"))
        )
    }
}
