# Introduction

## What this repository is

`AndroidCore` is an Android library module that provides a reusable foundation for feature development.

At its center is the package root `com.url.androidcore`, especially the shared toolkit under `app/src/main/java/com/url/androidcore/core`.

The library is designed to support:

- clean architectural boundaries
- MVI-based presentation flows
- consistent error handling
- reusable async and testing utilities
- AI-assisted feature generation without duplicating infrastructure

## What this documentation is for

The `docs/` folder is now the human-readable overview layer.

It helps you understand:

- what the system is
- how the pieces fit together
- how to approach feature development
- where to look next

It is **not** the canonical source for implementation rules.

## Where the real rules live

Responsibility is intentionally split:

- `docs/` explains the system at a high level
- `.github/skills/` defines reusable rules and standards
- `.github/agents/` defines execution units and orchestration responsibilities

If you need exact generation rules, naming rules, or layer constraints, use the files in `.github/skills/` and `.github/agents/`.

## Repository map

```text
AndroidCore/
├── app/
│   └── src/main/java/com/url/androidcore/
│       └── core/                  # Shared toolkit used by features
├── .github/agents/                # Execution units for feature generation
├── .github/skills/                # Canonical rules and knowledge modules
└── docs/                          # High-level understanding and onboarding
```

## Core building blocks you will see often

A few shared primitives shape most feature work:

- `MviIntent`, `MviUiState`, `MviUiEffect`
- `MviViewModel<I, S, E>`
- `UseCase<P, R>` returning `Result<R>`
- `AppError` and `Throwable.toAppError()`
- `launchData(...)` for async ViewModel work
- `safeApiCall(...)` for network access
- `DispatcherProvider` and test doubles in `core/testing/`

These are documented in code and reused by features rather than reimplemented.

## Suggested reading order

If you are new to the repository, read in this order:

1. `docs/overview/introduction.md`
2. `docs/overview/system-vision.md`
3. `docs/architecture/clean-architecture.md`
4. `docs/architecture/mvi-overview.md`
5. `docs/guides/how-agents-work.md`
6. `docs/guides/how-to-create-feature.md`
7. `docs/examples/end-to-end-example.md`

## What changed from the previous docs model

Historically, `docs/` mixed architecture overview, implementation rules, templates, rollout notes, and status reports.

After introducing `.github/skills/` and `.github/agents/`, that became redundant.

The new direction is simpler:

- fewer files
- easier scanning
- less repetition
- clearer ownership of knowledge

## If you need more detail

- For architecture rules, start in `.github/skills/clean-architecture.skill.md`
- For MVI requirements, start in `.github/skills/mvi-pattern.skill.md`
- For mapping rules, start in `.github/skills/mapping-rules.skill.md`
- For naming rules, start in `.github/skills/naming-conventions.skill.md`
- For generation flow, start in `.github/skills/agent-workflow.skill.md`
- For execution responsibilities, browse `.github/agents/*.agent.md`

