# Multi-Agent System - Visual Workflow Guide

## 🎯 Complete Feature Generation Workflow

```
START
  │
  ├─────────────────────────────────────────────────────────────┐
  │                                                               │
  │ STEP 0: PREPARE FEATURE SPECIFICATION                        │
  │ ─────────────────────────────────────────────────            │
  │ ✏️  Feature Name: UserProfile                                │
  │ ✏️  API: GET /api/v1/users/{id}                              │
  │ ✏️  Models: User, UserResponse                               │
  │ ✏️  Business Rules: Validation, caching                      │
  │ ✏️  UI Interactions: Load, refresh, error handling           │
  │                                                               │
  └─────────────────────────────────────────────────────────────┘
                         ↓
  ┌─────────────────────────────────────────────────────────────┐
  │ PHASE 1️⃣  DATA LAYER AGENT                                   │
  │ ─────────────────────────────────────────────────            │
  │ Creates: Network & Data Access Layer                         │
  │                                                               │
  │ 📄 INPUT:                                                    │
  │  └─ API specification                                        │
  │  └─ Data models                                              │
  │                                                               │
  │ 📦 OUTPUT:                                                   │
  │  ├─ UserService.kt (Retrofit interface)                     │
  │  ├─ UserDto.kt (Data model)                                 │
  │  ├─ UserDataSource.kt (Interface)                           │
  │  ├─ UserDataSourceImpl.kt (Implementation)                   │
  │  └─ UserRepositoryImpl.kt (Implements domain interface)      │
  │                                                               │
  │ ⏱️  Time: ~15 minutes                                        │
  │ ✅ Validation: All files compile, imports correct           │
  │                                                               │
  └─────────────────────────────────────────────────────────────┘
                         ↓
  ┌─────────────────────────────────────────────────────────────┐
  │ PHASE 2️⃣  DOMAIN AGENT                                       │
  │ ─────────────────────────────────────────────────            │
  │ Creates: Business Logic Layer                                │
  │                                                               │
  │ 📄 INPUT:                                                    │
  │  └─ UserRepositoryImpl from Phase 1                          │
  │  └─ Business requirements                                    │
  │  └─ Domain models spec                                       │
  │                                                               │
  │ 📦 OUTPUT:                                                   │
  │  ├─ UserRepository.kt (Domain interface)                    │
  │  ├─ UserModel.kt (Domain model)                             │
  │  ├─ GetUserUseCase.kt                                       │
  │  └─ RefreshUserUseCase.kt (if needed)                       │
  │                                                               │
  │ ⏱️  Time: ~15 minutes                                        │
  │ ✅ Validation: No Android imports, pure Kotlin              │
  │                                                               │
  └─────────────────────────────────────────────────────────────┘
                         ↓
  ┌─────────────────────────────────────────────────────────────┐
  │ PHASE 3️⃣  MAPPER AGENT                                       │
  │ ─────────────────────────────────────────────────            │
  │ Creates: Data Transformation Layer                           │
  │                                                               │
  │ 📄 INPUT:                                                    │
  │  └─ UserDto.kt from Phase 1                                 │
  │  └─ UserModel.kt from Phase 2                               │
  │  └─ UI state requirements                                    │
  │                                                               │
  │ 📦 OUTPUT:                                                   │
  │  └─ UserMapper.kt with:                                     │
  │     ├─ fun UserDto.toDomain(): UserModel                    │
  │     ├─ fun UserModel.toUi(): UserUiModel                    │
  │     └─ fun UserDto.toUi(): UserUiModel                      │
  │                                                               │
  │ ⏱️  Time: ~5 minutes                                         │
  │ ✅ Validation: Extension functions compile, no logic        │
  │                                                               │
  └─────────────────────────────────────────────────────────────┘
                         ↓
  ┌─────────────────────────────────────────────────────────────┐
  │ PHASE 4️⃣  PRESENTATION AGENT (MVI)                           │
  │ ─────────────────────────────────────────────────            │
  │ Creates: UI Layer (MVI Pattern)                              │
  │                                                               │
  │ 📄 INPUT:                                                    │
  │  └─ GetUserUseCase from Phase 2                             │
  │  └─ UserMapper from Phase 3                                 │
  │  └─ UI interactions requirements                             │
  │                                                               │
  │ 📦 OUTPUT:                                                   │
  │  ├─ UserIntent.kt (sealed class)                            │
  │  │  ├─ LoadUser                                              │
  │  │  ├─ RefreshUser                                           │
  │  │  └─ ClearError                                            │
  │  │                                                            │
  │  ├─ UserUiState.kt (data class)                             │
  │  │  ├─ isLoading: Boolean                                    │
  │  │  ├─ user: UserUiModel?                                    │
  │  │  └─ error: String?                                        │
  │  │                                                            │
  │  ├─ UserUiEffect.kt (sealed class)                          │
  │  │  ├─ ShowError(message)                                    │
  │  │  └─ NavigateToDetail                                      │
  │  │                                                            │
  │  └─ UserViewModel.kt                                         │
  │     ├─ extends MviViewModel                                  │
  │     ├─ uses launchData()                                     │
  │     └─ manages Intent → State → Effect                       │
  │                                                               │
  │ ⏱️  Time: ~20 minutes                                        │
  │ ✅ Validation: ViewModel extends MviViewModel, uses launchData│
  │                                                               │
  └─────────────────────────────────────────────────────────────┘
                         ↓
  ┌─────────────────────────────────────────────────────────────┐
  │ PHASE 5️⃣  TEST AGENT                                         │
  │ ─────────────────────────────────────────────────            │
  │ Creates: Comprehensive Test Suite                            │
  │                                                               │
  │ 📄 INPUT:                                                    │
  │  └─ All implementations from Phases 1-4                     │
  │                                                               │
  │ 📦 OUTPUT:                                                   │
  │  ├─ GetUserUseCaseTest.kt                                   │
  │  │  ├─ testSuccess()                                         │
  │  │  └─ testError()                                           │
  │  │                                                            │
  │  ├─ UserRepositoryImplTest.kt                               │
  │  │  ├─ testGetUserSuccess()                                 │
  │  │  └─ testGetUserError()                                    │
  │  │                                                            │
  │  ├─ UserDataSourceImplTest.kt                               │
  │  │  ├─ testNetworkCall()                                     │
  │  │  └─ testErrorHandling()                                   │
  │  │                                                            │
  │  └─ UserViewModelTest.kt                                    │
  │     ├─ testLoadStateTransitions()                           │
  │     ├─ testErrorEffect()                                     │
  │     └─ testRefresh()                                         │
  │                                                               │
  │ ⏱️  Time: ~30 minutes                                        │
  │ ✅ Validation: All tests compile, cover success & error     │
  │                                                               │
  └─────────────────────────────────────────────────────────────┘
                         ↓
  ┌─────────────────────────────────────────────────────────────┐
  │ PHASE 6️⃣  ENHANCEMENT AGENT (OPTIONAL)                       │
  │ ─────────────────────────────────────────────────            │
  │ Creates: Performance & Resilience Features                   │
  │                                                               │
  │ 📄 INPUT:                                                    │
  │  └─ Enhancement requirement (e.g., "add cache")             │
  │  └─ Existing architecture                                    │
  │                                                               │
  │ 📦 OUTPUT (Example - Cache):                                 │
  │  ├─ UserCache.kt (Cache layer)                              │
  │  ├─ UserDataSourceImpl.kt (updated)                          │
  │  └─ UserRepositoryImpl.kt (updated)                          │
  │                                                               │
  │ ⏱️  Time: Varies by enhancement                              │
  │ ✅ Validation: No architecture changes, backward compatible  │
  │                                                               │
  └─────────────────────────────────────────────────────────────┘
                         ↓
  ┌─────────────────────────────────────────────────────────────┐
  │ ✅ COMPLETE - READY FOR DEPLOYMENT                           │
  │ ─────────────────────────────────────────────────            │
  │                                                               │
  │ 📊 DELIVERABLES:                                             │
  │  ├─ 5 Data Layer Files                                       │
  │  ├─ 3-5 Domain Layer Files                                   │
  │  ├─ 1 Mapper File (with 3+ extension functions)             │
  │  ├─ 4 Presentation Layer Files (MVI)                        │
  │  ├─ 4-5 Test Files (unit + integration)                     │
  │  └─ (Optional) 1-3 Enhancement Files                         │
  │                                                               │
  │ 📈 METRICS:                                                  │
  │  ├─ Total Files: 20-30                                       │
  │  ├─ Total Code: 2,000+ lines                                 │
  │  ├─ Architecture: 100% Clean Architecture                    │
  │  ├─ Test Coverage: 80%+                                      │
  │  └─ Quality: Production-Ready                                │
  │                                                               │
  └─────────────────────────────────────────────────────────────┘
                         ↓
                        END
```

---

## 📊 Data Flow Through the System

```
┌─────────┐
│   UI    │  User clicks "Load Profile"
└────┬────┘
     │
     └──────► UserIntent.LoadUser
               │
               ├─────────────────────────────────────────┐
               │                                          │
            UserViewModel                               │
         (Receives Intent)                              │
               │                                         │
               └──► launchData {                         │
                      GetUserUseCase.invoke("123")       │
                    }                                    │
                    │                                     │
                    └──────────────────────────────────┤
                                                        │
                                                 UseCase
                                              (Business Logic)
                                                   │
                                                   ├─ Validate ID
                                                   ├─ Call Repository
                                                   └─ Catch errors
                                                        │
                                                        └──────────┤
                                                                  │
                                                            UserRepository
                                                          (Domain Interface)
                                                                  │
                                                                  ├─ Call DataSource
                                                                  └─ Handle response
                                                                        │
                                                                        └──────────┤
                                                                                  │
                                                                        UserDataSourceImpl
                                                                      (Network Access)
                                                                                  │
                                                                                  ├─ Call API
                                                                                  └─ Return DTO
                                                                                        │
                                                                                        └──────────┐
                                                                                                   │
         ┌─────────────────────────────────────────────────────────────────────────────────────┘
         │
      UserRepositoryImpl
      (Calls Mapper)
         │
         ├─ UserDto.toDomain()  ◄─── Mapper Agent
         └─ Result: UserModel
               │
               └───────────────────────────────────┐
                                                    │
                                        Back to UseCase
                                        Return UserModel
                                                    │
                                                    └───────────────────────────────┐
                                                                                     │
                                                                            Back to ViewModel
                                                                                     │
                                                                                     ├─ onSuccess:
                                                                                     │   ├─ Mapper.toUi()
                                                                                     │   └─ setState()
                                                                                     │
                                                                                     └─ onError:
                                                                                        ├─ setError()
                                                                                        └─ setEffect()
                                                                                              │
                                                                                              └─────────────┐
                                                                                                            │
                                                                                         UserUiState Updated
                                                                                         UserUiEffect Emitted
                                                                                                            │
                                                                                                            └─ Back to UI
                                                                                                               │
                                                                                                               └──► UI Re-renders
                                                                                                                   with new data
```

---

## 🔄 Agent Interaction Diagram

```
┌──────────────────────────────────────────────────────────────┐
│                    USER PROVIDES SPEC                        │
│              (Feature name, API, models, rules)              │
└────────────────────┬─────────────────────────────────────────┘
                     │
                     ▼
        ┌────────────────────────┐
        │  DATA LAYER AGENT      │
        │  (Network Access)      │
        │                        │
        │ Output:                │
        │  • Service             │
        │  • DTO                 │
        │  • DataSource          │
        │  • Repository (Data)   │
        └────────────┬───────────┘
                     │
                     ▼
        ┌────────────────────────┐
        │   DOMAIN AGENT         │
        │  (Business Logic)      │
        │                        │
        │ Input: RepositoryImpl   │
        │ Output:                │
        │  • Repository (Domain) │
        │  • Model               │
        │  • UseCase             │
        └────────────┬───────────┘
                     │
          ┌──────────┴──────────┐
          │                     │
          ▼                     ▼
   ┌─────────────────┐  ┌─────────────────┐
   │  MAPPER AGENT   │  │  TEST AGENT     │
   │ (Transform)     │  │  (Validation)   │
   │                 │  │                 │
   │ Input:          │  │ Input: All      │
   │  • DTO          │  │  • UseCase      │
   │  • Model        │  │  • Repository   │
   │                 │  │  • ViewModel    │
   │ Output:         │  │                 │
   │  • Extensions   │  │ Output:         │
   │  • Mappers      │  │  • Tests        │
   └────────┬────────┘  └────────┬────────┘
            │                    │
            └──────────┬─────────┘
                       │
                       ▼
        ┌────────────────────────────────┐
        │ PRESENTATION AGENT (MVI)       │
        │     (UI Layer)                 │
        │                                │
        │ Input:                         │
        │  • UseCase                     │
        │  • Mapper functions            │
        │                                │
        │ Output:                        │
        │  • Intent                      │
        │  • UiState                     │
        │  • UiEffect                    │
        │  • ViewModel                   │
        └────────────┬───────────────────┘
                     │
                     ▼
        ┌────────────────────────┐
        │ ENHANCEMENT AGENT      │
        │     (Optional)         │
        │                        │
        │ Add: Cache, Retry,     │
        │      Optimization      │
        └────────────┬───────────┘
                     │
                     ▼
             ✅ PRODUCTION READY
```

---

## ⏱️ Time Breakdown

```
Phase 1: DATA LAYER AGENT       ████████░░░░░░░░░░░ 15 min
Phase 2: DOMAIN AGENT            ████████░░░░░░░░░░░ 15 min
Phase 3: MAPPER AGENT            ██░░░░░░░░░░░░░░░░░  5 min
Phase 4: PRESENTATION AGENT      ██████████░░░░░░░░░ 20 min
Phase 5: TEST AGENT              ███████████████░░░░░ 30 min
Phase 6: ENHANCEMENT (Optional)   ░░░░░░░░░░░░░░░░░░░ Varies

TOTAL (Phases 1-5):              ░░░░░░░░░░░░░░░░░░░ ~85 min (1.5 hrs)
TOTAL (All 6 phases):            ░░░░░░░░░░░░░░░░░░░ ~115 min (2 hrs)
```

---

## 📁 File Structure Generated

```
feature/user-profile
│
├── data/user/
│   ├── datasource/
│   │   ├── UserDataSource.kt           (Interface)
│   │   └── UserDataSourceImpl.kt        (Implementation)
│   ├── model/
│   │   └── UserDto.kt                  (Network model)
│   ├── remote/
│   │   └── UserService.kt              (Retrofit interface)
│   └── repository/
│       └── UserRepositoryImpl.kt        (Data layer repository)
│
├── domain/user/
│   ├── model/
│   │   └── UserModel.kt                (Domain model)
│   ├── repository/
│   │   └── UserRepository.kt           (Domain interface)
│   └── usecase/
│       ├── GetUserUseCase.kt
│       └── RefreshUserUseCase.kt
│
├── presentation/user/
│   ├── contract/
│   │   ├── UserIntent.kt               (User actions)
│   │   ├── UserUiState.kt              (UI state)
│   │   └── UserUiEffect.kt             (Side effects)
│   ├── mapper/
│   │   └── UserMapper.kt               (Transformation)
│   └── viewmodel/
│       └── UserViewModel.kt            (MVI orchestrator)
│
├── test/
│   ├── domain/
│   │   └── GetUserUseCaseTest.kt
│   ├── data/
│   │   ├── UserRepositoryImplTest.kt
│   │   └── UserDataSourceImplTest.kt
│   └── presentation/
│       └── UserViewModelTest.kt
│
└── (Optional) enhancement/
    ├── cache/
    │   └── UserCache.kt
    └── retry/
        └── UserRetryPolicy.kt

TOTAL: 20-30 Files, 2000+ Lines of Code
```

---

## ✅ Quality Checkpoints

```
Phase 1 ─────────► Code Compiles?        ✅
                 Imports Correct?        ✅
                 Error Handling?         ✅
                         │
Phase 2 ─────────► No Android Imports?   ✅
                 Pure Kotlin?            ✅
                 UseCase Base Class?     ✅
                         │
Phase 3 ─────────► Extension Functions?  ✅
                 Mapping Logic?          ✅
                 No Business Logic?      ✅
                         │
Phase 4 ─────────► MviViewModel?         ✅
                 launchData() Used?      ✅
                 Immutable State?        ✅
                         │
Phase 5 ─────────► Tests Compile?        ✅
                 Success Cases?          ✅
                 Error Cases?            ✅
                 Mock Implementations?   ✅
                         │
Phase 6 ─────────► No Architecture Break? ✅
(Optional)       Backward Compatible?   ✅
                 Performance Improved?   ✅
                         │
            ✅ READY FOR PRODUCTION ✅
```

---

## 🎯 Decision Guide

```
START
  │
  ├─ "I need to understand the system first"
  │   └─► Read AGENT_QUICK_INDEX.md
  │       Read AGENT_SYSTEM_SETUP.md
  │
  ├─ "I'm ready to generate a feature"
  │   └─► Prepare feature spec
  │       Follow AGENT_EXECUTION_GUIDE.md
  │       Request DATA LAYER AGENT
  │
  ├─ "I need to see code examples"
  │   └─► Review AGENT_TEMPLATES.md
  │
  ├─ "I need to understand dependencies"
  │   └─► Read AGENT_INTEGRATION_POINTS.md
  │
  └─ "I'm stuck and need help"
      └─► Check AGENT_QUICK_INDEX.md troubleshooting
          section
```

---

**Ready to start? Follow the workflow above and request the DATA LAYER AGENT! 🚀**


