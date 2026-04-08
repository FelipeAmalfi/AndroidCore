package com.url.androidcore.core.flow

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.retryWhen
import kotlinx.coroutines.launch
import kotlin.time.DurationUnit
import kotlin.time.toDuration

/**
 * Emits the most recent value after a period of inactivity.
 * Wrapper around the standard [debounce] operator.
 *
 * @param timeoutMillis Debounce timeout in milliseconds
 * @return Debounced Flow
 */
fun <T> Flow<T>.debounceLatest(timeoutMillis: Long): Flow<T> =
    debounce(timeoutMillis.toDuration(DurationUnit.MILLISECONDS))

/**
 * Emits the first value within a time window, ignoring subsequent values until window resets.
 * Throttles emissions to at most one per time window.
 *
 * @param timeoutMillis Throttle window in milliseconds
 * @return Throttled Flow
 */
fun <T> Flow<T>.throttleFirst(timeoutMillis: Long): Flow<T> =
    flow {
        var lastEmissionTime = 0L
        this@throttleFirst.collect { value ->
            val currentTime = System.currentTimeMillis()
            if (currentTime - lastEmissionTime >= timeoutMillis) {
                emit(value)
                lastEmissionTime = currentTime
            }
        }
    }


/**
 * Collects this Flow within a CoroutineScope.
 *
 * @param scope The CoroutineScope to launch the collection in
 * @param action Action to perform on each emitted value
 */
fun <T> Flow<T>.collectIn(
    scope: CoroutineScope,
    action: suspend (T) -> Unit
) {
    scope.launch {
        collect(action)
    }
}

/**
 * Collects this Flow within a CoroutineScope using collectLatest.
 * If a new value is emitted before the previous action completes, the previous action is cancelled.
 *
 * @param scope The CoroutineScope to launch the collection in
 * @param action Action to perform on each emitted value
 */
fun <T> Flow<T>.collectLatestIn(
    scope: CoroutineScope,
    action: suspend (T) -> Unit
) {
    scope.launch {
        collectLatest(action)
    }
}

/**
 * Retries the Flow with a fixed delay between attempts on any exception.
 * Only retries on Exception, not on CancellationException.
 *
 * @param attempts Maximum number of retry attempts
 * @param delayMillis Delay in milliseconds between retries
 * @return Flow that retries with delay on failure
 */
fun <T> Flow<T>.retryWithDelay(
    attempts: Int = 3,
    delayMillis: Long = 1000
): Flow<T> {
    require(attempts >= 1) { "attempts must be at least 1" }
    return retryWhen { cause, attempt ->
        if (cause is CancellationException || attempt >= attempts - 1) {
            false
        } else {
            delay(delayMillis)
            true
        }
    }
}

/**
 * Maps a Flow<T> into Flow<AsyncState<T>>, handling loading, success, and error states.
 * Emits:
 * - AsyncState.Loading on subscription
 * - AsyncState.Success<T> on each value
 * - AsyncState.Error on exception
 *
 * @return Flow emitting AsyncState values
 */
fun <T> Flow<T>.asAsyncState(): Flow<AsyncState<T>> =
    this
        .map { value -> AsyncState.Success(value) as AsyncState<T> }
        .onStart { emit(AsyncState.Loading) }
        .catch { exception ->
            emit(AsyncState.Error(exception) as AsyncState<T>)
        }








