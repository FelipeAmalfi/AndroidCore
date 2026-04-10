---
id: figma-compose-style-mapping
type: skill
version: 1.0.0
schema_version: 1
updated_at: 2026-04-09
legacy_file: figma-compose-style-mapping.skill
---

# figma-compose-style-mapping

**Description:** Maps typography and visual properties from Figma into Compose style constructs.

## Typography
- Map text styles to `TextStyle` values or theme typography references first.
- Convert font size/line height/letter spacing to `sp` with precise rounding.
- Preserve weight and alignment; use nearest supported `FontWeight` when needed.
- Prefer semantic text tokens (`titleLarge`, `bodyMedium`) when naming implies a known role.

## Color and Tokens
- Map fills/strokes to `Color(...)` or project token references when available.
- Prefer semantic names for extracted constants (for example `PrimarySurface`, `OnPrimary`).
- Avoid repeating raw hex values across multiple composables; centralize reused values.

## Shape, Border, and Effects
- Corner radius -> `RoundedCornerShape(...)`.
- Stroke -> `Modifier.border(...)` with mapped width/color/shape.
- Single drop shadow -> `Modifier.shadow(...)` when equivalent.
- Complex multi-layer effects -> fallback comments + closest safe Compose approximation.

## Asset and Icon Handling
- Vector/icon nodes -> composable icon slot or painter resource placeholder.
- Image fills -> explicit image slot API or painter placeholder with content scale notes.
- Keep unsupported blend modes as TODO-only notes; do not fake visual complexity.

