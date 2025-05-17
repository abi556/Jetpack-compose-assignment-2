package com.project.todosync.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.todosync.data.TodoRepository
import com.project.todosync.data.model.TodoItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TodoListViewModel(private val repository: TodoRepository) : ViewModel() {
    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    val todos: StateFlow<List<TodoItem>> = repository.getAllTodos()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        refreshTodos()
    }

    fun refreshTodos() {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            val result = repository.refreshTodos()
            if (result.isFailure) {
                _error.value = result.exceptionOrNull()?.localizedMessage ?: "Unknown error"
            }
            _loading.value = false
        }
    }
} 