package com.url.androidcore.feature.todo.data.datasource

import com.url.androidcore.core.network.safeApiCall
import com.url.androidcore.core.usecase.Result
import com.url.androidcore.feature.todo.data.dto.TodoDto
import com.url.androidcore.feature.todo.data.service.TodoService

class TodoRemoteDataSourceImpl(
    private val todoService: TodoService
) : TodoRemoteDataSource {

    override suspend fun getTodoList(): Result<List<TodoDto>> = safeApiCall {
        todoService.getTodoList()
    }
}
