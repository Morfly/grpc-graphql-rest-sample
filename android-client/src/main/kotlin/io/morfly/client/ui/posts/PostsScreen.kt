package io.morfly.client.ui.posts

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import io.morfly.client.domain.Post
import io.morfly.client.ui.Divider


@Composable
fun PostsScreen(viewModel: PostsViewModel, onShowComments: (postId: String) -> Unit) {
    LaunchedEffect(viewModel) { viewModel.fetchPosts() }

    val posts by viewModel.posts.collectAsState()

    PostsScreenContent(posts, onShowComments = onShowComments)
}

@Composable
fun PostsScreenContent(
    posts: List<Post>,
    onShowComments: (postId: String) -> Unit
) {
    LazyColumn {
        itemsIndexed(posts) { i, post ->
            PostItemWithCommentsBanner(post, onShowComments)

            if (i < posts.lastIndex)
                Divider()
        }
    }
}