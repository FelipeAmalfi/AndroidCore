---
id: clean-architecture
type: skill
version: 1.0.0
schema_version: 1
updated_at: 2026-04-08
legacy_file: clean-architecture.skill
---

# clean-architecture

**Description:** Defines strict layer boundaries and dependency rules for Android feature generation.

## Rules
- Organize code into data, domain, and presentation layers with single responsibility per layer.
- Keep dependency direction inward: presentation -> domain, data -> domain contracts; domain never depends on data or Android UI.
- Keep domain code pure Kotlin with no Android framework imports.
- Keep business rules in use cases, data access in data sources/repositories, and UI orchestration in ViewModel.
- Respect package structure conventions for each layer to keep generated code discoverable.

## Examples
- presentation/user/viewmodel/UserViewModel.kt calls domain use cases only.
- domain/user/usecase/GetUserUseCase.kt validates input and returns Result.
- data/user/repository/UserRepositoryImpl.kt coordinates remote/local/cache sources.

