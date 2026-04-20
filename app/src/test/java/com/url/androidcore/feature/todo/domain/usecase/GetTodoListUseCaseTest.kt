package com.url.androidcore.feature.todo.domain.usecase

import com.url.androidcore.core.testing.FakeRepository
import com.url.androidcore.core.testing.assertIsFailure
import com.url.androidcore.core.testing.assertIsSuccess
import com.url.androidcore.core.usecase.Result
import com.url.androidcore.feature.todo.domain.model.TodoItem
import com.url.androidcore.feature.todo.domain.repository.TodoRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class GetTodoListUseCaseTest {

    private val fakeTodoItems = listOf(
        TodoItem(checked = false, description = "Buy groceries", endDate = 1712592000000L),
        TodoItem(checked = true, description = "Finish report", endDate = 1712678400000L)
    )

    @Test
    fun `invoke returns success result from repository`() = runBlocking {
        val repository = object : FakeRepository(), TodoRepository {
            override suspend fun getTodoList(): Result<List<TodoItem>> =
                Result.Success(fakeTodoItems)
        }
        val useCase = GetTodoListUseCase(repository)

        val result = useCase(Unit)

        result.assertIsSuccess { items ->
            assertEquals(2, items.size)
            assertEquals("Buy groceries", items[0].description)
            assertEquals("Finish report", items[1].description)
        }
    }

    @Test
    fun `invoke returns failure result when repository fails`() = runBlocking {
        val error = RuntimeException("Network error")
        val repository = object : FakeRepository(), TodoRepository {
            override suspend fun getTodoList(): Result<List<TodoItem>> =
                Result.Failure(error)
        }
        val useCase = GetTodoListUseCase(repository)

        val result = useCase(Unit)

        result.assertIsFailure { throwable ->
            assertEquals("Network error", throwable.message)
        }
    }
}
