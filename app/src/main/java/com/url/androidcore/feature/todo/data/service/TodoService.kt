package com.url.androidcore.feature.todo.data.service

import com.url.androidcore.feature.todo.data.dto.TodoDto
import retrofit2.http.GET

interface TodoService {

    @GET("todo/list")
    suspend fun getTodoList(): List<TodoDto>
}
