---
id: figma-compose-fallbacks
type: skill
version: 1.0.0
schema_version: 1
updated_at: 2026-04-09
legacy_file: figma-compose-fallbacks.skill
---

# figma-compose-fallbacks

**Description:** Safe fallback behavior for unsupported, missing, or ambiguous Figma data during Compose generation.

## Fallback Rules
- Missing tokens: generate named local constants and mark migration path to design tokens.
- Unsupported effects: apply closest safe Compose equivalent and add a single TODO explaining the gap.
- Ambiguous constraints: choose deterministic defaults (`wrapContent` + documented assumption) instead of guessing dynamic behavior.
- Deep nesting: preserve hierarchy only where visual order depends on it; otherwise flatten wrappers.
- Placeholder-worthy elements (unknown image/icon assets): generate slots/placeholders, never hardcode fake assets.

## Safety Constraints
- Never invent gestures, transitions, or navigation not present in design metadata.
- Avoid hidden side effects and non-UI logic in generated composables.
- Keep TODO comments concise and actionable, including the missing Figma detail.

## Reporting
- Append a short fallback report with each generation containing:
  - unresolved node ids/names
  - approximation decisions
  - placeholders introduced
  - next metadata needed for full fidelity

