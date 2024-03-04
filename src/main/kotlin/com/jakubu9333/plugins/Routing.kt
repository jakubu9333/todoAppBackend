package com.jakubu9333.plugins

import com.jakubu9333.repositories.InMemoryTodoRepository
import com.jakubu9333.routes.apiDeleteTodo
import com.jakubu9333.routes.apiGetAllTodo
import com.jakubu9333.routes.apiPostTodo
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    val inMemoryTodoRepository = InMemoryTodoRepository()
    routing {
        apiPostTodo(inMemoryTodoRepository)
        apiDeleteTodo(inMemoryTodoRepository)
        apiGetAllTodo(inMemoryTodoRepository)

        get("/") {
            call.respondText("Hello World!")
        }
    }
}
