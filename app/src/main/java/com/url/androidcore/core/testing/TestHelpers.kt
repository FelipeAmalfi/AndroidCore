package com.url.androidcore.core.testing

import com.url.androidcore.core.connectivity.NetworkMonitor
import com.url.androidcore.core.dispatchers.DispatcherProvider
import com.url.androidcore.core.logging.Logger
import com.url.androidcore.core.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Test implementation of DispatcherProvider using Dispatchers.Unconfined.
 *
 * Agents template: Test implementation pattern for all services.
 * Use in tests to run coroutines synchronously.
 */
class TestDispatcherProvider : DispatcherProvider {
    override val io = Dispatchers.Unconfined
    override val main = Dispatchers.Unconfined
    override val default = Dispatchers.Unconfined
}

/**
 * Fake Logger for testing with message recording.
 *
 * Agents template: Test fake pattern matching Logger interface.
 * Captures all log calls for assertion in tests.
 *
 * Usage:
 * ```kotlin
 * val fakeLogger = FakeLogger()
 * myService.logger = fakeLogger
 * myService.doSomething()
 *
 * assertThat(fakeLogger.debugMessages).contains("expected message")
 * ```
 */
class FakeLogger : Logger {

    val debugMessages = mutableListOf<String>()
    val infoMessages = mutableListOf<String>()
    val warnMessages = mutableListOf<String>()
    val errorMessages = mutableListOf<String>()

    override fun debug(tag: String, message: String, throwable: Throwable?) {
        debugMessages.add("[$tag] $message")
    }

    override fun info(tag: String, message: String, throwable: Throwable?) {
        infoMessages.add("[$tag] $message")
    }

    override fun warn(tag: String, message: String, throwable: Throwable?) {
        warnMessages.add("[$tag] $message")
    }

    override fun error(tag: String, message: String, throwable: Throwable?) {
        errorMessages.add("[$tag] $message")
    }

    /**
     * Get all logged messages across all levels.
     */
    fun getAllMessages(): List<String> = debugMessages + infoMessages + warnMessages + errorMessages

    /**
     * Clear all recorded messages.
     */
    fun clear() {
        debugMessages.clear()
        infoMessages.clear()
        warnMessages.clear()
        errorMessages.clear()
    }
}

/**
 * Fake NetworkMonitor for testing.
 *
 * Agents template: Test fake pattern matching NetworkMonitor interface.
 * Allows test control over network state.
 *
 * Usage:
 * ```kotlin
 * val fakeMonitor = FakeNetworkMonitor(initialConnected = true)
 * myService.networkMonitor = fakeMonitor
 *
 * fakeMonitor.setConnected(false)  // Simulate network disconnect
 * ```
 */
class FakeNetworkMonitor(initialConnected: Boolean = true) : NetworkMonitor {

    private val _isConnected = MutableStateFlow(initialConnected)

    override fun isConnected(): Flow<Boolean> = _isConnected.asStateFlow()

    override suspend fun getCurrentStatus(): Boolean = _isConnected.value

    /**
     * Test helper to set network connectivity status.
     */
    fun setConnected(connected: Boolean) {
        _isConnected.value = connected
    }
}

/**
 * Fake Repository for testing.
 *
 * Agents template: Test fake pattern matching Repository interface.
 * Base class for domain-specific fake repositories.
 *
 * Usage:
 * ```kotlin
 * class FakeUserRepository : FakeRepository(), UserRepository {
 *     fun setUsers(users: List<User>) { ... }
 * }
 * ```
 */
open class FakeRepository : Repository {
    // Base fake repository for extension
}

/**
 * Test helpers for AsyncState assertion.
 *
 * Agents template: Assertion pattern for sealed class testing.
 * Use in Flow/StateFlow tests to verify state transitions.
 *
 * Usage:
 * ```kotlin
 * val state = AsyncState.Success(data)
 * state.assertIsSuccess { data ->
 *     assertThat(data.id).isEqualTo(expectedId)
 * }
 * ```
 */
inline fun <T> com.url.androidcore.core.flow.AsyncState<T>.assertIsLoading() {
    if (this !is com.url.androidcore.core.flow.AsyncState.Loading) {
        throw AssertionError("Expected AsyncState.Loading but was $this")
    }
}

inline fun <T> com.url.androidcore.core.flow.AsyncState<T>.assertIsSuccess(block: (T) -> Unit) {
    val state = this as? com.url.androidcore.core.flow.AsyncState.Success
        ?: throw AssertionError("Expected AsyncState.Success but was $this")
    block(state.data)
}

inline fun <T> com.url.androidcore.core.flow.AsyncState<T>.assertIsError(block: (Throwable) -> Unit) {
    val state = this as? com.url.androidcore.core.flow.AsyncState.Error
        ?: throw AssertionError("Expected AsyncState.Error but was $this")
    block(state.throwable)
}

/**
 * Test helpers for Result assertion.
 *
 * Agents template: Assertion pattern for Result type testing.
 * Use in UseCase/Repository tests to verify outcomes.
 *
 * Usage:
 * ```kotlin
 * val result = myUseCase(input)
 * result.assertIsSuccess { data ->
 *     assertThat(data.id).isEqualTo(expectedId)
 * }
 * ```
 */
inline fun <T> com.url.androidcore.core.usecase.Result<T>.assertIsSuccess(block: (T) -> Unit) {
    val result = this as? com.url.androidcore.core.usecase.Result.Success
        ?: throw AssertionError("Expected Result.Success but was $this")
    block(result.data)
}

inline fun <T> com.url.androidcore.core.usecase.Result<T>.assertIsFailure(block: (Throwable) -> Unit) {
    val result = this as? com.url.androidcore.core.usecase.Result.Failure
        ?: throw AssertionError("Expected Result.Failure but was $this")
    block(result.error)
}

/**
 * Test helper to verify error is specific type.
 *
 * Agents template: Type-based assertion pattern.
 *
 * Usage:
 * ```kotlin
 * result.assertIsFailure { error ->
 *     error.assertIsInstanceOf<AppError.Network>()
 * }
 * ```
 */
inline fun <reified T : Throwable> Throwable.assertIsInstanceOf() {
    if (this !is T) {
        throw AssertionError("Expected ${T::class.simpleName} but was ${this::class.simpleName}: $this")
    }
}

