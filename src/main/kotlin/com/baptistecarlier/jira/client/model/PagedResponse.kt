package com.baptistecarlier.jira.client.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames
import kotlinx.serialization.Transient

@OptIn(ExperimentalSerializationApi::class)
@Serializable
internal data class PagedResponse<T>(
    val maxResults: Int? = null,
    val startAt: Int? = null,
    val total: Int? = null,
    val isLast: Boolean? = null,
    @JsonNames("values", "issues")
    val values: List<T>? = null
)

@Serializable
internal data class Sprint(
    val id: Long? = null,
    val self: String? = null,
    val state: String? = null,
    val name: String? = null,
    val startDate: String? = null,
    val endDate: String? = null,
    val createdDate: String? = null,
    val originBoardId: Long? = null,
    val goal: String? = null
)

@Serializable
internal data class Issue(
    @Transient
    val id: String? = null,
    val self: String? = null,
    val key: String? = null,
    val fields: IssueField? = null
)

@Serializable
internal data class IssueField(
    val summary: String? = null,
    val description: String? = null,
    val issuelinks: List<IssueLink>? = null,
    val assignee: Assignee? = null,
    val comment: Comment? = null,
    val statusCategory: StatusCategory? = null,
    val status: StatusTo? = null,
    val issuetype: IssueType? = null,
    val parent: Issue? = null,
    val priority: Priority? = null
)

@Serializable
internal data class Priority(
    val self: String,
    val iconUrl: String? = null,
    val name: String,
    val id: String
)

@Serializable
internal data class IssueType(
    val self: String? = null,
    val id: String? = null,
    val description: String? = null,
    val iconUrl: String? = null,
    val name: String? = null,
    val subtask: Boolean? = null,
    val avatarId: Int? = null,
    val hierarchyLevel: Int? = null
)
@Serializable
internal data class Comment(
    val comments: List<CommentItem>? = null,
    val self: String? = null,
    val maxResults: Int? = null,
    val total: Int? = null,
    val startAt: Int? = null
)

@Serializable
internal data class CommentItem(
    val self: String? = null,
    val id: String? = null,
    val author: CommentAuthor? = null,
    val body: String? = null,
    val updateAuthor: CommentAuthor? = null,
    val created: String? = null,
    val updated: String? = null,
    val jsdPublic: Boolean? = null,
)

@Serializable
internal data class CommentAuthor(
    val self: String? = null,
    val accountId: String? = null,
    val emailAddress: String? = null,
    val avatarUrls: AvatarUrls? = null,
    val displayName: String? = null,
    val active: Boolean? = null,
    val timeZone: String? = null,
    val accountType: String? = null,
)

@Serializable
internal data class Assignee(
    val self: String? = null,
    val accountId: String? = null,
    val emailAddress: String? = null,
    val avatarUrls: AvatarUrls? = null,
    val displayName: String? = null,
    val active: Boolean? = null,
    val timeZone: String? = null,
    val accountType: String? = null,
)

@Serializable
internal data class AvatarUrls(
    val x48x48: String? = null,
    val x24x24: String? = null,
    val x16x16: String? = null,
    val x32x32: String? = null,
)
@Serializable
internal data class IssueLink(
    val inwardIssue: InwardOutwardIssue? = null,
    val outwardIssue: InwardOutwardIssue? = null
)

@Serializable
internal data class InwardOutwardIssue(
    @Transient
    val id: String? =null,
    val key: String? = null,
    val fields: IssueField? = null
)

@Serializable
internal data class StatusCategory(
    val self: String? = null,
    val id: Int? = null,
    val key: String? = null,
    val colorName: String? = null,
    val name: String? = null
)

@Serializable
internal data class StatusTo(
    val self: String? = null,
    val description: String? = null,
    val iconUrl: String? = null,
    val name: String? = null,
    val id: String? = null,
    val statusCategory: StatusCategory? = null
)

@Serializable
internal data class Transition(
    val id: String? = null,
    val name: String? = null,
    val to: StatusTo? = null,
    val hasScreen: Boolean? = null,
    val isGlobal: Boolean? = null,
    val isInitial: Boolean? = null,
    val isAvailable: Boolean? = null,
    val isConditional: Boolean? = null,
    val isLooped: Boolean? = null
)

@Serializable
internal data class TransitionsResponse(
    val expand: String? = null,
    val transitions: List<Transition>? = null
)
