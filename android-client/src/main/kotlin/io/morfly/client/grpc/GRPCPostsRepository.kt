package io.morfly.client.grpc

import io.morfly.client.domain.*
import io.morfly.comments.Comments
import io.morfly.comments.CommentsServiceGrpcKt
import io.morfly.posts.Posts
import io.morfly.posts.PostsServiceGrpcKt
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class GRPCPostsRepository(
    private val postsServiceStub: PostsServiceGrpcKt.PostsServiceCoroutineStub,
    private val commentsServiceStub: CommentsServiceGrpcKt.CommentsServiceCoroutineStub
) : PostsRepository {

    override suspend fun listPosts(includeComments: Boolean): List<Post> {
        val request = Posts.ListPostsRequest.newBuilder()
            .setIncludeComments(includeComments)
            .build()
        val posts = postsServiceStub.listPosts(request).postsList

        return posts.map { it.toPost() }
    }

    override suspend fun getPost(postId: String, includeComments: Boolean): Post? {
        val request = Posts.GetPostRequest.newBuilder()
            .setPostId(postId)
            .setIncludeComments(includeComments)
            .build()

        val post = postsServiceStub.getPost(request).post ?: return null
        return post.toPost()
    }

    override suspend fun createPost(post: NewPost): Post? {
        val request = post.toGRPCNewPostRequest()

        val createdPost = postsServiceStub.createPost(request).post ?: return null
        return createdPost.toPost()
    }

    override suspend fun deletePost(postId: String): Boolean {
        val request = Posts.DeletePostRequest.newBuilder()
            .setPostId(postId)
            .build()

        return postsServiceStub.deletePost(request).deleted
    }

    override suspend fun listComments(postId: String): Flow<List<Comment>> {
        val request = Comments.ListCommentsRequest.newBuilder()
            .setPostId(postId)
            .build()

        return commentsServiceStub.listComments(request)
            .map { it.commentsList.map { it.toComment() } }
    }

    override suspend fun getComment(commentId: String): Comment? {
        val request = Comments.GetCommentRequest.newBuilder()
            .setCommentId(commentId)
            .build()

        val comment = commentsServiceStub.getComment(request).comment ?: return null
        return comment.toComment()
    }

    override suspend fun createComment(comment: NewComment): Comment? {
        val request = comment.toGRPCNewCommentRequest()

        val createdComment = commentsServiceStub.createComment(request).comment ?: return null
        return createdComment.toComment()
    }

    override suspend fun deleteComment(commentId: String): Boolean {
        val request = Comments.DeleteCommentRequest.newBuilder()
            .setCommentId(commentId)
            .build()

        return commentsServiceStub.deleteComment(request).deleted
    }
}