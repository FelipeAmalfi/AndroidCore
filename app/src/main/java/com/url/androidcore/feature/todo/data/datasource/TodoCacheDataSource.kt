package com.url.androidcore.feature.todo.data.datasource

import com.url.androidcore.core.datasource.CacheDataSource
import com.url.androidcore.feature.todo.data.dto.TodoDto

interface TodoCacheDataSource : CacheDataSource {

    fun getCache(): List<TodoDto>?

    fun setCache(todos: List<TodoDto>)

    fun clearCache()
}
