# Copilot Instructions

Follow `AGENTS.md` in the repository root as the primary project-specific guidance.

## Repository identity
- This repository is an Android **library** module, not an app.
- The main module is `:app` with package root `com.url.androidcore`.
- Shared production code lives under `app/src/main/java/com/url/androidcore/core/*`.

## What to optimize for
- Reuse the existing core toolkit instead of recreating architecture primitives.
- Preserve Clean Architecture direction: presentation -> domain, data -> domain contracts.
- Preserve MVI patterns for presentation work.
- Keep domain framework-light and free of Android UI imports.

## Core primitives to reuse
- `MviIntent`, `MviUiState`, `MviUiEffect`
- `MviViewModel<I, S, E>`
- `launchData(...)`
- `UseCase<P, R>` returning the library `core.usecase.Result<R>`
- `AppError`, `Throwable.toAppError()`, `getUserMessage()`
- `safeApiCall(...)`
- `DispatcherProvider`
- `Repository`, `RemoteDataSource`, `LocalDataSource`, `CacheDataSource`, `NetworkMonitor`
- shared test helpers in `core/testing/TestHelpers.kt`
- shared coroutine/Flow helpers in `core/coroutines/*` and `core/flow/*`

## Feature generation workflow
When generating a feature, move in this order:
1. data layer
2. domain layer
3. mapper layer
4. presentation layer
5. tests
6. dependency injection (Dagger Hilt modules via `di-agent`)
7. enhancement work only if needed

## Naming rules
Use the AndroidCore naming patterns already documented in `AGENTS.md` and `.github/skills/naming-conventions.skill.md`, including:
- `{Feature}Intent`, `{Feature}UiState`, `{Feature}UiEffect`, `{Feature}ViewModel`
- `{Action}{Feature}UseCase`
- `{Feature}Repository`, `{Feature}RepositoryImpl`
- `{Feature}Service`, `{Feature}Dto`
- `{Feature}DataSource`, `{Feature}DataSourceImpl`
- `{Feature}Mapper.kt`

## Files to consult for exact rules
- `AGENTS.md`
- `.github/skills/*.skill.md`
- `.github/skills/di-hilt.skill.md`
- `.github/agents/*.agent.md`
- `.github/agents/di.agent.md`
- `docs/guides/how-to-create-feature.md`
- `docs/guides/how-to-setup-di.md`
- `docs/guides/how-agents-work.md`

## Documentation sync rule
If you change high-level docs, keep these aligned:
- `docs/**`
- `app/src/main/assets/agents/**`
- `app/src/main/java/com/url/androidcore/core/agent/AgentDocumentation.kt`

## Important limitation
`AgentDocumentation` exposes bundled docs at runtime for consuming apps, but imported dependency assets are not automatically used by Copilot in other repositories. For downstream AI usage, use the consumer template pack or export script in this repository.

