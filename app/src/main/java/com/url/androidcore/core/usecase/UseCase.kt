package com.url.androidcore.core.usecase

/**
 * Sealed class representing operation outcomes with type-safe result handling.
 *
 * Template for AI agents: Replicate this sealed class pattern for other result/response types
 * (QueryResult, MutationResult, NetworkResult, etc.)
 *
 * @param T Success data type
 */
sealed class Result<out T> {

    /**
     * Operation succeeded with data.
     * Agents template: Use for successful operation outcomes.
     */
    data class Success<T>(val data: T) : Result<T>()

    /**
     * Operation failed with error.
     * Agents template: Use for operation failures with error details.
     */
    data class Failure(val error: Throwable) : Result<Nothing>()

    companion object {
        fun <T> success(data: T): Result<T> = Success(data)
        fun <T> failure(error: Throwable): Result<T> = Failure(error)
    }
}

/**
 * Extension function for safe result mapping.
 * Agents template: Pattern for composing results without null checks.
 */
inline fun <T, R> Result<T>.map(transform: (T) -> R): Result<R> = when (this) {
    is Result.Success -> Result.Success(transform(data))
    is Result.Failure -> this
}

/**
 * Extension function for result chaining.
 * Agents template: Pattern for sequential operations.
 */
inline fun <T, R> Result<T>.flatMap(transform: (T) -> Result<R>): Result<R> = when (this) {
    is Result.Success -> transform(data)
    is Result.Failure -> this
}

/**
 * Extension function to get value or null.
 * Agents template: Safe extraction pattern.
 */
fun <T> Result<T>.getOrNull(): T? = (this as? Result.Success)?.data

/**
 * Extension function to get error or null.
 * Agents template: Error extraction pattern.
 */
fun <T> Result<T>.exceptionOrNull(): Throwable? = (this as? Result.Failure)?.error

/**
 * Extension function for side-effect handling.
 * Agents template: Pattern for operations with callback handlers.
 */
inline fun <T> Result<T>.fold(
    onSuccess: (T) -> Unit,
    onFailure: (Throwable) -> Unit
) {
    when (this) {
        is Result.Success -> onSuccess(data)
        is Result.Failure -> onFailure(error)
    }
}

/**
 * Functional interface for use cases with input parameter and result output.
 *
 * Template for AI agents: Replicate this interface pattern for domain operations
 * (GetUserUseCase : UseCase<UserId, User>, CreatePostUseCase : UseCase<PostInput, Post>)
 *
 * @param P Parameter/Request type (use Unit if no parameter needed)
 * @param R Result data type (wrapped in Result<R> for safe handling)
 */
fun interface UseCase<in P, out R> {

    /**
     * Execute the use case with given parameter.
     *
     * Should return Result.Success on completion or Result.Failure on error.
     * Never throws exceptions; always wraps in Result.
     */
    suspend operator fun invoke(param: P): Result<R>
}

/**
 * Extension function for use case execution with side-effect handling.
 * Agents template: Pattern for executing use cases with callbacks.
 */
suspend inline fun <P, R> UseCase<P, R>.execute(
    param: P,
    onSuccess: (R) -> Unit = {},
    onFailure: (Throwable) -> Unit = {}
) {
    invoke(param).fold(onSuccess, onFailure)
}

/**
 * Extension function for use case chaining.
 * Agents template: Pattern for composing multiple use cases.
 */
suspend inline fun <P, R, R2> UseCase<P, R>.andThen(
    param: P,
    nextUseCase: UseCase<R, R2>
): Result<R2> {
    return invoke(param).flatMap { result -> nextUseCase(result) }
}

