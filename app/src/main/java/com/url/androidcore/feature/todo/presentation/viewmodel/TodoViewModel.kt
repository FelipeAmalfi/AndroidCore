package com.url.androidcore.feature.todo.presentation.viewmodel

import com.url.androidcore.core.dispatchers.DefaultDispatcherProvider
import com.url.androidcore.core.dispatchers.DispatcherProvider
import com.url.androidcore.core.error.getUserMessage
import com.url.androidcore.core.error.toAppError
import com.url.androidcore.core.mvi.MviViewModel
import com.url.androidcore.core.usecase.Result
import com.url.androidcore.feature.todo.domain.model.TodoItem
import com.url.androidcore.feature.todo.domain.usecase.GetTodoListUseCase
import com.url.androidcore.feature.todo.mapper.toUi

class TodoViewModel(
    private val getTodoListUseCase: GetTodoListUseCase,
    private val dispatchers: DispatcherProvider = DefaultDispatcherProvider()
) : MviViewModel<TodoIntent, TodoUiState, TodoUiEffect>(TodoUiState()) {

    override fun handleIntent(intent: TodoIntent) {
        when (intent) {
            is TodoIntent.LoadTodoList -> loadTodoList()
            is TodoIntent.Retry -> loadTodoList()
        }
    }

    private fun loadTodoList() {
        launchData<List<TodoItem>>(
            dispatcher = dispatchers.io,
            onStart = { copy(isLoading = true, errorMessage = null) },
            onSuccess = { todoItems ->
                copy(isLoading = false, items = todoItems.map { it.toUi() })
            },
            onError = { error ->
                val message = error.toAppError().getUserMessage()
                sendEffect { TodoUiEffect.ShowError(message) }
                copy(isLoading = false, errorMessage = message)
            }
        ) {
            when (val result = getTodoListUseCase(Unit)) {
                is Result.Success -> result.data
                is Result.Failure -> throw result.error
            }
        }
    }
}
