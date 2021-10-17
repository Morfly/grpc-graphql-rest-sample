package io.morfly.server.rest

import io.ktor.application.*
import io.ktor.routing.*
import io.morfly.server.domain.PostsRepository
import io.morfly.server.domain.UsersRepository
import org.koin.ktor.ext.inject


fun Application.registerRESTRoutes() {
    routing {
        route("/rest") {
            val usersRepository by inject<UsersRepository>()
            val postsRepository by inject<PostsRepository>()

            userRouting(usersRepository)
            postRouting(postsRepository)
            commentRouting(postsRepository)
        }
    }
}