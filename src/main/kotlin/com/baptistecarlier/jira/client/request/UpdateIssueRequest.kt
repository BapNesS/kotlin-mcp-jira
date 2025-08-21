package com.baptistecarlier.jira.client.request

import kotlinx.serialization.Serializable

@Serializable
internal data class UpdateIssueRequest(
    val update: UpdateFields? = null,
    val fields: Fields? = null
)

@Serializable
internal data class UpdateFields(
    val summary: List<SetField>? = null,
)

@Serializable
internal data class SetField(
    val set: String
)

@Serializable
internal data class Fields(
    val description: String? = null
)
