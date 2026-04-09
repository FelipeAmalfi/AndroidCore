# AndroidCore

`AndroidCore` is an Android library module that provides a reusable core toolkit for feature development.
The repository centers on the package root `com.url.androidcore`, especially the shared code under `app/src/main/java/com/url/androidcore/core`.

## What this project includes

The current library provides reusable building blocks for:

- Clean Architecture boundaries
- MVI presentation flows
- use case execution and result handling
- application-safe error mapping
- network safety helpers
- coroutine and Flow helpers
- shared test doubles and assertions
- bundled project guidance for humans and AI-assisted workflows

## Core packages

Key packages inside `core/` include:

- `core/mvi/` — `MviIntent`, `MviUiState`, `MviUiEffect`, `MviViewModel`
- `core/usecase/` — `UseCase<P, R>` and the library `Result<R>` type
- `core/error/` — `AppError`, `toAppError()`, `getUserMessage()`
- `core/network/` — `safeApiCall(...)`
- `core/utils/` — `launchData(...)`
- `core/coroutines/` — retry, timeout, and parallel helpers
- `core/flow/` — `AsyncState` and Flow extensions
- `core/testing/` — `TestDispatcherProvider`, `FakeLogger`, `FakeNetworkMonitor`, `FakeRepository`, assertion helpers
- `core/agent/` — runtime access to bundled markdown guidance

## Repository layout

```text
AndroidCore/
├── app/                             # Android library module
│   └── src/main/java/com/url/androidcore/
│       └── core/                    # Shared toolkit
├── .github/agents/                  # Agent descriptors
├── .github/skills/                  # Source-of-truth generation rules
├── docs/                            # Human-facing documentation
└── app/src/main/assets/agents/      # Bundled runtime copies of docs
```

## Build configuration

From the checked-in Gradle files, the library currently targets:

- `compileSdk 36`
- `minSdk 24`
- Java 11
- Kotlin JVM target 11

Typical commands from the repository root:

```powershell
.\gradlew.bat :app:assemble
.\gradlew.bat :app:test
.\gradlew.bat :app:lint
```

## Important limitation for Copilot and other IDE agents

Bundled Android assets are **static documentation** under `app/src/main/assets/agents/**`.
They are useful for reference and tooling that reads static files.

They are **not** automatically ingested by Copilot just because the library is imported.
If you want the consuming repository to benefit from this guidance during AI-assisted coding, use one of these approaches:

1. copy the relevant guidance into the consuming repository (`AGENTS.md`, `README.md`, or local docs)
2. paste or attach the generated markdown bundle into the chat prompt
3. export the bundle and save it to a promptable file in the consumer repository

See `docs/guides/using-agent-documentation.md` for the recommended workflow.

## Consumer-repository Copilot setup

This repository now provides two ways to make AndroidCore guidance visible inside a downstream repository:

1. a copyable starter pack under `templates/consumer-copilot/`
2. a repeatable export script at `scripts/export-androidcore-guidance.ps1`

Example export command:

```powershell
.\scripts\export-androidcore-guidance.ps1 -TargetRepoPath "C:\Path\To\ConsumerRepo" -Force
```

That command writes consumer-visible guidance files into the target repository:

- `AGENTS.md`
- `.github/copilot-instructions.md`
- `docs/androidcore/androidcore-guidance.md`

This repository also now includes its own `.github/copilot-instructions.md` so Copilot has a short, direct entrypoint when working inside AndroidCore itself.

## Local-first AI system

This repository now includes a complete local-first AI setup for developer tooling:

- bundled agents in `app/src/main/assets/agents/local-ai/*.json`
- bundled skills in `app/src/main/assets/skills/*`
- local MCP-style server in `mcp-server/` (Express, localhost only)
- Gradle bootstrap tasks to export and run everything

Primary automation tasks:

```powershell
.\gradlew.bat setupLocalAI
.\gradlew.bat startLocalMcp
.\gradlew.bat initAI
```

Generated local output:

```text
.ai/
  agents/
  skills/
  manifest.json
```

Read the full guide at `docs/guides/local-first-ai-system.md`.

## Where to start reading

Recommended reading order:

1. `docs/overview/introduction.md`
2. `docs/overview/system-vision.md`
3. `docs/architecture/clean-architecture.md`
4. `docs/architecture/mvi-overview.md`
5. `docs/guides/how-agents-work.md`
6. `docs/guides/how-to-create-feature.md`
7. `docs/guides/using-agent-documentation.md`
8. `docs/guides/local-first-ai-system.md`
9. `docs/examples/end-to-end-example.md`

## Notes for maintainers

Bundled markdown documentation lives in `app/src/main/assets/agents/**` and is synchronized from source docs.

## Current distribution state

This repository currently contains the Android library module itself.
No published artifact coordinates are defined in the checked-in build files here, so consumers should integrate it according to their own build and distribution setup.

