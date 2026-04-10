# extract-kotlin-components

## Goal
Extract Compose component contracts from Kotlin source files.

## Rules
- Parse `@Composable` functions.
- Extract function name and parameter list (name, type, default value when present).
- Detect preview functions annotated with `@Preview`.
- Detect state-like parameters (`state`, `uiState`, `*State`).
- Keep output deterministic and path-relative to repository root.

