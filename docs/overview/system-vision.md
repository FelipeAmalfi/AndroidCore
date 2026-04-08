# System Vision

## Why AndroidCore exists

`AndroidCore` exists to make feature development more consistent.

Instead of rebuilding the same ViewModel patterns, error mapping, coroutine setup, and test helpers in every feature, the library provides a shared core that teams and agents can compose.

## The intended outcome

A new feature should feel predictable.

Developers should be able to move from requirement to implementation with:

- a known layer structure
- a stable MVI presentation pattern
- reusable domain contracts
- standardized data access conventions
- straightforward testing support

## Design goals

### 1. Reuse over reinvention

The `core/` package is meant to absorb common infrastructure so features can stay focused on domain behavior.

### 2. Clear boundaries

The project favors a clean separation between presentation, domain, and data responsibilities.

### 3. Human-friendly and agent-friendly

Humans should be able to understand the system quickly.
Agents should be able to generate work consistently by following `.github/skills/` and `.github/agents/`.

### 4. Documentation with clear ownership

The system works best when each layer of knowledge has one job:

- `docs/` for understanding
- `.github/skills/` for rules
- `.github/agents/` for execution

### 5. Safe evolution

The architecture should support new features, refactors, and tooling improvements without forcing every feature to change shape.

## How to think about the system

### The library layer

The library gives you shared primitives such as:

- MVI contracts and base ViewModel
- error and network helpers
- use case and result abstractions
- flow and coroutine helpers
- testing helpers

### The knowledge layer

The repository also contains guidance for how new feature code should be produced:

- `.github/skills/` tells you the constraints
- `.github/agents/` tells you who does what
- `docs/` tells you how to understand the whole picture

## What this project is not trying to be

This repository is not trying to turn `docs/` into a second copy of the rule system.

It also is not trying to keep long implementation checklists, migration logs, or copy-paste-heavy templates in the documentation layer.

Those details either belong in:

- code
- tests
- `.github/skills/`
- `.github/agents/`

## Success signals

The system is working as intended when:

- new features follow the same architecture without debate
- rules are updated once in `.github/skills/`
- agent responsibilities remain lightweight and focused
- docs stay short, useful, and readable
- onboarding does not require reading dozens of overlapping markdown files

## Recommended mental model

Think of the repository as three cooperating layers of knowledge:

```text
Docs      -> explains the system
Skills    -> defines the standards
Agents    -> applies the standards
Code      -> implements the product
```

When those responsibilities stay separate, the project remains easier to maintain.

