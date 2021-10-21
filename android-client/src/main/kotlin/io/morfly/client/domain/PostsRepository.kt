package io.morfly.client.domain

import kotlinx.coroutines.flow.Flow


interface PostsRepository {

    suspend fun listPosts(includeComments: Boolean): List<Post>

    suspend fun getPost(postId: String, includeComments: Boolean): Post?

    suspend fun createPost(post: NewPost): Post?

    suspend fun deletePost(postId: String): Boolean

    suspend fun listComments(postId: String): Flow<List<Comment>>

    suspend fun getComment(commentId: String): Comment?

    suspend fun createComment(comment: NewComment): Comment?

    suspend fun deleteComment(commentId: String): Boolean
}