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
- Domain operations use `UseCase<P, R>` returning the library `core.usecase.Result<R>` (`core/usecase/UseCase.kt`).
- Error channel is `AppError` + `Throwable.toAppError()` + `getUserMessage()` (`core/error/AppError.kt`); network calls should use `safeApiCall` (`core/network/NetworkExt.kt`).
- Reuse dispatcher abstraction (`DispatcherProvider`), repository/data source/connectivity markers (`core/repository/Repository.kt`, `core/datasource/DataSource.kt`, `core/connectivity/NetworkMonitor.kt`), and shared test utilities (`TestDispatcherProvider`, `FakeLogger`, `FakeNetworkMonitor`, `FakeRepository`, assertion helpers) from `core/testing/TestHelpers.kt`.
- Prefer shared async helpers for retry/timeout/parallel/Flow work (`core/coroutines/RetryExt.kt`, `TimeoutExt.kt`, `ParallelExt.kt`, `core/flow/FlowExt.kt`) instead of ad-hoc coroutine/Flow plumbing; examples already provided include `retryWithExponentialBackoff`, `withTimeoutSafe`, `parallel`, `debounceLatest`, and `asAsyncState()`.

## Feature Generation Workflow (Project Convention)
- Required phase order: data-layer -> domain -> mapper -> presentation -> test -> di -> enhancement(optional) (`.github/skills/agent-workflow.skill.md`).
- Expected feature package layout and flow are documented at a high level in `docs/guides/how-to-create-feature.md`.
- DI wiring is handled by the `di-agent` (`di.agent.md`) using Dagger Hilt; rules live in `.github/skills/di-hilt.skill.md` and the human-facing guide is in `docs/guides/how-to-setup-di.md`.
- Naming is strict and suffix-driven (`.github/skills/naming-conventions.skill.md`): `{Feature}Intent`, `{Feature}UiState`, `{Feature}UiEffect`, `{Feature}ViewModel`, `{Action}{Feature}UseCase`, `{Feature}Repository`, `{Feature}RepositoryImpl`, `{Feature}Service`, `{Feature}Dto`, `{Feature}DataSource`/`{Feature}DataSourceImpl`, `{Feature}Mapper.kt`, `{Feature}DataModule`, `{Feature}DomainModule`.
- Mapper layer uses extension functions only (`Dto.toDomain()`, `Domain.toUi()`), no mapper classes (`.github/skills/mapping-rules.skill.md`, `.github/agents/mapper.agent.md`).
- Reference agents by stable `name` in prompts (for example `data-layer-agent`), not by filename.

## Build/Test Workflow
- Gradle wrapper is available at repo root (`gradlew.bat`, `settings.gradle.kts`, `include(":app")`).
- Typical commands:
  - `./gradlew.bat :app:assemble`
  - `./gradlew.bat :app:test`
  - `./gradlew.bat :app:lint`
- Module build settings currently target `compileSdk 36`, `minSdk 24`, and Java/Kotlin 11 (`app/build.gradle.kts`).

## Integration Notes for AI Agents
- Do not recreate core infrastructure already present in `core/*`; compose from it, including shared repository/data-source/connectivity abstractions (`.github/skills/core-usage.skill.md`).
- Keep data layer free of business rules; keep domain free of Android imports; keep presentation free of direct data-layer dependencies (`.github/skills/clean-architecture.skill.md`).
- For failures, map early to `AppError` and surface user-safe messages; preserve retryability semantics for network paths (`.github/skills/error-handling.skill.md`).
- Use Dagger Hilt for all dependency injection; follow `.github/skills/di-hilt.skill.md` for module structure, scoping, and `@Inject` placement.
- Normative generation rules live in `.github/skills/*.skill.md`; `.github/agents/*.agent.md` are execution descriptors that reference those skills; `docs/**/*.md` remains the human-facing overview layer (`docs/guides/how-agents-work.md`, `.github/skills/documentation-governance.skill.md`).
- When docs move or new docs are added, keep `docs/**`, mirrored assets under `app/src/main/assets/agents/**`, and `core/agent/AgentDocumentation.kt` (`availableDocuments` plus helper accessors) in sync.
- Documentation source of truth for high-level understanding lives in `docs/**/*.md`; bundled copies under `app/src/main/assets/agents/` are exposed via `core/agent/AgentDocumentation.kt`.

