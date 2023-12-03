package com.demomiru.blogburst.data

import com.google.gson.Gson
import com.lagradost.nicehttp.Requests
import kotlinx.coroutines.flow.Flow
import org.json.JSONArray
import java.io.File

class BlogRepoImpl(private val dao: BlogDao ) : BlogRepository {

    private val url = "https://blog.vrid.in/wp-json/wp/v2/posts?per_page=10&page="
    private val request = Requests()
    private val gson= Gson()
//    private val blogs = MutableStateFlow<List<Blog>>(listOf())

    override suspend fun getBlogs(page: Int): List<Blog>{
        val res = request.get("${url}$page").toString()
        val resParsed = JSONArray(res)
        val bs = mutableListOf<Blog>()
        for( i in 0 until resParsed.length())
            bs.add(gson.fromJson(resParsed.getJSONObject(i).toString(),Blog::class.java))
        return bs
    }

    override suspend fun insertBlog(blog: OfflineBlog) {
        return dao.insert(blog)
    }

    override suspend fun deleteBlog(id: Int) {
        val file = dao.getOfflineBlog(id).content
        println("Deleted: " + File(file).delete())
        return dao.deleteBlog(id)
    }

    override suspend fun getOfflineBlog(id: Int) : OfflineBlog?{
        return dao.getOfflineBlog(id)
    }

    override fun getOfflineBlogs() : Flow<List<OfflineBlog>> {
        return dao.getOfflineBlogs()
    }

    override suspend fun deleteAll() {
        return dao.deleteAll()
    }
}