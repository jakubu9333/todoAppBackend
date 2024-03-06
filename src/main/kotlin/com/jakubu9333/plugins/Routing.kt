package com.jakubu9333.plugins

import com.jakubu9333.repositories.TodoRepository
import com.jakubu9333.routes.apiDeleteTodo
import com.jakubu9333.routes.apiGetAllTodo
import com.jakubu9333.routes.apiPostMultipleTodo
import com.jakubu9333.routes.apiPostTodo
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting(todoRepository: TodoRepository) {
    routing {
        apiPostTodo(todoRepository)
        apiPostMultipleTodo(todoRepository)
        apiDeleteTodo(todoRepository)
        apiGetAllTodo(todoRepository)

        get("/") {
            call.respondText("Hello World!")
        }
    }
}
