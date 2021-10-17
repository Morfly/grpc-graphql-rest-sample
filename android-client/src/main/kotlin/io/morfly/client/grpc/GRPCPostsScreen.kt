@file:SuppressLint("RememberReturnType")

package io.morfly.client.grpc

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import io.morfly.client.ui.posts.PostsScreen
import io.morfly.client.ui.posts.PostsViewModel
import org.koin.androidx.compose.getViewModel


@Composable
fun GRPCPostsScreen(navController: NavController) {
    val viewModel = getViewModel<PostsViewModel>(gRPC)

    PostsScreen(viewModel, onShowComments = { postId: String ->
        navController.navigate("grpc/comments/$postId")
    })
}