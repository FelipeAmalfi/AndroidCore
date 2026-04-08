# Clean Architecture Overview

## Purpose

AndroidCore uses a layered structure so feature code stays understandable and testable.

The most important rule is directional dependency:

- presentation depends on domain
- data depends on domain contracts
- domain stays independent of Android UI and data implementations

The detailed rules live in `.github/skills/clean-architecture.skill.md`.
This document is the short architectural overview.

## The three feature layers

### Presentation

The presentation layer handles:

- user actions
- screen state
- one-time UI effects
- orchestration through ViewModels

It should know about domain use cases and UI mapping, but not data implementations.

### Domain

The domain layer handles:

- business rules
- validation
- use cases
- repository contracts
- domain models

This is the most stable layer and should remain framework-light.

### Data

The data layer handles:

- API access
- local persistence
- caching
- repository implementations
- DTO and entity mapping into domain models

It fulfills domain contracts without pulling business rules out of the domain layer.

## Shared core layer

The `core/` package is not a fourth business layer. It is shared infrastructure.

Common examples include:

- `core/mvi/` for presentation primitives
- `core/usecase/` for domain operation contracts
- `core/error/` and `core/network/` for failure handling
- `core/utils/` and `core/flow/` for async helpers
- `core/testing/` for test support

## Dependency picture

```text
UI / Presentation
        ↓
      Domain
        ↑
       Data
```

Another way to read this:

- presentation talks inward to domain
- data implements contracts defined by domain
- domain does not know the concrete data source details

## Typical runtime flow

```text
User action
→ ViewModel
→ UseCase
→ Repository contract
→ Repository implementation
→ Remote / Local / Cache source
→ Result back upward
```

## Where core utilities fit

The shared primitives help each layer stay thin:

| Need | Core utility |
|---|---|
| presentation state orchestration | `MviViewModel` and `launchData(...)` |
| domain operation contract | `UseCase<P, R>` and `Result<R>` |
| consistent failures | `AppError` and `toAppError()` |
| safer network calls | `safeApiCall(...)` |
| testability | `DispatcherProvider`, `TestDispatcherProvider`, fakes |

## Practical implications

When you are adding a feature, a good boundary check is:

- if it decides business behavior, it belongs in domain
- if it coordinates external data, it belongs in data
- if it turns user actions into screen state, it belongs in presentation

## What docs intentionally avoid

This page does not restate file-by-file naming rules or generation constraints.
Those belong in:

- `.github/skills/clean-architecture.skill.md`
- `.github/skills/naming-conventions.skill.md`
- `.github/skills/core-usage.skill.md`
- `.github/agents/*.agent.md`

## Related reading

- `docs/architecture/mvi-overview.md`
- `docs/guides/how-to-create-feature.md`
- `.github/skills/clean-architecture.skill.md`

