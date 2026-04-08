# Multi-Agent System for Android Code Generation

## Overview

This is a multi-agent system designed to generate production-ready Android code using Clean Architecture and MVI pattern. Each agent has a specific responsibility and operates independently within its scope.

---

## Agent Responsibilities

### 1. DATA LAYER AGENT

**Responsibility:** Generate backend data contracts and local data access layer.

**Input:**
- API endpoint specification (JSON/REST description)
- Data model requirements

**Output:**
- Retrofit service interface (`*Service.kt`)
- DTO models (`*Dto.kt`)
- DataSource interface (`*DataSource.kt`)
- DataSource implementation (`*DataSourceImpl.kt`)
- Repository implementation (data layer) (`*RepositoryImpl.kt`)

**Rules:**
- Use `@Suppress("UNCHECKED_CAST")` if necessary for type safety
- Apply safe call operators or use existing coroutine helpers
- Keep DTO separated from domain models
- No business logic - only data access
- Use `Flow<T>` for reactive streams
- Handle network exceptions with try-catch
- Map API errors to `AppError` using existing error handling

**Package Structure:**
```
data/
  └─ {feature}/
     ├─ datasource/
     │  ├─ {feature}DataSource.kt (interface)
     │  └─ {feature}DataSourceImpl.kt (implementation)
     ├─ model/
     │  └─ {feature}Dto.kt
     ├─ repository/
     │  └─ {feature}RepositoryImpl.kt
     └─ remote/
        └─ {feature}Service.kt
```

---

### 2. DOMAIN AGENT

**Responsibility:** Define business logic contracts and use cases (pure, no Android dependencies).

**Input:**
- Data layer contracts (Repository interface)
- Business requirements
- Domain models specification

**Output:**
- Repository interface (domain) (`*Repository.kt`)
- Domain models (`*Model.kt`)
- UseCase(s) (`*UseCase.kt`)

**Rules:**
- MUST be pure Kotlin (no Android dependencies)
- All exceptions must extend `AppError`
- Use `UseCase` base class from core library
- One responsibility per UseCase
- Return `Flow<T>` for reactive operations
- Use suspension for single operations (`suspend fun`)
- No UI concerns - only business logic

**Package Structure:**
```
domain/
  └─ {feature}/
     ├─ model/
     │  └─ {feature}Model.kt
     ├─ repository/
     │  └─ {feature}Repository.kt
     └─ usecase/
        ├─ Get{Feature}UseCase.kt
        ├─ Create{Feature}UseCase.kt
        ├─ Update{Feature}UseCase.kt
        └─ Delete{Feature}UseCase.kt
```

---

### 3. MAPPER AGENT

**Responsibility:** Transform data between layers using extension functions.

**Input:**
- DTO models (from Data Layer Agent)
- Domain models (from Domain Agent)
- UI state requirements (from Presentation Agent)

**Output:**
- DTO → Domain mappers
- Domain → UI mappers

**Rules:**
- ONLY extension functions (no mapper classes)
- Naming convention:
  - `DtoModel.toDomain(): DomainModel`
  - `DomainModel.toUi(): UiModel`
  - `DtoModel.toUi(): UiModel` (if needed)
- Keep mapping simple and explicit
- No business logic in mappers
- One extension function per transformation

**Package Structure:**
```
presentation/
  └─ {feature}/
     └─ mapper/
        └─ {feature}Mapper.kt
```

---

### 4. PRESENTATION AGENT (MVI)

**Responsibility:** Generate UI layer components following MVI pattern.

**Input:**
- Domain layer contracts (Repository, UseCase)
- UI requirements and user interactions

**Output:**
- Intent sealed class (`{Feature}Intent.kt`)
- UiState data class (`{Feature}UiState.kt`)
- UiEffect sealed class (`{Feature}UiEffect.kt`)
- ViewModel (`{Feature}ViewModel.kt`)

**Rules:**
- MUST extend `MviViewModel<Intent, UiState, UiEffect>`
- Use `launchData()` helper (do NOT reimplement)
- State must be immutable (use `data class` with `copy()`)
- Effects are one-time events (sealed class)
- Intents are user actions (sealed class)
- No UI styling - minimal and functional only
- Handle `CancellationException` properly
- Use injected `DispatcherProvider` for coroutines

**Package Structure:**
```
presentation/
  └─ {feature}/
     ├─ contract/
     │  ├─ {Feature}Intent.kt
     │  ├─ {Feature}UiState.kt
     │  └─ {Feature}UiEffect.kt
     └─ viewmodel/
        └─ {Feature}ViewModel.kt
```

**MVI Flow:**
```
User Action (UI) → Intent → ViewModel → UseCase → Repository
                                           ↓
                                      Emit UiState (View Updates)
                                      Emit UiEffect (Navigation, Toasts, etc.)
```

---

### 5. TEST AGENT

**Responsibility:** Generate comprehensive unit and integration tests.

**Input:**
- UseCase implementations
- Repository implementations
- ViewModel implementations

**Output:**
- UseCase unit tests
- Repository unit tests
- ViewModel tests (state transitions and effects)
- Integration tests (if applicable)

**Rules:**
- Use fake/mock implementations
- Cover success and error cases
- Validate MVI state transitions
- Use coroutine test utilities (`runTest`, `advanceUntilIdle`)
- Test exception handling and error mapping
- Assert on state changes and effects
- Use descriptive test names

**Package Structure:**
```
test/
  └─ java/com/url/androidcore/
     ├─ {feature}/
     │  ├─ domain/
     │  │  └─ {Feature}UseCaseTest.kt
     │  ├─ data/
     │  │  ├─ {Feature}RepositoryImplTest.kt
     │  │  └─ {Feature}DataSourceImplTest.kt
     │  └─ presentation/
     │     └─ {Feature}ViewModelTest.kt

androidTest/
  └─ java/com/url/androidcore/
     └─ {feature}/
        └─ {Feature}ScreenTest.kt
```

---

### 6. DEPENDENCY INJECTION AGENT

**Responsibility:** Generate Dagger Hilt dependency injection modules and components for the entire application.

**Input:**
- Existing feature components (Services, Repositories, UseCases, ViewModels)
- Application class requirements
- Scoping requirements

**Output:**
- Application class with `@HiltAndroidApp`
- Network module (Retrofit, OkHttp)
- Data module (DataSources, Repositories)
- Domain module (UseCases)
- Presentation module (ViewModels)
- Core module (Dispatchers, Logger, etc.)

**Rules:**
- Use `@Singleton` for application-scoped dependencies
- Use `@ViewModelScoped` for ViewModel dependencies
- Use `@Provides` for third-party libraries
- Use `@Binds` for interface implementations
- Follow Hilt best practices
- Include proper qualifiers for multiple implementations
- Document module responsibilities

**Package Structure:**
```
di/
  ├─ module/
  │  ├─ CoreModule.kt
  │  ├─ NetworkModule.kt
  │  ├─ DataModule.kt
  │  ├─ DomainModule.kt
  │  └─ PresentationModule.kt
  └─ Application.kt
```

---

### 7. ENHANCEMENT AGENT

**Responsibility:** Add optional features and optimizations to existing architecture.

**Input:**
- Existing architecture components
- Enhancement requirement (e.g., "add cache", "add retry", "transform data")

**Output:**
- Cache layer (memory or disk-based)
- Retry logic with exponential backoff
- Data transformation layers
- Updated repository/datasource

**Rules:**
- Do NOT break existing architecture
- Changes must be backward compatible
- Integrate at Repository or DataSource level
- Keep changes minimal and maintainable
- Document new behavior

**Common Enhancements:**
- **Caching:** Add memory/disk cache layer
- **Retry:** Implement retry logic with backoff
- **Transformation:** Add data transformation pipeline
- **Pagination:** Add offset/limit pagination
- **Filtering:** Add data filtering capability

---

## Workflow

### Step 1: Define the Feature
Provide feature specification including:
- Data model/API contract
- Business requirements
- UI interactions

### Step 2: Data Layer Agent
Generate data access layer components.

### Step 3: Domain Agent
Generate business logic layer components.

### Step 4: Mapper Agent
Generate transformation functions between layers.

### Step 5: Presentation Agent
Generate MVI components.

### Step 6: Test Agent
Generate comprehensive tests.

### Step 7: Dependency Injection Agent
Generate Dagger Hilt modules and components.

### Step 8: Enhancement Agent (Optional)
Add caching, retry, or other optimizations.

---

## Code Quality Standards

### All Agents MUST Follow:

✅ **Idiomatic Kotlin**
- Use data classes for models
- Use sealed classes for restricted hierarchies
- Use extension functions for utilities
- Use scope functions (`let`, `apply`, `run`)

✅ **Error Handling**
- Use existing `AppError` from core
- Map exceptions to `AppError`
- Use `Throwable.toAppError()` extension

✅ **Coroutines**
- Use `Flow<T>` for streams
- Use `suspend fun` for single operations
- Handle `CancellationException`
- Respect `DispatcherProvider`

✅ **Testing**
- Write testable code
- Mock external dependencies
- Verify behavior, not implementation

✅ **Documentation**
- Add KDoc comments for public APIs
- Document non-obvious logic
- Include usage examples in comments

---

## Naming Conventions

| Component | Pattern | Example |
|-----------|---------|---------|
| Feature Name | PascalCase | User, Product, Order |
| Intent | `{Feature}Intent` | `UserIntent` |
| UiState | `{Feature}UiState` | `UserUiState` |
| UiEffect | `{Feature}UiEffect` | `UserUiEffect` |
| ViewModel | `{Feature}ViewModel` | `UserViewModel` |
| UseCase | `{Action}{Feature}UseCase` | `GetUserUseCase`, `CreateUserUseCase` |
| Repository | `{Feature}Repository` | `UserRepository` |
| DataSource | `{Feature}DataSource` | `UserDataSource` |
| DTO | `{Feature}Dto` | `UserDto` |
| Domain Model | `{Feature}Model` or `{Feature}` | `UserModel`, `User` |
| Service | `{Feature}Service` | `UserService` |
| Mapper | `{Feature}Mapper.kt` (functions inside) | `UserMapper.kt` |

---

## Core Library Integration

The following utilities are already available and should NOT be recreated:

### From `core/`:
- `MviViewModel` - Base ViewModel class
- `UseCase` - Base UseCase interface
- `AppError` - Exception hierarchy
- `Throwable.toAppError()` - Error mapping
- `DispatcherProvider` - Coroutine dispatchers
- `Logger` - Logging utility
- `launchData()` - Coroutine helper
- `Flow` utilities - Reactive helpers

### Security (from `core/security/`):
- `Encryption` interface
- `Base64Encryption`
- `Hash` object
- `SecureStorage`

---

## Output Validation Checklist

Before considering an agent's work complete:

- [ ] All files created with correct package structure
- [ ] All imports are correct and complete
- [ ] Code compiles without errors
- [ ] No recreation of core library utilities
- [ ] Follows naming conventions
- [ ] Follows Clean Architecture layers
- [ ] Handles errors properly with `AppError`
- [ ] Uses coroutines correctly
- [ ] Tests cover success and error cases
- [ ] KDoc comments for public APIs

---

## Quick Reference

### Create a New Feature
```
1. DATA LAYER AGENT → Creates data/{feature}/*
2. DOMAIN AGENT → Creates domain/{feature}/*
3. MAPPER AGENT → Creates presentation/{feature}/mapper/*
4. PRESENTATION AGENT → Creates presentation/{feature}/* (MVI)
5. TEST AGENT → Creates test/* (unit tests)
6. DEPENDENCY INJECTION AGENT → Creates di/module/*
7. ENHANCEMENT AGENT → Optional improvements
```

### Integration Point
All agents integrate through:
- **Repository interface** (domain contract)
- **UseCase** (business logic)
- **ViewModel** (UI orchestration)

---

## Questions or Clarifications?

When starting a feature:
1. Ensure all requirements are documented
2. Reference this guide for agent responsibilities
3. Follow the workflow step-by-step
4. Validate output before moving to next agent

