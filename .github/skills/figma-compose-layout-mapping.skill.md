---
id: figma-compose-layout-mapping
type: skill
version: 1.0.0
schema_version: 1
updated_at: 2026-04-09
legacy_file: figma-compose-layout-mapping.skill
---

# figma-compose-layout-mapping

**Description:** Standardizes conversion of design layout structures into Compose container skeletons.

## Rules
- Map vertical/horizontal auto-layout intent to `Column` and `Row` with explicit arrangement and alignment.
- Map overlap/absolute positioning intent to `Box` with scoped alignment or offset notes.
- Map wrapping intent (chips/tags/multi-line horizontal flow) to `FlowRow` when Figma indicates wrap behavior.
- Preserve spacing semantics using token-driven values before hardcoded dimensions.
- Map sizing constraints explicitly:
  - fixed -> `Modifier.width/height/size(...)`
  - hug -> `wrapContentWidth`/`wrapContentHeight` or intrinsic measurement fallback
  - fill -> `fillMaxWidth`/`fillMaxHeight` or `weight` on the active axis
- Represent scrolling intent with `LazyColumn`, `LazyRow`, or `Modifier.verticalScroll` only when required by design behavior.
- Document unresolved size constraints (fixed, wrap, fill) as TODO notes in generated code.

## Examples
- Vertical list frame -> `LazyColumn` with `verticalArrangement = Arrangement.spacedBy(...)`.
- Header + floating action badge -> `Box` with aligned child composables.


