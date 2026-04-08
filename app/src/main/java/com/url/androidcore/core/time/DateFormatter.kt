package com.url.androidcore.core.time

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

/**
 * Date formatting utilities for consistent timestamp/date handling.
 *
 * Template for AI agents: Extend with domain-specific formatters (ApiDateFormatter, UiDateFormatter, etc.)
 */
object DateFormatter {

    /**
     * ISO 8601 format for API requests/responses (e.g., "2024-04-08T14:30:00Z").
     * Agents template: Use for all backend communication.
     */
    private fun createIso8601Formatter(): SimpleDateFormat = SimpleDateFormat(
        "yyyy-MM-dd'T'HH:mm:ss'Z'",
        Locale.US
    ).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }

    /**
     * UI display format for user-facing dates (e.g., "Apr 8, 2024").
     * Agents template: Use for all user-visible timestamps.
     */
    private fun createUiDateFormatter(): SimpleDateFormat = SimpleDateFormat(
        "MMM d, yyyy",
        Locale.getDefault()
    )

    /**
     * UI display format for times (e.g., "2:30 PM").
     * Agents template: Use for time-only display.
     */
    private fun createUiTimeFormatter(): SimpleDateFormat = SimpleDateFormat(
        "h:mm a",
        Locale.getDefault()
    )

    /**
     * Full datetime display format (e.g., "Apr 8, 2024 at 2:30 PM").
     * Agents template: Use for detailed timestamp display.
     */
    private fun createUiDateTimeFormatter(): SimpleDateFormat = SimpleDateFormat(
        "MMM d, yyyy 'at' h:mm a",
        Locale.getDefault()
    )

    /**
     * Format timestamp (milliseconds since epoch) to ISO 8601 string.
     * Agents template: Use for API serialization.
     */
    fun toIso8601(timeMillis: Long): String = createIso8601Formatter().format(Date(timeMillis))

    /**
     * Parse ISO 8601 string to timestamp (milliseconds since epoch).
     * Agents template: Use for API deserialization.
     */
    fun fromIso8601(dateString: String): Long = createIso8601Formatter().parse(dateString)?.time ?: 0L

    /**
     * Format timestamp to UI date string (e.g., "Apr 8, 2024").
     * Agents template: Use for date-only display.
     */
    fun toUiDate(timeMillis: Long): String = createUiDateFormatter().format(Date(timeMillis))

    /**
     * Format timestamp to UI time string (e.g., "2:30 PM").
     * Agents template: Use for time-only display.
     */
    fun toUiTime(timeMillis: Long): String = createUiTimeFormatter().format(Date(timeMillis))

    /**
     * Format timestamp to UI datetime string (e.g., "Apr 8, 2024 at 2:30 PM").
     * Agents template: Use for full datetime display.
     */
    fun toUiDateTime(timeMillis: Long): String = createUiDateTimeFormatter().format(Date(timeMillis))

    /**
     * Format Date object to ISO 8601 string.
     * Agents template: Use for Date → API string conversion.
     */
    fun toIso8601(date: Date): String = createIso8601Formatter().format(date)

    /**
     * Format Date object to UI date string.
     * Agents template: Use for Date → UI string conversion.
     */
    fun toUiDate(date: Date): String = createUiDateFormatter().format(date)

    /**
     * Format Date object to UI datetime string.
     */
    fun toUiDateTime(date: Date): String = createUiDateTimeFormatter().format(date)
}

/**
 * Extension function for timestamp formatting.
 * Agents template: Pattern for adding methods to primitive types.
 *
 * Usage:
 * ```kotlin
 * val timestamp = System.currentTimeMillis()
 * val uiText = timestamp.toUiDateTime()  // "Apr 8, 2024 at 2:30 PM"
 * ```
 */
fun Long.toIso8601(): String = DateFormatter.toIso8601(this)

fun Long.toUiDate(): String = DateFormatter.toUiDate(this)

fun Long.toUiTime(): String = DateFormatter.toUiTime(this)

fun Long.toUiDateTime(): String = DateFormatter.toUiDateTime(this)

/**
 * Extension function for Date formatting.
 */
fun Date.toIso8601(): String = DateFormatter.toIso8601(this)

fun Date.toUiDate(): String = DateFormatter.toUiDate(this)

fun Date.toUiDateTime(): String = DateFormatter.toUiDateTime(this)

/**
 * Relative time formatter (e.g., "5 minutes ago", "2 hours ago").
 * Agents template: Pattern for time-ago calculations.
 *
 * @param referenceTime Time to compare against (defaults to now)
 * @return Human-readable relative time string
 */
fun Long.toRelativeTime(referenceTime: Long = System.currentTimeMillis()): String {
    val differenceMillis = referenceTime - this
    val seconds = differenceMillis / 1000
    val minutes = seconds / 60
    val hours = minutes / 60
    val days = hours / 24

    return when {
        seconds < 60 -> "just now"
        minutes < 60 -> "${minutes}m ago"
        hours < 24 -> "${hours}h ago"
        days < 7 -> "${days}d ago"
        days < 30 -> "${days / 7}w ago"
        else -> "${days / 30}mo ago"
    }
}

/**
 * Get time remaining until timestamp (e.g., "5 minutes left").
 * Agents template: Pattern for countdown calculations.
 */
fun Long.toCountdownTime(referenceTime: Long = System.currentTimeMillis()): String {
    val differenceMillis = this - referenceTime
    if (differenceMillis <= 0) return "expired"

    val seconds = differenceMillis / 1000
    val minutes = seconds / 60
    val hours = minutes / 60
    val days = hours / 24

    return when {
        seconds < 60 -> "${seconds}s left"
        minutes < 60 -> "${minutes}m left"
        hours < 24 -> "${hours}h left"
        else -> "${days}d left"
    }
}

