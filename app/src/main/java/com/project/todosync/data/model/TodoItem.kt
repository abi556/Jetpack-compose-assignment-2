package com.project.todosync.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todos")
data class TodoItem(
    val userId: Int,
    @PrimaryKey val id: Int,
    val title: String,
    val completed: Boolean
) 