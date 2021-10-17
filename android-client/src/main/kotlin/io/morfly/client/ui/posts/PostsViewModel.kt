package io.morfly.client.ui.posts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.morfly.client.domain.Post
import io.morfly.client.domain.PostsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class PostsViewModel(
    private val postsRepository: PostsRepository
) : ViewModel() {

    private val mutablePosts = MutableStateFlow<List<Post>>(emptyList())
    val posts: StateFlow<List<Post>> = mutablePosts

    fun fetchPosts() {
        viewModelScope.launch(Dispatchers.IO) {
            val posts = postsRepository.listPosts(includeComments = false)
            mutablePosts.value = posts
        }
    }
}