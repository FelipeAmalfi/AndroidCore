package com.url.androidcore.feature.todo.data.repository

import com.url.androidcore.core.usecase.Result
import com.url.androidcore.feature.todo.data.datasource.TodoCacheDataSource
import com.url.androidcore.feature.todo.data.datasource.TodoRemoteDataSource
import com.url.androidcore.feature.todo.domain.model.TodoItem
import com.url.androidcore.feature.todo.domain.repository.TodoRepository
import com.url.androidcore.feature.todo.mapper.toDomain

class TodoRepositoryImpl(
    private val remoteDataSource: TodoRemoteDataSource,
    private val cacheDataSource: TodoCacheDataSource
) : TodoRepository {

    override suspend fun getTodoList(): Result<List<TodoItem>> {
        val remoteResult = remoteDataSource.getTodoList()
        return when (remoteResult) {
            is Result.Success -> {
                cacheDataSource.setCache(remoteResult.data)
                Result.Success(remoteResult.data.map { it.toDomain() })
            }
            is Result.Failure -> {
                val cached = cacheDataSource.getCache()
                if (cached != null) {
                    Result.Success(cached.map { it.toDomain() })
                } else {
                    remoteResult
                }
            }
        }
    }
}
