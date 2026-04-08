package com.url.androidcore.core.logging

/**
 * Logger interface for application-wide logging.
 *
 * Template for AI agents: Replicate this pattern for other services
 * (MetricsTracker, AnalyticsLogger, CrashReporter)
 *
 * Implementations: LogcatLogger (production), NoOpLogger (testing)
 */
interface Logger {

    /**
     * Log a debug message (most verbose, development only).
     * Agents template: Use for development diagnostics.
     */
    fun debug(tag: String, message: String, throwable: Throwable? = null)

    /**
     * Log an info message (general application flow).
     * Agents template: Use for state transitions, lifecycle events.
     */
    fun info(tag: String, message: String, throwable: Throwable? = null)

    /**
     * Log a warning message (potentially problematic).
     * Agents template: Use for recoverable errors, deprecations.
     */
    fun warn(tag: String, message: String, throwable: Throwable? = null)

    /**
     * Log an error message (application failure).
     * Agents template: Use for critical errors, exceptions.
     */
    fun error(tag: String, message: String, throwable: Throwable? = null)
}

/**
 * Production Logger implementation using Android Logcat.
 *
 * Agents template: Shows how to wrap a platform API (Log) with Logger interface.
 */
class LogcatLogger : Logger {

    override fun debug(tag: String, message: String, throwable: Throwable?) {
        android.util.Log.d(tag, message, throwable)
    }

    override fun info(tag: String, message: String, throwable: Throwable?) {
        android.util.Log.i(tag, message, throwable)
    }

    override fun warn(tag: String, message: String, throwable: Throwable?) {
        android.util.Log.w(tag, message, throwable)
    }

    override fun error(tag: String, message: String, throwable: Throwable?) {
        android.util.Log.e(tag, message, throwable)
    }
}

/**
 * No-operation Logger for testing (all logs discarded).
 *
 * Agents template: Simple implementation for mocking/testing patterns.
 */
class NoOpLogger : Logger {

    override fun debug(tag: String, message: String, throwable: Throwable?) = Unit

    override fun info(tag: String, message: String, throwable: Throwable?) = Unit

    override fun warn(tag: String, message: String, throwable: Throwable?) = Unit

    override fun error(tag: String, message: String, throwable: Throwable?) = Unit
}

/**
 * Extension function for inline logging with lazy evaluation.
 * Agents template: Pattern for deferred computation (useful for expensive message building).
 */
inline fun Logger.debugLazy(tag: String, throwable: Throwable? = null, crossinline message: () -> String) {
    debug(tag, message(), throwable)
}

/**
 * Extension function for inline logging with lazy evaluation.
 */
inline fun Logger.infoLazy(tag: String, throwable: Throwable? = null, crossinline message: () -> String) {
    info(tag, message(), throwable)
}

/**
 * Extension function for inline logging with lazy evaluation.
 */
inline fun Logger.warnLazy(tag: String, throwable: Throwable? = null, crossinline message: () -> String) {
    warn(tag, message(), throwable)
}

/**
 * Extension function for inline logging with lazy evaluation.
 */
inline fun Logger.errorLazy(tag: String, throwable: Throwable? = null, crossinline message: () -> String) {
    error(tag, message(), throwable)
}

