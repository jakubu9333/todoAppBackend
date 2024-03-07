package com.jakubu9333.repositories

import kotlinx.coroutines.test.runTest
import org.h2.jdbcx.JdbcConnectionPool
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.UUID


class JdbcDatabaseRepositoryTest {

    private lateinit var jdbcDatabaseRepository: JdbcDatabaseRepository

    @BeforeEach
    fun setUp() {
        val jdbcConnectionPool = JdbcConnectionPool.create("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", "root", "")
        jdbcDatabaseRepository = JdbcDatabaseRepository(jdbcConnectionPool)
    }

    @AfterEach
    fun tearDown(){
        val jdbcConnectionPool = JdbcConnectionPool.create("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", "root", "")
        jdbcConnectionPool.connection.prepareStatement("DROP TABLE TODOS").executeUpdate()
    }

    @Test
    fun add() = runTest {
        val todoItem = TodoItem(UUID.randomUUID(), "xd")
        val actualItem = jdbcDatabaseRepository.add(todoItem)

        val expected = listOf(todoItem)
        assertEquals(actualItem, todoItem)
        assertEquals(expected, jdbcDatabaseRepository.getAll())
    }

    @Test
    fun addBatch() = runTest {
        val todoItem = TodoItem(UUID.randomUUID(), "xd")
        val todoItem2 = TodoItem(UUID.randomUUID(), "dd")
        val expected = listOf(todoItem, todoItem2)

        jdbcDatabaseRepository.addBatch(expected)
        val actual = jdbcDatabaseRepository.getAll()
        assertEquals(expected,actual)
    }

    @Test
    fun delete() = runTest{
        val todoItem = TodoItem(UUID.randomUUID(), "xd")
        val todoItemAdded = jdbcDatabaseRepository.add(todoItem)

        val expected = listOf(todoItem)
        assertEquals(todoItemAdded.id, todoItem.id)
        assertEquals(expected, jdbcDatabaseRepository.getAll())
        jdbcDatabaseRepository.delete(todoItemAdded.id)
        assertEquals(listOf<TodoItem>(),jdbcDatabaseRepository.getAll())
    }
}