package com.demomiru.blogburst.util

import com.demomiru.blogburst.data.Blog

sealed class UiEvent{
    object PopBackStack: UiEvent()
    data class Navigate(val route: String) : UiEvent()
    data class ShowSnackBar(
        val message: String,
        val action : String? = null
    ) : UiEvent()

}

sealed class BlogListEvent{
    object ShowList : BlogListEvent()
    data class OnBlogItemClick(val blog: Blog): BlogListEvent()
}
