---
id: figma-compose-fallback-rules
type: skill
version: 1.0.0
schema_version: 1
updated_at: 2026-04-09
legacy_file: figma-compose-fallback-rules.skill
---

# figma-compose-fallback-rules

**Description:** Provides deterministic fallback behavior when Figma metadata is missing, conflicting, or unsupported.

## Rules
- If layout intent is missing, default to a simple `Column` with token-based spacing.
- If token mapping is missing, fallback to safe Material3 defaults and annotate with TODO markers.
- If component type is unknown, render with `Box` and placeholder content plus explicit assumption notes.
- If interaction details are incomplete, emit non-interactive UI skeletons and add extension hooks.
- If constraints conflict, prioritize readability and compilation safety over visual fidelity.

## Examples
- Unknown icon source -> `Icon(imageVector = Icons.Default.Help, contentDescription = null)`.
- Missing text style -> `MaterialTheme.typography.bodyMedium` with TODO note.

