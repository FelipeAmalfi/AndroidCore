# AGENTS.md

## Scope and Big Picture
- This project consumes `AndroidCore`; prefer reusing its shared primitives instead of recreating architecture infrastructure.
- Keep feature code aligned with Clean Architecture + MVI.
- Preserve dependency direction: presentation -> domain, data -> domain contracts; keep domain framework-light.

## Core Contracts You Must Reuse
- Use `MviIntent`, `MviUiState`, `MviUiEffect`, and `MviViewModel<I,S,E>` for presentation contracts.
- Use `launchData(...)` for ViewModel async orchestration.
- Use `UseCase<P, R>` returning the AndroidCore `core.usecase.Result<R>` for domain operations.
- Use `AppError`, `Throwable.toAppError()`, and `getUserMessage()` for failure mapping.
- Use `safeApiCall(...)` for network calls.
- Reuse `DispatcherProvider`, `Repository`, `RemoteDataSource`, `LocalDataSource`, `CacheDataSource`, and `NetworkMonitor` when designing layers around the imported library.
- Prefer AndroidCore async helpers from `core/coroutines/*` and `core/flow/*` over ad-hoc coroutine or Flow plumbing.

## Feature Generation Workflow
- Build features in this order: data-layer -> domain -> mapper -> presentation -> test -> enhancement(optional).
- Keep business rules in domain.
- Keep data layer focused on transport, persistence, caching, and repository implementation.
- Keep presentation limited to MVI state/effect orchestration and domain use case calls.

## Naming Rules
- Use `{Feature}Intent`, `{Feature}UiState`, `{Feature}UiEffect`, `{Feature}ViewModel`.
- Use `{Action}{Feature}UseCase` and `{Feature}Repository` for domain contracts.
- Use `{Feature}Service`, `{Feature}Dto`, `{Feature}DataSource`, `{Feature}DataSourceImpl`, and `{Feature}RepositoryImpl` for data artifacts.
- Keep mappers as extension functions in `{Feature}Mapper.kt` with `toDomain()` / `toUi()`.

## How to Use AndroidCore Guidance Here
- Treat `docs/androidcore/androidcore-guidance.md` as the local copy of AndroidCore guidance that Copilot can read in this repository.
- When generating features, reference the AndroidCore workflow and naming rules from that file.
- If this local guidance drifts from the source library, re-sync it from AndroidCore.

## AI Workflow Hint
- If asked to create a feature, first identify the requested data layer pieces, then domain contracts/use cases, then mappings, then presentation ViewModel/contracts, then tests.
- Reference stable AndroidCore agent names conceptually such as `data-layer-agent`, `domain-agent`, `mapper-agent`, `presentation-agent`, `test-agent`, and `enhancement-agent` when describing the workflow.

