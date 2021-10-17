package io.morfly.server.rest

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.morfly.server.domain.NewPost
import io.morfly.server.domain.PostsRepository


fun Route.postRouting(postsRepository: PostsRepository) {
    route("/post") {
        get {
            val includeComments = call.request.queryParameters["includeComments"]
                ?.toBooleanStrictOrNull() ?: false

            val posts = postsRepository.listPosts(includeComments)
            if (posts.isNotEmpty())
                call.respond(posts)
            else
                call.respondText("Posts not found", status = HttpStatusCode.NotFound)
        }

        get("/{id}") {
            val postId = call.parameters["id"] ?: return@get call.respond(HttpStatusCode.BadRequest)
            val includeComments = call.request.queryParameters["includeComments"]
                ?.toBooleanStrictOrNull() ?: false

            val post = postsRepository.getPost(postId, includeComments) ?: return@get call.respondText(
                "Post not found",
                status = HttpStatusCode.NotFound
            )
            call.respond(post)
        }

        post {
            val post = call.receive<NewPost>()
            val createdPost = postsRepository.createPost(post)
            if (createdPost != null)
                call.respond(status = HttpStatusCode.Created, createdPost)
            else
                call.respondText("Unable to create user", status = HttpStatusCode.InternalServerError)
        }

        delete("/{id}") {
            val postId = call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest)
            val isDeleted = postsRepository.deletePost(postId)
            if (isDeleted)
                call.respondText("Post is deleted", status = HttpStatusCode.Accepted)
            else
                call.respondText("Post not found", status = HttpStatusCode.NotFound)
        }
    }
}