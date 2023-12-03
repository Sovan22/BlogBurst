package com.demomiru.blogburst.ui.blogList

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.demomiru.blogburst.R
import com.demomiru.blogburst.data.Blog
import com.demomiru.blogburst.data.OfflineBlog
import com.demomiru.blogburst.databinding.ActivityMainBinding
import com.demomiru.blogburst.ui.blog.BlogFragment
import com.demomiru.blogburst.util.BlogAdapter
import com.demomiru.blogburst.util.ConnectionLiveData
import com.demomiru.blogburst.util.OfflineBlogAdapter
import com.demomiru.blogburst.util.rvTouchListener

import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@Suppress("DEPRECATION")
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel : BlogListViewModel by viewModels()
    private var page = MutableLiveData(1)
    private lateinit var binding :ActivityMainBinding
    private lateinit var connectionLiveData: ConnectionLiveData
    private val rvOnAdapter = BlogAdapter(this){

        val ft = supportFragmentManager.beginTransaction()
        ft.addToBackStack("blogFragment")

        ft.add(R.id.frag_placeholder,BlogFragment(it))
        ft.commit()
    }

    private val rvOffAdapter = OfflineBlogAdapter(this,::onDelete){
        val blog = Blog(it.id.toString(),title = Blog.Rendered(it.title), content = Blog.Rendered(it.content), excerpt = Blog.Rendered(it.excerpt))
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.frag_placeholder,BlogFragment(blog))
        ft.commit()
    }

    private fun onDelete(blog: OfflineBlog){
        viewModel.removeOfflineBlog(blog.id)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        viewModel.getBlogsData()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.blogListRv.adapter = rvOffAdapter
        val next = binding.nextPage
        val prev = binding.previousPage
        val pageTv = binding.pageNumber
        getOfflineBlogs()
        connectionLiveData = ConnectionLiveData(this)
        connectionLiveData.observe(this) { isNetworkAvailable ->
            isNetworkAvailable?.let {
                if(it){
                    binding.blogListRv.adapter = rvOnAdapter
                    page.value = 1
                    pageTv.visibility = View.VISIBLE

                    getOnlineBlogs(page.value?:1)
                }else{
                    Toast.makeText(this,"You are offline",Toast.LENGTH_LONG).show()
                    binding.blogListRv.adapter = rvOffAdapter
                    getOfflineBlogs()
                    page.value = 0
                    pageTv.visibility = View.GONE
                    next.visibility = View.GONE
                    prev.visibility = View.GONE
                }
            }
        }
        binding.blogListRv.layoutManager = LinearLayoutManager(this)
        pageHandler()
        }


    private fun getOnlineBlogs(page:Int){
        lifecycleScope.launch {
            viewModel.getOnlineBlogs(page)
            viewModel.blogs.collect{
                    rvOnAdapter.submitList(it)
                    binding.loadingPage.visibility = View.GONE
            }
        }
    }

    private fun getOfflineBlogs()
    {
        lifecycleScope.launch {

            viewModel.offlineBlog.collect{
                    rvOffAdapter.submitList(it)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun pageHandler(){
        page.observe(this){
            if(it!=0) {
                getOnlineBlogs(it)
                binding.pageNumber.text = "Page $it"
            }
            when(it){
                0 -> Log.e("Offline","no need of page")
                1 -> {
                    binding.previousPage.visibility = View.GONE
                    binding.nextPage.visibility = View.VISIBLE
                }
                21 -> {
                    binding.nextPage.visibility = View.GONE
                    binding.previousPage.visibility = View.VISIBLE
                }
                else ->{
                    binding.previousPage.visibility = View.VISIBLE
                    binding.nextPage.visibility = View.VISIBLE
                }
            }
        }

        binding.nextPage.setOnClickListener {
            binding.loadingPage.visibility = View.VISIBLE
            page.value = page.value!! + 1
        }
        binding.previousPage.setOnClickListener {
            binding.loadingPage.visibility = View.VISIBLE
            page.value = page.value!! - 1
        }

    }


    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStackImmediate()
        } else {
            super.onBackPressed()
        }
    }

    }
