package com.example.mainlayout

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.group_fragment_row.view.*

class GroupRecyclerAdapter(private val items: ArrayList<UserInfo>) : RecyclerView.Adapter<GroupRecyclerAdapter.ViewHolder>() {
    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: GroupRecyclerAdapter.ViewHolder, position: Int) {
        val item = items[position]
        val listener = View.OnClickListener { it->
            Toast.makeText(it.context, "Click", Toast.LENGTH_SHORT).show()
        }
        holder.apply{
            bind(listener, item)
            itemView.tag = item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupRecyclerAdapter.ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(R.layout.group_fragment_row, parent, false)
        return GroupRecyclerAdapter.ViewHolder(inflatedView)
    }
    class ViewHolder(v: View): RecyclerView.ViewHolder(v){
        private var view:View = v
        fun bind(listener: View.OnClickListener, item: UserInfo) {
            view.name_textView.text = item.userNames
            view.position_textview.text = item.userInfos

        }
    }
}
