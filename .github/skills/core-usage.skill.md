---
id: core-usage
type: skill
version: 1.0.0
schema_version: 1
updated_at: 2026-04-08
legacy_file: core-usage.skill
---

# core-usage

**Description:** Reuse policy for AndroidCore primitives and utility packages.

## Rules
- Reuse existing core utilities; do not recreate MviViewModel, UseCase, AppError, launchData, DispatcherProvider, Logger, or Flow helpers.
- Import from core packages as the canonical source of shared behavior.
- Keep generated feature code thin by composing core abstractions instead of duplicating infrastructure.
- Use core testing utilities (FakeLogger, FakeNetworkMonitor, TestDispatcherProvider, assertion helpers) for test implementations.
- Keep feature-level code aligned with core interfaces (Repository, DataSource markers, NetworkMonitor, Result wrappers).

## Examples
- class UserViewModel(...) : MviViewModel<UserIntent, UserUiState, UserUiEffect>()
- class GetUserUseCase(...) : UseCase<String, User>
- val result = safeApiCall { api.getUser(id) }

