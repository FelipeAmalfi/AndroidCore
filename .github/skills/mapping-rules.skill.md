---
id: mapping-rules
type: skill
version: 1.0.0
schema_version: 1
updated_at: 2026-04-08
legacy_file: mapping-rules.skill
---

# mapping-rules

**Description:** Rules for deterministic transformations between DTO, domain, and UI models.

## Rules
- Implement mappings as extension functions, not mapper classes.
- Keep mapping explicit and side-effect free; no business logic in mappers.
- Use canonical conversion names: Dto.toDomain(), Domain.toUi(), and Dto.toUi() only when needed.
- Keep type boundaries clear: DTO types remain in data layer, domain models in domain, UI models in presentation.
- Ensure mapping coverage for all fields and nullability constraints.

## Examples
- fun UserDto.toDomain(): User
- fun User.toUi(): UserUiModel
- fun UserDto.toUi(): UserUiModel = toDomain().toUi()

