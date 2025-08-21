package com.baptistecarlier.jira.client

import com.baptistecarlier.mcp.jira.argument.JiraServerConfig
import io.ktor.util.encodeBase64
import kotlin.test.Test
import kotlin.test.assertEquals

class JiraServerConfigTest {

    @Test
    fun `Test fromArgs with valid arguments`() {
        // Given
        val args = arrayOf(
            "-PboardId:1234",
            "-PbaseUrl:https://tmp.atlassian.net",
            "-Pmail:mymailaddress@mydomain.com",
            "-PapiKey:apiTokenValue"
        )

        // When
        val actual = JiraServerConfig.fromArgs(args)

        // Then
        val expect = JiraServerConfig(
            boardId = "1234",
            baseUrl = "https://tmp.atlassian.net",
            mail = "mymailaddress@mydomain.com",
            apiKey = "apiTokenValue",
        )
        assertEquals(expect, actual)
    }

    @Test
    fun `Test bearer generation`() {
        // Given
        val mail = "mymailaddress@mydomain.com"
        val apiKey = "apiTokenValue"

        // When
        val basicAuth = "${mail}:${apiKey}".encodeBase64()

        // Just display
        println(basicAuth)
    }

}