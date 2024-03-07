package com.jakubu9333.repositories

import java.util.UUID

class InMemoryTodoRepository : TodoRepository {

    private val storage = HashMap<UUID, TodoItem>()

    override suspend fun add(todoItem: TodoItem): UUID{
        storage[todoItem.id] = todoItem
        return todoItem.id
    }

    override suspend fun addBatch(todoItems: List<TodoItem>) {
        todoItems.forEach {
            storage[it.id] = it
        }
    }

    override suspend fun getAll(): List<TodoItem> {
        return storage.values.toList()
    }

    override suspend fun delete(id: UUID) {
        storage.remove(id)
    }
}