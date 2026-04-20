package com.url.androidcore.feature.todo.data.datasource

import com.url.androidcore.core.datasource.RemoteDataSource
import com.url.androidcore.core.usecase.Result
import com.url.androidcore.feature.todo.data.dto.TodoDto

interface TodoRemoteDataSource : RemoteDataSource {

    suspend fun getTodoList(): Result<List<TodoDto>>
}
