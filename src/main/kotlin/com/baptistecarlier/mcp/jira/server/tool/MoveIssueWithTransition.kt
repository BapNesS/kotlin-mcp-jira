package com.baptistecarlier.mcp.jira.server.tool

import com.baptistecarlier.jira.client.JiraClient
import com.baptistecarlier.jira.client.ensureStatusIsOK
import com.baptistecarlier.mcp.jira.server.key.callToolResultError
import com.baptistecarlier.mcp.jira.server.key.errorMissingParameter
import com.baptistecarlier.mcp.jira.server.key.getString
import com.baptistecarlier.mcp.jira.server.key.issueIdOrKey
import com.baptistecarlier.mcp.jira.server.key.putJsonObject
import com.baptistecarlier.mcp.jira.server.key.transitionName
import io.modelcontextprotocol.kotlin.sdk.CallToolResult
import io.modelcontextprotocol.kotlin.sdk.TextContent
import io.modelcontextprotocol.kotlin.sdk.Tool
import io.modelcontextprotocol.kotlin.sdk.server.Server
import kotlinx.serialization.json.buildJsonObject

internal fun Server.moveIssueWithTransition(
    jiraClient: JiraClient,
) {

    val inputSchema = Tool.Input(
        properties = buildJsonObject {
            putJsonObject(issueIdOrKey)
            putJsonObject(transitionName)
        },
        required = listOf(issueIdOrKey.key, transitionName.key)
    )

    addTool(
        name = "move-issue-with-transition",
        description = "Move a Jira issue to a new status using a transition by its ID or Key.",
        inputSchema = inputSchema
    ) { input ->

        val issueIdOrKey = input.arguments[issueIdOrKey.key]?.getString()
            ?: return@addTool errorMissingParameter(issueIdOrKey)
        val transitionName = input.arguments[transitionName.key]?.getString()?.trim()
            ?: return@addTool errorMissingParameter(transitionName)

        val result = runCatching {
            val transitionList = jiraClient.getIssueTransitions(issueIdOrKey)
            val transitionId = transitionList
                ?.find { it.name.equals(transitionName, ignoreCase = true) }
                ?.id
                ?: return@runCatching callToolResultError("Transition '$transitionName' not found for issue '$issueIdOrKey'.")
            jiraClient.postTransitionIssue(issueIdOrKey, transitionId)
                .ensureStatusIsOK("Error transitioning ticket $issueIdOrKey to $transitionName.")
        }
        result.fold(
            onSuccess = {
                CallToolResult(content = listOf(TextContent("Issue $issueIdOrKey successfully moved with transition '$transitionName'.")))
            },
            onFailure = {
                callToolResultError("Error moving issue: ${it.message}")
            }
        )

    }

}

