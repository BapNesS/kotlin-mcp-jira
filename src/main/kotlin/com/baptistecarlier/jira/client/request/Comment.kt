package com.baptistecarlier.jira.client.request

import kotlinx.serialization.Serializable

@Serializable
internal data class Comment(
    val body: String? = null,
)