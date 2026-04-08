# Agent System - Integration Points

This document explains how the agents integrate with the existing core library and with each other.

---

## 🔗 Core Library Integration

### Available Core Utilities (Already Implemented)

All agents MUST use these utilities instead of recreating them:

#### **Architecture Base**
- Location: `core/`
- `MviViewModel` - Base ViewModel for MVI pattern
- `UseCase` - Base interface for all use cases
- `Repository` - Marker interface for repositories
- `DataSource` - Marker interface for data sources

#### **Error Handling**
- Location: `core/error/`
- `AppError` - Exception hierarchy for all app errors
- Extension: `Throwable.toAppError()` - Converts exceptions to AppError

#### **Coroutines & Dispatchers**
- Location: `core/dispatchers/`
- `DispatcherProvider` - Abstraction for IO, Main, Default dispatchers
- Extension: `launchData()` - Helper for launching coroutines in ViewModel

#### **Flow & Reactive**
- Location: `core/flow/`
- `AsyncState<T>` - Loading/Success/Error state management
- `FlowExt` - Flow utility extensions

#### **Logging**
- Location: `core/logging/`
- `Logger` - Centralized logging system

#### **Security**
- Location: `core/security/`
- `Encryption` interface + `Base64Encryption`
- `Hash` object (SHA-256, MD5, salted hashing)
- `SecureStorage` - In-memory secret storage

#### **Utilities**
- Location: `core/utils/`
- `DateFormatter` - Date/time formatting
- `NetworkExt` - Network-related helpers
- `TestHelpers` - Testing utilities

---

## 🔀 Agent-to-Agent Integration Points

### Flow: How Data Moves Through Agents

```
┌────────────────────────────────────────────────────────────────┐
│                                                                  │
│  USER ACTION (UI)                                               │
│        ↓                                                         │
│  PRESENTATION AGENT                                             │
│  ├─ Intent (represents user action)                             │
│  └─ ViewModel (orchestrates via launchData)                     │
│        ↓                                                         │
│  MAPPER AGENT                                                   │
│  └─ toUi() (transforms domain to presentation)                  │
│        ↓                                                         │
│  DOMAIN AGENT                                                   │
│  ├─ UseCase (business logic)                                    │
│  └─ Repository Interface (domain contract)                      │
│        ↓                                                         │
│  MAPPER AGENT                                                   │
│  └─ toDomain() (transforms DTO to domain)                       │
│        ↓                                                         │
│  DATA LAYER AGENT                                               │
│  ├─ RepositoryImpl (implements domain interface)                 │
│  ├─ DataSource (network/local access)                           │
│  └─ Service (API calls)                                         │
│        ↓                                                         │
│  ENHANCEMENT AGENT (Optional)                                   │
│  ├─ Cache Layer                                                 │
│  └─ Retry Logic                                                 │
│        ↓                                                         │
│  UiState / UiEffect                                             │
│  └─ Back to UI                                                  │
│                                                                  │
└────────────────────────────────────────────────────────────────┘
```

---

## 📦 Dependencies Between Agents

### Agent 1: DATA LAYER AGENT
**Depends On:** Nothing (foundation)  
**Provides To:** Domain Agent, Mapper Agent

```
Outputs:
- UserService (Retrofit)
- UserDto
- UserDataSource
- UserDataSourceImpl
- UserRepositoryImpl (implements UserRepository)

Used By:
- Domain Agent (needs UserRepositoryImpl to create domain repository)
- Mapper Agent (needs UserDto for mapping)
```

### Agent 2: DOMAIN AGENT
**Depends On:** Data Layer Agent  
**Provides To:** Mapper Agent, Presentation Agent

```
Requires:
- UserRepositoryImpl from Data Layer

Outputs:
- UserRepository (interface)
- UserModel
- GetUserUseCase
- UpdateUserUseCase

Used By:
- Mapper Agent (needs UserModel for mapping)
- Presentation Agent (needs UseCase for business logic)
```

### Agent 3: MAPPER AGENT
**Depends On:** Data Layer Agent, Domain Agent  
**Provides To:** Presentation Agent

```
Requires:
- UserDto (Data Layer)
- UserModel (Domain Layer)
- UiModel specification (from Presentation requirements)

Outputs:
- UserMapper.kt with:
  - UserDto.toDomain(): UserModel
  - UserModel.toUi(): UserUiModel

Used By:
- Presentation Agent (needs mappers to transform data for UI)
- Data Layer Agent (uses toDomain in RepositoryImpl)
```

### Agent 4: PRESENTATION AGENT (MVI)
**Depends On:** Domain Agent, Mapper Agent  
**Provides To:** Test Agent

```
Requires:
- UseCase implementations (Domain Layer)
- Mapper functions (Mapper Agent)
- DispatcherProvider (from core)
- launchData helper (from core)

Outputs:
- UserIntent
- UserUiState
- UserUiEffect
- UserViewModel

Used By:
- Test Agent (needs ViewModel for testing)
- UI Layer (later phase)
```

### Agent 5: TEST AGENT
**Depends On:** All previous agents  
**Provides To:** Integration validation

```
Requires:
- UseCase (Domain Layer)
- RepositoryImpl (Data Layer)
- ViewModel (Presentation Layer)
- Fake implementations

Outputs:
- GetUserUseCaseTest
- UserRepositoryImplTest
- UserViewModelTest
- UserScreenTest (instrumented)

Validates:
- All agent outputs compile
- Business logic works correctly
- State transitions are correct
- Error handling works
```

### Agent 6: ENHANCEMENT AGENT (Optional)
**Depends On:** Existing architecture  
**Provides To:** Optimization phase

```
Requires:
- Existing UserRepositoryImpl
- Existing UserDataSourceImpl
- Clear enhancement requirements

Outputs (example - caching):
- UserCacheLayer
- Updated UserRepositoryImpl with cache integration
- Cache invalidation logic

Validates:
- No architecture breakage
- Backward compatible
- Performance improvements
```

---

## 🔌 File Naming & Location Convention

Each agent creates files in specific locations:

### Data Layer Agent
```
app/src/main/java/com/url/androidcore/data/{feature}/
├── datasource/
│   ├── {Feature}DataSource.kt (interface)
│   └── {Feature}DataSourceImpl.kt (implementation)
├── model/
│   └── {Feature}Dto.kt
├── remote/
│   └── {Feature}Service.kt
└── repository/
    └── {Feature}RepositoryImpl.kt
```

### Domain Agent
```
app/src/main/java/com/url/androidcore/domain/{feature}/
├── model/
│   └── {Feature}Model.kt
├── repository/
│   └── {Feature}Repository.kt
└── usecase/
    ├── Get{Feature}UseCase.kt
    ├── Create{Feature}UseCase.kt
    ├── Update{Feature}UseCase.kt
    └── Delete{Feature}UseCase.kt
```

### Mapper Agent
```
app/src/main/java/com/url/androidcore/presentation/{feature}/
└── mapper/
    └── {Feature}Mapper.kt
```

### Presentation Agent
```
app/src/main/java/com/url/androidcore/presentation/{feature}/
├── contract/
│   ├── {Feature}Intent.kt
│   ├── {Feature}UiState.kt
│   └── {Feature}UiEffect.kt
└── viewmodel/
    └── {Feature}ViewModel.kt
```

### Test Agent
```
app/src/test/java/com/url/androidcore/{feature}/
├── domain/
│   └── {Feature}UseCaseTest.kt
├── data/
│   ├── {Feature}RepositoryImplTest.kt
│   └── {Feature}DataSourceImplTest.kt
└── presentation/
    └── {Feature}ViewModelTest.kt

app/src/androidTest/java/com/url/androidcore/{feature}/
└── {Feature}ScreenTest.kt
```

---

## 🎯 How Agents Reference Each Other

### Data Layer Agent References
```kotlin
// Imports from Data Layer only
import com.url.androidcore.data.{feature}.model.{Feature}Dto
import com.url.androidcore.data.{feature}.remote.{Feature}Service
import com.url.androidcore.data.{feature}.datasource.{Feature}DataSource

// Uses from Core library
import com.url.androidcore.core.datasource.DataSource
import com.url.androidcore.core.error.AppError
import com.url.androidcore.core.network.NetworkExt.toAppError
```

### Domain Agent References
```kotlin
// Imports from Domain only
import com.url.androidcore.domain.{feature}.model.{Feature}Model
import com.url.androidcore.domain.{feature}.repository.{Feature}Repository

// Uses from Core library
import com.url.androidcore.core.usecase.UseCase
import com.url.androidcore.core.dispatchers.DispatcherProvider

// Does NOT import from Data or Presentation
```

### Mapper Agent References
```kotlin
// Imports from all layers (mapper bridges them)
import com.url.androidcore.data.{feature}.model.{Feature}Dto
import com.url.androidcore.domain.{feature}.model.{Feature}Model
import com.url.androidcore.presentation.{feature}.contract.{Feature}UiModel

// Extension functions provide the bridge
fun {Feature}Dto.toDomain(): {Feature}Model { ... }
fun {Feature}Model.toUi(): {Feature}UiModel { ... }
```

### Presentation Agent References
```kotlin
// IMPORTANT: Use MVI Contracts from Core
import com.url.androidcore.core.mvi.MviIntent
import com.url.androidcore.core.mvi.MviUiState
import com.url.androidcore.core.mvi.MviUiEffect

// Intent MUST extend MviIntent
sealed class {Feature}Intent : MviIntent {
    data object Load{Feature} : {Feature}Intent()
    // ...
}

// UiState MUST extend MviUiState
data class {Feature}UiState(
    // ...
) : MviUiState {
    companion object {
        fun initial() = {Feature}UiState()
    }
}

// UiEffect MUST extend MviUiEffect
sealed class {Feature}UiEffect : MviUiEffect {
    data class ShowError(val message: String) : {Feature}UiEffect()
    // ...
}

// Imports from Domain and Mapper
import com.url.androidcore.domain.{feature}.usecase.Get{Feature}UseCase
import com.url.androidcore.presentation.{feature}.mapper.toDomain
import com.url.androidcore.presentation.{feature}.mapper.toUi

// Uses from Core library
import com.url.androidcore.core.mvi.MviViewModel
import com.url.androidcore.core.dispatchers.DispatcherProvider
import com.url.androidcore.core.logging.Logger

// ViewModel MUST extend MviViewModel with the three contracts
class {Feature}ViewModel(
    private val useCase: Get{Feature}UseCase,
    private val dispatchers: DispatcherProvider,
    private val logger: Logger
) : MviViewModel<{Feature}Intent, {Feature}UiState, {Feature}UiEffect>() {
    // ...
}

// Uses helper from Core
launchData { /* ... */ }

// Does NOT import from Data layer directly
```

### Test Agent References
```kotlin
// Imports from all layers for testing
import com.url.androidcore.data.{feature}.repository.{Feature}RepositoryImpl
import com.url.androidcore.domain.{feature}.usecase.Get{Feature}UseCase
import com.url.androidcore.presentation.{feature}.viewmodel.{Feature}ViewModel

// Uses from Testing utilities
import com.url.androidcore.core.testing.TestHelpers
import com.url.androidcore.core.dispatchers.TestDispatcherProvider
```

---

## ✅ Integration Validation Checklist

After each agent completes, verify integration points:

### After DATA LAYER
- [ ] Service interface compiles
- [ ] DTO models are serializable (Gson-compatible)
- [ ] DataSource handles errors with AppError
- [ ] RepositoryImpl is accessible to Domain Agent

### After DOMAIN
- [ ] Repository interface matches DataSource interface
- [ ] Models are pure Kotlin (no Android imports)
- [ ] UseCase extends core UseCase
- [ ] All exceptions are AppError

### After MAPPER
- [ ] Extension functions compile
- [ ] toDomain() and toUi() work bidirectionally
- [ ] No business logic in mappers
- [ ] Naming follows conventions

### After PRESENTATION
- [ ] Intent extends MviIntent
- [ ] UiState extends MviUiState
- [ ] UiEffect extends MviUiEffect
- [ ] ViewModel extends MviViewModel<Intent, UiState, UiEffect>
- [ ] launchData() is used correctly
- [ ] State is immutable (data class)
- [ ] Effects are one-time events
- [ ] Intent represents all user actions

### After TEST
- [ ] All tests compile
- [ ] Success and error cases covered
- [ ] State transitions validated
- [ ] Mock implementations work
- [ ] Tests use TestDispatcherProvider

### After ENHANCEMENT
- [ ] No architectural changes
- [ ] Backward compatible
- [ ] Cache/retry logic integrated
- [ ] Performance metrics improved

---

## 🔄 Common Integration Patterns

### Pattern 1: Simple Fetch
```
UserDataSource.getUser() 
  → UserRepositoryImpl.getUser() 
  → GetUserUseCase.invoke() 
  → UserViewModel.handleIntent(LoadUser) 
  → UserMapper.toUi() 
  → UserUiState updated
```

### Pattern 2: Create with Validation
```
UserIntent.CreateUser 
  → ViewModel validates 
  → CreateUserUseCase.invoke() 
  → Repository.create() 
  → DataSource.create() 
  → Service.post() 
  → Error mapping with toAppError()
  → UiEffect.ShowError or UiState.Success
```

### Pattern 3: Cached Read
```
UserDataSource.getUser() 
  → Check UserCache 
  → Hit: return cached data 
  → Miss: fetch from Service 
  → Cache result 
  → RepositoryImpl returns data 
  → ViewModel emits UiState
```

---

## 🚀 Ready for Integration

The multi-agent system is fully configured with:
- ✅ Clear separation of concerns
- ✅ Dependency flow defined
- ✅ Integration points documented
- ✅ File locations specified
- ✅ Reference patterns established
- ✅ Validation checkpoints defined

**Next Step:** Define your first feature and request the DATA LAYER AGENT to begin generation.


