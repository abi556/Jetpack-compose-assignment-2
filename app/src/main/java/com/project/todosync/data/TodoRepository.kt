package com.project.todosync.data

import com.project.todosync.data.local.TodoDao
import com.project.todosync.data.model.TodoItem
import com.project.todosync.data.remote.TodoApiService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class TodoRepository(
    private val todoDao: TodoDao,
    private val apiService: TodoApiService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    fun getAllTodos(): Flow<List<TodoItem>> = todoDao.getAllTodos()

    fun getTodoById(id: Int): Flow<TodoItem?> = todoDao.getTodoById(id)

    suspend fun refreshTodos(): Result<Unit> = withContext(ioDispatcher) {
        return@withContext try {
            val todos = apiService.getTodos()
            todoDao.insertTodos(todos)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
} 