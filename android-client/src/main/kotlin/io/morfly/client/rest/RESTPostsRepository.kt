package io.morfly.client.rest

import io.morfly.client.domain.*


class RESTPostsRepository(
    private val service: RESTService
) : PostsRepository {

    override suspend fun listPosts(includeComments: Boolean): List<Post> =
        service.listPosts(includeComments)

    override suspend fun getPost(postId: String, includeComments: Boolean): Post? =
        service.getPost(postId, includeComments)

    override suspend fun createPost(post: NewPost): Post? =
        service.createPost(post)

    override suspend fun deletePost(postId: String): Boolean =
        service.deletePost(postId)

    override suspend fun listComments(postId: String): List<Comment> =
        service.listComments(postId)

    override suspend fun getComment(commentId: String): Comment? =
        service.getComment(commentId)

    override suspend fun createComment(comment: NewComment): Comment? =
        service.createComment(comment)

    override suspend fun deleteComment(commentId: String): Boolean =
        service.deleteComment(commentId)
}