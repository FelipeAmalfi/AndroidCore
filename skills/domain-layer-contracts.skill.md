---
id: domain-layer-contracts
type: skill
version: 1.0.0
schema_version: 1
updated_at: 2026-04-08
legacy_file: domain-layer-contracts.skill
---

# domain-layer-contracts

**Description:** Domain layer rules for repository contracts, models, and use cases.

## Rules
- Generate domain repository interfaces independent of data-layer implementations.
- Model business operations as focused use cases, one responsibility per use case.
- Keep domain models framework-agnostic and pure Kotlin.
- Return Result and Flow according to operation semantics; avoid nullable control flow where Result is available.
- Encode validation and business invariants in the domain layer before calling repositories.

## Examples
- domain/user/repository/UserRepository.kt
- domain/user/model/User.kt
- domain/user/usecase/GetUserUseCase.kt

