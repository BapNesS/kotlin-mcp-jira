package com.baptistecarlier.mcp.jira.server.key

import io.modelcontextprotocol.kotlin.sdk.CallToolResult
import io.modelcontextprotocol.kotlin.sdk.TextContent
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObjectBuilder
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.put

internal class InputSchemaField(
    val key: String,
    val builderAction: JsonObjectBuilder.() -> Unit,
)

internal fun JsonObjectBuilder.putJsonObject(inputSchemaField: InputSchemaField): JsonElement? =
    put(inputSchemaField.key, buildJsonObject(inputSchemaField.builderAction))

public fun JsonElement?.getString(): String? = this?.jsonPrimitive?.content

public fun callToolResultError(label: String): CallToolResult = CallToolResult(
    isError = true,
    content = listOf(TextContent(label))
)

internal fun errorMissingParameter(inputSchemaField: InputSchemaField): CallToolResult =
    callToolResultError("Required parameter '${inputSchemaField.key}' is missing.")

// Parameters:

internal val issueIdOrKey = InputSchemaField(
    key = "issueIdOrKey",
    builderAction = {
        put("type", "string")
        put("description", "ID or key of the Jira issue to retrieve transitions for.")
    }
)

internal val comment = InputSchemaField(
    key = "comment",
    builderAction = {
        put("type", "string")
        put("description", "Text of the comment to add to the ticket.")
    }
)

internal val transitionName = InputSchemaField(
    key = "transitionName",
    builderAction = {
        put("type", "string")
        put("description", "Text of the transition to apply to the ticket.")
    }
)

internal val issueSummary = InputSchemaField(
    key = "issueSummary",
    builderAction = {
        put("type", "string")
        put("description", "Title of the Jira issue.")
    }
)

internal val issueBody = InputSchemaField(
    key = "issueBody",
    builderAction = {
        put("type", "string")
        put("description", "Body of the Jira issue to update. It's the full description, not just a comment.")
    }
)
