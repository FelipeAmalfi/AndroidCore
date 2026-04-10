# androidcore-figma-compose

Use these rules when generating Compose from Figma MCP design data:

- Read selected Figma nodes with hierarchy, layout, style, and variant metadata before mapping.
- Map auto layout horizontal to `Row`, vertical to `Column`, and overlays to `Box`.
- Map wrapping structures (chips/tags) to `FlowRow` when wrap intent is explicit.
- Preserve spacing, padding, alignment, and sizing constraints through Compose modifiers.
- Map fixed/hug/fill constraints deterministically:
  - fixed -> `Modifier.width/height/size(...)`
  - hug -> `wrapContentWidth`/`wrapContentHeight`
  - fill -> `fillMaxWidth`/`fillMaxHeight` or `weight`
- Map text styles to `MaterialTheme.typography` first, then explicit `TextStyle` when needed.
- Map fills/strokes/radius/shadows to Compose style primitives with token-friendly constants.
- Prefer reusable, stateless composables and extract repeated child patterns into subcomposables.
- Use `modifier: Modifier = Modifier` as the first optional UI parameter.
- Keep output Compose-only, avoid business logic, and avoid inventing interactions not present in design.
- Add TODO comments only when design data is missing or ambiguous.

Fallback and reporting requirements:

- Missing tokens: generate named local constants and include migration notes to design tokens.
- Unsupported visual effects: use closest safe Compose approximation and report the approximation.
- Ambiguous constraints: choose deterministic compile-safe defaults instead of speculative behavior.
- Unknown assets: expose image/icon slots or placeholders; do not hardcode fake assets.
- Append a concise fallback report listing unresolved node ids/names, approximations, and next metadata required.


