package com.demomiru.blogburst.data

import androidx.lifecycle.SavedStateHandle
import com.google.gson.Gson
import com.lagradost.nicehttp.Requests
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asFlow
import org.json.JSONArray
import javax.inject.Inject

interface BlogRepository {


    suspend fun insertBlog(blog: OfflineBlog)

    suspend fun deleteBlog(id: Int)

    suspend fun getOfflineBlog(id: Int) : OfflineBlog?

    fun getOfflineBlogs() : Flow<List<OfflineBlog>>

    suspend fun getBlogs(page:Int = 1) : List<Blog>

    suspend fun deleteAll()

}