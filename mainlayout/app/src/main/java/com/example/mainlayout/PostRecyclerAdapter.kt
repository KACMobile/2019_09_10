package com.example.mainlayout

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.post_fragment_row.view.*

public class PostRecyclerAdapter(val context: Context, private val postList: ArrayList<Post>) : RecyclerView.Adapter<PostRecyclerAdapter.ViewHolder>() {
    lateinit var muserInfo:UserInfo
    override fun getItemCount(): Int {
        return postList.size
    }

    override fun onBindViewHolder(holder: PostRecyclerAdapter.ViewHolder, position: Int) {
        muserInfo = postList[position].userInfo
        val post = postList[position]
        val listener = View.OnClickListener { it->
        }
        holder.apply{
            bind(context, listener, post, muserInfo)
            itemView.tag = post
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostRecyclerAdapter.ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(R.layout.post_fragment_row, parent, false)
        return PostRecyclerAdapter.ViewHolder(inflatedView)
    }
    class ViewHolder(v: View): RecyclerView.ViewHolder(v){
        private var view: View = v
        fun bind(context: Context, listener: View.OnClickListener, post:Post, userInfo: UserInfo) {
            val year:String = post.postDate.substring(0,4)
            val month:String = post.postDate.substring(4,6)
            val day:String = post.postDate.substring(6,8)
            val hour:String = post.postTime.substring(0,2)
            val minute:String = post.postTime.substring(2,4)
            view.post_userName.text = userInfo.userInfos
            view.post_Text.text = post.postText
            view.post_Date.text = year + "년 " + month + "월 " + day + "일 " + hour + ":" + minute
            if(userInfo.userIcons != null)
                Glide.with(context).load(userInfo.userIcons).into(view.post_userIcon)
            if(post.postImage != "null")
                Glide.with(context).load(post.postImage).into(view.post_Image)
            else
                view.post_Image.visibility = View.GONE


        }
    }
}
