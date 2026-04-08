# MVI Overview

## Purpose

AndroidCore uses a lightweight MVI foundation for presentation logic.

The strict rules are defined in `.github/skills/mvi-pattern.skill.md`.
This page explains the model at a human level.

## Core contracts

The library exposes three marker interfaces in `core/mvi/MviContracts.kt`:

- `MviIntent`
- `MviUiState`
- `MviUiEffect`

Features use these contracts with `MviViewModel<I, S, E>`.

## What each part means

### Intent

An intent represents something the user or screen wants to do.

Examples:

- load data
- refresh data
- submit a form
- clear an error

### UiState

State represents the current screen model.

It should answer questions like:

- is the screen loading?
- what data should be shown?
- is there an error message to render?

### UiEffect

An effect represents a one-time event that should not be stored as durable screen state.

Typical examples:

- navigate to details
- show a toast or snackbar
- open a dialog

### ViewModel

`MviViewModel` is the coordinator.

It receives intents, calls domain logic, updates state with reducers, and emits one-time effects.

## Typical flow

```text
User action
→ Intent
→ ViewModel.handleIntent(...)
→ launchData(...)
→ UseCase
→ Result
→ setState(...) and/or sendEffect(...)
→ UI renders or reacts
```

## Why `launchData(...)` matters

The shared `launchData(...)` helper keeps async ViewModel work consistent.

Instead of repeating coroutine boilerplate in every feature, ViewModels can focus on:

- what starts loading
- what success looks like
- what failure means for the screen

## State vs effect

A simple rule of thumb:

- if the UI should still know it after recomposition, it is state
- if it should happen once and then be consumed, it is an effect

Examples:

| Concern | Use |
|---|---|
| loading spinner | state |
| list of items | state |
| form field contents | state |
| navigation event | effect |
| transient success message | effect |

## Error handling in MVI

Presentation code should surface safe, user-facing errors.

In practice that usually means:

- domain/data returns a failure wrapped in `Result`
- failures are mapped to `AppError`
- UI-facing text comes from `getUserMessage()`

The exact failure rules live in `.github/skills/error-handling.skill.md` and the core error utilities.

## Minimal example shape

```text
{Feature}Intent
{Feature}UiState
{Feature}UiEffect
{Feature}ViewModel
```

The exact naming and structure rules are intentionally kept in:

- `.github/skills/mvi-pattern.skill.md`
- `.github/skills/naming-conventions.skill.md`
- `.github/agents/presentation.agent.md`

## What good MVI looks like here

In this repository, a well-formed presentation layer usually has these qualities:

- intents are easy to read
- state is immutable and screen-focused
- effects are rare and purposeful
- ViewModels do orchestration, not business logic
- async work uses shared helpers instead of custom boilerplate

## Related reading

- `docs/architecture/clean-architecture.md`
- `docs/guides/how-to-create-feature.md`
- `.github/skills/mvi-pattern.skill.md`
- `.github/agents/presentation.agent.md`

