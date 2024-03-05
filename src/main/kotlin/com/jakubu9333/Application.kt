package com.jakubu9333

import com.jakubu9333.plugins.*
import com.jakubu9333.repositories.DatabaseTodoRepository
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.h2.jdbcx.JdbcConnectionPool
import org.jetbrains.exposed.sql.Database

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureSerialization()
    configureDatabases()
    //configureTemplating()
    configureHTTP()
    configureMonitoring()
    val database = initDatabase()
    val databaseTodoRepository = DatabaseTodoRepository(database)
    configureRouting(databaseTodoRepository)
}

fun initDatabase(): Database {
    val driverClassName = "org.h2.Driver"
    val jdbcURL = "jdbc:h2:file:./build/db"
    val pool = JdbcConnectionPool.create(jdbcURL,"","")
    val database = Database.connect(pool)
    return database
}
