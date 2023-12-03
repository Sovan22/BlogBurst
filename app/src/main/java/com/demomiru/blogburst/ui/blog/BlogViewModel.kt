package com.demomiru.blogburst.ui.blog

import android.text.Html
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demomiru.blogburst.data.Blog
import com.demomiru.blogburst.data.BlogRepository
import com.demomiru.blogburst.data.OfflineBlog
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BlogViewModel @Inject constructor(private val repository: BlogRepository) : ViewModel(){

    private val blogs = repository.getOfflineBlogs()
    val blogContent = MutableStateFlow("")

    fun saveBlog(blog: Blog, content: String){
        val offlineBlog = OfflineBlog(blog.id.toInt(),
            Html.fromHtml(blog.title.rendered, Html.FROM_HTML_MODE_LEGACY).toString(),blog.excerpt.rendered,content)
        viewModelScope.launch {
            repository.insertBlog(offlineBlog)
        }
    }

    fun getOfflineVersion(id: Int)
    {
        viewModelScope.launch {
            val blog = repository.getOfflineBlog(id)

//            println(blog)
            blogContent.value = blog?.content?:"not found"
        }
    }


}