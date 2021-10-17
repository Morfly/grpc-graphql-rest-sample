package io.morfly.server.grpc

import com.google.rpc.ErrorInfo
import io.grpc.Metadata
import io.grpc.Status
import io.grpc.StatusException
import io.grpc.protobuf.ProtoUtils
import io.morfly.posts.Posts
import io.morfly.posts.PostsServiceGrpcKt
import io.morfly.server.domain.Post
import io.morfly.server.domain.PostsRepository


class PostsService(
    private val postsRepository: PostsRepository
) : PostsServiceGrpcKt.PostsServiceCoroutineImplBase() {

    override suspend fun listPosts(request: Posts.ListPostsRequest): Posts.PostList {
        println("gRPC: listPosts")
        try {
            val posts = postsRepository
                .listPosts(request.includeComments)
                .also { println("gRPC: listPosts from repo") }
                .map(Post::toGRPCPost)
                .also { println("gRPC: map toGRPCPost") }
                .toGRPCPostList()
            println("gRPC: listPosts: ${posts.postsCount}")
            return posts
        } catch (e: Throwable) {
            println("gRPC: listPosts error: ${e.message}")
            throw e
        }
    }


    override suspend fun getPost(request: Posts.GetPostRequest): Posts.PostResponse {
        val post = postsRepository.getPost(request.postId, request.includeComments)
            ?: throw postNotFoundException()

        return Posts.PostResponse.newBuilder()
            .setPost(post.toGRPCPost())
            .build()
    }

    override suspend fun createPost(request: Posts.NewPostRequest): Posts.PostResponse {
        val post = postsRepository.createPost(request.toNewPost())
            ?: throw postNotFoundException()

        return Posts.PostResponse.newBuilder()
            .setPost(post.toGRPCPost())
            .build()
    }

    override suspend fun deletePost(request: Posts.DeletePostRequest): Posts.PostDeletedResponse {
        val deleted = postsRepository.deletePost(request.postId)

        return Posts.PostDeletedResponse.newBuilder()
            .setDeleted(deleted)
            .build()
    }

    private fun postNotFoundException(): StatusException {
        val postNotFoundError = ErrorInfo.newBuilder()
            .setReason("Post not found")
            .build()
        val errorDetail = Metadata()
        errorDetail.put(ProtoUtils.keyForProto(postNotFoundError), postNotFoundError)

        return StatusException(Status.NOT_FOUND, errorDetail)
    }
}