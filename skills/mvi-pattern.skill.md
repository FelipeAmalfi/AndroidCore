---
id: mvi-pattern
type: skill
version: 1.0.0
schema_version: 1
updated_at: 2026-04-08
legacy_file: mvi-pattern.skill
---

# mvi-pattern

**Description:** Defines contract-compliant MVI presentation structure and state/effect behavior.

## Rules
- Intent must be a sealed class implementing MviIntent.
- UiState must be an immutable data class implementing MviUiState and provide initial() in companion object.
- UiEffect must be a sealed class implementing MviUiEffect and represent one-time events only.
- ViewModel must extend MviViewModel<Intent, UiState, UiEffect> and route user actions through handleIntent.
- Use launchData and state reducers (copy) for async operations; avoid ad-hoc coroutine orchestration.

## Examples
- sealed class UserIntent : MviIntent
- data class UserUiState(...) : MviUiState { companion object { fun initial() = UserUiState() } }
- sealed class UserUiEffect : MviUiEffect

