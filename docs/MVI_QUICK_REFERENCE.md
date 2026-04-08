# 🚀 MVI Contracts Quick Reference Card

## The Three Required Contracts

### 1️⃣ MviIntent
```kotlin
import com.url.androidcore.core.mvi.MviIntent

sealed class MyFeatureIntent : MviIntent {
    data object LoadData : MyFeatureIntent()
    data class Create(val item: ItemUi) : MyFeatureIntent()
    data object ClearError : MyFeatureIntent()
}
```
✓ sealed class  
✓ extends MviIntent  
✓ user actions

### 2️⃣ MviUiState
```kotlin
import com.url.androidcore.core.mvi.MviUiState

data class MyFeatureUiState(
    val isLoading: Boolean = false,
    val data: List<ItemUi> = emptyList(),
    val error: String? = null
) : MviUiState {
    companion object {
        fun initial() = MyFeatureUiState()
    }
}
```
✓ data class  
✓ extends MviUiState  
✓ immutable  
✓ initial() function

### 3️⃣ MviUiEffect
```kotlin
import com.url.androidcore.core.mvi.MviUiEffect

sealed class MyFeatureUiEffect : MviUiEffect {
    data class ShowError(val msg: String) : MyFeatureUiEffect()
    data object ShowSuccess : MyFeatureUiEffect()
    data object NavigateBack : MyFeatureUiEffect()
}
```
✓ sealed class  
✓ extends MviUiEffect  
✓ one-time events

### 4️⃣ ViewModel
```kotlin
import com.url.androidcore.core.mvi.MviViewModel

class MyFeatureViewModel(
    private val useCase: MyUseCase,
    private val dispatchers: DispatcherProvider,
    private val logger: Logger
) : MviViewModel<MyFeatureIntent, MyFeatureUiState, MyFeatureUiEffect>() {
    
    override fun createInitialState() = MyFeatureUiState.initial()
    
    override fun handleIntent(intent: MyFeatureIntent) {
        when (intent) {
            is MyFeatureIntent.LoadData -> loadData()
            is MyFeatureIntent.Create -> create(intent.item)
            MyFeatureIntent.ClearError -> setState { copy(error = null) }
        }
    }
}
```
✓ extends MviViewModel<Intent, State, Effect>  
✓ createInitialState()  
✓ handleIntent(intent)

---

## File Structure

```
presentation/myfeature/
├── contract/
│   ├── MyFeatureIntent.kt
│   ├── MyFeatureUiState.kt
│   └── MyFeatureUiEffect.kt
├── mapper/
│   └── MyFeatureMapper.kt
└── viewmodel/
    └── MyFeatureViewModel.kt
```

---

## Imports Needed

```kotlin
// Contracts
import com.url.androidcore.core.mvi.MviIntent
import com.url.androidcore.core.mvi.MviUiState
import com.url.androidcore.core.mvi.MviUiEffect
import com.url.androidcore.core.mvi.MviViewModel

// Core utilities
import com.url.androidcore.core.dispatchers.DispatcherProvider
import com.url.androidcore.core.logging.Logger
```

---

## Common Patterns

### Loading List
```kotlin
sealed class ListIntent : MviIntent {
    data object LoadList : ListIntent()
    data object RefreshList : ListIntent()
}

data class ListUiState(
    val isLoading: Boolean = false,
    val items: List<ItemUi> = emptyList(),
    val error: String? = null
) : MviUiState {
    companion object { fun initial() = ListUiState() }
}

sealed class ListUiEffect : MviUiEffect {
    data class ShowError(val msg: String) : ListUiEffect()
}
```

### Loading Detail
```kotlin
sealed class DetailIntent : MviIntent {
    data class LoadDetail(val id: String) : DetailIntent()
    data class Update(val item: ItemUi) : DetailIntent()
}

data class DetailUiState(
    val isLoading: Boolean = false,
    val item: ItemUi? = null,
    val error: String? = null
) : MviUiState {
    companion object { fun initial() = DetailUiState() }
}

sealed class DetailUiEffect : MviUiEffect {
    data object NavigateBack : DetailUiEffect()
}
```

### Form with Validation
```kotlin
sealed class FormIntent : MviIntent {
    data class ChangeField(val field: String, val value: String) : FormIntent()
    data object Submit : FormIntent()
}

data class FormUiState(
    val field1: String = "",
    val field2: String = "",
    val isValid: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
) : MviUiState {
    companion object { fun initial() = FormUiState() }
}

sealed class FormUiEffect : MviUiEffect {
    data object ShowSuccess : FormUiEffect()
    data object NavigateToList : FormUiEffect()
}
```

---

## Validation Checklist

Before submitting code:

```
☐ Intent extends MviIntent
☐ UiState extends MviUiState
☐ UiEffect extends MviUiEffect
☐ ViewModel extends MviViewModel<Intent, State, Effect>
☐ All imports from core.mvi
☐ UiState is data class
☐ Intent and UiEffect are sealed classes
☐ UiState has initial() companion function
☐ ViewModel has createInitialState()
☐ ViewModel has handleIntent(intent)
☐ Code compiles without errors
```

---

## ViewModel Methods

### setState
```kotlin
setState { 
    copy(isLoading = false, data = newData) 
}
```

### setEffect
```kotlin
setEffect(MyFeatureUiEffect.ShowError("Error message"))
```

### launchData (helper)
```kotlin
private fun loadData() = launchData(
    action = { useCase.getData() },
    onSuccess = { data ->
        setState { copy(data = data.map { it.toUi() }) }
    },
    onError = { error ->
        setEffect(MyFeatureUiEffect.ShowError(error?.message ?: "Error"))
    }
)
```

---

## Testing Pattern

```kotlin
@Test
fun handleIntent_loadsData() = runTest {
    // Arrange
    useCase.setMockData(listOf(...))
    
    // Act
    viewModel.handleIntent(MyFeatureIntent.LoadData)
    advanceUntilIdle()
    
    // Assert
    val state = viewModel.uiState.value
    assert(state.data.isNotEmpty())
}
```

---

## Common Mistakes ❌

```kotlin
// ❌ WRONG: Missing : MviIntent
sealed class MyIntent {
    data object Load : MyIntent()
}

// ✅ CORRECT
sealed class MyIntent : MviIntent {
    data object Load : MyIntent()
}

// ❌ WRONG: Using class instead of data class for state
class MyState(val loading: Boolean)

// ✅ CORRECT
data class MyState(val loading: Boolean = false) : MviUiState

// ❌ WRONG: Missing initial() function
data class MyState(...) : MviUiState

// ✅ CORRECT
data class MyState(...) : MviUiState {
    companion object {
        fun initial() = MyState()
    }
}
```

---

## Documentation

- **Full Guide:** `docs/MVI_CONTRACTS_GUIDE.md`
- **Templates:** `docs/AGENT_TEMPLATES.md`
- **Integration:** `docs/AGENT_INTEGRATION_POINTS.md`
- **Example:** `presentation/item/` (Item feature)

---

## Copy-Paste Template

```kotlin
package com.url.androidcore.presentation.myfeature.contract

import com.url.androidcore.core.mvi.MviIntent
import com.url.androidcore.core.mvi.MviUiState
import com.url.androidcore.core.mvi.MviUiEffect

// Intent
sealed class MyFeatureIntent : MviIntent {
    data object Load : MyFeatureIntent()
    data object ClearError : MyFeatureIntent()
}

// UiState
data class MyFeatureUiState(
    val isLoading: Boolean = false,
    val data: List<ItemUi> = emptyList(),
    val error: String? = null
) : MviUiState {
    companion object {
        fun initial() = MyFeatureUiState()
    }
}

data class ItemUi(
    val id: String,
    val name: String
)

// UiEffect
sealed class MyFeatureUiEffect : MviUiEffect {
    data class ShowError(val message: String) : MyFeatureUiEffect()
    data object ShowSuccess : MyFeatureUiEffect()
}
```

---

**Remember: All three contracts MUST be used together! ✓**

For more: Read `MVI_CONTRACTS_GUIDE.md` 📚

