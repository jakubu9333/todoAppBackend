package com.jakubu9333.repositories

import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

class ExposedDatabaseTodoRepository(database: Database) : TodoRepository {
    object Todos : Table() {
        val id = uuid("id")
        val item = varchar("item", length = 255)

        override val primaryKey = PrimaryKey(id)
    }

    init {
        transaction(database) {
            SchemaUtils.create(Todos)
        }
    }

    private suspend fun <T> dbQuery(block: suspend Transaction.() -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }

    override suspend fun add(todoItem: TodoItem): TodoItem {
        return dbQuery {
            Todos.insert {
                it[id] = todoItem.id
                it[item] = todoItem.item
            }
            return@dbQuery todoItem
        }
    }

    override suspend fun addBatch(todoItems: List<TodoItem>) {
        dbQuery {
            Todos.batchInsert(todoItems) {
                this[Todos.item] = it.item
            }
        }
    }

    override suspend fun getAll(): List<TodoItem> {
        return dbQuery {
            Todos.selectAll().map { TodoItem(it[Todos.id], it[Todos.item]) }
        }

    }

    override suspend fun delete(id: UUID) {
        dbQuery {
            Todos.deleteWhere { Todos.id.eq(id) }
        }
    }
}