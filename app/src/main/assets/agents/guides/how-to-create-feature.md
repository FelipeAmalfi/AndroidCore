# How to Create a Feature

## Goal

This guide gives the human-facing path for building a feature in AndroidCore.

For strict generation rules, use:

- `.github/skills/agent-workflow.skill.md`
- `.github/skills/clean-architecture.skill.md`
- `.github/skills/naming-conventions.skill.md`
- `.github/agents/*.agent.md`

## Before you start

Prepare a small feature brief with:

- feature name
- endpoint or data source information
- important models
- business rules
- expected user interactions
- success and failure scenarios

You do not need a full specification document, but you do need enough context to move through the layers in order.

## Recommended build order

AndroidCore expects a feature to be developed in this sequence:

1. data layer
2. domain layer
3. mapper layer
4. presentation layer
5. tests
6. dependency injection
7. enhancement work if needed

This order keeps contracts stable and reduces rework.

## Step 1: Build the data layer

Focus on how the feature gets and stores data.

Typical outputs:

- service or remote contract
- DTOs or entities
- data sources
- repository implementation

Questions to answer:

- What external or local sources exist?
- What failures can happen early?
- What should be mapped into domain models later?

## Step 2: Build the domain layer

Now define the feature in business terms.

Typical outputs:

- domain models
- repository contracts
- one or more use cases

Questions to answer:

- What business rules belong here?
- What validation should happen before data access returns to UI?
- Which operations should return a single `Result` and which should stream?

## Step 3: Add mapping

Create the transformations between layers.

Typical outputs:

- DTO to domain mappings
- domain to UI mappings

This step should stay simple. Mapping is not where business logic belongs.

## Step 4: Build presentation

Translate user interactions into UI state and one-time effects.

Typical outputs:

- `{Feature}Intent`
- `{Feature}UiState`
- `{Feature}UiEffect`
- `{Feature}ViewModel`

The ViewModel should orchestrate domain work through shared primitives like `MviViewModel` and `launchData(...)`.

## Step 5: Write tests

Cover the paths that matter most:

- success
- validation failure
- network or data failure
- fallback behavior
- state transitions and effects

The core testing helpers are intended to keep this fast and consistent.

## Step 6: Wire dependency injection

Use Dagger Hilt to connect all interface/implementation pairs.

Typical outputs:

- `{Feature}DataModule` binding repositories and data sources
- `{Feature}DomainModule` binding domain components when needed
- `@Inject constructor` on all implementation classes
- `@HiltViewModel` on the ViewModel

Questions to answer:

- Which bindings need `@Singleton` scope?
- Are there multiple implementations of the same interface that require qualifiers?
- Are any third-party instances (Retrofit service, SharedPreferences) provided via `@Provides`?

See `docs/guides/how-to-setup-di.md` for the full module patterns.

## Step 7: Add enhancements only after the base feature works

Enhancement work is optional and should happen after the core path is stable.

Examples:

- caching
- retry behavior
- pagination
- filtering
- performance improvements

## What to reuse

Feature code should reuse core infrastructure rather than recreate it.

Common examples:

- `UseCase<P, R>`
- `Result<R>`
- `AppError`
- `safeApiCall(...)`
- `MviViewModel`
- `launchData(...)`
- `DispatcherProvider`
- `FakeLogger`, `FakeNetworkMonitor`, `TestDispatcherProvider`

## Quick checklist

Before considering a feature complete, confirm that:

- layer boundaries are still clear
- the domain layer has no Android UI dependencies
- the presentation layer does not depend directly on data implementations
- error handling is user-safe
- tests cover both happy path and failure path

## Where to look next

- For system understanding: `docs/guides/how-agents-work.md`
- For architecture context: `docs/architecture/clean-architecture.md`
- For DI setup: `docs/guides/how-to-setup-di.md`
- For a concrete walkthrough: `docs/examples/end-to-end-example.md`

