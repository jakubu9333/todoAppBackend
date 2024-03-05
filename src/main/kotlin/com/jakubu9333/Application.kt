package com.jakubu9333

import com.jakubu9333.plugins.*
import com.jakubu9333.repositories.JdbcDatabaseRepository
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.h2.jdbcx.JdbcConnectionPool
import javax.sql.DataSource

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
    val database = initDataBasePool()
    val databaseTodoRepository = JdbcDatabaseRepository(database)
    configureRouting(databaseTodoRepository)
}

fun initDataBasePool(): DataSource{
    val driverClassName = "org.h2.Driver"
    val jdbcURL = "jdbc:h2:file:./build/db"
    val pool = JdbcConnectionPool.create(jdbcURL,"","")
    return pool
}
