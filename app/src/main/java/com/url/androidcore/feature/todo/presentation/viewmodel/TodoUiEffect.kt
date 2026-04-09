package com.url.androidcore.feature.todo.presentation.viewmodel

import com.url.androidcore.core.mvi.MviUiEffect

sealed class TodoUiEffect : MviUiEffect {
    data class ShowError(val message: String) : TodoUiEffect()
}
