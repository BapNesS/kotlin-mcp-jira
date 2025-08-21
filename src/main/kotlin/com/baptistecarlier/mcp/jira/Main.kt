package com.baptistecarlier.mcp.jira

import com.baptistecarlier.mcp.jira.di.appModule
import com.baptistecarlier.mcp.jira.server.McpJiraServer
import io.modelcontextprotocol.kotlin.sdk.server.StdioServerTransport
import kotlinx.coroutines.Job
import kotlinx.coroutines.runBlocking
import kotlinx.io.asSink
import kotlinx.io.asSource
import kotlinx.io.buffered
import org.koin.core.context.startKoin
import org.koin.core.parameter.parametersOf
import org.koin.java.KoinJavaComponent.get

public fun main(args: Array<String>) {
    startKoin {
        modules(appModule)
    }

    val server = get<McpJiraServer>(McpJiraServer::class.java) {
        parametersOf(args)
    }.buildServer()

    val transport = StdioServerTransport(
        inputStream = System.`in`.asSource().buffered(),
        outputStream = System.out.asSink().buffered()
    )
    runBlocking {
        val job = Job()
        server.onClose { job.complete() }
        server.connect(transport)
        job.join()
    }
}