# Core Utilities Package Documentation

A production-ready Kotlin core utilities library for Android projects using Clean Architecture and MVI pattern.

## Overview

The core utilities package provides reusable coroutine and Flow utilities for safe, composable asynchronous operations without coupling to Android UI components (no Compose, Activity, or Fragment dependencies).

**Location:** `com.url.androidcore.core`

**Structure:**
```
core/
├── coroutines/      # Coroutine utilities
├── flow/            # Flow operators and state management
└── dispatchers/     # Dispatcher abstraction for DI
```

---

## 1. Dispatchers Package

**Purpose:** Provide dispatcher abstraction for dependency injection and testability.

### DispatcherProvider (Interface)

Abstracts coroutine dispatchers for easy testing and configuration.

```kotlin
interface DispatcherProvider {
    val io: CoroutineDispatcher
    val main: CoroutineDispatcher
    val default: CoroutineDispatcher
}
```

**Usage:**
```kotlin
class MyRepository(private val dispatcherProvider: DispatcherProvider) {
    suspend fun fetchData() = withContext(dispatcherProvider.io) {
        // Fetch data
    }
}
```

### DefaultDispatcherProvider (Implementation)

Production implementation using standard Dispatchers.

```kotlin
class DefaultDispatcherProvider : DispatcherProvider {
    override val io = Dispatchers.IO
    override val main = Dispatchers.Main
    override val default = Dispatchers.Default
}
```

---

## 2. Coroutines Package

**Purpose:** Provide safe, composable suspend functions for common coroutine patterns.

### retry()

Retries a suspend block with fixed delay between attempts. Respects `CancellationException`.

**Signature:**
```kotlin
suspend inline fun <T> retry(
    attempts: Int = 3,
    delayMillis: Long = 1000,
    block: suspend () -> T
): T
```

**Behavior:**
- Executes `block` up to `attempts` times
- On failure, waits `delayMillis` before retrying
- Throws `CancellationException` immediately without retry
- Throws last exception if all attempts fail

**Example:**
```kotlin
try {
    val data = retry(attempts = 3, delayMillis = 500) {
        apiService.fetchData()
    }
} catch (e: Exception) {
    // Handle error after 3 attempts
}
```

### retryWithExponentialBackoff()

Retries with exponential backoff: delay = `initialDelay × 2^attemptNumber` (capped at `maxDelayMillis`).

**Signature:**
```kotlin
suspend inline fun <T> retryWithExponentialBackoff(
    attempts: Int = 3,
    initialDelayMillis: Long = 1000,
    maxDelayMillis: Long = 30000,
    block: suspend () -> T
): T
```

**Example:**
```kotlin
val data = retryWithExponentialBackoff(
    attempts = 4,
    initialDelayMillis = 500,
    maxDelayMillis = 10000
) {
    apiService.fetchData()
}
// Delays: 500ms → 1000ms → 2000ms → fails
```

### withTimeoutSafe()

Wraps `withTimeout()` to return a fallback value instead of throwing exception.

**Signature:**
```kotlin
suspend inline fun <T> withTimeoutSafe(
    timeoutMillis: Long,
    fallback: T,
    block: suspend () -> T
): T
```

**Behavior:**
- Returns fallback value on `TimeoutCancellationException`
- Propagates other exceptions normally
- Does not throw timeout exception

**Example:**
```kotlin
val result = withTimeoutSafe(
    timeoutMillis = 5000,
    fallback = CachedData(),
    block = { apiService.fetchData() }
)
```

### parallel()

Runs two suspend blocks concurrently and returns results as a `Pair`.

**Signature:**
```kotlin
suspend inline fun <A, B> parallel(
    crossinline blockA: suspend () -> A,
    crossinline blockB: suspend () -> B
): Pair<A, B>
```

**Behavior:**
- Both blocks start immediately using `async`
- Waits for both to complete
- Throws first exception that occurs

**Example:**
```kotlin
val (users, posts) = parallel(
    { apiService.getUsers() },
    { apiService.getPosts() }
)
```

---

## 3. Flow Package

**Purpose:** Provide composable Flow operators and async state management.

### AsyncState

Sealed class for tracking async operation states.

**Type Definition:**
```kotlin
sealed class AsyncState<out T> {
    object Loading : AsyncState<Nothing>()
    data class Success<T>(val data: T) : AsyncState<T>()
    data class Error(val throwable: Throwable) : AsyncState<Nothing>()
    
    fun getOrNull(): T?
    inline fun onSuccess(block: (T) -> Unit): Unit
    inline fun onError(block: (Throwable) -> Unit): Unit
    inline fun onLoading(block: () -> Unit): Unit
}
```

**Usage:**
```kotlin
state.onLoading { showLoadingUI() }
state.onSuccess { data -> updateUI(data) }
state.onError { error -> showError(error) }
```

### debounceLatest()

Emits the most recent value after a period of inactivity.

**Signature:**
```kotlin
fun <T> Flow<T>.debounceLatest(timeoutMillis: Long): Flow<T>
```

**Example:**
```kotlin
searchInput.debounceLatest(300).collect { query ->
    // Only called 300ms after user stops typing
}
```

### throttleFirst()

Emits first value within time window; ignores subsequent values until window resets.

**Signature:**
```kotlin
fun <T> Flow<T>.throttleFirst(timeoutMillis: Long): Flow<T>
```

**Example:**
```kotlin
buttonClicks.throttleFirst(1000).collect { 
    // Click events limited to at most 1 per second
}
```

### collectIn()

Collects Flow within a CoroutineScope without blocking the current coroutine.

**Signature:**
```kotlin
fun <T> Flow<T>.collectIn(
    scope: CoroutineScope,
    action: suspend (T) -> Unit
)
```

**Example:**
```kotlin
dataFlow.collectIn(viewModelScope) { data ->
    updateState(data)
}
```

### collectLatestIn()

Collects Flow using `collectLatest` within a CoroutineScope (cancels previous action on new value).

**Signature:**
```kotlin
fun <T> Flow<T>.collectLatestIn(
    scope: CoroutineScope,
    action: suspend (T) -> Unit
)
```

**Example:**
```kotlin
searchResults.collectLatestIn(viewModelScope) { results ->
    // Previous action cancelled if new value arrives
    updateSearchResults(results)
}
```

### retryWithDelay()

Retries Flow collection with fixed delay between attempts.

**Signature:**
```kotlin
fun <T> Flow<T>.retryWithDelay(
    attempts: Int = 3,
    delayMillis: Long = 1000
): Flow<T>
```

**Behavior:**
- Respects `CancellationException`
- Only retries on regular exceptions
- Limited to specified number of attempts

**Example:**
```kotlin
apiFlow()
    .retryWithDelay(attempts = 3, delayMillis = 1000)
    .collect { data -> process(data) }
```

### asAsyncState()

Maps `Flow<T>` into `Flow<AsyncState<T>>` with automatic Loading and Error handling.

**Signature:**
```kotlin
fun <T> Flow<T>.asAsyncState(): Flow<AsyncState<T>>
```

**Behavior:**
- Emits `AsyncState.Loading` on subscription
- Emits `AsyncState.Success(value)` for each value
- Emits `AsyncState.Error(exception)` on exception
- Does not rethrow exceptions

**Example:**
```kotlin
val dataState: Flow<AsyncState<Data>> = apiFlow()
    .asAsyncState()

dataState.collectIn(viewModelScope) { state ->
    when (state) {
        is AsyncState.Loading -> setState { copy(isLoading = true) }
        is AsyncState.Success -> setState { copy(data = state.data, isLoading = false) }
        is AsyncState.Error -> setState { copy(error = state.throwable, isLoading = false) }
    }
}
```

---

## Integration with Existing Code

### Using with launchData

The existing `launchData` function remains in `com.url.androidcore.utils.CoroutineExt` and is NOT duplicated.

```kotlin
// In MviViewModel
protected fun <T> launchData(
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
    onStart: (S.() -> S)? = null,
    onSuccess: (S.(T) -> S),
    onError: (S.(Throwable) -> S)? = null,
    onCompletion: (S.() -> S)? = null,
    block: suspend () -> T
)
```

You can now enhance it with core utilities:

```kotlin
// Retry API call with exponential backoff
protected fun <T> launchDataWithRetry(
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
    onSuccess: (S.(T) -> S),
    onError: (S.(Throwable) -> S)? = null,
    block: suspend () -> T
) {
    launchData(
        dispatcher = dispatcher,
        onSuccess = onSuccess,
        onError = onError,
        block = {
            retryWithExponentialBackoff(attempts = 3) {
                block()
            }
        }
    )
}
```

---

## Best Practices

1. **Use DispatcherProvider for DI:**
   ```kotlin
   class MyRepository(private val dispatcherProvider: DispatcherProvider) {
       // Easy to test with custom dispatcher provider
   }
   ```

2. **Prefer parallel() over manual async/await:**
   ```kotlin
   // Good
   val (a, b) = parallel({ getA() }, { getB() })
   
   // Avoid manual boilerplate
   val a = async { getA() }
   val b = async { getB() }
   ```

3. **Use AsyncState for UI state management:**
   ```kotlin
   val uiState: StateFlow<AsyncState<Data>> = dataFlow
       .asAsyncState()
       .stateIn(viewModelScope, SharingStarted.Eagerly, AsyncState.Loading)
   ```

4. **Combine operators for complex flows:**
   ```kotlin
   searchInput
       .debounceLatest(300)
       .map { query -> query.trim() }
       .filter { it.isNotEmpty() }
       .distinctUntilChanged()
       .asAsyncState()
       .retryWithDelay(attempts = 3, delayMillis = 500)
   ```

5. **Handle CancellationException properly:**
   - All retry functions automatically respect `CancellationException`
   - No special handling needed for scope cancellation

---

## Dependencies

Built entirely on standard Kotlin/Coroutines libraries:
- `kotlinx-coroutines-core` (already in your project)
- No additional dependencies added

---

## Testing

Test with custom `DispatcherProvider` implementation:

```kotlin
class TestDispatcherProvider : DispatcherProvider {
    override val io = Dispatchers.Unconfined
    override val main = Dispatchers.Unconfined
    override val default = Dispatchers.Unconfined
}

@Test
fun testMyRepository() {
    val repository = MyRepository(TestDispatcherProvider())
    // Run tests synchronously
}
```

---

## Summary

This core utilities package provides:

✅ **Type-safe** coroutine helpers
✅ **Composable** Flow operators
✅ **Testable** dispatcher abstraction
✅ **Production-ready** error handling
✅ **Zero duplication** of existing `launchData`
✅ **No UI coupling** - pure coroutine logic
✅ **Idiomatic Kotlin** - extension functions, generics
✅ **Clean Architecture** - follows MVI and dependency injection patterns

