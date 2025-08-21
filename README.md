# Model Context Protocol Kotlin SDK - Jira Integration

> [!NOTE]
> This is a **proof of concept** and is not intended for production use. It is designed to showcase the capabilities of the Model Context Protocol Kotlin SDK and how it can be used to interact with Jira.

## What is it?

This project demonstrates how to build an integration between **LLM clients** (like Claude Desktop or Copilot) and 
**Jira** using the **Model Context Protocol** (MCP) Kotlin SDK. It enables natural language control and automation of
Jira tasks via MCP.

It runs locally and can be used with any LLM client that supports MCP.

### Feature Demo

Click the image below to see a video demonstration of the integration in action:

[![CLICK TO SEE THE VIDEO](https://img.youtube.com/vi/B4ePgV35oVw/maxresdefault.jpg)](https://www.youtube.com/watch?v=B4ePgV35oVw)

## How does it work?

- Defines interfaces for communication between MCP and Jira.
- Implements MCP server logic using the official Kotlin SDK.
- Uses Ktor for HTTP server/client functionality.
- Provides configuration and debugging via the MCP inspector.

## Getting Started

- Clone the repository
- Sync and launch gradle command: `./gradlew installDist`

### Required information for Jira connection

To use the integration, you must provide **4 essential pieces of information** to the application:

| Parameter | Description                                                                                                                                   | Example                       | 
|-----------|-----------------------------------------------------------------------------------------------------------------------------------------------|-------------------------------|
| `boardId` | Jira board identifier (in your Jira board URL for example)                                                                                    | 1234                          |
| `baseUrl` | Base URL of your Jira instance                                                                                                                | https://yourorg.atlassian.net |
| `mail`    | Email address of your Jira account                                                                                                            | user@example.com              |
| `apiKey`  | Jira API key (access token), generate in your profile's security settings.<br/>(https://id.atlassian.com/manage-profile/security for example) | abcd1234...                   |

To generate an API key, log in to Jira, go to **Account Settings > Security > Create and manage API tokens**.

## Run your MCP

You can run it in two ways

### With [MCP Inspector](https://github.com/modelcontextprotocol/mcp-inspector):

1. Once installed, start the MCP Inspector: `npx @modelcontextprotocol/inspector`
2. Run the application: `/Users/ABSOLUTE_PATH_TO_YOUR_REPO/kotlin-mcp-jira/build/scripts/kotlin-mcp-jira`
3. Use the following arguments :
   `-PboardId:xxx -PbaseUrl:https://xxx.atlassian.net -Pmail:yyy@xxx.com -PapiKey:xxx`

### Directly into your LLM client with MCP configuration, like:

```
{
  "servers": {
    "kotlin-mcp-jira": {
      "command": "/Users/ABSOLUTE_PATH_TO_YOUR_REPO/kotlin-mcp-jira/build/install/kotlin-mcp-jira/bin/kotlin-mcp-jira",
      "args": [
        "-PboardId:xxx",
        "-PbaseUrl:https://xxx.atlassian.net",
        "-Pmail:yyy@xxx.com",
        "-PapiKey:xxx"
      ]
    }
  }
}
```

## Available tools

- `get-jira-current-sprint`: Returns the current sprint from Jira.
- `get-issues-from-current-sprint`: Retrieves the issues from the current sprint.
- `get-issue-details`: Retrieves the details of a Jira issue by its ID or key.
- `get-issue-details-for-list`: Retrieves the details of a list of Jira issues.
- `get-issue-transitions`: Retrieves the list of possible transitions for a Jira issue.
- `add-comment-for-issue`: Adds a comment to a Jira issue by its ID or key.
- `move-issue-with-transition`: Moves a Jira issue to a new status using a transition.
- `update-issue-title-or-body` : Update the title or body of a Jira issue by its ID or key.

## Tools & Libraries Used

- [Model Context Protocol Kotlin SDK](https://github.com/modelcontextprotocol/kotlin-sdk)
- [Ktor](https://ktor.io/)
- [Koin](https://insert-koin.io/)
- [Kotlinx Serialization](https://github.com/Kotlin/kotlinx.serialization)
- [Jira REST API](https://developer.atlassian.com/cloud/jira/platform/rest/v3/intro/)

## Related Links

- [Model Context Protocol Kotlin SDK](https://github.com/modelcontextprotocol/kotlin-sdk)
- [Claude Desktop](https://github.com/modelcontextprotocol/claude-desktop)
- [MCP Inspector](https://github.com/modelcontextprotocol/mcp-inspector)
- [Ktor](https://github.com/ktorio/ktor)
- [Koin](https://github.com/InsertKoinIO/koin)

## License

This project is licensed under the MIT License. See the [LICENSE](./LICENSE) file for details.