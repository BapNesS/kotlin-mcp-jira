package com.baptistecarlier.jira.client.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class TransitionRequest(
    @SerialName("transition")
    val transition: TransitionId
) {
    @Serializable
    data class TransitionId(
        @SerialName("id")
        val id: String
    )
}

