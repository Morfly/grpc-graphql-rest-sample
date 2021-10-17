@file:SuppressLint("RememberReturnType")

package io.morfly.client.rest

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import io.morfly.client.ui.posts.PostsScreen
import io.morfly.client.ui.posts.PostsViewModel
import org.koin.androidx.compose.getViewModel


@Composable
fun RESTPostsScreen(navController: NavController) {
    val viewModel = getViewModel<PostsViewModel>(REST)

    PostsScreen(viewModel, onShowComments = { postId: String ->
        navController.navigate("rest/comments/$postId")
    })
}