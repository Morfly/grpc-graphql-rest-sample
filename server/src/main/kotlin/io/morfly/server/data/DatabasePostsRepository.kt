package io.morfly.server.data

import io.morfly.server.data.database.CommentsTable
import io.morfly.server.data.database.PostsTable
import io.morfly.server.data.database.UsersTable
import io.morfly.server.domain.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SortOrder.ASC
import org.jetbrains.exposed.sql.transactions.transaction


class DatabasePostsRepository : PostsRepository {

    @Suppress("RemoveRedundantQualifierName")
    override suspend fun listPosts(includeComments: Boolean): List<Post> = transaction {
        if (includeComments) {
            val commentAuthorsTable = UsersTable.alias("COMMENT_AUTHORS")

            val comments = CommentsTable
                .innerJoin(
                    commentAuthorsTable,
                    { CommentsTable.authorId },
                    { commentAuthorsTable[UsersTable.id] })

            val posts = PostsTable
                .innerJoin(UsersTable, { PostsTable.authorId }, { UsersTable.id })
                .leftJoin(comments, { PostsTable.id }, { CommentsTable.postId })

            posts.selectAll()
                .orderBy(PostsTable.timestamp to ASC, CommentsTable.timestamp to ASC)
                .groupBy(
                    { it.extractPost() },
                    { it.tryExtractComment { extractUser(commentAuthorsTable) } })
                .map { (post, comments) -> post.copy(comments = comments.filterNotNull()) }
        } else
            (PostsTable innerJoin UsersTable)
                .select { PostsTable.authorId eq UsersTable.id }
                .map { it.extractPost() }
    }

    @Suppress("RemoveRedundantQualifierName")
    override suspend fun getPost(postId: String, includeComments: Boolean): Post? {
        val intPostId = postId.toIntOrNull() ?: return null

        return transaction {

            if (includeComments) {
                val commentAuthorsTable = UsersTable.alias("COMMENT_AUTHORS")

                val comments = CommentsTable
                    .innerJoin(
                        commentAuthorsTable,
                        { CommentsTable.authorId },
                        { commentAuthorsTable[UsersTable.id] })

                val posts = PostsTable
                    .innerJoin(UsersTable, { PostsTable.authorId }, { UsersTable.id })
                    .leftJoin(comments, { PostsTable.id }, { CommentsTable.postId })

                posts
                    .select { PostsTable.id eq intPostId }
                    .orderBy(PostsTable.timestamp to ASC, CommentsTable.timestamp to ASC)
                    .groupBy(
                        { it.extractPost() },
                        { it.tryExtractComment { extractUser(commentAuthorsTable) } })
                    .map { (post, comments) -> post.copy(comments = comments.filterNotNull()) }
                    .firstOrNull()
            } else
                (PostsTable innerJoin UsersTable)
                    .select { PostsTable.id eq intPostId }
                    .firstOrNull()
                    ?.extractPost()
        }
    }

    override suspend fun createPost(post: NewPost): Post? {
        val intUserId = post.authorId.toIntOrNull() ?: return null

        return transaction {

            val inserted = PostsTable.insert {
                it[content] = post.content
                it[timestamp] = post.timestamp
                it[authorId] = intUserId
            }.resultedValues?.firstOrNull()

            val insertedPostId = inserted?.get(PostsTable.id) ?: return@transaction null

            (PostsTable innerJoin UsersTable)
                .select { PostsTable.id eq insertedPostId }
                .firstOrNull()
                ?.extractPost()
        }
    }

    override suspend fun deletePost(postId: String): Boolean {
        val intPostId = postId.toIntOrNull() ?: return false

        return transaction {

            val numDeleted: Int = PostsTable.deleteWhere { PostsTable.id eq intPostId }
            numDeleted > 0
        }
    }

    override suspend fun listComments(postId: String): List<Comment> {
        val intPostId = postId.toIntOrNull() ?: return emptyList()

        return transaction {

            (CommentsTable innerJoin UsersTable)
                .select { (CommentsTable.postId eq intPostId) and (CommentsTable.authorId eq UsersTable.id) }
                .map { it.extractComment() }
        }
    }

    override suspend fun getComment(commentId: String): Comment? {
        val intCommentId = commentId.toIntOrNull() ?: return null

        return transaction {

            (CommentsTable innerJoin UsersTable)
                .select { (CommentsTable.id eq intCommentId) and (CommentsTable.authorId eq UsersTable.id) }
                .firstOrNull()
                ?.extractComment()
        }
    }

    override suspend fun createComment(comment: NewComment): Comment? {
        val intPostId = comment.postId.toIntOrNull() ?: return null
        val intUserId = comment.authorId.toIntOrNull() ?: return null

        return transaction {

            val inserted = CommentsTable.insert {
                it[postId] = intPostId
                it[authorId] = intUserId
                it[content] = comment.content
                it[timestamp] = comment.timestamp
            }.resultedValues?.firstOrNull()

            inserted ?: return@transaction null

            (CommentsTable innerJoin UsersTable)
                .select {
                    (CommentsTable.id eq inserted[CommentsTable.id]) and (CommentsTable.authorId eq UsersTable.id)
                }
                .firstOrNull()
                ?.extractComment()
        }
    }

    override suspend fun deleteComment(commentId: String): Boolean {
        val intCommentId = commentId.toIntOrNull() ?: return false

        return transaction {

            val numDeleted: Int = CommentsTable.deleteWhere { CommentsTable.id eq intCommentId }
            numDeleted > 0
        }
    }
}