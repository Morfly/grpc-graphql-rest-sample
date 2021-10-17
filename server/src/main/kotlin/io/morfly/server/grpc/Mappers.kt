package io.morfly.server.grpc

import com.google.protobuf.Timestamp
import io.morfly.comments.Comments
import io.morfly.posts.Posts
import io.morfly.server.domain.*
import io.morfly.users.Users
import kotlinx.datetime.Instant


fun Timestamp.toInstant() =
    Instant.fromEpochSeconds(seconds)

fun Instant.toGRPCTimestamp(): Timestamp = Timestamp.newBuilder()
    .setSeconds(epochSeconds)
    .build()

// ===== User ======

fun User.toGRPCUser(): Users.User {
    val builder = Users.User.newBuilder()
        .setId(id)
        .setUserName(userName)
        .setDisplayName(displayName)

    if (avatarUrl != null) builder.avatarUrl = avatarUrl
    return builder.build()
}

fun List<Users.User>.toGRPCUserList(): Users.UserList = Users.UserList.newBuilder()
    .addAllUsers(this)
    .build()

fun Users.NewUserRequest.toNewUser() = NewUser(
    userName = userName,
    displayName = displayName,
    avatarUrl = avatarUrl
)

// ===== Post =====

fun Post.toGRPCPost(): Posts.Post = Posts.Post.newBuilder()
    .setId(id)
    .setAuthor(author.toGRPCUser())
    .setContent(content)
    .setTimestamp(timestamp.toGRPCTimestamp())
    .addAllComments(comments.map(Comment::toGRPCComment))
    .build()

fun List<Posts.Post>.toGRPCPostList(): Posts.PostList = Posts.PostList.newBuilder()
    .addAllPosts(this)
    .build()

fun Posts.NewPostRequest.toNewPost() = NewPost(
    authorId = authorId,
    content = content,
    timestamp = timestamp.toInstant()
)

// ===== Comment =====

fun Comment.toGRPCComment(): Comments.Comment = Comments.Comment.newBuilder()
    .setId(id)
    .setPostId(postId)
    .setAuthor(author.toGRPCUser())
    .setContent(content)
    .setTimestamp(timestamp.toGRPCTimestamp())
    .build()

fun Comments.NewCommentRequest.toNewComment() = NewComment(
    postId = postId,
    authorId = authorId,
    content = content,
    timestamp = timestamp.toInstant()
)

fun List<Comments.Comment>.toGRPCCommentList(): Comments.CommentList =
    Comments.CommentList.newBuilder()
        .addAllComments(this)
        .build()