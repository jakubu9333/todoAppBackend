package com.jakubu9333.repositories

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.sql.Connection
import java.sql.Statement
import javax.sql.DataSource

class JdbcDatabaseRepository(private val dataSource: DataSource) : TodoRepository {

    companion object {
        private const val CREATE_TABLE_SQL =
            "CREATE TABLE IF NOT EXISTS TODOS (ID SERIAL PRIMARY KEY, \"ITEM\" VARCHAR(50) NOT NULL)"
        private const val INSERT_SQL = "INSERT INTO TODOS (ITEM) VALUES ?"
        private const val SELECT_ALL_SQL = "SELECT ITEM FROM TODOS"
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

    override suspend fun add(todoItem: TodoItem): Int =
        runQuery {
            val statement = it.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)
            statement.setString(1, todoItem.item)
            statement.executeUpdate()
            val generatedKeys = statement.generatedKeys
            if (generatedKeys.next()) {
                return@runQuery generatedKeys.getInt(1)
            } else {
                throw Exception("Unable to retrieve the id of the newly inserted city")
            }

        }

    override suspend fun addBatch(todoItems: List<TodoItem>) {
       runQuery { connection ->
           val statement = connection.prepareStatement(INSERT_SQL)
           todoItems.forEach {
               statement.setString(1,it.item)
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
                val str = dbResult.getString(1)
                result.add(TodoItem(str))
            }
            return@runQuery result

        }


    override suspend fun delete(id: Int) =
        runQuery {
            val statement = it.prepareStatement(DELETE_ID_SQL)
            statement.setInt(1, id)
            statement.execute()
            return@runQuery
        }


}