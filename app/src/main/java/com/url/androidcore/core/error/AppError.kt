package com.url.androidcore.core.error

/**
 * Sealed class representing application errors with domain-specific subtypes.
 *
 * Template for AI agents: Replicate this sealed class pattern for other error domains
 * (ValidationError, PaymentError, PermissionError, etc.)
 *
 * @see Throwable.toAppError for exception mapping
 */
sealed class AppError(
    open val message: String = "",
    open val cause: Throwable? = null
) : Throwable(message, cause) {

    /**
     * Network-related errors (HTTP, timeout, connectivity).
     * Agents template: Use for any remote service failures.
     */
    data class Network(
        override val message: String = "Network error",
        val statusCode: Int? = null,
        override val cause: Throwable? = null,
        val isRetryable: Boolean = statusCode?.let { it in 408..429 || it in 500..599 } ?: true
    ) : AppError(message, cause)

    /**
     * Database-related errors (query, transaction, constraints).
     * Agents template: Use for any local persistence failures.
     */
    data class Database(
        override val message: String = "Database error",
        override val cause: Throwable? = null,
        val isFatal: Boolean = false
    ) : AppError(message, cause)

    /**
     * Validation errors (input validation, business rules).
     * Agents template: Use for any constraint violations.
     */
    data class Validation(
        override val message: String = "Validation error",
        val fieldName: String = "",
        val fieldValue: Any? = null
    ) : AppError(message, null)

    /**
     * Unknown/unmapped errors.
     * Agents template: Default case for unclassified exceptions.
     */
    data class Unknown(
        override val message: String = "Unknown error",
        override val cause: Throwable? = null
    ) : AppError(message, cause)

    companion object {
        fun network(message: String = "Network error", statusCode: Int? = null, cause: Throwable? = null) =
            Network(message, statusCode, cause)

        fun database(message: String = "Database error", cause: Throwable? = null, isFatal: Boolean = false) =
            Database(message, cause, isFatal)

        fun validation(message: String, fieldName: String = "", fieldValue: Any? = null) =
            Validation(message, fieldName, fieldValue)

        fun unknown(message: String = "Unknown error", cause: Throwable? = null) =
            Unknown(message, cause)
    }
}

/**
 * Extension function to map Throwable to AppError.
 *
 * Template for AI agents: Create similar mappers for other exception types
 * (e.g., ApiError.toAppError(), DatabaseException.toAppError())
 */
fun Throwable.toAppError(): AppError = when (this) {
    is AppError -> this
    else -> AppError.unknown(
        message = this.message ?: "An unexpected error occurred",
        cause = this
    )
}

/**
 * Check if error is retryable (for network operations).
 * Agents template: Extend this for domain-specific retry logic.
 */
fun AppError.isRetryable(): Boolean = when (this) {
    is AppError.Network -> this.isRetryable
    is AppError.Database -> !this.isFatal
    else -> false
}

/**
 * User-friendly error message extraction.
 * Agents template: Extend for localization, fallback messages, etc.
 */
fun AppError.getUserMessage(): String = when (this) {
    is AppError.Network -> this.message.takeIf { it.isNotEmpty() } ?: "Network unavailable"
    is AppError.Database -> "Data access failed"
    is AppError.Validation -> this.message.takeIf { it.isNotEmpty() } ?: "Invalid input"
    is AppError.Unknown -> "Something went wrong"
}

