package com.demomiru.blogburst.ui.blog

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import android.widget.ImageButton
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.demomiru.blogburst.R
import com.demomiru.blogburst.data.Blog
import com.demomiru.blogburst.databinding.FragmentBlogBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
@AndroidEntryPoint
class BlogFragment (private val blog :Blog) : Fragment(){
    private val viewModel: BlogViewModel by viewModels()
    private var binding: FragmentBlogBinding? = null
    private lateinit var downloadButton: ImageButton
    private lateinit var webClient: WebView
    private val offline = MutableLiveData(false)
    @SuppressLint("SetJavaScriptEnabled", "ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentBlogBinding.inflate(inflater, container, false)
        var offlineContent = "about:blank"
        viewModel.getOfflineVersion(blog.id.toInt())

        lifecycleScope.launch {
            viewModel.blogContent.collectLatest {
                offlineContent = it
                offline.value = it.isNotEmpty() && it !="not found"
            }
        }
        webClient = binding?.webView!!
        if (savedInstanceState!=null)webClient.restoreState(savedInstanceState)
        webClient?.setOnTouchListener(listener)
        downloadButton = binding?.downloadOption!!
        webClient?.webViewClient = object : WebViewClient() {

            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                return request?.url.toString() != view?.url
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                binding?.loading?.visibility = View.GONE
                if (offline.value!!) return
                val fileName = "Blog${blog.id}.mht"
                val pathFilename = "${requireContext().filesDir.path}/$fileName"
                offlineContent = pathFilename
                downloadButton.visibility = View.VISIBLE
                view?.saveWebArchive(pathFilename)
                super.onPageFinished(view, url)
            }
        }
        webClient?.settings?.apply{
            javaScriptEnabled = true
            domStorageEnabled = true
            allowFileAccess = true
            allowContentAccess = true
        }



        offline.observe(requireActivity()){
            if(it) { webClient?.loadUrl("file://$offlineContent")
                println(offlineContent)
                println("offline view")}
            else {

                webClient?.loadUrl(blog.link?:"about:blank")
                println("online view")}

        }

        downloadButton.setOnClickListener {
            offline.value = true
            viewModel.saveBlog(blog,offlineContent)
            downloadButton.setImageResource(R.drawable.baseline_check_24)
            downloadButton.setOnClickListener(null)
        }



        return binding?.root
    }

    private val listener = object : View.OnTouchListener {
        private var initialY = 0f

        override fun onTouch(v: View, event: MotionEvent): Boolean {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    initialY = event.y
                    return false
                }
                MotionEvent.ACTION_UP -> {
                    val finalY = event.y
                    val deltaY = finalY - initialY

                    if (deltaY > 0) {
                        // Scroll down
                        downloadButton.visibility = View.VISIBLE
                    } else if (deltaY < 0) {
                        // Scroll up
                        downloadButton.visibility = View.GONE
                    }
                    return false
                }
            }
            return false
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        webClient.saveState(outState)
        super.onSaveInstanceState(outState)
    }


    override fun onDestroyView() {
        binding?.webView?.loadUrl("about:blank")
        binding = null
        super.onDestroyView()

    }

}