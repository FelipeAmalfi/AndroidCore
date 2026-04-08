---
id: enhancement-patterns
type: skill
version: 1.0.0
schema_version: 1
updated_at: 2026-04-08
legacy_file: enhancement-patterns.skill
---

# enhancement-patterns

**Description:** Optional architecture-safe optimizations such as cache, retry, and transformation pipelines.

## Rules
- Add enhancements without breaking existing contracts or layer boundaries.
- Keep changes backward compatible and minimally invasive.
- Integrate cache and retry at data source or repository level, not in presentation.
- Document enhancement behavior, invalidation strategy, and fallback order.
- Verify performance/resilience gains with targeted tests.

## Examples
- Add UserCache with TTL and invalidation on refresh.
- Wrap remote calls with retryWithExponentialBackoff.
- Add pagination/filtering in repository query pipeline.

