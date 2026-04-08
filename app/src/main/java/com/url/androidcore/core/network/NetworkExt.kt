package com.url.androidcore.core.network

import com.url.androidcore.core.error.AppError
import com.url.androidcore.core.usecase.Result
import kotlinx.coroutines.CancellationException

/**
 * Safe API call wrapper that catches exceptions and maps them to AppError.
 *
 * Template for AI agents: Use this pattern for all network operations.
 * Extends to other external service calls (payment, analytics, etc.)
 *
 * Usage:
 * ```kotlin
 * val result: Result<UserDto> = safeApiCall {
 *     apiService.getUser(id)
 * }
 * ```
 *
 * @param T Success data type
 * @param block Suspend function performing the API call
 * @return Result<T> containing Success(data) or Failure(error)
 */
suspend inline fun <T> safeApiCall(crossinline block: suspend () -> T): Result<T> = try {
    Result.Success(block())
} catch (e: CancellationException) {
    throw e // Always propagate cancellation
} catch (e: Exception) {
    Result.Failure(e.toNetworkError())
}

/**
 * Maps exceptions to network-specific AppError with retry logic.
 *
 * Template for AI agents: Extend for protocol-specific errors (HTTP, gRPC, WebSocket, etc.)
 *
 * @return AppError.Network with status code and retry hints
 */
fun Exception.toNetworkError(): AppError.Network {
    return when (this) {
        is AppError.Network -> this
        else -> AppError.Network(
            message = this.message ?: "Network request failed",
            statusCode = null,
            cause = this,
            isRetryable = isRetryableException(this)
        )
    }
}

/**
 * Determine if exception should trigger retry.
 *
 * Agents template: Extend for domain-specific retry policies.
 */
fun isRetryableException(exception: Exception): Boolean = when (exception) {
    is java.net.SocketTimeoutException -> true
    is java.net.ConnectException -> true
    is java.io.IOException -> true
    else -> false
}

/**
 * Result wrapper for API responses with optional data transformation.
 *
 * Agents template: Pattern for response mapping pipelines.
 *
 * @param T API response type
 * @param R Mapped result type
 * @param transform Function to map API response to domain model
 * @return Result<R> with transformation applied
 */
suspend inline fun <T, R> safeApiCall(
    crossinline block: suspend () -> T,
    crossinline transform: (T) -> R
): Result<R> = safeApiCall(block).map { transform(it) }

/**
 * Result wrapper with error handler callback.
 *
 * Agents template: Pattern for logging/analytics on API failures.
 *
 * @param onError Callback invoked on failure with error details
 */
suspend inline fun <T> safeApiCallWithHandler(
    onError: (AppError) -> Unit = {},
    crossinline block: suspend () -> T
): Result<T> = safeApiCall(block).also { result ->
    (result as? Result.Failure)?.error?.let { error ->
        if (error is AppError.Network) {
            onError(error)
        }
    }
}

/**
 * HTTP status code mapper to AppError with retry hints.
 *
 * Agents template: Extend for protocol-specific status codes.
 */
fun mapHttpStatusToError(statusCode: Int, message: String): AppError.Network {
    val isRetryable = statusCode in 408..429 || statusCode in 500..599
    val errorMessage = when (statusCode) {
        400 -> "Bad request: $message"
        401 -> "Unauthorized"
        403 -> "Forbidden"
        404 -> "Not found"
        409 -> "Conflict"
        429 -> "Too many requests"
        500 -> "Server error"
        502 -> "Bad gateway"
        503 -> "Service unavailable"
        504 -> "Gateway timeout"
        else -> message.takeIf { it.isNotEmpty() } ?: "HTTP $statusCode"
    }
    return AppError.Network(
        message = errorMessage,
        statusCode = statusCode,
        isRetryable = isRetryable
    )
}

