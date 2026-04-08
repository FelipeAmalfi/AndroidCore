package com.url.androidcore.core.flow

/**
 * Sealed class representing the state of an asynchronous operation.
 * Used as a wrapper for Flow<T> to track loading, success, and error states.
 */
sealed class AsyncState<out T> {
    /**
     * Represents the initial loading state of an operation.
     */
    object Loading : AsyncState<Nothing>()

    /**
     * Represents successful completion with data.
     *
     * @param data The successful result
     */
    data class Success<T>(val data: T) : AsyncState<T>()

    /**
     * Represents failure with an exception.
     *
     * @param throwable The exception that occurred
     */
    data class Error(val throwable: Throwable) : AsyncState<Nothing>()

    /**
     * Extracts the value if state is Success, otherwise returns null.
     */
    fun getOrNull(): T? = (this as? Success)?.data

    /**
     * Executes a block if state is Success.
     */
    inline fun onSuccess(block: (T) -> Unit) {
        if (this is Success) block(data)
    }

    /**
     * Executes a block if state is Error.
     */
    inline fun onError(block: (Throwable) -> Unit) {
        if (this is Error) block(throwable)
    }

    /**
     * Executes a block if state is Loading.
     */
    inline fun onLoading(block: () -> Unit) {
        if (this is Loading) block()
    }
}

