package com.url.androidcore.core.coroutines

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay
import kotlin.math.pow

/**
 * Retries a suspend block with a fixed delay between attempts.
 * Only retries on Exception, not on CancellationException.
 *
 * @param attempts Total number of attempts (minimum 1)
 * @param delayMillis Delay in milliseconds between retry attempts
 * @param block The suspend block to execute
 * @return The result of the block
 * @throws The last exception if all attempts fail
 */
suspend inline fun <T> retry(
    attempts: Int = 3,
    delayMillis: Long = 1000,
    block: suspend () -> T
): T {
    require(attempts >= 1) { "attempts must be at least 1" }
    
    var lastException: Exception? = null
    repeat(attempts) { attempt ->
        try {
            return block()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            lastException = e
            if (attempt < attempts - 1) {
                delay(delayMillis)
            }
        }
    }
    throw lastException ?: IllegalStateException("retry failed with unknown error")
}

/**
 * Retries a suspend block with exponential backoff.
 * Only retries on Exception, not on CancellationException.
 * 
 * Delay calculation: baseDelayMillis * (2 ^ attemptNumber)
 *
 * @param attempts Total number of attempts (minimum 1)
 * @param initialDelayMillis Initial delay in milliseconds for the first retry
 * @param maxDelayMillis Maximum delay cap in milliseconds
 * @param block The suspend block to execute
 * @return The result of the block
 * @throws The last exception if all attempts fail
 */
suspend inline fun <T> retryWithExponentialBackoff(
    attempts: Int = 3,
    initialDelayMillis: Long = 1000,
    maxDelayMillis: Long = 30000,
    block: suspend () -> T
): T {
    require(attempts >= 1) { "attempts must be at least 1" }
    require(initialDelayMillis > 0) { "initialDelayMillis must be positive" }
    
    var lastException: Exception? = null
    repeat(attempts) { attempt ->
        try {
            return block()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            lastException = e
            if (attempt < attempts - 1) {
                val exponentialDelay = (initialDelayMillis * (2.0.pow(attempt))).toLong()
                val delayMillis = minOf(exponentialDelay, maxDelayMillis)
                delay(delayMillis)
            }
        }
    }
    throw lastException ?: IllegalStateException("retry failed with unknown error")
}

