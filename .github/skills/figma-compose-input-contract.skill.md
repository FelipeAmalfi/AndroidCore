---
id: figma-compose-input-contract
type: skill
version: 1.0.0
schema_version: 1
updated_at: 2026-04-09
legacy_file: figma-compose-input-contract.skill
---

# figma-compose-input-contract

**Description:** Defines the minimum design payload and assumptions required before Figma-to-Compose generation starts.

## Rules
- Require a feature name and target package path before creating any files.
- Accept input as either Figma node metadata, design-spec text, or manually described component tree.
- Require basic layout metadata: direction, spacing, alignment, and size behavior where available.
- Capture token hints for color, typography, spacing, shape, and elevation with explicit defaults when absent.
- Record unresolved design ambiguities as explicit assumptions in output notes.

## Examples
- Input includes "ProfileCard" with vertical stack, 16dp spacing, and primary title text style.
- Input includes a button state matrix (default, pressed, disabled) for variant mapping.

