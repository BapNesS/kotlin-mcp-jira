package com.baptistecarlier.jira.client

import com.baptistecarlier.jira.client.model.Issue
import com.baptistecarlier.jira.client.model.PagedResponse
import com.baptistecarlier.jira.client.model.Sprint
import com.baptistecarlier.jira.client.model.Transition
import io.ktor.http.HttpStatusCode

internal interface JiraClient {
    suspend fun getMyself(): String
    suspend fun getActiveSprints(
        boardId: String,
    ): PagedResponse<Sprint>
    suspend fun getIssue(
        issueIdOrKey: String,
    ): Issue
    suspend fun addCommentToIssue(
        issueIdOrKey: String,
        comment: String
    ): HttpStatusCode
    suspend fun getIssuesFromSprint(
        boardId: String,
        sprintId: String,
    ): PagedResponse<Issue>
    suspend fun getIssueTransitions(
        issueIdOrKey: String
    ): List<Transition>?
    suspend fun postTransitionIssue(
        issueIdOrKey: String,
        transitionId: String
    ): HttpStatusCode
    suspend fun updateIssueSummaryOrDescription(
        issueIdOrKey: String,
        summary: String?,
        description: String?
    ): HttpStatusCode
}

/**
 * Checks that the HTTP status code is OK.
 * Throws an exception with the provided comment if the status is not OK.
 */
internal fun HttpStatusCode.ensureStatusIsOK(comment: String) {
    check(this in HttpStatusCode.OK..HttpStatusCode.MultiStatus) { "$comment\nHttpStatus: $this" }
}