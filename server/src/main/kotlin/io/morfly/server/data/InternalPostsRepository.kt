package io.morfly.server.data

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.morfly.server.DATABASE_SERVICE_ENDPOINT
import io.morfly.server.domain.*


class InternalPostsRepository(
    private val client: HttpClient
) : PostsRepository {

    override suspend fun listPosts(includeComments: Boolean): List<Post> =
        client.request("$DATABASE_SERVICE_ENDPOINT/rest/post?includeComments=$includeComments") {
            method = HttpMethod.Get
        }

    override suspend fun getPost(postId: String, includeComments: Boolean): Post? =
        client.request("$DATABASE_SERVICE_ENDPOINT/rest/post/$postId?includeComments=$includeComments") {
            method = HttpMethod.Get
        }

    override suspend fun createPost(post: NewPost): Post? =
        client.request("$DATABASE_SERVICE_ENDPOINT/rest/post") {
            method = HttpMethod.Post
            header(HttpHeaders.ContentType, ContentType.Application.Json)
            body = post
        }

    override suspend fun deletePost(postId: String): Boolean {
        val response: HttpResponse =
            client.request("$DATABASE_SERVICE_ENDPOINT/rest/post/$postId") {
                method = HttpMethod.Delete
            }
        return response.status == HttpStatusCode.Accepted
    }

    override suspend fun listComments(postId: String): List<Comment> =
        client.request("$DATABASE_SERVICE_ENDPOINT/rest/post/$postId/comment") {
            method = HttpMethod.Get
        }

    override suspend fun getComment(commentId: String): Comment? =
        client.request("$DATABASE_SERVICE_ENDPOINT/rest/post/comment/$commentId") {
            method = HttpMethod.Get
        }

    override suspend fun createComment(comment: NewComment): Comment? =
        client.request("$DATABASE_SERVICE_ENDPOINT/rest/post/comment") {
            method = HttpMethod.Post
            header(HttpHeaders.ContentType, ContentType.Application.Json)
            body = comment
        }

    override suspend fun deleteComment(commentId: String): Boolean =
        client.request("$DATABASE_SERVICE_ENDPOINT/rest/post/comment/$commentId") {
            method = HttpMethod.Delete
        }
}