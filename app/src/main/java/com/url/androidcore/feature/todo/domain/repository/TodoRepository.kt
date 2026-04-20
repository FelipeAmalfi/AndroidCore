package com.url.androidcore.feature.todo.domain.repository

import com.url.androidcore.core.repository.Repository
import com.url.androidcore.core.usecase.Result
import com.url.androidcore.feature.todo.domain.model.TodoItem

interface TodoRepository : Repository {

    suspend fun getTodoList(): Result<List<TodoItem>>
}
