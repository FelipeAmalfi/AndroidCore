# AGENTS.md

## Scope and Big Picture
- This repository is an Android library module (`:app`), not an app; the package root is `com.url.androidcore` (`app/build.gradle.kts`).
- Current production code is a reusable core toolkit under `app/src/main/java/com/url/androidcore/core/*` (MVI base, errors, use cases, networking, flow, logging, testing helpers).
- Architecture intent is Clean Architecture + MVI for generated features; high-level references are in `docs/architecture/*.md` and bundled under `app/src/main/assets/agents/`.
- Layer dependency direction to preserve: presentation -> domain, data -> domain contracts; domain stays framework-agnostic (`.github/skills/clean-architecture.skill.md`).

## Core Contracts You Must Reuse
- MVI contracts are marker interfaces: `MviIntent`, `MviUiState`, `MviUiEffect` (`core/mvi/MviContracts.kt`).
- ViewModels must extend `MviViewModel<I,S,E>` and use reducer-style updates (`setState`) plus one-shot effects via `sendEffect` (`core/mvi/MviViewModel.kt`).
- Async orchestration should use `launchData` (`core/utils/CoroutineExt.kt`) instead of ad-hoc coroutine boilerplate.
- Domain operations use `UseCase<P, R>` returning `Result<R>` (`core/usecase/UseCase.kt`).
- Error channel is `AppError` + `Throwable.toAppError()` + `getUserMessage()` (`core/error/AppError.kt`); network calls should use `safeApiCall` (`core/network/NetworkExt.kt`).
- Reuse dispatcher abstraction (`DispatcherProvider`) and test doubles (`TestDispatcherProvider`, `FakeLogger`, `FakeNetworkMonitor`) from `core/testing/TestHelpers.kt`.

## Feature Generation Workflow (Project Convention)
- Required phase order: data-layer -> domain -> mapper -> presentation -> test -> enhancement(optional) (`.github/skills/agent-workflow.skill.md`).
- Expected feature package layout and flow are documented at a high level in `docs/guides/how-to-create-feature.md`.
- Naming is strict and suffix-driven (`.github/skills/naming-conventions.skill.md`): `{Feature}Intent`, `{Feature}UiState`, `{Feature}UiEffect`, `{Action}{Feature}UseCase`, `{Feature}RepositoryImpl`, `{Feature}Mapper.kt`.
- Mapper layer uses extension functions only (`Dto.toDomain()`, `Domain.toUi()`), no mapper classes (`.github/skills/mapping-rules.skill.md`, `.github/agents/mapper.agent.md`).
- Reference agents by stable `name` in prompts (for example `data-layer-agent`), not by filename.

## Build/Test Workflow
- Gradle wrapper is available at repo root (`gradlew.bat`, `settings.gradle.kts`, `include(":app")`).
- Typical commands:
  - `./gradlew.bat :app:assemble`
  - `./gradlew.bat :app:test`
  - `./gradlew.bat :app:lint`
- In this environment, Gradle execution currently fails before build because `JAVA_HOME` is not set (verified once during analysis).

## Integration Notes for AI Agents
- Do not recreate core infrastructure already present in `core/*`; compose from it (`.github/skills/core-usage.skill.md`).
- Keep data layer free of business rules; keep domain free of Android imports; keep presentation free of direct data-layer dependencies (`.github/skills/clean-architecture.skill.md`).
- For failures, map early to `AppError` and surface user-safe messages; preserve retryability semantics for network paths (`.github/skills/error-handling.skill.md`).
- Documentation source of truth for high-level understanding lives in `docs/**/*.md`; bundled copies under `app/src/main/assets/agents/` are exposed via `core/agent/AgentDocumentation.kt`.

