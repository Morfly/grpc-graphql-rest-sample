package io.morfly.client.ui.comments

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.morfly.client.domain.Comment
import io.morfly.client.domain.Post
import io.morfly.client.ui.posts.PostItem


@Composable
fun CommentsScreen(postId: String, viewModel: CommentsViewModel) {
    LaunchedEffect(viewModel) { viewModel.fetchComments(postId) }

    val post by viewModel.post.collectAsState()
    val comments by viewModel.comments.collectAsState()

    post?.let {
        CommentsScreenContent(
            post = it,
            comments = comments,
            onSendComment = viewModel::sendComment
        )
    }
}

@Composable
fun CommentsScreenContent(
    post: Post,
    comments: List<Comment>,
    onSendComment: (comment: String) -> Unit
) {
    Column {
        PostItem(post)
        Divider(Modifier.padding(top = 16.dp))
        Box(Modifier.weight(1f)) {
            if (comments.isNotEmpty())
                CommentList(comments)
            else NoCommentsBanner()
        }
        SendComment(onSendComment)
    }
}

@Composable
fun NoCommentsBanner() {
    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Text("No comments yet", style = MaterialTheme.typography.body2)
    }
}