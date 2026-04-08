package com.url.androidcore.core.connectivity

import kotlinx.coroutines.flow.Flow

/**
 * Monitor for network connectivity status.
 *
 * Template for AI agents: Replicate this pattern for other system state monitors
 * (BatteryMonitor, LocationMonitor, BluetoothMonitor, etc.)
 *
 * Provides reactive Flow<Boolean> for network state changes.
 * true = connected, false = disconnected
 *
 * Usage:
 * ```kotlin
 * networkMonitor.isConnected().collect { isOnline ->
 *     if (isOnline) {
 *         // Sync pending operations
 *     } else {
 *         // Show offline UI
 *     }
 * }
 * ```
 */
interface NetworkMonitor {

    /**
     * Observe network connectivity status.
     *
     * Emits:
     * - true when connected (WiFi, cellular, or other)
     * - false when disconnected
     *
     * @return Flow<Boolean> reactive stream of connectivity status
     */
    fun isConnected(): Flow<Boolean>

    /**
     * Get current connectivity status synchronously.
     *
     * Use sparingly; prefer Flow for reactive updates.
     */
    suspend fun getCurrentStatus(): Boolean
}

