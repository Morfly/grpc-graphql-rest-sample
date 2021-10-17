package io.morfly.server.grpc

import com.google.rpc.ErrorInfo
import io.grpc.Metadata
import io.grpc.Status
import io.grpc.StatusException
import io.grpc.protobuf.ProtoUtils
import io.morfly.comments.Comments
import io.morfly.comments.CommentsServiceGrpcKt
import io.morfly.server.domain.Comment
import io.morfly.server.domain.PostsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf


class CommentsService(
    private val postsRepository: PostsRepository
) : CommentsServiceGrpcKt.CommentsServiceCoroutineImplBase() {

    override fun listComments(request: Comments.ListCommentsRequest): Flow<Comments.CommentList> =
        flow {
            postsRepository
                .listComments(request.postId)
                .map(Comment::toGRPCComment)
                .toGRPCCommentList()
                .also { emitAll(flowOf(it)) }
        }

    override suspend fun getComment(request: Comments.GetCommentRequest): Comments.CommentResponse {
        val comment = postsRepository.getComment(request.commentId)
            ?: throw commentNotFoundException()

        return Comments.CommentResponse.newBuilder()
            .setComment(comment.toGRPCComment())
            .build()
    }

    override suspend fun createComment(request: Comments.NewCommentRequest): Comments.CommentResponse {
        val comment = postsRepository.createComment(request.toNewComment())
            ?: throw commentNotFoundException()

        return Comments.CommentResponse.newBuilder()
            .setComment(comment.toGRPCComment())
            .build()
    }

    override suspend fun deleteComment(request: Comments.DeleteCommentRequest): Comments.CommentDeletedResponse {
        val deleted = postsRepository.deleteComment(request.commentId)

        return Comments.CommentDeletedResponse.newBuilder()
            .setDeleted(deleted)
            .build()
    }

    private fun commentNotFoundException(): StatusException {
        val commentNotFoundError = ErrorInfo.newBuilder()
            .setReason("Comment not found")
            .build()
        val errorDetail = Metadata()
        errorDetail.put(ProtoUtils.keyForProto(commentNotFoundError), commentNotFoundError)

        return StatusException(Status.NOT_FOUND, errorDetail)
    }
}