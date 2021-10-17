package io.morfly.server.rest

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.morfly.server.domain.NewUser
import io.morfly.server.domain.UsersRepository


fun Route.userRouting(usersRepository: UsersRepository) {
    route("/user") {
        get {
            val users = usersRepository.listUsers()
            if (users.isNotEmpty())
                call.respond(users)
            else
                call.respondText("Users not found", status = HttpStatusCode.NotFound)
        }

        get("/{id}") {
            val userId = call.parameters["id"] ?: return@get call.respond(HttpStatusCode.BadRequest)
            val user = usersRepository.getUser(userId) ?: return@get call.respondText(
                "User not found",
                status = HttpStatusCode.NotFound
            )
            call.respond(user)
        }

        post {
            val user = call.receive<NewUser>()
            val createdUser = usersRepository.createUser(user)
            if (createdUser != null)
                call.respond(status = HttpStatusCode.Created, createdUser)
            else
                call.respondText("Unable to create user", status = HttpStatusCode.InternalServerError)
        }

        delete("/{id}") {
            val userId = call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest)
            val isDeleted = usersRepository.deleteUser(userId)
            if (isDeleted)
                call.respondText("User is deleted", status = HttpStatusCode.Accepted)
            else
                call.respondText("User not found", status = HttpStatusCode.NotFound)
        }
    }
}