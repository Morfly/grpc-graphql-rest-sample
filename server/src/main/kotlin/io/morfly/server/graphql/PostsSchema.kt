package io.morfly.server.graphql

import com.apurebase.kgraphql.schema.dsl.SchemaBuilder
import io.morfly.server.domain.NewPost
import io.morfly.server.domain.PostsRepository


fun SchemaBuilder.postsSchema(postsRepository: PostsRepository) {
    query("posts") {
        resolver { includeComments: Boolean -> postsRepository.listPosts(includeComments) }
            .withArgs { arg<Boolean> { name = "includeComments"; defaultValue = false } }
    }

    query("post") {
        resolver { postId: String, includeComments: Boolean -> postsRepository.getPost(postId, includeComments) }
            .withArgs { arg<Boolean> { name = "includeComments"; defaultValue = false } }
    }

    mutation("createPost") {
        resolver { post: NewPost -> postsRepository.createPost(post) }
    }

    mutation("deletePost") {
        resolver { postId: String -> postsRepository.deletePost(postId) }
    }
}