package com.project.todosync.data.remote

import com.project.todosync.data.model.TodoItem
import retrofit2.http.GET
 
interface TodoApiService {
    @GET("todos")
    suspend fun getTodos(): List<TodoItem>
} 