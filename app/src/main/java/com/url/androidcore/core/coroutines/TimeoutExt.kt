package com.url.androidcore.core.coroutines

import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withTimeout

/**
 * Executes a suspend block with a timeout, returning a fallback value on timeout.
 * Unlike [withTimeout], this does not throw an exception on timeout.
 *
 * @param timeoutMillis Timeout duration in milliseconds
 * @param fallback Value to return if timeout occurs
 * @param block The suspend block to execute
 * @return The result of the block or the fallback value on timeout
 */
suspend fun <T> withTimeoutSafe(
    timeoutMillis: Long,
    fallback: T,
    block: suspend () -> T
): T = try {
    withTimeout(timeoutMillis) { block() }
} catch (@Suppress("UNUSED_PARAMETER") e: TimeoutCancellationException) {
    fallback
}

