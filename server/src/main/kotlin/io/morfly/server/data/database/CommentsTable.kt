package io.morfly.server.data.database

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp


object CommentsTable : IntIdTable() {
    val postId = reference("post_id", PostsTable, onDelete = ReferenceOption.CASCADE)
    val authorId = reference("author_id", UsersTable, onDelete = ReferenceOption.CASCADE)

    val content = varchar("content", length = 300)
    val timestamp = timestamp("timestamp")
}