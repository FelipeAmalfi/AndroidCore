package com.url.androidcore.feature.todo.domain.usecase

import com.url.androidcore.core.usecase.Result
import com.url.androidcore.core.usecase.UseCase
import com.url.androidcore.feature.todo.domain.model.TodoItem
import com.url.androidcore.feature.todo.domain.repository.TodoRepository

class GetTodoListUseCase(
    private val todoRepository: TodoRepository
) : UseCase<Unit, List<TodoItem>> {

    override suspend fun invoke(param: Unit): Result<List<TodoItem>> =
        todoRepository.getTodoList()
}
