# How to Set Up Dependency Injection

## Goal

This guide explains how to wire Dagger Hilt across all layers of a feature in AndroidCore.

For strict generation rules, use:

- `.github/skills/di-hilt.skill.md`
- `.github/agents/di.agent.md`
- `.github/skills/naming-conventions.skill.md`

## Prerequisites

Before running the DI agent, the following layers must be complete for the target feature:

- data layer (repositories, data sources, services, DTOs)
- domain layer (domain models, repository contracts, use cases)
- mapper layer
- presentation layer (ViewModel, contracts)

## Required Hilt setup in the host project

### 1. Add Hilt dependencies

```toml
# gradle/libs.versions.toml
[versions]
hilt = "2.51.1"

[libraries]
hilt-android = { group = "com.google.dagger", name = "hilt-android", version.ref = "hilt" }
hilt-compiler = { group = "com.google.dagger", name = "hilt-android-compiler", version.ref = "hilt" }

[plugins]
hilt = { id = "com.google.dagger.hilt.android", version.ref = "hilt" }
ksp = { id = "com.google.devtools.ksp", version = "2.0.21-1.0.28" }
```

```kotlin
// app/build.gradle.kts
plugins {
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
}

dependencies {
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
}
```

### 2. Annotate the Application class

```kotlin
@HiltAndroidApp
class MyApplication : Application()
```

### 3. Annotate entry points

```kotlin
@AndroidEntryPoint
class MainActivity : AppCompatActivity()
```

## Generated module structure

The DI agent produces one module per layer and places them under:

```
feature/{feature}/di/
    {Feature}DataModule.kt      ← repositories, data sources, services
    {Feature}DomainModule.kt    ← domain bindings (only if needed)
```

## Data module pattern

```kotlin
@Module
@InstallIn(SingletonComponent::class)
abstract class TodoDataModule {

    @Binds
    @Singleton
    abstract fun bindTodoRepository(
        impl: TodoRepositoryImpl
    ): TodoRepository

    @Binds
    @Singleton
    abstract fun bindTodoRemoteDataSource(
        impl: TodoRemoteDataSourceImpl
    ): TodoRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindTodoLocalDataSource(
        impl: TodoLocalDataSourceImpl
    ): TodoLocalDataSource

    companion object {
        @Provides
        @Singleton
        fun provideTodoService(retrofit: Retrofit): TodoService =
            retrofit.create(TodoService::class.java)

        @Provides
        @Singleton
        fun provideTodoSharedPreferences(
            @ApplicationContext context: Context
        ): SharedPreferences =
            context.getSharedPreferences("todo_prefs", Context.MODE_PRIVATE)
    }
}
```

## ViewModel pattern

```kotlin
@HiltViewModel
class TodoViewModel @Inject constructor(
    private val getTodoListUseCase: GetTodoListUseCase
) : MviViewModel<TodoIntent, TodoUiState, TodoUiEffect>() {
    // ...
}
```

## Implementation constructor pattern

```kotlin
class TodoRepositoryImpl @Inject constructor(
    private val remoteDataSource: TodoRemoteDataSource,
    private val localDataSource: TodoLocalDataSource,
    private val dispatcherProvider: DispatcherProvider
) : TodoRepository {
    // ...
}
```

## Qualifier pattern for multiple bindings

```kotlin
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class RemoteDataSource

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class LocalDataSource
```

## Scoping rules

| Binding target | Scope | Component |
|---|---|---|
| Repository | `@Singleton` | `SingletonComponent` |
| Remote / Local / Cache DataSource | `@Singleton` | `SingletonComponent` |
| Retrofit Service | `@Singleton` | `SingletonComponent` |
| SharedPreferences | `@Singleton` | `SingletonComponent` |
| ViewModel | managed by Hilt | `ViewModelComponent` (implicit) |

## Quick checklist

Before considering DI complete for a feature, verify:

- all interface/implementation pairs have a `@Binds` entry
- third-party objects are provided via `@Provides`
- no domain class accepts Android framework types
- ViewModels use `@HiltViewModel` and `@Inject constructor`
- implementation constructors use `@Inject constructor`
- module files are in `feature/{feature}/di/` package

## Where to look next

- For naming rules: `.github/skills/naming-conventions.skill.md`
- For layer boundaries: `docs/architecture/clean-architecture.md`
- For a concrete walkthrough: `docs/examples/end-to-end-example.md`


