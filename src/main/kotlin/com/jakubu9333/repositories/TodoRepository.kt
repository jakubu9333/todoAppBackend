package com.jakubu9333.repositories

interface TodoRepository {

    suspend fun add(todoItem: TodoItem): Int

    suspend fun getAll(): List<TodoItem>

    suspend fun delete(id: Int)
}