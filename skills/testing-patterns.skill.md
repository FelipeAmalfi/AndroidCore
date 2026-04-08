---
id: testing-patterns
type: skill
version: 1.0.0
schema_version: 1
updated_at: 2026-04-08
legacy_file: testing-patterns.skill
---

# testing-patterns

**Description:** Testing strategy for repositories, use cases, and MVI presentation logic.

## Rules
- Cover success, failure, and edge cases for every generated use case and repository operation.
- Use fake or mock dependencies; verify behavior and state transitions instead of implementation details.
- Test ViewModel intent handling, UiState transitions, and UiEffect emissions.
- Use coroutine test utilities (runTest, advanceUntilIdle, TestDispatcherProvider) for deterministic async tests.
- Validate error mapping to AppError and retry/fallback behavior where applicable.

## Examples
- GetUserUseCaseTest checks blank ID validation and success path.
- UserRepositoryImplTest checks cache-hit, remote success, and offline fallback.
- UserViewModelTest checks loading, success, and error states.

