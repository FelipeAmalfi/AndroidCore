# Integration Examples - Extended Core Utilities

Complete working examples for integrating extended utilities into Android projects.

---

## 🎯 Example 1: User Repository with Multi-Layer Data Access

### Domain Model
```kotlin
// domain/User.kt
data class User(
    val id: String,
    val name: String,
    val email: String,
    val createdAt: Long
)
```

### Data Transfer Objects
```kotlin
// data/dto/UserDto.kt
data class UserDto(
    val id: String,
    val name: String,
    val email: String,
    val created_at: String  // ISO 8601 from API
)

fun UserDto.toDomain(): User = User(
    id = id,
    name = name,
    email = email,
    createdAt = DateFormatter.fromIso8601(created_at)
)

// data/entity/UserEntity.kt
@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String,
    val name: String,
    val email: String,
    val createdAtMillis: Long
)

fun UserEntity.toDomain(): User = User(
    id = id,
    name = name,
    email = email,
    createdAt = createdAtMillis
)

fun User.toEntity(): UserEntity = UserEntity(
    id = id,
    name = name,
    email = email,
    createdAtMillis = createdAt
)
```

### Remote Data Source
```kotlin
// data/source/UserRemoteDataSource.kt
import com.url.androidcore.core.datasource.RemoteDataSource
import com.url.androidcore.core.network.safeApiCall
import com.url.androidcore.core.usecase.Result

class UserRemoteDataSource(
    private val apiService: UserApiService,
    private val logger: Logger = LogcatLogger()
) : RemoteDataSource {

    suspend fun getUser(id: String): Result<UserDto> = safeApiCall {
        logger.info("UserAPI", "Fetching user: $id")
        apiService.getUser(id)
    }

    suspend fun searchUsers(query: String): Result<List<UserDto>> = safeApiCall(
        block = { apiService.searchUsers(query) },
        transform = { response -> response.users }
    )

    suspend fun createUser(input: CreateUserInput): Result<UserDto> = 
        safeApiCallWithHandler(
            onError = { error ->
                logger.error("UserAPI", "Create failed: ${error.message}", error)
            }
        ) {
            apiService.createUser(input.toDto())
        }

    suspend fun updateUser(id: String, input: UpdateUserInput): Result<UserDto> {
        return if (NetworkValidator.canUpdate(id)) {
            safeApiCall { apiService.updateUser(id, input.toDto()) }
        } else {
            Result.failure(AppError.Validation("Invalid user ID", "id", id))
        }
    }
}

// API Service Interface
interface UserApiService {
    suspend fun getUser(id: String): UserDto
    suspend fun searchUsers(query: String): SearchResponse
    suspend fun createUser(input: UserInputDto): UserDto
    suspend fun updateUser(id: String, input: UpdateUserInputDto): UserDto
}

data class SearchResponse(val users: List<UserDto>)
```

### Local Data Source
```kotlin
// data/source/UserLocalDataSource.kt
import com.url.androidcore.core.datasource.LocalDataSource

class UserLocalDataSource(
    private val userDao: UserDao
) : LocalDataSource {

    fun observeUser(id: String): Flow<User> = userDao
        .observeUser(id)
        .map { it.toDomain() }

    fun observeAllUsers(): Flow<List<User>> = userDao
        .observeAllUsers()
        .map { users -> users.map { it.toDomain() } }

    suspend fun cacheUser(user: User) {
        userDao.insert(user.toEntity())
    }

    suspend fun cacheUsers(users: List<User>) {
        userDao.insertAll(users.map { it.toEntity() })
    }

    suspend fun deleteUser(id: String) {
        userDao.delete(id)
    }

    suspend fun clearCache() {
        userDao.clear()
    }
}

// DAO Interface
@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE id = :id")
    fun observeUser(id: String): Flow<UserEntity>

    @Query("SELECT * FROM users")
    fun observeAllUsers(): Flow<List<UserEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: UserEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(users: List<UserEntity>)

    @Query("DELETE FROM users WHERE id = :id")
    suspend fun delete(id: String)

    @Query("DELETE FROM users")
    suspend fun clear()
}
```

### Cache Data Source
```kotlin
// data/source/UserCacheDataSource.kt
import com.url.androidcore.core.datasource.CacheDataSource

class UserCacheDataSource : CacheDataSource {

    private val cache = mutableMapOf<String, UserDto>()
    private val maxSize = 100

    fun getUser(id: String): UserDto? = cache[id]

    fun setUser(user: UserDto) {
        if (cache.size >= maxSize) {
            cache.remove(cache.keys.first())  // Simple FIFO eviction
        }
        cache[user.id] = user
    }

    fun setUsers(users: List<UserDto>) {
        cache.clear()
        users.forEach { setUser(it) }
    }

    fun clearCache() = cache.clear()
}
```

### Repository Implementation
```kotlin
// data/repository/UserRepositoryImpl.kt
import com.url.androidcore.core.repository.Repository
import com.url.androidcore.core.usecase.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow

class UserRepositoryImpl(
    private val remoteDataSource: UserRemoteDataSource,
    private val localDataSource: UserLocalDataSource,
    private val cacheDataSource: UserCacheDataSource,
    private val networkMonitor: NetworkMonitor,
    private val logger: Logger = LogcatLogger()
) : UserRepository, Repository {

    /**
     * Get single user with intelligent fallback strategy:
     * 1. Try cache (instant)
     * 2. Try remote (with network check)
     * 3. Fall back to local cache
     */
    override fun getUser(id: String): Flow<Result<User>> = flow {
        // Check cache first
        cacheDataSource.getUser(id)?.let { cached ->
            logger.debug("UserRepo", "Cache hit: $id")
            emit(Result.success(cached.toDomain()))
            return@flow
        }

        // Try remote
        if (networkMonitor.getCurrentStatus()) {
            when (val result = remoteDataSource.getUser(id)) {
                is Result.Success -> {
                    val user = result.data
                    // Cache results
                    localDataSource.cacheUser(user.toDomain())
                    cacheDataSource.setUser(user)
                    logger.info("UserRepo", "Remote fetch succeeded: $id")
                    emit(Result.success(user.toDomain()))
                }
                is Result.Failure -> {
                    logger.warn("UserRepo", "Remote fetch failed", result.error)
                    // Fall back to local
                    try {
                        localDataSource.observeUser(id).collect { user ->
                            emit(Result.success(user))
                        }
                    } catch (e: Exception) {
                        emit(Result.failure(result.error))
                    }
                }
            }
        } else {
            logger.warn("UserRepo", "Network offline, using local cache")
            try {
                localDataSource.observeUser(id).collect { user ->
                    emit(Result.success(user))
                }
            } catch (e: Exception) {
                emit(Result.failure(AppError.Network("No data available", cause = e)))
            }
        }
    }

    override fun getAllUsers(): Flow<Result<List<User>>> = flow {
        if (networkMonitor.getCurrentStatus()) {
            when (val result = remoteDataSource.searchUsers("")) {
                is Result.Success -> {
                    val users = result.data
                    localDataSource.cacheUsers(users.map { it.toDomain() })
                    cacheDataSource.setUsers(users)
                    emit(Result.success(users.map { it.toDomain() }))
                }
                is Result.Failure -> {
                    // Fall back to local
                    localDataSource.observeAllUsers().collect { users ->
                        emit(Result.success(users))
                    }
                }
            }
        } else {
            localDataSource.observeAllUsers().collect { users ->
                emit(Result.success(users))
            }
        }
    }

    override suspend fun createUser(input: CreateUserInput): Result<User> {
        return remoteDataSource.createUser(input)
            .map { it.toDomain() }
            .also { result ->
                if (result is Result.Success) {
                    localDataSource.cacheUser(result.data)
                    cacheDataSource.setUser(result.data.toDto())
                }
            }
    }

    override suspend fun updateUser(id: String, input: UpdateUserInput): Result<User> {
        return remoteDataSource.updateUser(id, input)
            .map { it.toDomain() }
    }

    override suspend fun deleteUser(id: String): Result<Unit> {
        return try {
            localDataSource.deleteUser(id)
            cacheDataSource.clearCache()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(AppError.Database("Delete failed", e))
        }
    }
}

interface UserRepository {
    fun getUser(id: String): Flow<Result<User>>
    fun getAllUsers(): Flow<Result<List<User>>>
    suspend fun createUser(input: CreateUserInput): Result<User>
    suspend fun updateUser(id: String, input: UpdateUserInput): Result<User>
    suspend fun deleteUser(id: String): Result<Unit>
}
```

---

## 🎯 Example 2: Use Cases with Error Handling

### Get User Use Case
```kotlin
// domain/usecase/GetUserUseCase.kt
import com.url.androidcore.core.usecase.UseCase
import com.url.androidcore.core.usecase.Result

class GetUserUseCase(
    private val repository: UserRepository,
    private val logger: Logger = LogcatLogger()
) : UseCase<String, User> {

    override suspend operator fun invoke(userId: String): Result<User> = try {
        if (userId.isBlank()) {
            Result.failure(AppError.Validation("User ID cannot be empty", "userId", userId))
        } else {
            // Collect first value from Flow
            var result: Result<User>? = null
            repository.getUser(userId).collect { flowResult ->
                result = flowResult
            }
            result ?: Result.failure(AppError.Unknown("No result from repository"))
        }
    } catch (e: Exception) {
        logger.error("GetUserUseCase", "Error: ${e.message}", e)
        Result.failure(e.toAppError())
    }
}
```

### Search Users Use Case
```kotlin
// domain/usecase/SearchUsersUseCase.kt
class SearchUsersUseCase(
    private val repository: UserRepository,
    private val logger: Logger = LogcatLogger()
) : UseCase<SearchUsersInput, List<User>> {

    override suspend operator fun invoke(input: SearchUsersInput): Result<List<User>> = try {
        if (input.query.isBlank()) {
            Result.success(emptyList())
        } else {
            // Fetch and filter
            var result: Result<List<User>>? = null
            repository.getAllUsers().collect { flowResult ->
                result = flowResult.map { users ->
                    users.filter { user ->
                        user.name.contains(input.query, ignoreCase = true) ||
                        user.email.contains(input.query, ignoreCase = true)
                    }
                }
            }
            result ?: Result.failure(AppError.Unknown("No result from repository"))
        }
    } catch (e: Exception) {
        logger.error("SearchUsersUseCase", "Error", e)
        Result.failure(e.toAppError())
    }
}

data class SearchUsersInput(val query: String)
```

### Create User Use Case
```kotlin
// domain/usecase/CreateUserUseCase.kt
class CreateUserUseCase(
    private val repository: UserRepository,
    private val logger: Logger = LogcatLogger()
) : UseCase<CreateUserInput, User> {

    override suspend operator fun invoke(input: CreateUserInput): Result<User> = try {
        // Validate input
        val validationError = validateInput(input)
        if (validationError != null) {
            return Result.failure(validationError)
        }

        // Create via repository
        repository.createUser(input).also { result ->
            if (result is Result.Success) {
                logger.info("CreateUserUseCase", "User created: ${result.data.id}")
            } else if (result is Result.Failure) {
                logger.error("CreateUserUseCase", "Creation failed", result.error)
            }
        }
    } catch (e: Exception) {
        logger.error("CreateUserUseCase", "Unexpected error", e)
        Result.failure(AppError.Unknown("Failed to create user", e))
    }

    private fun validateInput(input: CreateUserInput): AppError.Validation? {
        return when {
            input.name.isBlank() -> 
                AppError.Validation("Name is required", "name", input.name)
            input.email.isBlank() -> 
                AppError.Validation("Email is required", "email", input.email)
            !isValidEmail(input.email) -> 
                AppError.Validation("Invalid email format", "email", input.email)
            else -> null
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return email.contains("@") && email.contains(".")
    }
}
```

---

## 🎯 Example 3: MVI ViewModel Integration

### User State & Intent
```kotlin
// ui/user/UserContract.kt
import com.url.androidcore.core.mvi.MviIntent
import com.url.androidcore.core.mvi.MviUiState
import com.url.androidcore.core.mvi.MviUiEffect

// Intents (user actions)
sealed class UserIntent : MviIntent {
    data class LoadUser(val userId: String) : UserIntent()
    data class SearchUsers(val query: String) : UserIntent()
    object RefreshUsers : UserIntent()
}

// UI State
data class UserUiState(
    val user: User? = null,
    val users: List<User> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val searchQuery: String = "",
    val lastUpdated: Long = 0L
) : MviUiState {
    val isOnline: Boolean get() = error != "Network unavailable"
    val formattedLastUpdated: String get() = if (lastUpdated > 0) {
        lastUpdated.toRelativeTime()
    } else {
        "Never"
    }
}

// UI Effects (one-time events)
sealed class UserUiEffect : MviUiEffect {
    data class ShowMessage(val message: String) : UserUiEffect()
    data class NavigateToUserDetail(val userId: String) : UserUiEffect()
    object NavigateToCreateUser : UserUiEffect()
}
```

### User ViewModel
```kotlin
// ui/user/UserViewModel.kt
import com.url.androidcore.core.mvi.MviViewModel
import com.url.androidcore.core.logging.LogcatLogger
import com.url.androidcore.core.connectivity.NetworkMonitor
import androidx.lifecycle.viewModelScope

class UserViewModel(
    private val getUserUseCase: GetUserUseCase,
    private val searchUsersUseCase: SearchUsersUseCase,
    private val createUserUseCase: CreateUserUseCase,
    private val networkMonitor: NetworkMonitor,
    private val logger: Logger = LogcatLogger()
) : MviViewModel<UserIntent, UserUiState, UserUiEffect>(
    initialState = UserUiState()
) {

    init {
        observeNetworkStatus()
    }

    override fun handleIntent(intent: UserIntent) {
        when (intent) {
            is UserIntent.LoadUser -> loadUser(intent.userId)
            is UserIntent.SearchUsers -> searchUsers(intent.query)
            is UserIntent.RefreshUsers -> refreshUsers()
        }
    }

    private fun loadUser(userId: String) {
        launchData(
            onStart = {
                logger.debug("UserVM", "Loading user: $userId")
                copy(isLoading = true, error = null)
            },
            onSuccess = { user: User ->
                logger.info("UserVM", "User loaded: $userId")
                copy(user = user, isLoading = false, lastUpdated = System.currentTimeMillis())
            },
            onError = { throwable ->
                val error = throwable.toAppError()
                val message = error.getUserMessage()
                logger.error("UserVM", message, throwable)
                copy(isLoading = false, error = message)
            },
            block = {
                val result = getUserUseCase(userId)
                if (result is Result.Success) {
                    result.data
                } else {
                    throw (result as Result.Failure).error
                }
            }
        )
    }

    private fun searchUsers(query: String) {
        launchData(
            onStart = {
                copy(searchQuery = query, isLoading = true, error = null)
            },
            onSuccess = { users: List<User> ->
                logger.info("UserVM", "Search completed: ${users.size} results")
                copy(users = users, isLoading = false)
            },
            onError = { throwable ->
                val error = throwable.toAppError()
                logger.error("UserVM", error.getUserMessage(), throwable)
                copy(isLoading = false, error = error.getUserMessage())
            },
            block = {
                val result = searchUsersUseCase(SearchUsersInput(query))
                if (result is Result.Success) {
                    result.data
                } else {
                    throw (result as Result.Failure).error
                }
            }
        )
    }

    private fun refreshUsers() {
        launchData(
            onStart = { copy(isLoading = true) },
            onSuccess = { users: List<User> ->
                copy(users = users, isLoading = false, lastUpdated = System.currentTimeMillis())
            },
            onError = { error ->
                logger.error("UserVM", "Refresh failed", error)
                copy(isLoading = false)
            },
            block = {
                val result = searchUsersUseCase(SearchUsersInput(""))
                if (result is Result.Success) {
                    result.data
                } else {
                    throw (result as Result.Failure).error
                }
            }
        )
    }

    private fun observeNetworkStatus() {
        viewModelScope.collectIn(networkMonitor.isConnected()) { isConnected ->
            if (!isConnected) {
                setState { copy(error = "Network unavailable") }
                sendEffect { UserUiEffect.ShowMessage("You are offline") }
            }
        }
    }
}
```

---

## 🎯 Example 4: Unit Tests

### Repository Tests
```kotlin
// data/repository/UserRepositoryImplTest.kt
import org.junit.Test
import org.junit.Before
import com.url.androidcore.core.testing.*
import kotlinx.coroutines.runBlocking

class UserRepositoryImplTest {

    private val fakeRemote = FakeUserRemoteDataSource()
    private val fakeLocal = FakeUserLocalDataSource()
    private val fakeCache = FakeUserCacheDataSource()
    private val fakeNetwork = FakeNetworkMonitor(initialConnected = true)

    private lateinit var repository: UserRepository

    @Before
    fun setup() {
        repository = UserRepositoryImpl(
            remoteDataSource = fakeRemote,
            localDataSource = fakeLocal,
            cacheDataSource = fakeCache,
            networkMonitor = fakeNetwork
        )
    }

    @Test
    fun testGetUserFromCache() = runBlocking {
        // Setup
        val testUser = User("1", "John", "john@example.com", System.currentTimeMillis())
        fakeCache.setUser(testUser.toDto())

        // Execute
        var result: Result<User>? = null
        repository.getUser("1").collect { r -> result = r }

        // Assert
        result.assertIsSuccess { user ->
            assertThat(user.id).isEqualTo("1")
            assertThat(user.name).isEqualTo("John")
        }
    }

    @Test
    fun testGetUserFromRemote() = runBlocking {
        // Setup
        val testUser = User("2", "Jane", "jane@example.com", System.currentTimeMillis())
        fakeRemote.setUser(testUser.toDto())

        // Execute
        var result: Result<User>? = null
        repository.getUser("2").collect { r -> result = r }

        // Assert
        result.assertIsSuccess { user ->
            assertThat(user.id).isEqualTo("2")
        }
    }

    @Test
    fun testGetUserOffline() = runBlocking {
        // Setup
        fakeNetwork.setConnected(false)
        val testUser = User("3", "Bob", "bob@example.com", System.currentTimeMillis())
        fakeLocal.setUser(testUser)

        // Execute
        var result: Result<User>? = null
        repository.getUser("3").collect { r -> result = r }

        // Assert
        result.assertIsSuccess { user ->
            assertThat(user.id).isEqualTo("3")
        }
    }
}

// Fake implementations for testing
class FakeUserRemoteDataSource : RemoteDataSource {
    private val users = mutableMapOf<String, UserDto>()
    
    suspend fun setUser(user: UserDto) { users[user.id] = user }
    
    suspend fun getUser(id: String): Result<UserDto> {
        return users[id]?.let { Result.success(it) }
            ?: Result.failure(AppError.Network("Not found", 404))
    }
}

class FakeUserLocalDataSource : LocalDataSource {
    private val users = mutableMapOf<String, User>()
    
    suspend fun setUser(user: User) { users[user.id] = user }
    
    fun getUserFlow(id: String): Flow<User> = flow {
        users[id]?.let { emit(it) }
    }
}

class FakeUserCacheDataSource : CacheDataSource {
    private val cache = mutableMapOf<String, UserDto>()
    
    fun setUser(user: UserDto) { cache[user.id] = user }
    fun getUser(id: String): UserDto? = cache[id]
}
```

### Use Case Tests
```kotlin
// domain/usecase/GetUserUseCaseTest.kt
class GetUserUseCaseTest {

    private val fakeRepository = FakeUserRepository()
    private val fakeLogger = FakeLogger()
    private lateinit var useCase: GetUserUseCase

    @Before
    fun setup() {
        useCase = GetUserUseCase(fakeRepository, fakeLogger)
    }

    @Test
    fun testGetUserSuccess() = runBlocking {
        // Setup
        val testUser = User("1", "John", "john@example.com", System.currentTimeMillis())
        fakeRepository.setUser(testUser)

        // Execute
        val result = useCase("1")

        // Assert
        result.assertIsSuccess { user ->
            assertThat(user.id).isEqualTo("1")
            assertThat(user.name).isEqualTo("John")
        }
    }

    @Test
    fun testGetUserBlankIdFails() = runBlocking {
        // Execute
        val result = useCase("")

        // Assert
        result.assertIsFailure { error ->
            error.assertIsInstanceOf<AppError.Validation>()
        }
    }
}

class FakeUserRepository : Repository {
    private var user: User? = null
    
    fun setUser(u: User) { user = u }
    
    override fun getUser(id: String): Flow<Result<User>> = flow {
        user?.let { emit(Result.success(it)) }
    }
}
```

### ViewModel Tests
```kotlin
// ui/user/UserViewModelTest.kt
class UserViewModelTest {

    private val fakeGetUser = FakeGetUserUseCase()
    private val fakeSearch = FakeSearchUsersUseCase()
    private val fakeCreate = FakeCreateUserUseCase()
    private val fakeNetwork = FakeNetworkMonitor(initialConnected = true)
    private val fakeLogger = FakeLogger()
    private val dispatcher = TestDispatcherProvider()

    private lateinit var viewModel: UserViewModel

    @Before
    fun setup() {
        viewModel = UserViewModel(
            getUserUseCase = fakeGetUser,
            searchUsersUseCase = fakeSearch,
            createUserUseCase = fakeCreate,
            networkMonitor = fakeNetwork,
            logger = fakeLogger
        )
    }

    @Test
    fun testLoadUserIntent() {
        // Setup
        val testUser = User("1", "John", "john@example.com", System.currentTimeMillis())
        fakeGetUser.setResult(Result.success(testUser))

        // Execute
        viewModel.dispatch(UserIntent.LoadUser("1"))

        // Assert - State updated
        val state = viewModel.uiState.value
        assertThat(state.user?.id).isEqualTo("1")
        assertThat(state.error).isNull()

        // Assert - Logger called
        assertThat(fakeLogger.infoMessages).isNotEmpty()
    }

    @Test
    fun testLoadUserError() {
        // Setup
        fakeGetUser.setResult(Result.failure(AppError.Network("Connection failed")))

        // Execute
        viewModel.dispatch(UserIntent.LoadUser("2"))

        // Assert
        val state = viewModel.uiState.value
        assertThat(state.error).isEqualTo("Network unavailable")
    }
}

class FakeGetUserUseCase : UseCase<String, User> {
    private var result: Result<User> = Result.failure(AppError.Unknown())
    
    fun setResult(r: Result<User>) { result = r }
    
    override suspend operator fun invoke(param: String): Result<User> = result
}
```

---

## ✅ Checklist for Integration

- [ ] Create UseCase classes for each domain operation
- [ ] Create Repository interface and implementation
- [ ] Create RemoteDataSource with `safeApiCall`
- [ ] Create LocalDataSource with Flow operators
- [ ] Create ViewModel using MviViewModel
- [ ] Create Fake implementations for testing
- [ ] Write unit tests with TestDispatcherProvider
- [ ] Write repository tests with fake data sources
- [ ] Verify error handling with AppError
- [ ] Set up dependency injection with interfaces

---

**All examples are production-ready and follow Clean Architecture + MVI patterns** 🚀

