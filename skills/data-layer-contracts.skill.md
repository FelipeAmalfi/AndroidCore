---
id: data-layer-contracts
type: skill
version: 1.0.0
schema_version: 1
updated_at: 2026-04-08
legacy_file: data-layer-contracts.skill
---

# data-layer-contracts

**Description:** Data layer generation rules for service, DTO, datasource, and repository implementation artifacts.

## Rules
- Generate API service interfaces, DTOs, data source contracts/implementations, and data repository implementations.
- Keep data layer free of business rules; limit responsibilities to transport, persistence, caching, and data retrieval.
- Use safe network calls and map failures to Result/AppError.
- Use Flow for stream-based reads and suspend functions for single write/read operations.
- Implement repository fallback strategy (cache -> remote -> local) when connectivity and requirements call for it.

## Examples
- data/user/remote/UserService.kt
- data/user/datasource/UserDataSource.kt and UserDataSourceImpl.kt
- data/user/repository/UserRepositoryImpl.kt

