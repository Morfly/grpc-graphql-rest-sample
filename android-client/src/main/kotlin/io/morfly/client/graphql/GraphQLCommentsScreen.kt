@file:SuppressLint("RememberReturnType")

package io.morfly.client.graphql

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import io.morfly.client.ui.comments.CommentsScreen
import io.morfly.client.ui.comments.CommentsViewModel
import org.koin.androidx.compose.getViewModel


@Composable
fun GraphQLCommentsScreen(postId: String) {
    val viewModel = getViewModel<CommentsViewModel>(GraphQL)

    CommentsScreen(postId, viewModel)
}