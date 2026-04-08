# MVI Contracts Usage Guide

## Overview

The AndroidCore library provides three core MVI contract interfaces that **MUST** be used for all MVI components.

These interfaces ensure type safety and consistency across the entire application.

## The Three MVI Contracts

Located in: `com.url.androidcore.core.mvi`

### 1. MviIntent

**Purpose:** Define all possible user interactions/actions in your feature.

**Requirements:**
- MUST be a sealed class
- MUST extend `MviIntent` interface
- Each subclass represents a single user action

**Example:**
```kotlin
package com.url.androidcore.presentation.user.contract

import com.url.androidcore.core.mvi.MviIntent

sealed class UserIntent : MviIntent {
    data object LoadUsers : UserIntent()
    data object RefreshUsers : UserIntent()
    data class LoadUser(val id: String) : UserIntent()
    data class CreateUser(val request: UserUiModel) : UserIntent()
    data object ClearError : UserIntent()
}
```

### 2. MviUiState

**Purpose:** Represent the current state of your UI.

**Requirements:**
- MUST be a data class
- MUST extend `MviUiState` interface
- Must be immutable (use `copy()` for updates)
- Must have an `initial()` companion function

**Example:**
```kotlin
package com.url.androidcore.presentation.user.contract

import com.url.androidcore.core.mvi.MviUiState

data class UserUiState(
    val isLoading: Boolean = false,
    val users: List<UserUiModel> = emptyList(),
    val selectedUser: UserUiModel? = null,
    val error: String? = null,
    val isSuccess: Boolean = false
) : MviUiState {
    companion object {
        fun initial() = UserUiState()
    }
}

data class UserUiModel(
    val id: String,
    val displayName: String,
    val email: String,
    val createdAt: String
)
```

### 3. MviUiEffect

**Purpose:** Handle one-time events (side effects) like navigation or toasts.

**Requirements:**
- MUST be a sealed class
- MUST extend `MviUiEffect` interface
- Each subclass represents a single effect/event

**Example:**
```kotlin
package com.url.androidcore.presentation.user.contract

import com.url.androidcore.core.mvi.MviUiEffect

sealed class UserUiEffect : MviUiEffect {
    data class ShowError(val message: String) : UserUiEffect()
    data object ShowSuccess : UserUiEffect()
    data class NavigateToUserDetail(val userId: String) : UserUiEffect()
    data object NavigateBack : UserUiEffect()
}
```

## ViewModel Usage

Your ViewModel MUST extend `MviViewModel<YourIntent, YourUiState, YourUiEffect>`

**Example:**
```kotlin
package com.url.androidcore.presentation.user.viewmodel

import com.url.androidcore.core.mvi.MviViewModel
import com.url.androidcore.core.dispatchers.DispatcherProvider
import com.url.androidcore.core.logging.Logger
import com.url.androidcore.presentation.user.contract.UserIntent
import com.url.androidcore.presentation.user.contract.UserUiState
import com.url.androidcore.presentation.user.contract.UserUiEffect

class UserViewModel(
    private val getUserUseCase: GetUserUseCase,
    private val getAllUsersUseCase: GetAllUsersUseCase,
    private val dispatchers: DispatcherProvider,
    private val logger: Logger
) : MviViewModel<UserIntent, UserUiState, UserUiEffect>() {

    override fun createInitialState() = UserUiState.initial()

    override fun handleIntent(intent: UserIntent) {
        when (intent) {
            is UserIntent.LoadUsers -> loadUsers()
            is UserIntent.RefreshUsers -> loadUsers()
            is UserIntent.LoadUser -> loadUser(intent.id)
            is UserIntent.CreateUser -> createUser(intent.request)
            UserIntent.ClearError -> clearError()
        }
    }

    private fun loadUsers() = launchData(
        action = { getAllUsersUseCase() },
        onSuccess = { users ->
            setState {
                copy(
                    isLoading = false,
                    users = users.map { it.toUi() },
                    error = null,
                    isSuccess = true
                )
            }
        },
        onError = { error ->
            logger.e("Error loading users: $error")
            setState { copy(isLoading = false, error = error?.message) }
            setEffect(UserUiEffect.ShowError(error?.message ?: "Unknown error"))
        }
    )

    private fun clearError() {
        setState { copy(error = null) }
    }
}
```

## File Structure

```
presentation/
└── {feature}/
    ├── contract/
    │   ├── {Feature}Intent.kt      ← Sealed class : MviIntent
    │   ├── {Feature}UiState.kt     ← Data class : MviUiState
    │   └── {Feature}UiEffect.kt    ← Sealed class : MviUiEffect
    ├── mapper/
    │   └── {Feature}Mapper.kt      ← Mapping functions
    └── viewmodel/
        └── {Feature}ViewModel.kt   ← Extends MviViewModel<Intent, State, Effect>
```

## MVI Flow

```
┌─────────────────────────────────────────────────────────┐
│ UI Layer                                                │
└─────────────┬───────────────────────────────────────────┘
              │ User clicks button
              ↓
┌─────────────────────────────────────────────────────────┐
│ Intent: UserIntent.LoadUsers                            │
└─────────────┬───────────────────────────────────────────┘
              │ ViewModel receives intent
              ↓
┌─────────────────────────────────────────────────────────┐
│ ViewModel.handleIntent(intent)                          │
└─────────────┬───────────────────────────────────────────┘
              │ Processes business logic
              ↓
┌─────────────────────────────────────────────────────────┐
│ UseCase execution                                       │
└─────────────┬───────────────────────────────────────────┘
              │ Collects data from Repository
              ↓
┌─────────────────────────────────────────────────────────┐
│ Success/Error callback                                  │
└─────────────┬───────────────────────────────────────────┘
              │
       ┌──────┴──────┐
       │             │
       ↓             ↓
    Success        Error
       │             │
       ├─→ setState({new state : MviUiState})
       │   └─→ emit new state to UI
       │
       └─→ setEffect(effect : MviUiEffect)
           └─→ one-time event to UI (navigation, toast, etc.)
```

## Common Patterns

### Pattern 1: Load Data with List

```kotlin
sealed class ProductIntent : MviIntent {
    data object LoadProducts : ProductIntent()
    data object RefreshProducts : ProductIntent()
    data object ClearError : ProductIntent()
}

data class ProductUiState(
    val isLoading: Boolean = false,
    val products: List<ProductUiModel> = emptyList(),
    val error: String? = null
) : MviUiState {
    companion object {
        fun initial() = ProductUiState()
    }
}

sealed class ProductUiEffect : MviUiEffect {
    data class ShowError(val message: String) : ProductUiEffect()
    data object ShowSuccess : ProductUiEffect()
}
```

### Pattern 2: Load Single Item with Details

```kotlin
sealed class ProductDetailIntent : MviIntent {
    data class LoadProduct(val id: String) : ProductDetailIntent()
    data class UpdateProduct(val product: ProductUiModel) : ProductDetailIntent()
    data class DeleteProduct(val id: String) : ProductDetailIntent()
    data object ClearError : ProductDetailIntent()
}

data class ProductDetailUiState(
    val isLoading: Boolean = false,
    val product: ProductUiModel? = null,
    val error: String? = null,
    val isSuccess: Boolean = false
) : MviUiState {
    companion object {
        fun initial() = ProductDetailUiState()
    }
}

sealed class ProductDetailUiEffect : MviUiEffect {
    data class ShowError(val message: String) : ProductDetailUiEffect()
    data object ShowSuccess : ProductDetailUiEffect()
    data object NavigateBack : ProductDetailUiEffect()
}
```

### Pattern 3: Form with Validation

```kotlin
sealed class CreateProductIntent : MviIntent {
    data class ChangeProductName(val name: String) : CreateProductIntent()
    data class ChangeProductPrice(val price: String) : CreateProductIntent()
    data object SubmitForm : CreateProductIntent()
    data object ClearError : CreateProductIntent()
}

data class CreateProductUiState(
    val name: String = "",
    val price: String = "",
    val isLoading: Boolean = false,
    val isValidName: Boolean = false,
    val isValidPrice: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false
) : MviUiState {
    companion object {
        fun initial() = CreateProductUiState()
    }
    
    val isFormValid get() = isValidName && isValidPrice
}

sealed class CreateProductUiEffect : MviUiEffect {
    data class ShowError(val message: String) : CreateProductUiEffect()
    data object ShowSuccess : CreateProductUiEffect()
    data object NavigateToProductList : CreateProductUiEffect()
}
```

## Benefits of Using MVI Contracts

✅ **Type Safety** - Compiler enforces correct usage  
✅ **Consistency** - All features follow same pattern  
✅ **Testability** - Easy to mock and test  
✅ **Maintainability** - Clear contracts between layers  
✅ **Scalability** - Works for small and large apps  
✅ **Documentation** - Self-documenting code  

## Important Reminders

❌ **Don't:**
- Create Intent without extending MviIntent
- Create UiState without extending MviUiState
- Create UiEffect without extending MviUiEffect
- Use other base classes or interfaces
- Mix MVI with other architectural patterns

✅ **Do:**
- Always extend the MVI contract interfaces
- Use sealed classes for Intent and UiEffect
- Use data class for UiState
- Keep state immutable
- Use `copy()` for state updates
- Keep effects one-time events only

## Testing MVI Components

```kotlin
class ProductViewModelTest {
    
    private lateinit var viewModel: ProductViewModel
    private lateinit var useCase: FakeGetProductsUseCase
    
    @Before
    fun setup() {
        useCase = FakeGetProductsUseCase()
        viewModel = ProductViewModel(
            useCase,
            TestDispatcherProvider(),
            TestLogger()
        )
    }
    
    @Test
    fun handleIntent_emitsLoadingState() = runTest {
        // Act
        viewModel.handleIntent(ProductIntent.LoadProducts)
        
        // Assert
        val state = viewModel.uiState.value
        assert(state is MviUiState)
        assert(state.isLoading)
    }
}
```

## Summary

Always follow this structure for MVI:

```
✓ Intent extends MviIntent
✓ UiState extends MviUiState  
✓ UiEffect extends MviUiEffect
✓ ViewModel extends MviViewModel<Intent, State, Effect>
```

This ensures your feature is properly integrated with the AndroidCore MVI system!

