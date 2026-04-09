# Copilot Instructions

Follow `AGENTS.md` in the repository root as the primary project-specific guidance.

## AndroidCore integration intent
- This project consumes `AndroidCore` and should reuse its architecture primitives instead of recreating them.
- Prefer AndroidCore MVI, use case, error, network, coroutine, Flow, and testing helpers where applicable.
- Preserve Clean Architecture direction: presentation -> domain, data -> domain contracts.

## Feature generation workflow
When generating features, move in this order:
1. data layer
2. domain layer
3. mapper layer
4. presentation layer
5. tests
6. enhancement work only if needed

## Local files to consult first
- `AGENTS.md`
- `docs/androidcore/androidcore-guidance.md`

## Important limitation
AndroidCore may expose runtime documentation APIs, but Copilot follows the files available in this repository. Keep the AndroidCore guidance copied or synced here if you want it applied consistently.

