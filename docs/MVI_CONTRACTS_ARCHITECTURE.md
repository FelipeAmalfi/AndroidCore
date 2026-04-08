# MVI Contracts Architecture Diagram

## Complete MVI Architecture with Contracts

```
┌─────────────────────────────────────────────────────────────────┐
│                          UI LAYER                               │
│                      (Jetpack Compose)                          │
└────────────────────────────┬────────────────────────────────────┘
                             │
                    User Clicks Button
                             │
                             ↓
┌─────────────────────────────────────────────────────────────────┐
│                    PRESENTATION LAYER                           │
│                                                                  │
│  ┌──────────────────────────────────────────────────────────┐   │
│  │           {Feature}Intent : MviIntent                   │   │
│  ├──────────────────────────────────────────────────────────┤   │
│  │  data object LoadData : {Feature}Intent()               │   │
│  │  data class Create(...) : {Feature}Intent()             │   │
│  │  data object ClearError : {Feature}Intent()             │   │
│  └──────────────────────────────────────────────────────────┘   │
│                             ↓                                    │
│  ┌──────────────────────────────────────────────────────────┐   │
│  │        {Feature}ViewModel :                             │   │
│  │    MviViewModel<Intent, State, Effect>                  │   │
│  │                                                          │   │
│  │  - createInitialState()                                 │   │
│  │  - handleIntent(intent: {Feature}Intent)                │   │
│  │  - setState { ... }                                     │   │
│  │  - setEffect(effect: {Feature}UiEffect)                 │   │
│  │  - launchData { ... }                                   │   │
│  └──────────────────────────────────────────────────────────┘   │
│         ↙              ↓              ↘                          │
│        /               │               \                         │
│       /                │                \                        │
│      ↙                 ↙                 ↘                       │
│ ┌───────────┐    ┌──────────────────┐   ┌──────────────────┐   │
│ │  setState │    │  invokeUseCase   │   │  setEffect       │   │
│ ├───────────┤    ├──────────────────┤   ├──────────────────┤   │
│ │ Copy      │    │ Coroutine scope  │   │ Single-shot      │   │
│ │ immutable │    │ launchData()     │   │ navigation       │   │
│ │ state     │    │                  │   │ toast            │   │
│ └───────────┘    └────────┬─────────┘   └──────────────────┘   │
│                           │                                      │
│                    ┌──────┴─────┐                                │
│                    │            │                                │
│              onSuccess    onError                                │
└────────────────────────────────────────────────────────────────┬─┘
                    │                 │
        ┌───────────┘                 └────────┐
        │                                      │
        ↓                                      ↓
┌──────────────────────────┐    ┌──────────────────────────┐
│  {Feature}UiState        │    │  {Feature}UiEffect       │
│  : MviUiState            │    │  : MviUiEffect           │
├──────────────────────────┤    ├──────────────────────────┤
│  isLoading: Boolean      │    │  ShowError(msg)          │
│  data: List<ItemUi>      │    │  ShowSuccess             │
│  error: String?          │    │  NavigateToDetail(id)    │
│                          │    │  NavigateBack            │
│  companion {             │    │                          │
│    fun initial()         │    │  (One-time events)       │
│  }                       │    │                          │
└────────────┬─────────────┘    └────────────┬─────────────┘
             │                               │
             │                               │
  Emits to UI State Flow        Emits to Effects Flow
             │                               │
             ↓                               ↓
        ┌─────────────────────────────────────────┐
        │  UI OBSERVES & RE-COMPOSES              │
        │  showProgress(isLoading)                │
        │  showItems(data.map { ... })            │
        │  showError(error)                       │
        │                                         │
        │  Side Effects:                          │
        │  navigateToDetail(itemId)               │
        │  showToast(message)                     │
        │  navigateBack()                         │
        └─────────────────────────────────────────┘
```

---

## File Structure & Contracts Relationship

```
FEATURE
│
├── contract/                           (MVI Contracts)
│   ├── {Feature}Intent.kt
│   │   sealed class X : MviIntent
│   │
│   ├── {Feature}UiState.kt
│   │   data class X(...) : MviUiState
│   │
│   └── {Feature}UiEffect.kt
│       sealed class X : MviUiEffect
│
├── viewmodel/
│   └── {Feature}ViewModel.kt
│       class X : MviViewModel<Intent, State, Effect>
│
├── mapper/
│   └── {Feature}Mapper.kt
│       (Uses contracts for mapping)
│
└── ui/
    └── {Feature}Screen.kt
        (Observes state, sends intents)
```

---

## Contract Inheritance Hierarchy

```
core.mvi Package:
├── MviIntent (marker interface)
│   └── {Feature}Intent : MviIntent
│       ├── LoadData : {Feature}Intent()
│       ├── Create(...) : {Feature}Intent()
│       └── ClearError : {Feature}Intent()
│
├── MviUiState (marker interface)
│   └── {Feature}UiState(...) : MviUiState
│       └── companion object {
│           fun initial() = {Feature}UiState()
│       }
│
├── MviUiEffect (marker interface)
│   └── {Feature}UiEffect : MviUiEffect
│       ├── ShowError(...) : {Feature}UiEffect()
│       ├── ShowSuccess : {Feature}UiEffect()
│       └── NavigateBack : {Feature}UiEffect()
│
└── MviViewModel<I, S, E>
    └── {Feature}ViewModel : MviViewModel<Intent, State, Effect>
        ├── createInitialState(): State
        ├── handleIntent(intent: Intent)
        ├── setState(block)
        ├── setEffect(effect)
        └── launchData(action, onSuccess, onError)
```

---

## Data Flow with Contracts

```
USER INTERACTION
      ↓
UI sends ItemIntent.LoadItems
      ↓
ViewModel receives (override handleIntent)
      ↓
ViewModel calls useCase
      ↓
UseCase queries Repository
      ↓
Repository returns List<ItemModel>
      ↓
Mapper: ItemModel.toUi() → ItemUiModel
      ↓
ViewModel calls setState()
      ↓
ItemUiState updated (immutable copy)
      ↓
      ┌────────────────────────────┐
      │ ItemUiState : MviUiState   │
      │ {                          │
      │   isLoading: false,        │
      │   items: [ItemUiModel],    │
      │   error: null              │
      │ }                          │
      └────────────────────────────┘
      ↓
UI observes new state
      ↓
UI re-composes with new data
```

---

## State Immutability Pattern

```
OLD STATE (Immutable)
┌──────────────────────────┐
│ isLoading: true          │
│ items: []                │
│ error: null              │
└──────────────────────────┘
           │
       copy() call
           │
           ↓
NEW STATE (New instance)
┌──────────────────────────┐
│ isLoading: false         │  ← Changed
│ items: [Item1, Item2]    │  ← Changed
│ error: null              │  ← Same
└──────────────────────────┘

setState { 
    copy(
        isLoading = false,
        items = newItems
    )
}
```

---

## Intent Handling Flow

```
ViewModel.handleIntent(intent: ItemIntent)
    │
    ├─ when (intent) {
    │
    ├─ ItemIntent.LoadItems
    │   └─→ loadItems()
    │       ├─ setState { copy(isLoading = true) }
    │       ├─ launchData(
    │       │   action = { useCase() },
    │       │   onSuccess = { items →
    │       │       setState { copy(items = items) }
    │       │   },
    │       │   onError = { error →
    │       │       setEffect(ShowError(error.msg))
    │       │   }
    │       └─ )
    │
    ├─ ItemIntent.CreateItem(item)
    │   └─→ createItem(item)
    │
    ├─ ItemIntent.ClearError
    │   └─→ setState { copy(error = null) }
    │
    └─ }
```

---

## Effect Emission Pattern

```
ONE-TIME EVENT FLOW

ViewModel triggers effect:
    ↓
setEffect(ItemUiEffect.ShowSuccess)
    ↓
Effect emitted to UI
    ↓
UI receives effect
    ↓
Handler processes effect:
    ├─ ShowError(msg) → showToast(msg)
    ├─ ShowSuccess → showToast("Success")
    ├─ Navigate... → navigate()
    └─ NavigateBack → pop()
    ↓
Effect is CONSUMED
    (Won't fire again on recomposition)
```

---

## Complete Example: Load Items Feature

```
File Structure:
presentation/item/
├── contract/
│   ├── ItemIntent.kt
│   │   sealed class ItemIntent : MviIntent {
│   │       data object LoadItems : ItemIntent()
│   │       data object RefreshItems : ItemIntent()
│   │       data class DeleteItem(val id: String) : ItemIntent()
│   │   }
│   │
│   ├── ItemUiState.kt
│   │   data class ItemUiState(
│   │       val isLoading: Boolean = false,
│   │       val items: List<ItemUiModel> = emptyList(),
│   │       val error: String? = null
│   │   ) : MviUiState {
│   │       companion object {
│   │           fun initial() = ItemUiState()
│   │       }
│   │   }
│   │
│   ├── ItemUiEffect.kt
│   │   sealed class ItemUiEffect : MviUiEffect {
│   │       data class ShowError(val msg: String) : ItemUiEffect()
│   │       data object ShowSuccess : ItemUiEffect()
│   │   }
│   │
│   └── ItemViewModel.kt
│       class ItemViewModel(
│           private val useCase: GetAllItemsUseCase,
│           private val dispatchers: DispatcherProvider,
│           private val logger: Logger
│       ) : MviViewModel<ItemIntent, ItemUiState, ItemUiEffect>() {
│           
│           override fun createInitialState() = ItemUiState.initial()
│           
│           override fun handleIntent(intent: ItemIntent) {
│               when (intent) {
│                   ItemIntent.LoadItems -> loadItems()
│                   ItemIntent.RefreshItems -> loadItems()
│                   is ItemIntent.DeleteItem -> deleteItem(intent.id)
│               }
│           }
│           
│           private fun loadItems() = launchData(
│               action = { useCase() },
│               onSuccess = { items →
│                   setState {
│                       copy(
│                           isLoading = false,
│                           items = items.map { it.toUi() }
│                       )
│                   }
│               },
│               onError = { error →
│                   setEffect(ItemUiEffect.ShowError(error?.msg ?: "Error"))
│               }
│           )
│       }

UI Layer (Compose):
@Composable
fun ItemScreen(viewModel: ItemViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsState()
    val effect by viewModel.uiEffect.collectAsState()
    
    LaunchedEffect(Unit) {
        viewModel.handleIntent(ItemIntent.LoadItems)
    }
    
    when {
        state.isLoading → LoadingView()
        state.error != null → ErrorView(state.error)
        else → ItemListView(state.items)
    }
}
```

---

## Validation Checklist

```
✓ Intent extends MviIntent
  └─ sealed class ItemIntent : MviIntent

✓ UiState extends MviUiState
  └─ data class ItemUiState(...) : MviUiState

✓ UiEffect extends MviUiEffect
  └─ sealed class ItemUiEffect : MviUiEffect

✓ ViewModel extends MviViewModel<I, S, E>
  └─ class ItemViewModel : MviViewModel<ItemIntent, ItemUiState, ItemUiEffect>

✓ All imports from core.mvi
  └─ import com.url.androidcore.core.mvi.*

✓ UiState is data class (immutable)
✓ Intent and UiEffect are sealed classes
✓ UiState has initial() companion function
✓ ViewModel implements createInitialState()
✓ ViewModel implements handleIntent(intent)
✓ Code compiles without errors
```

---

## Quick Mental Model

```
MviIntent   = "What user did"
MviUiState  = "Current screen state"
MviUiEffect = "One-time notifications"

All three work together:

User Action → Intent → ViewModel → 
    ├─ setState → UiState → UI Updates
    └─ setEffect → UiEffect → Navigation/Toast
```

---

**Remember: MVI Contracts provide type safety and consistency across all features! 🎯**

