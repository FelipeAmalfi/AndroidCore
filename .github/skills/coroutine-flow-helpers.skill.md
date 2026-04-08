---
id: coroutine-flow-helpers
type: skill
version: 1.0.0
schema_version: 1
updated_at: 2026-04-08
legacy_file: coroutine-flow-helpers.skill
---

# coroutine-flow-helpers

**Description:** Coroutine and Flow utility usage patterns for resilient asynchronous code.

## Rules
- Use DispatcherProvider abstraction for IO/Main/Default dispatchers to keep code testable.
- Use Flow for reactive streams and suspend functions for single-shot operations.
- Use standardized helpers for retry, timeout, and parallel execution instead of ad-hoc coroutine code.
- Respect CancellationException propagation and do not suppress cancellation in retry logic.
- Use Flow operators (debounce/throttle/retry/asAsyncState/collectIn) to model UI and data streams predictably.

## Examples
- retryWithExponentialBackoff { apiCall() }
- withTimeoutSafe(timeoutMillis = 5000, fallback = cachedData) { fetchRemote() }
- searchFlow.debounceLatest(300).asAsyncState()

