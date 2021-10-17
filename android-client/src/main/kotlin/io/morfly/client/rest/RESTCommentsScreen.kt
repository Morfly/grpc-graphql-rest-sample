@file:SuppressLint("RememberReturnType")

package io.morfly.client.rest

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import io.morfly.client.ui.comments.CommentsScreen
import io.morfly.client.ui.comments.CommentsViewModel
import org.koin.androidx.compose.getViewModel


@Composable
fun RESTCommentsScreen(postId: String) {
    val viewModel = getViewModel<CommentsViewModel>(REST)

    CommentsScreen(postId, viewModel)
}