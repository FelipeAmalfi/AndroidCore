---
id: documentation-governance
type: skill
version: 1.0.0
schema_version: 1
updated_at: 2026-04-08
legacy_file: documentation-governance.skill
---

# documentation-governance

**Description:** Rules for keeping skills as source-of-truth and agents as orchestration-only artifacts.

## Rules
- Keep normative rules in `.skill.md` files; agents reference and apply skills instead of restating rules.
- Keep each skill focused on one concept and reusable across multiple agents.
- Keep agents single-responsibility and lightweight with explicit skill dependencies.
- Maintain consistent naming across skills, agents, generated files, and package paths.
- Preserve traceability by linking generated outputs to the skills used.

## Examples
- test.agent.md references testing-patterns and error-handling without redefining assertions.
- presentation.agent.md references mvi-pattern and naming-conventions for contract generation.

