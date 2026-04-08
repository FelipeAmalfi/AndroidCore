# Agent Templates & Code Examples

This document shows template patterns for each agent's output.

---

## 1. DATA LAYER AGENT Templates

### Service Interface Template
```kotlin
package com.url.androidcore.data.{feature}.remote

import retrofit2.http.*
import com.url.androidcore.data.{feature}.model.{Feature}Dto

interface {Feature}Service {
    @GET("/api/v1/{feature}/{id}")
    suspend fun get{Feature}(@Path("id") id: String): {Feature}Dto

    @GET("/api/v1/{feature}")
    suspend fun getAll{Feature}s(): List<{Feature}Dto>

    @POST("/api/v1/{feature}")
    suspend fun create{Feature}(@Body request: {Feature}Dto): {Feature}Dto

    @PUT("/api/v1/{feature}/{id}")
    suspend fun update{Feature}(@Path("id") id: String, @Body request: {Feature}Dto): {Feature}Dto

    @DELETE("/api/v1/{feature}/{id}")
    suspend fun delete{Feature}(@Path("id") id: String)
}
```

### DTO Model Template
```kotlin
package com.url.androidcore.data.{feature}.model

data class {Feature}Dto(
    val id: String,
    val name: String,
    val email: String,
    val createdAt: String
)
```

### DataSource Interface Template
```kotlin
package com.url.androidcore.data.{feature}.datasource

import kotlinx.coroutines.flow.Flow
import com.url.androidcore.core.error.AppError
import com.url.androidcore.data.{feature}.model.{Feature}Dto

interface {Feature}DataSource {
    suspend fun get{Feature}(id: String): Result<{Feature}Dto>
    fun getAll{Feature}s(): Flow<Result<List<{Feature}Dto>>>
    suspend fun create{Feature}(request: {Feature}Dto): Result<{Feature}Dto>
}
```

### DataSource Implementation Template
```kotlin
package com.url.androidcore.data.{feature}.datasource

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import com.url.androidcore.core.error.AppError
import com.url.androidcore.core.network.NetworkExt.toAppError
import com.url.androidcore.data.{feature}.model.{Feature}Dto
import com.url.androidcore.data.{feature}.remote.{Feature}Service

class {Feature}DataSourceImpl(
    private val service: {Feature}Service
) : {Feature}DataSource {

    override suspend fun get{Feature}(id: String): Result<{Feature}Dto> = try {
        Result.success(service.get{Feature}(id))
    } catch (e: Exception) {
        Result.failure(e.toAppError())
    }

    override fun getAll{Feature}s(): Flow<Result<List<{Feature}Dto>>> = flow {
        try {
            emit(Result.success(service.getAll{Feature}s()))
        } catch (e: Exception) {
            emit(Result.failure(e.toAppError()))
        }
    }

    override suspend fun create{Feature}(request: {Feature}Dto): Result<{Feature}Dto> = try {
        Result.success(service.create{Feature}(request))
    } catch (e: Exception) {
        Result.failure(e.toAppError())
    }
}
```

### Repository Implementation Template (Data Layer)
```kotlin
package com.url.androidcore.data.{feature}.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.url.androidcore.domain.{feature}.model.{Feature}Model
import com.url.androidcore.domain.{feature}.repository.{Feature}Repository
import com.url.androidcore.data.{feature}.datasource.{Feature}DataSource
import com.url.androidcore.data.{feature}.model.{Feature}Dto

class {Feature}RepositoryImpl(
    private val dataSource: {Feature}DataSource
) : {Feature}Repository {

    override suspend fun get{Feature}(id: String): {Feature}Model? = 
        dataSource.get{Feature}(id).getOrNull()?.toDomain()

    override fun getAll{Feature}s(): Flow<List<{Feature}Model>> =
        dataSource.getAll{Feature}s().map { result ->
            result.getOrEmptyList().map { it.toDomain() }
        }

    private fun <T> Result<T>.getOrEmptyList(): List<T> =
        getOrNull()?.let { listOf(it) } ?: emptyList()
}
```

---

## 2. DOMAIN AGENT Templates

### Repository Interface Template (Domain)
```kotlin
package com.url.androidcore.domain.{feature}.repository

import kotlinx.coroutines.flow.Flow
import com.url.androidcore.domain.{feature}.model.{Feature}Model

interface {Feature}Repository {
    suspend fun get{Feature}(id: String): {Feature}Model?
    fun getAll{Feature}s(): Flow<List<{Feature}Model>>
    suspend fun create{Feature}(request: {Feature}Model): {Feature}Model?
}
```

### Domain Model Template
```kotlin
package com.url.androidcore.domain.{feature}.model

data class {Feature}Model(
    val id: String,
    val name: String,
    val email: String,
    val createdAt: String
)
```

### UseCase Template
```kotlin
package com.url.androidcore.domain.{feature}.usecase

import kotlinx.coroutines.flow.Flow
import com.url.androidcore.core.usecase.UseCase
import com.url.androidcore.core.dispatchers.DispatcherProvider
import com.url.androidcore.domain.{feature}.model.{Feature}Model
import com.url.androidcore.domain.{feature}.repository.{Feature}Repository

class Get{Feature}UseCase(
    private val repository: {Feature}Repository,
    private val dispatchers: DispatcherProvider
) : UseCase() {

    suspend operator fun invoke(id: String): {Feature}Model? =
        withContext(dispatchers.io) {
            repository.get{Feature}(id)
        }
}

class GetAll{Feature}sUseCase(
    private val repository: {Feature}Repository,
    private val dispatchers: DispatcherProvider
) : UseCase() {

    operator fun invoke(): Flow<List<{Feature}Model>> =
        repository.getAll{Feature}s()
}
```

---

## 3. MAPPER AGENT Templates

### Mapper Functions Template
```kotlin
package com.url.androidcore.presentation.{feature}.mapper

import com.url.androidcore.data.{feature}.model.{Feature}Dto
import com.url.androidcore.domain.{feature}.model.{Feature}Model
import com.url.androidcore.presentation.{feature}.contract.{Feature}UiModel

// DTO → Domain mapping
fun {Feature}Dto.toDomain(): {Feature}Model = {Feature}Model(
    id = this.id,
    name = this.name,
    email = this.email,
    createdAt = this.createdAt
)

// Domain → UI mapping
fun {Feature}Model.toUi(): {Feature}UiModel = {Feature}UiModel(
    id = this.id,
    displayName = this.name,
    contactEmail = this.email,
    registeredDate = this.createdAt
)

// Direct DTO → UI mapping (if needed)
fun {Feature}Dto.toUi(): {Feature}UiModel = this.toDomain().toUi()
```

---

## 4. PRESENTATION AGENT (MVI) Templates

### Intent Template
```kotlin
package com.url.androidcore.presentation.{feature}.contract

import com.url.androidcore.core.mvi.MviIntent

sealed class {Feature}Intent : MviIntent {
    data object Load{Feature} : {Feature}Intent()
    data object Refresh{Feature} : {Feature}Intent()
    data class Create{Feature}(val request: {Feature}UiModel) : {Feature}Intent()
    data object ClearError : {Feature}Intent()
}
```

### UiState Template
```kotlin
package com.url.androidcore.presentation.{feature}.contract

import com.url.androidcore.core.mvi.MviUiState

data class {Feature}UiState(
    val isLoading: Boolean = false,
    val {feature}: {Feature}UiModel? = null,
    val error: String? = null,
    val isSuccess: Boolean = false
) : MviUiState {
    companion object {
        fun initial() = {Feature}UiState()
    }
}

data class {Feature}UiModel(
    val id: String,
    val displayName: String,
    val contactEmail: String,
    val registeredDate: String
)
```

### UiEffect Template
```kotlin
package com.url.androidcore.presentation.{feature}.contract

import com.url.androidcore.core.mvi.MviUiEffect

sealed class {Feature}UiEffect : MviUiEffect {
    data class ShowError(val message: String) : {Feature}UiEffect()
    data object Navigate{Feature}Detail : {Feature}UiEffect()
    data object ShowSuccess : {Feature}UiEffect()
}
```

### ViewModel Template
```kotlin
package com.url.androidcore.presentation.{feature}.viewmodel

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import com.url.androidcore.core.mvi.MviViewModel
import com.url.androidcore.core.dispatchers.DispatcherProvider
import com.url.androidcore.core.logging.Logger
import com.url.androidcore.domain.{feature}.usecase.Get{Feature}UseCase
import com.url.androidcore.domain.{feature}.usecase.GetAll{Feature}sUseCase
import com.url.androidcore.presentation.{feature}.contract.{Feature}Intent
import com.url.androidcore.presentation.{feature}.contract.{Feature}UiState
import com.url.androidcore.presentation.{feature}.contract.{Feature}UiEffect
import com.url.androidcore.presentation.{feature}.mapper.toUi

class {Feature}ViewModel(
    private val get{Feature}UseCase: Get{Feature}UseCase,
    private val getAll{Feature}sUseCase: GetAll{Feature}sUseCase,
    private val dispatchers: DispatcherProvider,
    private val logger: Logger
) : MviViewModel<{Feature}Intent, {Feature}UiState, {Feature}UiEffect>() {

    override fun createInitialState() = {Feature}UiState.initial()

    override fun handleIntent(intent: {Feature}Intent) {
        when (intent) {
            is {Feature}Intent.Load{Feature} -> load{Feature}()
            is {Feature}Intent.Refresh{Feature} -> load{Feature}()
            is {Feature}Intent.Create{Feature} -> create{Feature}(intent)
            {Feature}Intent.ClearError -> clearError()
        }
    }

    private fun load{Feature}() = launchData(
        action = {
            get{Feature}UseCase("123") // Replace with actual ID
        },
        onSuccess = { {feature} ->
            setState {
                copy(
                    isLoading = false,
                    {feature} = {feature}?.toUi(),
                    error = null,
                    isSuccess = true
                )
            }
        },
        onError = { error ->
            logger.e("Error loading {feature}: $error")
            setState { copy(isLoading = false, error = error?.message) }
            setEffect({Feature}UiEffect.ShowError(error?.message ?: "Unknown error"))
        }
    )

    private fun create{Feature}(intent: {Feature}Intent.Create{Feature}) {
        // Implementation
    }

    private fun clearError() {
        setState { copy(error = null) }
    }
}
```

---

## 5. TEST AGENT Templates

### UseCase Test Template
```kotlin
package com.url.androidcore.domain.{feature}.usecase

import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.Before
import com.url.androidcore.core.dispatchers.TestDispatcherProvider
import com.url.androidcore.domain.{feature}.model.{Feature}Model
import com.url.androidcore.domain.{feature}.repository.{Feature}Repository

class Get{Feature}UseCaseTest {

    private lateinit var useCase: Get{Feature}UseCase
    private lateinit var repository: FakeUserRepository

    @Before
    fun setup() {
        repository = FakeUserRepository()
        useCase = Get{Feature}UseCase(repository, TestDispatcherProvider())
    }

    @Test
    fun invoke_returnsSucess_whenRepositoryReturnsData() = runTest {
        // Arrange
        val expected = {Feature}Model(
            id = "1",
            name = "Test",
            email = "test@example.com",
            createdAt = "2024-01-01"
        )
        repository.setMock{Feature}(expected)

        // Act
        val result = useCase("1")

        // Assert
        assert(result == expected)
    }

    @Test
    fun invoke_returnsNull_whenRepositoryReturnsNull() = runTest {
        // Act
        val result = useCase("999")

        // Assert
        assert(result == null)
    }
}

// Fake implementation
class Fake{Feature}Repository : {Feature}Repository {
    private var mock{Feature}: {Feature}Model? = null

    fun setMock{Feature}(model: {Feature}Model?) {
        mock{Feature} = model
    }

    override suspend fun get{Feature}(id: String): {Feature}Model? = mock{Feature}

    override fun getAll{Feature}s() = TODO()
    override suspend fun create{Feature}(request: {Feature}Model) = TODO()
}
```

### ViewModel Test Template
```kotlin
package com.url.androidcore.presentation.{feature}.viewmodel

import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.Before
import com.url.androidcore.core.dispatchers.TestDispatcherProvider
import com.url.androidcore.core.logging.TestLogger
import com.url.androidcore.domain.{feature}.usecase.Get{Feature}UseCase
import com.url.androidcore.presentation.{feature}.contract.{Feature}Intent
import com.url.androidcore.presentation.{feature}.contract.{Feature}UiState

class {Feature}ViewModelTest {

    private lateinit var viewModel: {Feature}ViewModel
    private lateinit var get{Feature}UseCase: FakeGet{Feature}UseCase

    @Before
    fun setup() {
        get{Feature}UseCase = FakeGet{Feature}UseCase()
        viewModel = {Feature}ViewModel(
            get{Feature}UseCase,
            FakeGetAll{Feature}sUseCase(),
            TestDispatcherProvider(),
            TestLogger()
        )
    }

    @Test
    fun handleIntent_loadsData_whenLoadIntentReceived() = runTest {
        // Act
        viewModel.handleIntent({Feature}Intent.Load{Feature})
        advanceUntilIdle()

        // Assert
        val state = viewModel.uiState.value
        assert(state.isSuccess)
        assert(state.{feature} != null)
    }

    @Test
    fun handleIntent_showsError_whenLoadFails() = runTest {
        // Arrange
        get{Feature}UseCase.setShouldFail(true)

        // Act
        viewModel.handleIntent({Feature}Intent.Load{Feature})
        advanceUntilIdle()

        // Assert
        val state = viewModel.uiState.value
        assert(state.error != null)
        assert(!state.isSuccess)
    }
}
```

---

## 6. DEPENDENCY INJECTION AGENT Templates

### Application Class Template
```kotlin
package com.url.androidcore.di

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Main Application class with Hilt dependency injection setup.
 *
 * This class serves as the root component for the entire dependency graph.
 * All Hilt modules are installed here and available throughout the app.
 */
@HiltAndroidApp
class AndroidCoreApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        // Application-level initialization can go here
    }
}
```

### Core Module Template
```kotlin
package com.url.androidcore.di.module

import com.url.androidcore.core.dispatchers.DispatcherProvider
import com.url.androidcore.core.dispatchers.DispatcherProviderImpl
import com.url.androidcore.core.logging.Logger
import com.url.androidcore.core.logging.LoggerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Core module providing application-wide dependencies.
 *
 * Includes: Dispatchers, Logger, Error handling, Security utilities
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class CoreModule {

    @Binds
    @Singleton
    abstract fun bindDispatcherProvider(
        impl: DispatcherProviderImpl
    ): DispatcherProvider

    @Binds
    @Singleton
    abstract fun bindLogger(
        impl: LoggerImpl
    ): Logger
}
```

### Network Module Template
```kotlin
package com.url.androidcore.di.module

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.url.androidcore.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * Network module providing HTTP client and API service dependencies.
 *
 * Includes: OkHttp, Retrofit, Moshi, API services
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideMoshi(): Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        moshi: Moshi
    ): Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.API_BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
}
```

### Data Module Template
```kotlin
package com.url.androidcore.di.module

import com.url.androidcore.data.{feature}.datasource.{Feature}DataSource
import com.url.androidcore.data.{feature}.datasource.{Feature}DataSourceImpl
import com.url.androidcore.data.{feature}.remote.{Feature}Service
import com.url.androidcore.domain.{feature}.repository.{Feature}Repository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * Data layer module providing repositories and data sources.
 *
 * Includes: API services, DataSources, Repositories
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    @Singleton
    abstract fun bind{Feature}DataSource(
        impl: {Feature}DataSourceImpl
    ): {Feature}DataSource

    companion object {

        @Provides
        @Singleton
        fun provide{Feature}Service(retrofit: Retrofit): {Feature}Service =
            retrofit.create({Feature}Service::class.java)
    }
}
```

### Domain Module Template
```kotlin
package com.url.androidcore.di.module

import com.url.androidcore.core.dispatchers.DispatcherProvider
import com.url.androidcore.domain.{feature}.repository.{Feature}Repository
import com.url.androidcore.domain.{feature}.usecase.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Domain layer module providing use cases and business logic.
 *
 * Includes: UseCases, Business rules, Domain services
 */
@Module
@InstallIn(SingletonComponent::class)
object DomainModule {

    @Provides
    @Singleton
    fun provideGet{Feature}UseCase(
        repository: {Feature}Repository,
        dispatchers: DispatcherProvider
    ): Get{Feature}UseCase = Get{Feature}UseCase(repository, dispatchers)

    @Provides
    @Singleton
    fun provideGetAll{Feature}sUseCase(
        repository: {Feature}Repository,
        dispatchers: DispatcherProvider
    ): GetAll{Feature}sUseCase = GetAll{Feature}sUseCase(repository, dispatchers)

    @Provides
    @Singleton
    fun provideCreate{Feature}UseCase(
        repository: {Feature}Repository,
        dispatchers: DispatcherProvider
    ): Create{Feature}UseCase = Create{Feature}UseCase(repository, dispatchers)

    @Provides
    @Singleton
    fun provideUpdate{Feature}UseCase(
        repository: {Feature}Repository,
        dispatchers: DispatcherProvider
    ): Update{Feature}UseCase = Update{Feature}UseCase(repository, dispatchers)

    @Provides
    @Singleton
    fun provideDelete{Feature}UseCase(
        repository: {Feature}Repository,
        dispatchers: DispatcherProvider
    ): Delete{Feature}UseCase = Delete{Feature}UseCase(repository, dispatchers)
}
```

### Presentation Module Template
```kotlin
package com.url.androidcore.di.module

import com.url.androidcore.core.dispatchers.DispatcherProvider
import com.url.androidcore.core.logging.Logger
import com.url.androidcore.domain.{feature}.usecase.*
import com.url.androidcore.presentation.{feature}.viewmodel.{Feature}ViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

/**
 * Presentation layer module providing ViewModels and UI dependencies.
 *
 * Includes: ViewModels, UI state managers, Navigation helpers
 */
@Module
@InstallIn(ViewModelComponent::class)
object PresentationModule {

    @Provides
    @ViewModelScoped
    fun provide{Feature}ViewModel(
        get{Feature}UseCase: Get{Feature}UseCase,
        getAll{Feature}sUseCase: GetAll{Feature}sUseCase,
        create{Feature}UseCase: Create{Feature}UseCase,
        update{Feature}UseCase: Update{Feature}UseCase,
        delete{Feature}UseCase: Delete{Feature}UseCase,
        dispatchers: DispatcherProvider,
        logger: Logger
    ): {Feature}ViewModel = {Feature}ViewModel(
        get{Feature}UseCase,
        getAll{Feature}sUseCase,
        create{Feature}UseCase,
        update{Feature}UseCase,
        delete{Feature}UseCase,
        dispatchers,
        logger
    )
}
```

---

## 7. ENHANCEMENT AGENT Templates

### Cache Layer Template
```kotlin
package com.url.androidcore.data.{feature}.cache

import com.url.androidcore.data.{feature}.model.{Feature}Dto
import java.util.concurrent.TimeUnit

class {Feature}Cache {
    private val cache = mutableMapOf<String, CachedItem<{Feature}Dto>>()
    private val cacheDuration = TimeUnit.HOURS.toMillis(1)

    fun get(key: String): {Feature}Dto? {
        val item = cache[key] ?: return null
        return if (System.currentTimeMillis() - item.timestamp < cacheDuration) {
            item.data
        } else {
            cache.remove(key)
            null
        }
    }

    fun put(key: String, data: {Feature}Dto) {
        cache[key] = CachedItem(data, System.currentTimeMillis())
    }

    fun clear() = cache.clear()

    private data class CachedItem<T>(val data: T, val timestamp: Long)
}
```

### Retry Logic Template
```kotlin
package com.url.androidcore.data.{feature}.retry

import com.url.androidcore.core.error.AppError
import kotlinx.coroutines.delay

suspend fun <T> retryWithBackoff(
    maxAttempts: Int = 3,
    delayMs: Long = 1000,
    block: suspend () -> T
): T {
    var lastException: Exception? = null

    repeat(maxAttempts) { attempt ->
        try {
            return block()
        } catch (e: Exception) {
            lastException = e
            if (attempt < maxAttempts - 1) {
                delay(delayMs * (attempt + 1))
            }
        }
    }

    throw lastException ?: Exception("Unknown error")
}
```

---

