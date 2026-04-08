# Quick Reference - Extended Core Utilities

**One-page cheat sheet for common operations**

---

## 🎯 Error Handling

### Create AppError
```kotlin
val networkError = AppError.network("Connection failed", statusCode = 500)
val validationError = AppError.validation("Email required", fieldName = "email")
val databaseError = AppError.database("Query failed", isFatal = false)
val unknownError = AppError.unknown("Something went wrong")
```

### Map Exception → AppError
```kotlin
val error: AppError = exception.toAppError()
```

### Handle AppError
```kotlin
when (error) {
    is AppError.Network -> showNetworkError(error.message)
    is AppError.Validation -> highlightField(error.fieldName)
    is AppError.Database -> showDatabaseError()
    is AppError.Unknown -> showGenericError()
}
```

### Check if Retryable
```kotlin
if (error.isRetryable()) {
    retryOperation()
}
```

### User-Friendly Messages
```kotlin
val message = error.getUserMessage()  // Safe to show in UI
```

---

## 📝 Logging

### Using Logger
```kotlin
val logger = LogcatLogger()  // or NoOpLogger in tests

logger.debug("TAG", "Debug message")
logger.info("TAG", "Info message")
logger.warn("TAG", "Warning message")
logger.error("TAG", "Error message", throwable)
```

### Lazy Logging
```kotlin
logger.debugLazy("TAG") { 
    "Expensive computation: ${expensiveCall()}"  // Only computed if debug enabled
}
```

---

## 🔄 Network Calls

### Safe API Call
```kotlin
val result: Result<UserDto> = safeApiCall {
    apiService.getUser(id)
}
```

### With Transformation
```kotlin
val result: Result<User> = safeApiCall(
    block = { apiService.getUser(id) },
    transform = { dto -> dto.toDomain() }
)
```

### With Error Handler
```kotlin
val result: Result<UserDto> = safeApiCallWithHandler(
    onError = { error -> logger.error("API", error.message, error) }
) {
    apiService.getUser(id)
}
```

### Map HTTP Status
```kotlin
val error = mapHttpStatusToError(404, "User not found")
// Returns: AppError.Network(message = "Not found", statusCode = 404, isRetryable = false)
```

---

## 🎯 UseCase Pattern

### Define UseCase
```kotlin
class MyUseCase(private val repo: MyRepository) : UseCase<Input, Output> {
    override suspend operator fun invoke(param: Input): Result<Output> {
        return try {
            val result = repo.fetch(param)
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e.toAppError())
        }
    }
}
```

### Execute UseCase
```kotlin
val result: Result<Output> = myUseCase(input)

result.fold(
    onSuccess = { output -> process(output) },
    onFailure = { error -> handleError(error) }
)
```

### Map Result
```kotlin
val result2: Result<String> = result.map { it.toString() }
val result3: Result<Int> = result.flatMap { nextOp(it) }
```

### Extract Value
```kotlin
val value: Output? = result.getOrNull()
val error: Throwable? = result.exceptionOrNull()
```

---

## 📊 Repository & DataSource

### Implement Repository
```kotlin
class MyRepository(
    private val remote: MyRemoteDataSource,
    private val local: MyLocalDataSource,
    private val monitor: NetworkMonitor
) : Repository {
    
    suspend fun fetch(id: String): Result<Data> {
        return if (monitor.getCurrentStatus()) {
            remote.fetch(id)
        } else {
            local.fetch(id)
        }
    }
}
```

### Remote DataSource
```kotlin
class MyRemoteDataSource(private val api: MyApiService) : RemoteDataSource {
    suspend fun fetch(id: String): Result<Dto> = safeApiCall {
        api.getItem(id)
    }
}
```

### Local DataSource
```kotlin
class MyLocalDataSource(private val dao: MyDao) : LocalDataSource {
    fun observeItem(id: String): Flow<Entity> = dao.observe(id)
    
    suspend fun cache(item: Entity) = dao.insert(item)
}
```

---

## 🌐 Connectivity

### Monitor Network Status
```kotlin
val monitor = DefaultNetworkMonitor(context)

monitor.isConnected().collect { isOnline ->
    if (isOnline) {
        syncData()
    } else {
        showOfflineUI()
    }
}
```

### Check Current Status
```kotlin
val isOnline = monitor.getCurrentStatus()
```

---

## ⏰ Date & Time

### Format Timestamps
```kotlin
val timestamp = System.currentTimeMillis()

// ISO 8601 (for APIs)
val iso = timestamp.toIso8601()      // "2024-04-08T14:30:00Z"

// UI formats
val date = timestamp.toUiDate()      // "Apr 8, 2024"
val time = timestamp.toUiTime()      // "2:30 PM"
val datetime = timestamp.toUiDateTime()  // "Apr 8, 2024 at 2:30 PM"

// Relative time
val relative = timestamp.toRelativeTime()  // "5 minutes ago"
val countdown = future.toCountdownTime()   // "2h left"
```

### Parse ISO 8601
```kotlin
val millis = DateFormatter.fromIso8601("2024-04-08T14:30:00Z")
```

---

## 🔐 Security

### Hashing
```kotlin
val hash = "password".sha256()
val hashed = Hash.hashWithSalt("password", salt = "mysalt")
val isValid = Hash.verify("password", hashed, salt = "mysalt")
```

### Encoding/Decoding
```kotlin
val encoded = "secret".encodeBase64()    // "c2VjcmV0"
val decoded = encoded.decodeBase64()     // "secret"
```

### Secure Storage
```kotlin
val storage = SecureStorage()
storage.put("token", myToken, encrypt = true)
val retrieved = storage.get("token", decrypt = true)
storage.remove("token")
storage.clear()
```

---

## 🧪 Testing

### Test Dispatchers
```kotlin
val dispatcher = TestDispatcherProvider()
val repo = MyRepository(dispatcher)  // Inject test dispatcher
// All coroutines run synchronously
```

### Fake Logger
```kotlin
val fakeLogger = FakeLogger()
val service = MyService(fakeLogger)
service.doSomething()
assertThat(fakeLogger.infoMessages).contains("expected")
```

### Fake Network Monitor
```kotlin
val monitor = FakeNetworkMonitor(initialConnected = true)
monitor.setConnected(false)  // Simulate offline
val repo = MyRepository(monitor)
// Test offline behavior
```

### Fake Repository
```kotlin
class FakeUserRepository : FakeRepository(), UserRepository {
    override fun getUser(id: String): Flow<Result<User>> = flow {
        emit(Result.success(testUser))
    }
}
```

### Assert Result
```kotlin
val result = useCase(input)

result.assertIsSuccess { data ->
    assertThat(data.id).isEqualTo(123)
}

result.assertIsFailure { error ->
    error.assertIsInstanceOf<AppError.Network>()
}
```

### Assert AsyncState
```kotlin
val state = flow.first()

state.assertIsLoading()

state.assertIsSuccess { data ->
    assertThat(data).isNotNull()
}

state.assertIsError { error ->
    assertThat(error).isInstanceOf(AppError::class.java)
}
```

---

## 🏗️ Complete Example: User Feature

### 1. Define UseCase
```kotlin
class GetUserUseCase(private val repo: UserRepository) : UseCase<String, User> {
    override suspend operator fun invoke(userId: String): Result<User> {
        if (userId.isBlank()) return Result.failure(AppError.validation("ID required"))
        return repo.getUser(userId)
    }
}
```

### 2. Define ViewModel
```kotlin
class UserViewModel(private val getUser: GetUserUseCase) : MviViewModel<...>() {
    override fun handleIntent(intent: UserIntent) {
        when (intent) {
            is Load -> loadUser(intent.id)
        }
    }
    
    private fun loadUser(id: String) {
        launchData(
            onStart = { setState { copy(loading = true) } },
            onSuccess = { user: User -> setState { copy(user = user, loading = false) } },
            onError = { err -> setState { copy(error = err.toAppError().getUserMessage()) } },
            block = { getUser(id).getOrNull() ?: throw Exception("Failed") }
        )
    }
}
```

### 3. Test
```kotlin
@Test fun testLoadUser() = runBlocking {
    val fake = FakeUserRepository()
    val useCase = GetUserUseCase(fake)
    val result = useCase("123")
    result.assertIsSuccess { assertThat(it.id).isEqualTo("123") }
}
```

---

## 📚 Documentation

| Reference | Purpose |
|-----------|---------|
| `EXTENDED_UTILITIES.md` | Complete API reference |
| `INTEGRATION_EXAMPLES.md` | Real-world patterns |
| `README_EXTENDED_CORE.md` | Architecture overview |
| Source KDoc | Inline help (IDE Ctrl+Q) |

---

## 🎯 Common Patterns

### Safe Network Call with Fallback
```kotlin
fun getDataSafely(): Result<Data> = safeApiCall {
    try {
        remote.fetch()
    } catch (e: Exception) {
        local.fetch()  // Fallback
    }
}
```

### Validate Then Call
```kotlin
override suspend operator fun invoke(input: Input): Result<Output> {
    val error = validate(input)
    if (error != null) return Result.failure(error)
    return execute(input)
}
```

### Retry on Network Error
```kotlin
val result = retry(attempts = 3, delayMillis = 1000) {
    safeApiCall { apiService.fetch() }
}
```

### Monitor Connectivity
```kotlin
monitor.isConnected().collect { isOnline ->
    val data = if (isOnline) remote.fetch() else local.fetch()
}
```

---

**Quick Reference Complete**
**For detailed info, see documentation files**

