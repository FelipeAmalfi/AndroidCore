---
id: figma-compose-reusability
type: skill
version: 1.0.0
schema_version: 1
updated_at: 2026-04-09
legacy_file: figma-compose-reusability.skill
---

# figma-compose-reusability

**Description:** Extracts repeated visual patterns into reusable Compose components without losing fidelity.

## Rules
- Detect repeated child subtrees (same structure and near-identical styles) and extract a subcomposable.
- Prefer stateless APIs: pass state through parameters and expose event lambdas when interactive.
- Keep composables focused: one component per visual responsibility.
- Use `modifier: Modifier = Modifier` as first optional UI parameter.
- Collapse repeated primitive values into named constants or token references.

## Component Variant Guidance
- If Figma variants are explicit, map to a sealed/config parameter (for example `ButtonVariant`).
- Keep variant APIs small and exhaustive; avoid booleans that combine into ambiguous states.
- Share base layout implementation and switch style fragments by variant.

## Extraction Threshold
- Extract when repetition appears 2+ times and reuse improves clarity.
- Do not extract one-off wrappers that reduce readability.
- Preserve local readability with concise component names tied to design intent.

