package com.url.androidcore.core.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.url.androidcore.core.utils.launchData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class MviViewModel<I : MviIntent, S : MviUiState, E : MviUiEffect>(
    initialState: S
) : ViewModel() {

    private val _uiState = MutableStateFlow(initialState)
    val uiState: StateFlow<S> = _uiState

    private val _effects = Channel<E>(Channel.BUFFERED)
    val effects: Flow<E> = _effects.receiveAsFlow()

    protected fun setState(reducer: S.() -> S) {
        _uiState.update { current -> current.reducer() }
    }

    protected fun sendEffect(builder: () -> E) {
        viewModelScope.launch {
            _effects.send(builder())
        }
    }

    protected fun launch(
        onError: (Throwable) -> Unit = {},
        block: suspend () -> Unit
    ) {
        viewModelScope.launch {
            runCatching { block() }
                .onFailure { onError(it) }
        }
    }

    abstract fun handleIntent(intent: I)

    fun dispatch(intent: I) {
        handleIntent(intent)
    }

    protected fun <T> launchData(
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
        onStart: (S.() -> S)? = null,
        onSuccess: (S.(T) -> S),
        onError: (S.(Throwable) -> S)? = null,
        onCompletion: (S.() -> S)? = null,
        block: suspend () -> T
    ) {
        viewModelScope.launchData(
            dispatcher = dispatcher,
            onStart = {
                onStart?.let { setState(it) }
            },
            onSuccess = { data ->
                setState { onSuccess(data) }
            },
            onError = { error ->
                onError?.let { setState { it(error) } }
            },
            onCompletion = {
                onCompletion?.let { setState(it) }
            },
            block = block
        )
    }

}
