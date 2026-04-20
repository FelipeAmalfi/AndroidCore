package com.url.androidcore.feature.todo.data.repository

import com.url.androidcore.core.testing.assertIsSuccess
import com.url.androidcore.core.usecase.Result
import com.url.androidcore.feature.todo.data.datasource.TodoCacheDataSourceImpl
import com.url.androidcore.feature.todo.data.datasource.TodoRemoteDataSource
import com.url.androidcore.feature.todo.data.dto.TodoDto
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class TodoRepositoryImplTest {

    private val sampleDtos = listOf(
        TodoDto(checked = false, description = "Task 1", endDate = "2024-04-08T00:00:00Z"),
        TodoDto(checked = true, description = "Task 2", endDate = "2024-04-09T00:00:00Z")
    )

    @Test
    fun `getTodoList returns mapped domain items on remote success`() = runBlocking {
        val remoteDataSource = object : TodoRemoteDataSource {
            override suspend fun getTodoList(): Result<List<TodoDto>> =
                Result.Success(sampleDtos)
        }
        val cacheDataSource = TodoCacheDataSourceImpl()
        val repository = TodoRepositoryImpl(remoteDataSource, cacheDataSource)

        val result = repository.getTodoList()

        result.assertIsSuccess { items ->
            assertEquals(2, items.size)
            assertEquals("Task 1", items[0].description)
            assertEquals(false, items[0].checked)
        }
    }

    @Test
    fun `getTodoList updates cache on remote success`() = runBlocking {
        val remoteDataSource = object : TodoRemoteDataSource {
            override suspend fun getTodoList(): Result<List<TodoDto>> =
                Result.Success(sampleDtos)
        }
        val cacheDataSource = TodoCacheDataSourceImpl()
        val repository = TodoRepositoryImpl(remoteDataSource, cacheDataSource)

        repository.getTodoList()

        assertEquals(2, cacheDataSource.getCache()?.size)
    }

    @Test
    fun `getTodoList returns cached items when remote fails and cache is populated`() = runBlocking {
        val remoteDataSource = object : TodoRemoteDataSource {
            override suspend fun getTodoList(): Result<List<TodoDto>> =
                Result.Failure(RuntimeException("Network error"))
        }
        val cacheDataSource = TodoCacheDataSourceImpl()
        cacheDataSource.setCache(sampleDtos)
        val repository = TodoRepositoryImpl(remoteDataSource, cacheDataSource)

        val result = repository.getTodoList()

        result.assertIsSuccess { items ->
            assertEquals(2, items.size)
            assertEquals("Task 1", items[0].description)
        }
    }

    @Test
    fun `getTodoList returns failure when remote fails and cache is empty`() = runBlocking {
        val error = RuntimeException("Network error")
        val remoteDataSource = object : TodoRemoteDataSource {
            override suspend fun getTodoList(): Result<List<TodoDto>> =
                Result.Failure(error)
        }
        val cacheDataSource = TodoCacheDataSourceImpl()
        val repository = TodoRepositoryImpl(remoteDataSource, cacheDataSource)

        val result = repository.getTodoList()

        assertTrue(result is Result.Failure)
    }
}
