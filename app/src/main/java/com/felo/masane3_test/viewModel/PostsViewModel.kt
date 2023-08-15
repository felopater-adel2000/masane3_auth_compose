package com.felo.masane3_test.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.felo.masane3_test.data.models.PostModel
import com.felo.masane3_test.data.state.DataState
import com.felo.masane3_test.repository.PostsRepository
import com.felo.masane3_test.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PostsViewModel @Inject constructor(val postsRepository: PostsRepository) : ViewModel()
{

    var postsResponse: LiveData<DataState<List<PostModel>>>? = null

    fun getPosts(
        page: Int = 0,
        limit: Int = Constants.PAGINATION_PAGE_SIZE,
        search: String? = null,
        id: Int? = null,
        groupId: Int? = null,
        userId: Int? = null
    ){
        postsResponse = postsRepository.getPosts(page, limit, search, id, groupId, userId)
    }

}