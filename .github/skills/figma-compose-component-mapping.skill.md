---
id: figma-compose-component-mapping
type: skill
version: 1.0.0
schema_version: 1
updated_at: 2026-04-09
legacy_file: figma-compose-component-mapping.skill
---

# figma-compose-component-mapping

**Description:** Maps common Figma component semantics to idiomatic Compose UI primitives and wrappers.

## Rules
- Prefer Material3 composables first (`Text`, `Button`, `Icon`, `Card`, `Surface`) unless input specifies custom behavior.
- Convert design variants into explicit state parameters instead of branching on hardcoded labels.
- Keep generated composables stateless by default and hoist state when interaction is required.
- Name generated composables using feature-first naming conventions aligned with existing project rules.
- Flag unsupported interactions (gesture combinations, timeline animations) for fallback handling.

## Examples
- Figma "Primary Button / Disabled" -> `PrimaryButton(enabled = false, ...)`.
- Component set with size variants -> `Avatar(size: AvatarSize, ...)`.

