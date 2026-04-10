---
id: figma-compose-codegen
type: skill
version: 1.0.0
schema_version: 1
updated_at: 2026-04-09
legacy_file: figma-compose-codegen.skill
---

# figma-compose-codegen

**Description:** Produces production-quality Jetpack Compose Kotlin code from mapped design structures.

## Output Contract
- Generate Kotlin Compose only with clear package/import sections.
- Keep public API minimal and readable; private helpers for internal composition.
- Use predictable naming (`{Feature}{Component}Section`, `{Feature}Card`, `{Feature}Chip`).
- Include previews when they help quick visual checks.
- Keep formatting stable and idiomatic.

## Compose Standards
- Use Material 3 primitives when appropriate.
- Keep UI logic light; avoid business/domain coupling.
- Add accessibility-minded defaults (`contentDescription`, semantic roles) when inferable.
- Prefer immutable parameters and stable data contracts.

## Output Sections
- Main reusable composables.
- Internal subcomposables extracted for repeated blocks.
- Constants/token references for repeated style values.
- Optional `@Preview` examples.
- Assumption notes with TODOs only for unresolved design details.

