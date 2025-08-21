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

internal fun Server.getIssueDetails(
    jiraClient: JiraClient,
) {
    // Define the input schema for the tool
    val inputSchema = Tool.Input(
        properties = buildJsonObject {
            putJsonObject(issueIdOrKey)
        },
        required = listOf(issueIdOrKey.key)
    )

    addTool(
        name = "get-issue-details",
        description = "Get details of a Jira issue by its ID or Key.",
        inputSchema = inputSchema
    ) { input ->

        val issueIdOrKey = input.arguments[issueIdOrKey.key]?.getString()
            ?: return@addTool errorMissingParameter(issueIdOrKey)

        val issue = runCatching {
            jiraClient.getIssue(issueIdOrKey)
        }
        val issueDetails = issue.getOrElse {
            return@addTool callToolResultError("Error retrieving issue details: ${it.message}")
        }

        CallToolResult(
            content = listOf(TextContent("Issue details: ${Json.encodeToString(issueDetails)}"))
        )
    }
}