package com.demomiru.blogburst.util

import android.content.Context
import android.content.res.ColorStateList
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.demomiru.blogburst.R
import com.demomiru.blogburst.data.Blog
import com.demomiru.blogburst.data.OfflineBlog

class BlogAdapter( private val context: Context,private val onClick : (Blog)->Unit): ListAdapter<Blog, BlogAdapter.ViewHolder>(
    DiffCallBack
) {



    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val title : TextView = view.findViewById(R.id.title_tv)
//        val short : TextView = view.findViewById(R.id.short_tv)
        val download: ImageButton = view.findViewById(R.id.download_blog)
    }

    object DiffCallBack: DiffUtil.ItemCallback<Blog>(){
        override fun areItemsTheSame(
            oldItem: Blog,
            newItem: Blog
        ): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(
            oldItem: Blog,
            newItem: Blog
        ): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.blog_item,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val blog = getItem(position)
        holder.title.text = Html.fromHtml(blog.title.rendered, Html.FROM_HTML_MODE_LEGACY).toString()
//        holder.short.text = blog.excerpt.rendered
        val color = ContextCompat.getColor(context, R.color.ultraLightBlue)
        val textColor = ContextCompat.getColor(context,R.color.darkBlue)
        val color2 = ContextCompat.getColor(context, R.color.lightBlue)
        val textColor2 = ContextCompat.getColor(context,R.color.white)
        if (position %2!=0) {
            holder.itemView.backgroundTintList = ColorStateList.valueOf(color)
            holder.title.setTextColor(textColor)
        }else{
            holder.itemView.backgroundTintList = ColorStateList.valueOf(color2)
            holder.title.setTextColor(textColor2)
        }
        holder.itemView.setOnClickListener {
            onClick(blog)
        }

    }
}

class OfflineBlogAdapter(private val context: Context,private val onDelete: (OfflineBlog) -> Unit,private val onClick : (OfflineBlog)->Unit): ListAdapter<OfflineBlog,OfflineBlogAdapter.ViewHolder>(
    DiffCallBack
) {



    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val title : TextView = view.findViewById(R.id.title_tv)
        //        val short : TextView = view.findViewById(R.id.short_tv)
        val download: ImageButton = view.findViewById(R.id.download_blog)
    }

    object DiffCallBack: DiffUtil.ItemCallback<OfflineBlog>(){
        override fun areItemsTheSame(
            oldItem: OfflineBlog,
            newItem: OfflineBlog
        ): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(
            oldItem: OfflineBlog,
            newItem: OfflineBlog
        ): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.blog_item,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val blog = getItem(position)
        holder.title.text = blog.title
//        holder.short.text = blog.excerpt.rendered
        holder.itemView.setOnClickListener {
            onClick(blog)
        }
        val color = ContextCompat.getColor(context, R.color.ultraLightBlue)
        val textColor = ContextCompat.getColor(context,R.color.darkBlue)
        val color2 = ContextCompat.getColor(context, R.color.lightBlue)
        val textColor2 = ContextCompat.getColor(context,R.color.white)
        if (position%2!=0) {
            holder.itemView.backgroundTintList = ColorStateList.valueOf(color)

            holder.title.setTextColor(textColor)
        }else{
            holder.itemView.backgroundTintList = ColorStateList.valueOf(color2)
            holder.title.setTextColor(textColor2)
        }
        holder.download.setImageResource(R.drawable.baseline_close_24)
        holder.download.visibility = View.VISIBLE
        holder.download.setOnClickListener {
            onDelete(blog)
        }

    }
}