package com.baptistecarlier.mcp.jira.di

import com.baptistecarlier.jira.client.JiraClient
import com.baptistecarlier.jira.client.JiraClientImpl
import com.baptistecarlier.mcp.jira.argument.JiraServerConfig
import com.baptistecarlier.mcp.jira.server.McpJiraServer
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

internal val appModule = module {
    single { (args: Array<String>) ->
        JiraServerConfig.fromArgs(args)
    }
    factory { (args: Array<String>) ->
        val jiraServerConfig = get<JiraServerConfig> {
            parametersOf(args)
        }
        McpJiraServer(jiraServerConfig, get())
    }

    factory<JiraClient> { JiraClientImpl(get()) }
}

