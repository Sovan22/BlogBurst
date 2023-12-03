package com.demomiru.blogburst.ui.blogList

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demomiru.blogburst.data.Blog
import com.demomiru.blogburst.data.BlogRepository
import com.demomiru.blogburst.data.OfflineBlog
import com.demomiru.blogburst.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BlogListViewModel @Inject constructor(private val repository: BlogRepository) : ViewModel(){

    val blogs= MutableStateFlow<List<Blog>>(listOf())
    val offlineBlog = repository.getOfflineBlogs()

    fun getOnlineBlogs(page: Int = 1) {
        viewModelScope.launch {
            blogs.value = repository.getBlogs(page)
        }
    }

    fun removeOfflineBlog(id:Int){
        viewModelScope.launch {
            repository.deleteBlog(id)
        }
    }

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()



    private fun sendUiEvent(event: UiEvent){
        viewModelScope.launch {
            _uiEvent.send(event)
        }

}


}

