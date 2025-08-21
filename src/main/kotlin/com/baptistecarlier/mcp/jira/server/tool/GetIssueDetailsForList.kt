package com.baptistecarlier.mcp.jira.server.tool

import com.baptistecarlier.jira.client.JiraClient
import com.baptistecarlier.mcp.jira.server.key.callToolResultError
import io.modelcontextprotocol.kotlin.sdk.CallToolResult
import io.modelcontextprotocol.kotlin.sdk.TextContent
import io.modelcontextprotocol.kotlin.sdk.Tool
import io.modelcontextprotocol.kotlin.sdk.server.Server
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import kotlinx.serialization.json.*

internal fun Server.getIssueDetailsForList(
    jiraClient: JiraClient,
) {

    // Define the input schema for the tool
    val inputSchema = Tool.Input(
        properties = buildJsonObject {
            putJsonObject("ids") {
                put("type", "array")
                put("description", "List of issue IDs to retrieve details for.")
                putJsonObject("items") {
                    put("type", "string")
                }
            }
        },
        required = listOf("ids")
    )

    addTool(
        name = "get-issue-details-for-list",
        description = "Get details of a list of Jira issues by their IDs or Keys.",
        inputSchema = inputSchema
    ) { input ->

        val ids = input.arguments["ids"]?.jsonArray?.mapNotNull { it.jsonPrimitive.contentOrNull }
            ?: return@addTool callToolResultError("Required parameter 'ids' is missing or invalid.")

        // A small rate limit to avoid too many requests in parallel
        val semaphore = Semaphore(5)

        coroutineScope {
            val issues = ids.map { id ->
                async {
                    semaphore.withPermit {
                        runCatching {
                            jiraClient.getIssue(id)
                        }
                    }
                }
            }.awaitAll()

            val issueDetails = issues.mapNotNull { it.getOrElse { null } }

            if (issueDetails.isEmpty()) {
                return@coroutineScope callToolResultError("No issues found or all requests failed.")
            }

            CallToolResult(
                content = listOf(TextContent("Retrieved details for ${issueDetails.size} issues.")),
                structuredContent  = JsonObject(
                    mapOf(
                        "issues" to JsonArray(issueDetails.map { Json.encodeToJsonElement(it) })
                    )
                )
            )
        }
    }
}
