---
id: presentation-agent
type: agent
version: 1.0.0
schema_version: 1
updated_at: 2026-04-08
legacy_file: presentation.agent
---

# presentation-agent

**Description:** Generates MVI presentation contracts and ViewModel orchestration for a feature.

## Skills
- `documentation-governance`
- `agent-workflow`
- `clean-architecture`
- `mvi-pattern`
- `naming-conventions`
- `mapping-rules`
- `error-handling`
- `coroutine-flow-helpers`
- `core-usage`

## Instructions
- Consume domain use cases and mapper outputs with UI interaction requirements.
- Generate Intent, UiState, UiEffect, and ViewModel following MVI contracts.
- Route async work through launchData and update immutable state with reducer-style copy calls.
- Emit one-time UiEffect events for navigation and transient messaging.
- Return state/effect transition notes and open assumptions for test generation.

