package com.baptistecarlier.mcp.jira.server.tool

import com.baptistecarlier.jira.client.JiraClient
import com.baptistecarlier.jira.client.ensureStatusIsOK
import com.baptistecarlier.mcp.jira.server.key.callToolResultError
import com.baptistecarlier.mcp.jira.server.key.errorMissingParameter
import com.baptistecarlier.mcp.jira.server.key.getString
import com.baptistecarlier.mcp.jira.server.key.issueBody
import com.baptistecarlier.mcp.jira.server.key.issueIdOrKey
import com.baptistecarlier.mcp.jira.server.key.issueSummary
import com.baptistecarlier.mcp.jira.server.key.putJsonObject
import io.modelcontextprotocol.kotlin.sdk.CallToolResult
import io.modelcontextprotocol.kotlin.sdk.TextContent
import io.modelcontextprotocol.kotlin.sdk.Tool
import io.modelcontextprotocol.kotlin.sdk.server.Server
import kotlinx.serialization.json.buildJsonObject

internal fun Server.updateIssueTitleOrBody(
    jiraClient: JiraClient,
) {

    val inputSchema = Tool.Input(
        properties = buildJsonObject {
            putJsonObject(issueIdOrKey)
            putJsonObject(issueSummary)
            putJsonObject(issueBody)
        },
        required = listOf(issueIdOrKey.key)
    )

    addTool(
        name = "update-issue-title-or-body",
        description = "Update the title or body of a Jira issue. It can be used to update the summary or description of an issue.",
        inputSchema = inputSchema
    ) { input ->

        val issueIdOrKey = input.arguments[issueIdOrKey.key]?.getString()
            ?: return@addTool errorMissingParameter(issueIdOrKey)
        val issueTitle = input.arguments[issueSummary.key]?.getString()
        val issueBody = input.arguments[issueBody.key]?.getString()

        val result = runCatching {
            jiraClient.updateIssueSummaryOrDescription(
                issueIdOrKey = issueIdOrKey,
                summary = issueTitle,
                description = issueBody,
            ).ensureStatusIsOK("Error editing the issue $issueIdOrKey.")
        }
        result.fold(
            onSuccess = {
                CallToolResult(content = listOf(TextContent("Issue $issueIdOrKey has been successfully updated with summary '$issueTitle' and body '$issueBody'.")))
            },
            onFailure = {
                callToolResultError("Error editing the issue $issueIdOrKey: ${it.message}")
            }
        )
    }

}
