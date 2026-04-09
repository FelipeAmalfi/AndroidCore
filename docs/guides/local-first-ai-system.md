# Local-First AI System

## 1. Overview

AndroidCore now includes a fully local AI toolkit that ships with the library source:

- predefined agents as JSON files
- reusable skills as markdown files
- a local server with MCP-style HTTP endpoints
- Gradle automation to export and run everything

Everything runs on the local machine with no external APIs and no cloud dependencies.

## 2. Installation

### Prerequisites

- Java 11+ (already required by this repository)
- Node.js 18+ and npm available on `PATH`
- Gradle wrapper usage from repository root

### One-command bootstrap

```powershell
.\gradlew.bat initAI
```

`initAI` runs the export flow and starts the local server on `127.0.0.1:3333`.

## 3. Usage

### Export agents and skills only

```powershell
.\gradlew.bat setupLocalAI
```

This generates:

- `.ai/agents/*.json`
- `.ai/skills/*.md`
- `.ai/manifest.json`

### Start the local server

```powershell
.\gradlew.bat startLocalMcp
```

### Endpoint checks

```powershell
Invoke-RestMethod -Uri "http://127.0.0.1:3333/agents"
Invoke-RestMethod -Uri "http://127.0.0.1:3333/agents/android-feature-builder"
```

### Prompt composition request

```powershell
$body = @{
  agentName = "android-feature-builder"
  userInput = "Create a feature to load user profile with MVI and use cases"
  includeProjectContext = $true
} | ConvertTo-Json

Invoke-RestMethod -Method Post -Uri "http://127.0.0.1:3333/generate" -ContentType "application/json" -Body $body
```

## 4. MCP Configuration Example

Use this config structure in local AI tools that support custom HTTP MCP endpoints:

```json
{
  "name": "androidcore-local",
  "baseUrl": "http://127.0.0.1:3333",
  "endpoints": {
    "listAgents": "/agents",
    "getAgent": "/agents/:name",
    "generate": "/generate"
  }
}
```

If your tool expects a command-based local server launcher, call `./gradlew initAI` first and point the tool to `http://127.0.0.1:3333`.

## 5. Agents Structure

Agents are JSON files under `app/src/main/assets/agents/local-ai/`.
Each agent must define:

- `name`
- `description`
- `prompt`
- optional `skill`
- optional `version`

Example:

```json
{
  "name": "android-feature-builder",
  "version": "1.0.0",
  "description": "Generates Android feature scaffolding.",
  "prompt": "Follow AndroidCore workflow and architecture constraints.",
  "skill": "androidcore-clean-architecture"
}
```

## 6. Creating Custom Agents

1. Add a new `*.json` file in `app/src/main/assets/agents/local-ai/`.
2. Reference a skill by filename stem (for example `my-skill` -> `my-skill.md`).
3. Run `./gradlew setupLocalAI`.
4. Call `GET /agents` to confirm export and discovery.

## 7. Skills System

Skills are markdown or text files under `app/src/main/assets/skills/`.
They are injected into generated prompts when an agent references them through `skill`.

Prompt composition order in `POST /generate`:

1. agent name
2. agent prompt
3. skill content (if present)
4. user input
5. project context (optional)

## 8. Troubleshooting

- `setupLocalAI` fails: verify `app/src/main/assets/agents/local-ai` and `app/src/main/assets/skills` exist.
- `startLocalMcp` fails with npm error: verify Node.js and npm are installed.
- `GET /agents` returns empty list: rerun `setupLocalAI` and check `.ai/agents` for JSON files.
- `POST /generate` returns 404 agent not found: confirm `agentName` matches exported `name`.
- Endpoint connection refused: verify the server is running on `127.0.0.1:3333`.

## 9. Architecture Overview (Text Diagram)

```text
app/src/main/assets/agents/local-ai/*.json      app/src/main/assets/skills/*.md
                    |                                       |
                    +------------------ setupLocalAI -------+
                                      |
                                      v
                                .ai/agents + .ai/skills
                                      |
                                      v
                              mcp-server (Express)
                     GET /agents | GET /agents/:name | POST /generate
                                      |
                                      v
                         Local AI tools (Claude, Cursor, etc.)
```

The server runs locally, reads only local files, and composes prompts without external model calls.

