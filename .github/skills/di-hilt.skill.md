---
id: di-hilt
type: skill
version: 1.0.0
schema_version: 1
updated_at: 2026-04-09
---

# di-hilt

**Description:** Rules and patterns for wiring Dagger Hilt dependency injection across all layers.

## Rules
- Always use Dagger Hilt as the DI framework; never manually construct dependency graphs.
- Annotate the `Application` class with `@HiltAndroidApp`; annotate Android entry points (Activity, Fragment, Service) with `@AndroidEntryPoint`.
- Create one Hilt module per feature layer, placed in the `feature/{feature}/di/` package.
- Name data-layer modules `{Feature}DataModule` and domain-layer modules `{Feature}DomainModule`; combine into `{Feature}Module` only when the feature has a single layer of bindings.
- Use `@InstallIn(SingletonComponent::class)` for singleton-scoped bindings (repositories, remote/local/cache data sources, Retrofit services, SharedPreferences).
- Use `@InstallIn(ViewModelComponent::class)` for ViewModel-scoped bindings when a dependency must not outlive the ViewModel.
- Bind interfaces to their implementations exclusively with `@Binds` inside `abstract` module classes to avoid unnecessary allocation.
- Provide third-party objects, configured instances, or objects that cannot have `@Inject` constructors via `@Provides` functions.
- Annotate all ViewModel constructors with `@HiltViewModel` and `@Inject constructor`; never create ViewModel instances manually.
- Annotate repository and data source implementation constructors with `@Inject constructor` when they have a single concrete implementation.
- Use `@Singleton` together with `@Provides` or `@Binds` on any binding that must be shared application-wide.
- Use `@Qualifier` (custom annotation) or `@Named` to distinguish multiple bindings of the same interface or type.
- Never inject Android UI types or Context into domain classes; pass Context only to data-layer or infrastructure providers.
- Keep module files thin: they must not contain business logic, only binding declarations.

## Naming conventions
- `{Feature}DataModule` – data-layer bindings (repositories, data sources, services)
- `{Feature}DomainModule` – domain-layer bindings (use case factory overrides, if any)
- `{Feature}Module` – single combined module for small features
- Package path: `com.url.androidcore.feature.{feature}.di`

## Examples
```kotlin
// Data module — abstract class so @Binds works
@Module
@InstallIn(SingletonComponent::class)
abstract class TodoDataModule {

    @Binds
    @Singleton
    abstract fun bindTodoRepository(impl: TodoRepositoryImpl): TodoRepository

    @Binds
    @Singleton
    abstract fun bindTodoRemoteDataSource(impl: TodoRemoteDataSourceImpl): TodoRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindTodoLocalDataSource(impl: TodoLocalDataSourceImpl): TodoLocalDataSource

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

// ViewModel injection
@HiltViewModel
class TodoViewModel @Inject constructor(
    private val getTodoListUseCase: GetTodoListUseCase
) : MviViewModel<TodoIntent, TodoUiState, TodoUiEffect>()
```


