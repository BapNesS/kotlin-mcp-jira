package com.baptistecarlier.jira.client

import com.baptistecarlier.jira.client.model.Issue
import com.baptistecarlier.jira.client.model.PagedResponse
import com.baptistecarlier.jira.client.model.Sprint
import com.baptistecarlier.jira.client.model.Transition
import com.baptistecarlier.jira.client.model.TransitionsResponse
import com.baptistecarlier.jira.client.request.Fields
import com.baptistecarlier.mcp.jira.argument.JiraServerConfig
import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.defaultRequest
import io.ktor.serialization.kotlinx.json.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import java.security.cert.X509Certificate
import javax.net.ssl.X509TrustManager
import io.ktor.util.*
import io.ktor.http.*
import kotlinx.serialization.json.Json
import com.baptistecarlier.jira.client.request.Comment as RequestComment
import com.baptistecarlier.jira.client.request.TransitionRequest
import com.baptistecarlier.jira.client.request.UpdateIssueRequest
import com.baptistecarlier.jira.client.request.UpdateFields
import com.baptistecarlier.jira.client.request.SetField

internal class JiraClientImpl(
    config: JiraServerConfig
) : JiraClient {

    private val baseUrl = config.baseUrl
    private val basicAuth = "${config.mail}:${config.apiKey}".encodeBase64()

    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = false
    }

    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(json)
        }
        notSafeEngine()
        defaultRequest {
            header(HttpHeaders.Accept, "application/json")
            header(HttpHeaders.Authorization, "Basic $basicAuth")
        }
    }

    private fun HttpClientConfig<CIOEngineConfig>.notSafeEngine() {
        engine {
            https {
                trustManager = object : X509TrustManager {
                    override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {
                        // No-op: trust all clients
                        // This is not recommended for production use, but can be useful for testing purposes.
                        // In production, you should implement proper certificate validation.
                    }

                    override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {
                        // No-op: trust all servers
                        // This is not recommended for production use, but can be useful for testing purposes.
                        // In production, you should implement proper certificate validation.
                    }

                    override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
                }
            }
        }
    }

    override suspend fun getMyself() = client
        .get("${baseUrl}/rest/api/2/myself")
        .bodyAsText()

    override suspend fun getActiveSprints(
        boardId: String,
    ): PagedResponse<Sprint> = client
        .get("${baseUrl}/rest/agile/1.0/board/$boardId/sprint") {
            parameter("state", "active")
        }
        .body()

    override suspend fun getIssue(
        issueIdOrKey: String,
    ): Issue = client
        .get("${baseUrl}/rest/agile/1.0/issue/$issueIdOrKey")
        .body()

    override suspend fun addCommentToIssue(
        issueIdOrKey: String,
        comment: String
    ): HttpStatusCode {
        return client
            .post("${baseUrl}/rest/api/2/issue/$issueIdOrKey/comment") {
                contentType(ContentType.Application.Json)
                setBody(RequestComment(body = comment))
            }.status
    }

    override suspend fun getIssuesFromSprint(
        boardId: String,
        sprintId: String,
    ): PagedResponse<Issue> = client
        .get("${baseUrl}/rest/agile/1.0/board/${boardId}/sprint/${sprintId}/issue")
        .body()

    override suspend fun getIssueTransitions(
        issueIdOrKey: String
    ): List<Transition>? = client
        .get("${baseUrl}/rest/api/2/issue/$issueIdOrKey/transitions")
        .body<TransitionsResponse>()
        .transitions

    override suspend fun postTransitionIssue(
        issueIdOrKey: String,
        transitionId: String
    ): HttpStatusCode {
        val request = TransitionRequest(transition = TransitionRequest.TransitionId(id = transitionId))
        return client.post("${baseUrl}/rest/api/2/issue/$issueIdOrKey/transitions") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.status
    }

    override suspend fun updateIssueSummaryOrDescription(
        issueIdOrKey: String,
        summary: String?,
        description: String?
    ): HttpStatusCode {
        check(summary.isNullOrEmpty().not() || description.isNullOrEmpty().not()) {
            "At least one of summary or description must be provided.\n" +
                    "- summary: $summary\n" +
                    "- description: $description"
        }

        val body = UpdateIssueRequest(
            update = summary
                .takeIf { !(it.isNullOrEmpty()) }
                ?.let { UpdateFields(summary = listOf(SetField(it))) },
            fields = description
                .takeIf { !(it.isNullOrEmpty()) }
                ?.let { Fields(description = it) },
        )

        val put = client.put("${baseUrl}/rest/api/2/issue/$issueIdOrKey") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }

        return put.status
    }

}
