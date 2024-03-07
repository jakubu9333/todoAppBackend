package com.jakubu9333.repositories

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.sql.Connection
import java.sql.Statement
import java.util.UUID
import javax.sql.DataSource

class JdbcDatabaseRepository(private val dataSource: DataSource) : TodoRepository {

    companion object {
        private const val CREATE_TABLE_SQL =
            "CREATE TABLE IF NOT EXISTS TODOS (ID UUID PRIMARY KEY, \"ITEM\" VARCHAR(50) NOT NULL)"
        private const val INSERT_SQL = "INSERT INTO TODOS (ID,ITEM) VALUES (?,?)"
        private const val SELECT_ALL_SQL = "SELECT ID, ITEM FROM TODOS"
        private const val DELETE_ID_SQL = "DELETE FROM TODOS WHERE ID = ?"
    }


    init {
        val connection = dataSource.connection
        val statement = connection.createStatement()
        statement.executeUpdate(CREATE_TABLE_SQL)
        connection.close()
    }

    private suspend fun <T> runQuery(block: (Connection) -> T): T = withContext(Dispatchers.IO) {
        val connection = dataSource.connection
        connection.use {
            val result = block(connection)
            return@withContext result
        }
    }

    override suspend fun add(todoItem: TodoItem): UUID =
        runQuery {
            val statement = it.prepareStatement(INSERT_SQL)
            statement.setObject(1, todoItem.id)
            statement.setString(2, todoItem.item)
            statement.executeUpdate()
            return@runQuery todoItem.id
        }

    override suspend fun addBatch(todoItems: List<TodoItem>) {
        runQuery { connection ->
            val statement = connection.prepareStatement(INSERT_SQL)
            todoItems.forEach { todoItem ->
                statement.setObject(1, todoItem.id)
                statement.setString(2, todoItem.item)
                statement.addBatch()
            }
            statement.executeBatch()
        }
    }

    override suspend fun getAll(): List<TodoItem> =
        runQuery {
            val statement = it.createStatement()
            val result = mutableListOf<TodoItem>()
            val dbResult = statement.executeQuery(SELECT_ALL_SQL)
            while (dbResult.next()) {
                val id= dbResult.getObject(1) as UUID

                val item = dbResult.getString(2)
                result.add(TodoItem(id, item))
            }
            return@runQuery result

        }


    override suspend fun delete(id: UUID) =
        runQuery {
            val statement = it.prepareStatement(DELETE_ID_SQL)
            statement.setObject(1, id)
            statement.execute()
            return@runQuery
        }


}