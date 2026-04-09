# AndroidCore Guidance For Consumer Projects

This file is intended to live inside a repository that consumes `AndroidCore`.
Its purpose is to give Copilot a local, promptable copy of the guidance that matters most for feature generation.

## Repository assumption
- The consumer project imports `AndroidCore` and should reuse its shared primitives rather than recreating them.

## Architecture rules
- Preserve Clean Architecture direction: presentation -> domain, data -> domain contracts.
- Keep domain free of Android UI imports.
- Keep data free of business rules.
- Keep presentation free of direct data-layer dependencies.

## MVI rules
- Use `MviIntent`, `MviUiState`, `MviUiEffect`.
- Extend `MviViewModel<I, S, E>` for ViewModels.
- Use `setState` for reducer-style state updates.
- Use `sendEffect` for one-time UI events.
- Prefer `launchData(...)` for async ViewModel work.

## Domain rules
- Use `UseCase<P, R>` for domain operations.
- Return the AndroidCore `core.usecase.Result<R>` type.
- Keep validation and business rules in domain.
- Keep repository contracts in domain.

## Data rules
- Use `safeApiCall(...)` for network access.
- Reuse `Repository`, `RemoteDataSource`, `LocalDataSource`, `CacheDataSource`, and `NetworkMonitor` patterns.
- Keep transport, persistence, caching, and fallback logic in data.
- Do not move business rules into DTOs, services, or repository implementations.

## Error handling rules
- Map failures early to `AppError`.
- Use `Throwable.toAppError()`.
- Surface user-facing text through `getUserMessage()`.

## Coroutine and Flow rules
- Use `DispatcherProvider` for testable dispatchers.
- Prefer AndroidCore helpers in `core/coroutines/*` and `core/flow/*`.
- Reuse helpers such as `retryWithExponentialBackoff`, `withTimeoutSafe`, `parallel`, `debounceLatest`, and `asAsyncState()` when they match the need.

## Feature generation order
1. data layer
2. domain layer
3. mapper layer
4. presentation layer
5. tests
6. enhancement work only if needed

## Naming rules
- `{Feature}Intent`, `{Feature}UiState`, `{Feature}UiEffect`, `{Feature}ViewModel`
- `{Action}{Feature}UseCase`
- `{Feature}Repository`, `{Feature}RepositoryImpl`
- `{Feature}Service`, `{Feature}Dto`
- `{Feature}DataSource`, `{Feature}DataSourceImpl`
- `{Feature}Mapper.kt`

## Mapping rules
- Keep mapping in extension functions only.
- Use shapes like `Dto.toDomain()` and `Domain.toUi()`.
- Do not create mapper classes.

## Testing rules
- Prefer AndroidCore helpers from `core/testing/TestHelpers.kt`.
- Reuse `TestDispatcherProvider`, `FakeLogger`, `FakeNetworkMonitor`, `FakeRepository`, and assertion helpers where applicable.
- Cover success, failure, validation, and state/effect transitions.

## Practical feature checklist
- Identify remote/local/cache inputs first.
- Define repository contracts and use cases next.
- Add extension mappers.
- Build MVI presentation contracts and ViewModel orchestration.
- Add tests after the base flow works.
- Add enhancements only after the core path is stable.

## Sync note
This file is a consumer copy of AndroidCore guidance.
Re-sync it when the source AndroidCore repository updates its architecture or generation rules.

