---
id: figma-compose-output-contract
type: skill
version: 1.0.0
schema_version: 1
updated_at: 2026-04-09
legacy_file: figma-compose-output-contract.skill
---

# figma-compose-output-contract

**Description:** Defines required output artifacts and metadata produced by the Figma-to-Compose generation flow.

## Rules
- Always produce a file manifest listing generated paths and ownership by layer.
- Produce at least one Compose screen/container skeleton and one reusable component file when applicable.
- Emit an assumptions section describing unresolved mappings and fallback decisions.
- Include integration notes for presentation-layer wiring (MVI state/events hooks) without generating business logic.
- Include validation checklist items for naming, imports, compile readiness, and architecture boundaries.

## Examples
- Output bundle includes `ProfileUi.kt`, `ProfileComponents.kt`, and `ProfileMappingNotes.md`.
- Output notes include unresolved design tokens and proposed follow-up actions.

