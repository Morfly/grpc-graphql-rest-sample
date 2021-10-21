package io.morfly.client.ui.comments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.morfly.client.domain.Comment
import io.morfly.client.domain.NewComment
import io.morfly.client.domain.Post
import io.morfly.client.domain.PostsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock


class CommentsViewModel(
    private val postsRepository: PostsRepository
) : ViewModel() {

    private val mutablePost = MutableStateFlow<Post?>(null)
    val post: StateFlow<Post?> = mutablePost

    private val mutableComments = MutableStateFlow<List<Comment>>(emptyList())
    val comments: StateFlow<List<Comment>> = mutableComments

    fun fetchComments(postId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            mutablePost.value = postsRepository.getPost(postId, includeComments = false)
            postsRepository
                .listComments(postId)
                .flowOn(Dispatchers.IO)
                .collectLatest { mutableComments.value = it }
        }
    }

    fun sendComment(comment: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val newComment = NewComment(
                postId = post.value!!.id,
                authorId = "1",
                content = comment,
                timestamp = Clock.System.now()
            )
            val createdComment = postsRepository.createComment(newComment) ?: return@launch
            mutableComments.value = (mutableComments.value + createdComment)
                .sortedBy { it.timestamp }
        }
    }
}

