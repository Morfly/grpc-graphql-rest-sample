package io.morfly.client.graphql

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import io.morfly.client.ui.posts.PostsScreen
import io.morfly.client.ui.posts.PostsViewModel
import org.koin.androidx.compose.getViewModel


@Composable
fun GraphQlPostsScreen(navController: NavController) {
    val viewModel = getViewModel<PostsViewModel>(GraphQL)

    PostsScreen(viewModel, onShowComments = { postId: String ->
        navController.navigate("rest/comments/$postId")
    })
}