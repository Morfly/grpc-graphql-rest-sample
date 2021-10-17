package io.morfly.server.graphql

import com.apurebase.kgraphql.schema.dsl.SchemaBuilder
import io.morfly.server.domain.NewComment
import io.morfly.server.domain.PostsRepository


fun SchemaBuilder.commentsSchema(postsRepository: PostsRepository) {
    query("comments") {
        resolver { postId: String -> postsRepository.listComments(postId) }
    }

    query("comment") {
        resolver { commentId: String -> postsRepository.getComment(commentId) }
    }

    mutation("createComment") {
        resolver { comment: NewComment -> postsRepository.createComment(comment) }
    }

    mutation("deleteComment") {
        resolver { commentId: String -> postsRepository.deleteComment(commentId) }
    }
}