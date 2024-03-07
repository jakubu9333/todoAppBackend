package com.jakubu9333.repositories

import java.util.UUID

interface TodoRepository {

    suspend fun add(todoItem: TodoItem): UUID
    suspend fun addBatch(todoItems: List<TodoItem>)

    suspend fun getAll(): List<TodoItem>

    suspend fun delete(id: UUID)
}