---
id: error-handling
type: skill
version: 1.0.0
schema_version: 1
updated_at: 2026-04-08
legacy_file: error-handling.skill
---

# error-handling

**Description:** Unified error modeling, mapping, and user-facing error behaviors.

## Rules
- Represent failures with AppError hierarchy (Network, Database, Validation, Unknown) or Result failure channels.
- Map thrown exceptions through Throwable.toAppError or safeApiCall mappings before returning to upper layers.
- Do not leak raw transport exceptions to presentation; expose user-safe messages through getUserMessage.
- Attach retryability semantics to network failures and use them to drive retry strategy.
- Keep validation errors explicit with field-level context when available.

## Examples
- val result = safeApiCall { service.getUser(id) }
- Result.failure(AppError.Validation("Email is required", "email", input.email))
- val message = error.toAppError().getUserMessage()

