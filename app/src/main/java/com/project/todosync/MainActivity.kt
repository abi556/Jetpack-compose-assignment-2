package com.project.todosync

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.project.todosync.data.TodoRepository
import com.project.todosync.data.local.TodoDatabase
import com.project.todosync.data.remote.RetrofitInstance
import com.project.todosync.ui.TodoListViewModel
import com.project.todosync.ui.TodoDetailViewModel
import com.project.todosync.ui.TodoNavGraph
import com.project.todosync.ui.theme.TodoSyncTheme
import androidx.lifecycle.viewmodel.compose.viewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val database = TodoDatabase.getDatabase(applicationContext)
        val repository = TodoRepository(database.todoDao(), RetrofitInstance.api)
        setContent {
            TodoSyncTheme {
                val listViewModel: TodoListViewModel = viewModel(factory = object : androidx.lifecycle.ViewModelProvider.Factory {
                    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                        @Suppress("UNCHECKED_CAST")
                        return TodoListViewModel(repository) as T
                    }
                })
                TodoNavGraph(
                    listViewModel = listViewModel,
                    detailViewModelProvider = { todoId ->
                        viewModel(factory = object : androidx.lifecycle.ViewModelProvider.Factory {
                            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                                @Suppress("UNCHECKED_CAST")
                                return TodoDetailViewModel(repository, todoId) as T
                            }
                        })
                }
                )
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TodoSyncTheme {
        Greeting("Android")
    }
}