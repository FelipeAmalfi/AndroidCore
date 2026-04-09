---
id: agent-workflow
type: skill
version: 1.0.0
schema_version: 1
updated_at: 2026-04-08
legacy_file: agent-workflow.skill
---

# agent-workflow

**Description:** Defines the end-to-end execution order, required inputs, and validation gates for feature generation.

## Rules
- Start every feature with a complete specification: feature name, API endpoints, models, business rules, and UI interactions.
- Execute generation in order: data-layer -> domain -> mapper -> presentation -> test -> di -> enhancement (optional).
- Pass outputs from each phase as explicit inputs to the next phase.
- Validate after each phase: file creation, imports, compile readiness, naming consistency, and architecture compliance.
- Do not move to the next phase when current phase contracts are incomplete.
- Run the di-agent after tests to wire all interface/implementation pairs with Hilt modules and apply @Inject constructor annotations.

## Examples
- Data layer outputs UserDto and UserRepositoryImpl before domain creates UserRepository and use cases.
- Mapper phase consumes UserDto plus UserModel before presentation uses toUi mappings.
- DI phase consumes all generated interfaces and implementations to produce UserDataModule and annotate UserViewModel with @HiltViewModel.

