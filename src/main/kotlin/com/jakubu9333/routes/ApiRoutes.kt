package com.jakubu9333.routes

import com.jakubu9333.repositories.TodoItem
import com.jakubu9333.repositories.TodoRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun Route.apiPostTodo(todoRepository: TodoRepository) {
    route("/api/todo") {
        post {
            val todo = call.receive<TodoItem>()
            val id = todoRepository.add(todo)
            call.respond(HttpStatusCode.Created, id)
        }
    }
}

fun Route.apiPostMultipleTodo(todoRepository: TodoRepository) {
    route("/api/todo/batch") {
        post {
            val todos = call.receive<List<TodoItem>>()
            todoRepository.addBatch(todos)
            call.respond(HttpStatusCode.Created)
        }
    }
}

fun Route.apiGetAllTodo(todoRepository: TodoRepository) {
    get("/api/todo") {

        val todos = todoRepository.getAll()
        call.respond(HttpStatusCode.OK, todos)
    }
}

fun Route.apiDeleteTodo(todoRepository: TodoRepository) {
    delete("/api/todo/{id}") {
        val id = call.parameters["id"]?.toInt()
        if (id == null) {
            call.respond(HttpStatusCode.BadRequest, "no id")
            return@delete
        }

        todoRepository.delete(id)
        call.respond(HttpStatusCode.NoContent)
    }
}
