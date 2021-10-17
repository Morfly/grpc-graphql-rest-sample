package io.morfly.client.graphql

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.coroutines.await
import com.apollographql.apollo.exception.ApolloException
import io.morfly.client.*
import io.morfly.client.domain.*


class GraphQLPostsRepository(
    private val client: ApolloClient
) : PostsRepository {

    override suspend fun listPosts(includeComments: Boolean): List<Post> {
        val response = try {
            client.query(ListPostsQuery()).await()
        } catch (e: ApolloException) {
            return emptyList()
        }

        val posts = response.data?.posts ?: emptyList()
        return posts.map {
            Post(
                id = it.id,
                author = User(
                    it.author.id,
                    it.author.userName,
                    it.author.displayName,
                    it.author.avatarUrl
                ),
                content = it.content,
                timestamp = it.timestamp,
                comments = emptyList()
            )
        }
    }

    override suspend fun getPost(postId: String, includeComments: Boolean): Post? {
        val response = try {
            client.query(GetPostQuery(postId)).await()
        } catch (e: ApolloException) {
            return null
        }

        val post = response.data?.post ?: return null
        return post.run {
            Post(
                id = id,
                author = User(
                    author.id,
                    author.userName,
                    author.displayName,
                    author.avatarUrl
                ),
                content = content,
                timestamp = timestamp,
                comments = emptyList()
            )
        }
    }

    override suspend fun createPost(post: NewPost): Post? {
        val newPost = io.morfly.client.type.NewPost(
            authorId = post.authorId,
            content = post.content,
            timestamp = post.timestamp
        )
        val mutation = CreatePostMutation(newPost)
        val response = try {
            client.mutate(mutation).await()
        } catch (e: ApolloException) {
            return null
        }

        val createdPost = response.data?.createPost ?: return null
        return createdPost.run {
            Post(
                id = id,
                author = User(
                    author.id,
                    author.userName,
                    author.displayName,
                    author.avatarUrl
                ),
                content = content,
                timestamp = timestamp,
                comments = emptyList()
            )
        }
    }

    override suspend fun deletePost(postId: String): Boolean {
        val mutation = DeletePostMutation(postId)
        val response = try {
            client.mutate(mutation).await()
        } catch (e: ApolloException) {
            return false
        }

        return response.data?.deletePost ?: false
    }

    override suspend fun listComments(postId: String): List<Comment> {
        val response = try {
            client.query(ListCommentsQuery(postId)).await()
        } catch (e: ApolloException) {
            return emptyList()
        }

        val comments = response.data?.comments ?: emptyList()
        return comments.map {
            Comment(
                id = it.id,
                postId = it.postId,
                author = User(
                    it.author.id,
                    it.author.userName,
                    it.author.displayName,
                    it.author.avatarUrl
                ),
                content = it.content,
                timestamp = it.timestamp
            )
        }
    }

    override suspend fun getComment(commentId: String): Comment? {
        val response = try {
            client.query(GetCommentQuery(commentId)).await()
        } catch (e: ApolloException) {
            return null
        }

        val comment = response.data?.comment ?: return null
        return comment.run {
            Comment(
                id = id,
                postId = postId,
                author = User(
                    author.id,
                    author.userName,
                    author.displayName,
                    author.avatarUrl
                ),
                content = content,
                timestamp = timestamp
            )
        }
    }

    override suspend fun createComment(comment: NewComment): Comment? {
        val newComment = io.morfly.client.type.NewComment(
            authorId = comment.authorId,
            content = comment.content,
            postId = comment.postId,
            timestamp = comment.timestamp
        )
        val mutation = CreateCommentMutation(newComment)
        val response = try {
            client.mutate(mutation).await()
        } catch (e: ApolloException) {
            return null
        }

        val createdComment = response.data?.createComment ?: return null
        return createdComment.run {
            Comment(
                id = id,
                postId = postId,
                author = User(
                    author.id,
                    author.userName,
                    author.displayName,
                    author.avatarUrl
                ),
                content = content,
                timestamp = timestamp
            )
        }
    }

    override suspend fun deleteComment(commentId: String): Boolean {
        val mutation = DeleteCommentMutation(commentId)
        val response = try {
            client.mutate(mutation).await()
        } catch (e: ApolloException) {
            return false
        }

        return response.data?.deleteComment ?: false
    }
}