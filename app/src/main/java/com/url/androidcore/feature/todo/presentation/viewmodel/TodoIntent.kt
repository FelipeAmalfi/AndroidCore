package com.url.androidcore.feature.todo.presentation.viewmodel

import com.url.androidcore.core.mvi.MviIntent

sealed class TodoIntent : MviIntent {
    object LoadTodoList : TodoIntent()
    object Retry : TodoIntent()
}
