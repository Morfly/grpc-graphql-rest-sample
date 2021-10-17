package io.morfly.client.ui.posts

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.morfly.client.domain.Post
import io.morfly.client.ui.theme.LinkColor


@Composable
fun PostItemWithCommentsBanner(
    post: Post,
    onShowComments: (postId: String) -> Unit,
) {
    Column {
        PostItem(post)
        ShowCommentsBanner(post.id, onShowComments, Modifier.padding(top = 12.dp))
    }
}

@Composable
fun PostItem(post: Post) {
    Column(Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp)) {
        Text(post.author.displayName, style = MaterialTheme.typography.subtitle1)
        Text(post.author.userName, style = MaterialTheme.typography.subtitle2)
        Text(
            post.content,
            Modifier.padding(top = 16.dp),
            style = MaterialTheme.typography.body1
        )
    }
}

@Composable
fun ShowCommentsBanner(
    postId: String,
    onShowComments: (postId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier
            .fillMaxWidth()
            .clickable { onShowComments(postId) }
            .padding(vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            "Show comments",
            style = MaterialTheme.typography.body2,
            color = LinkColor
        )
    }
}