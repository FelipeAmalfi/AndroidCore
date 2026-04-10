---
id: extract-kotlin-components
type: skill
version: 1.0.0
schema_version: 1
updated_at: 2026-04-10
---

# extract-kotlin-components

**Description:** Extract Compose component contracts from Kotlin source.

## Rules
- Parse `@Composable` function declarations.
- Extract parameters (name, type, default value).
- Detect preview functions annotated with `@Preview`.
- Detect state-like parameters (`state`, `uiState`, `*State`).
- Output stable, path-relative metadata.

