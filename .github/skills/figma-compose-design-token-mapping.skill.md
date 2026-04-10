---
id: figma-compose-design-token-mapping
type: skill
version: 1.0.0
schema_version: 1
updated_at: 2026-04-09
legacy_file: figma-compose-design-token-mapping.skill
---

# figma-compose-design-token-mapping

**Description:** Aligns design tokens from Figma inputs with Compose theme and style usage.

## Rules
- Map colors to `MaterialTheme.colorScheme` roles before introducing literal color values.
- Map typography to `MaterialTheme.typography` styles with extension points for missing roles.
- Map spacing, radius, and elevation to centralized token objects when available.
- Keep token mapping deterministic and document any temporary fallback constants.
- Never embed token decisions in domain/data layers; keep them in presentation UI artifacts.

## Examples
- Token `color.primary.500` -> `MaterialTheme.colorScheme.primary`.
- Token `type.heading.lg` -> `MaterialTheme.typography.headlineMedium`.

