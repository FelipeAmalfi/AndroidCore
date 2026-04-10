---
id: figma-mcp-reading
type: skill
version: 1.0.0
schema_version: 1
updated_at: 2026-04-09
legacy_file: figma-mcp-reading.skill
---

# figma-mcp-reading

**Description:** Rules for reading and normalizing Figma node data from MCP before Compose generation.

## Rules
- Read the minimum complete scope first: selected frame/component, descendants, and shared style references.
- Preserve node identity (`id`, `name`, `type`) to keep traceability from generated code back to design.
- Normalize values into explicit units and enums before mapping (for example layout mode, alignments, spacing).
- Prefer explicit design values over inferred values; inference is a fallback, not the default.
- Keep extraction deterministic: same input tree should produce equivalent normalized output.

## Required Node Data
- hierarchy: parent/children order and depth
- layout: auto layout direction, spacing, padding, alignment, sizing mode/constraints
- visual style: fills, strokes, corner radius, effects, opacity
- typography: font family, weight, size, line height, letter spacing, text alignment
- assets: image/icon references and component/variant metadata when available

## Validation Checklist
- Ensure every rendered node has a mapped container/text/image role.
- Flag missing or partial metadata early for fallback handling.
- Collapse hidden nodes unless hidden state is explicitly needed.
- Record ambiguous constraints for TODO-safe output only.

