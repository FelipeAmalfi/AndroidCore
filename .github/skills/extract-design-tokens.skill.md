---
id: extract-design-tokens
type: skill
version: 1.0.0
schema_version: 1
updated_at: 2026-04-10
---

# extract-design-tokens

**Description:** Extract design tokens from local design-system code and optional MCP token exports.

## Rules
- Parse colors from `DSColors.kt`.
- Parse spacing defaults from `DSSpacing.kt`.
- Parse typography defaults from `DSTypography.kt`.
- Optionally merge MCP token payload when configured.
- Keep output deterministic and source-attributed.

