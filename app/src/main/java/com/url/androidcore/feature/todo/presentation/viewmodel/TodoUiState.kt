package com.url.androidcore.feature.todo.presentation.viewmodel

import com.url.androidcore.core.mvi.MviUiState
import com.url.androidcore.feature.todo.presentation.model.TodoUiItem

data class TodoUiState(
    val items: List<TodoUiItem> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
) : MviUiState
