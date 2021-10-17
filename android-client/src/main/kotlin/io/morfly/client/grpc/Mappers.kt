package io.morfly.client.grpc

import com.google.protobuf.Timestamp
import io.morfly.client.domain.*
import io.morfly.comments.Comments
import io.morfly.posts.Posts
import io.morfly.users.Users
import kotlinx.datetime.Instant


fun Timestamp.toInstant() =
    Instant.fromEpochSeconds(seconds)

fun Instant.toGRPCTimestamp(): Timestamp = Timestamp
    .newBuilder()
    .setSeconds(epochSeconds)
    .build()

// ===== User =====

fun Users.User.toUser() = User(
    id = id,
    userName = userName,
    displayName = displayName,
    avatarUrl = avatarUrl
)

fun NewUser.toGRPCNewUserRequest(): Users.NewUserRequest = Users.NewUserRequest
    .newBuilder()
    .setUserName(userName)
    .setDisplayName(displayName)
    .setAvatarUrl(avatarUrl)
    .build()

// ===== Post =====

fun Posts.Post.toPost() = Post(
    id = id,
    author = author.toUser(),
    content = content,
    timestamp = timestamp.toInstant(),
    comments = commentsList.map { it.toComment() }
)

fun NewPost.toGRPCNewPostRequest(): Posts.NewPostRequest = Posts.NewPostRequest
    .newBuilder()
    .setAuthorId(authorId)
    .setContent(content)
    .setTimestamp(timestamp.toGRPCTimestamp())
    .build()

// ===== Comment =====

fun Comments.Comment.toComment() = Comment(
    id = id,
    postId = postId,
    author = author.toUser(),
    content = content,
    timestamp = timestamp.toInstant()
)

fun NewComment.toGRPCNewCommentRequest(): Comments.NewCommentRequest = Comments.NewCommentRequest
    .newBuilder()
    .setPostId(postId)
    .setAuthorId(authorId)
    .setContent(content)
    .setTimestamp(timestamp.toGRPCTimestamp())
    .build()