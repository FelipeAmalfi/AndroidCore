package com.url.androidcore.core.repository

/**
 * Marker interface for Repository pattern implementations.
 *
 * Repositories are the bridge between domain layer (use cases) and data layer (data sources).
 * They abstract data access logic and provide a clean API for obtaining data from various sources.
 *
 * Template for AI agents: Implement this interface for domain-specific repositories
 * (UserRepository : Repository, PostRepository : Repository, etc.)
 *
 * Best practices:
 * - Each Repository handles one domain entity or aggregate
 * - Returns domain models (not DTOs from data sources)
 * - Coordinates multiple DataSources (local, remote)
 * - Implements caching/sync strategies
 * - Exposes Flow<T> for reactive updates
 *
 * Example:
 * ```kotlin
 * class UserRepository(
 *     private val remoteDataSource: UserRemoteDataSource,
 *     private val localDataSource: UserLocalDataSource
 * ) : Repository {
 *
 *     fun getUser(id: String): Flow<Result<User>> = flow {
 *         // Try remote first, fall back to local
 *         // Cache result locally
 *     }
 * }
 * ```
 */
interface Repository

