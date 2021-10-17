package io.morfly.client.ui.comments

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.morfly.client.domain.Comment


@Composable
fun CommentItem(comment: Comment, modifier: Modifier = Modifier) {
    Column(modifier.padding(16.dp)) {
        Text(comment.author.displayName, style = MaterialTheme.typography.subtitle1)
        Text(comment.author.userName, style = MaterialTheme.typography.subtitle2)
        Text(
            comment.content,
            Modifier.padding(top = 16.dp),
            style = MaterialTheme.typography.body1
        )
    }
}