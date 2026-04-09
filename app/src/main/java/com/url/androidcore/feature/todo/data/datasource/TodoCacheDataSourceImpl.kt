package com.url.androidcore.feature.todo.data.datasource

import com.url.androidcore.feature.todo.data.dto.TodoDto

class TodoCacheDataSourceImpl : TodoCacheDataSource {

    private var cache: List<TodoDto>? = null

    override fun getCache(): List<TodoDto>? = cache

    override fun setCache(todos: List<TodoDto>) {
        cache = todos
    }

    override fun clearCache() {
        cache = null
    }
}
