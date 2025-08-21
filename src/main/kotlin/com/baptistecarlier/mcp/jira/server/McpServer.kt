package com.baptistecarlier.mcp.jira.server

import io.modelcontextprotocol.kotlin.sdk.Implementation
import io.modelcontextprotocol.kotlin.sdk.ServerCapabilities
import io.modelcontextprotocol.kotlin.sdk.server.Server
import io.modelcontextprotocol.kotlin.sdk.server.ServerOptions
import com.baptistecarlier.jira.client.JiraClient
import com.baptistecarlier.mcp.jira.argument.JiraServerConfig
import com.baptistecarlier.mcp.jira.server.tool.addCommentForIssue
import com.baptistecarlier.mcp.jira.server.tool.getCurrentSprint
import com.baptistecarlier.mcp.jira.server.tool.getIssueDetails
import com.baptistecarlier.mcp.jira.server.tool.getIssueDetailsForList
import com.baptistecarlier.mcp.jira.server.tool.getIssuesFromCurrentSprint
import com.baptistecarlier.mcp.jira.server.tool.getIssueTransitions
import com.baptistecarlier.mcp.jira.server.tool.moveIssueWithTransition
import com.baptistecarlier.mcp.jira.server.tool.updateIssueTitleOrBody

internal class McpJiraServer(
    private val jiraServerConfig: JiraServerConfig,
    private val client: JiraClient
) {

    fun buildServer(): Server {
        val info = Implementation(
            "Jira MCP",
            "1.0.0"
        )
        val options = ServerOptions(
            capabilities = ServerCapabilities(tools = ServerCapabilities.Tools(true))
        )
        return Server(info, options).apply {
            getCurrentSprint(
                jiraClient = client,
                boardId = jiraServerConfig.boardId,
            )

            getIssuesFromCurrentSprint(
                jiraClient = client,
                boardId = jiraServerConfig.boardId,
            )

            getIssueDetails(
                jiraClient = client,
            )

            getIssueDetailsForList(
                jiraClient = client,
            )

            getIssueTransitions(
                jiraClient = client,
            )

            addCommentForIssue(
                jiraClient = client,
            )

            moveIssueWithTransition(
                jiraClient = client,
            )

            updateIssueTitleOrBody(
                jiraClient = client,
            )
        }
    }
}