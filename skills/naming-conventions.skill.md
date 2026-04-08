---
id: naming-conventions
type: skill
version: 1.0.0
schema_version: 1
updated_at: 2026-04-08
legacy_file: naming-conventions.skill
---

# naming-conventions

**Description:** Standard naming rules for generated Android architecture artifacts.

## Rules
- Use PascalCase feature names and apply suffix patterns consistently.
- Use {Feature}Intent, {Feature}UiState, {Feature}UiEffect, and {Feature}ViewModel for presentation contracts.
- Use {Action}{Feature}UseCase for domain operations and {Feature}Repository for domain contracts.
- Use {Feature}Service, {Feature}Dto, {Feature}DataSource, and {Feature}RepositoryImpl for data layer artifacts.
- Keep mapper file names as {Feature}Mapper.kt with extension function names toDomain and toUi.

## Examples
- UserIntent, UserUiState, UserUiEffect, UserViewModel
- GetUserUseCase, UpdateUserUseCase
- UserService, UserDto, UserDataSourceImpl, UserRepositoryImpl

