package io.morfly.server.rest

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.morfly.server.domain.NewComment
import io.morfly.server.domain.PostsRepository


fun Route.commentRouting(postsRepository: PostsRepository) {
    route("/post") {
        route("/comment") {
            get("/{id}") {
                val commentId = call.parameters["id"] ?: return@get call.respond(HttpStatusCode.BadRequest)
                val comment = postsRepository.getComment(commentId) ?: return@get call.respondText(
                    "Comment not found",
                    status = HttpStatusCode.NotFound
                )
                call.respond(comment)
            }

            post {
                val comment = call.receive<NewComment>()
                val createdComment = postsRepository.createComment(comment)
                if (createdComment != null)
                    call.respond(status = HttpStatusCode.Created, createdComment)
                else
                    call.respondText("Unable to create comment", status = HttpStatusCode.InternalServerError)
            }

            delete("/{id}") {
                val commentId = call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest)
                val isDeleted = postsRepository.deleteComment(commentId)
                if (isDeleted)
                    call.respondText("Comment is deleted", status = HttpStatusCode.Accepted)
                else
                    call.respondText("Comment not found", status = HttpStatusCode.NotFound)
            }
        }

        get("/{id}/comment") {
            val postId = call.parameters["id"] ?: return@get call.respond(HttpStatusCode.BadRequest)
            val comments = postsRepository.listComments(postId)
            if (comments.isNotEmpty())
                call.respond(comments)
            else
                call.respondText("Comments not found", status = HttpStatusCode.NotFound)
        }
    }
}