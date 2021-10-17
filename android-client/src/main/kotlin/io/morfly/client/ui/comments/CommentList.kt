package io.morfly.client.ui.comments

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.morfly.client.domain.Comment
import io.morfly.client.ui.Divider


@Composable
fun CommentList(comments: List<Comment>) {
    LazyColumn(Modifier.padding(top = 16.dp)) {
        itemsIndexed(comments) { i, comment ->
            CommentItem(comment, Modifier.padding(start = 48.dp))

            if (i < comments.lastIndex)
                Divider(Modifier.padding(start = 32.dp))
        }
    }
}