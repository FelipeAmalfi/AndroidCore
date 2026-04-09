package com.url.androidcore.feature.todo.domain.model

data class TodoItem(
    val checked: Boolean,
    val description: String,
    val endDate: Long
)
