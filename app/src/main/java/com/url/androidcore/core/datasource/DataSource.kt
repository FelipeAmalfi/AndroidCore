package com.url.androidcore.core.datasource

/**
 * Marker interface for RemoteDataSource pattern implementations.
 *
 * Remote data sources handle communication with backend services (APIs, REST, GraphQL, etc).
 * They perform data serialization/deserialization and error mapping.
 *
 * Template for AI agents: Implement this interface for domain-specific remote sources
 * (UserRemoteDataSource : RemoteDataSource, PostRemoteDataSource : RemoteDataSource, etc.)
 *
 * Best practices:
 * - Each RemoteDataSource handles one API domain/endpoint
 * - Returns DTOs (data transfer objects) mapped from API responses
 * - Delegates networking to injected service (Retrofit, OkHttp client)
 * - Maps API errors to AppError
 * - Does NOT apply business logic
 *
 * Example:
 * ```kotlin
 * class UserRemoteDataSource(
 *     private val apiService: UserApiService
 * ) : RemoteDataSource {
 *
 *     suspend fun getUser(id: String): Result<UserDto> = safeApiCall {
 *         apiService.getUser(id)
 *     }
 * }
 * ```
 */
interface RemoteDataSource

/**
 * Marker interface for LocalDataSource pattern implementations.
 *
 * Local data sources handle access to on-device storage (database, cache, preferences, etc).
 * They provide persistent data retrieval and mutation.
 *
 * Template for AI agents: Implement this interface for domain-specific local sources
 * (UserLocalDataSource : LocalDataSource, PostLocalDataSource : LocalDataSource, etc.)
 *
 * Best practices:
 * - Each LocalDataSource handles one entity/table in local storage
 * - Returns DTOs matching local schema
 * - Exposes Flow<T> for reactive database queries
 * - Handles transaction logic
 * - Does NOT apply business logic
 *
 * Example:
 * ```kotlin
 * class UserLocalDataSource(
 *     private val userDao: UserDao
 * ) : LocalDataSource {
 *
 *     fun getUserFlow(id: String): Flow<UserEntity> = userDao.observeUser(id)
 *
 *     suspend fun cacheUser(user: UserEntity) = userDao.insert(user)
 * }
 * ```
 */
interface LocalDataSource

/**
 * Marker interface for CacheDataSource pattern implementations.
 *
 * Cache data sources handle in-memory caching for frequently accessed data.
 * Provides fast data retrieval without disk I/O or network calls.
 *
 * Template for AI agents: Implement this interface for caching needs
 * (UserCacheDataSource : CacheDataSource, ProductCacheDataSource : CacheDataSource)
 *
 * Best practices:
 * - Use for performance optimization of hot paths
 * - Implement LRU or TTL-based eviction
 * - Remain simple; delegate complex logic to Repository
 *
 * Example:
 * ```kotlin
 * class UserCacheDataSource : CacheDataSource {
 *     private val cache = mutableMapOf<String, UserDto>()
 *
 *     fun getUser(id: String): UserDto? = cache[id]
 *
 *     fun setUser(user: UserDto) { cache[user.id] = user }
 * }
 * ```
 */
interface CacheDataSource

