package com.jakubu9333.repositories

class InMemoryTodoRepository : TodoRepository {

    private val storage = HashMap<Int, TodoItem>()

    private var id = 0
    override suspend fun add(todoItem: TodoItem): Int {
        storage[id] = todoItem
        return id++
    }

    override suspend fun addBatch(todoItems: List<TodoItem>) {
        todoItems.forEach {
            storage[id] = it
            id++
        }
    }

    override suspend fun getAll(): List<TodoItem> {
        return storage.values.toList()
    }

    override suspend fun delete(id: Int) {
        storage.remove(id)
    }
}