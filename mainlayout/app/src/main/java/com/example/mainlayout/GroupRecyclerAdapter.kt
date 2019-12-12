package com.example.mainlayout

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.group_fragment_row.view.*
// 그룹 일정관련 어댑터 - 황선혁
class GroupRecyclerAdapter(val context:Context, private val dataList: ArrayList<GroupFragmentData>) : RecyclerView.Adapter<GroupRecyclerAdapter.ViewHolder>() {
    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: GroupRecyclerAdapter.ViewHolder, position: Int) {
        val data = dataList[position]
        val listener = View.OnClickListener { it->
            Toast.makeText(it.context, "Click", Toast.LENGTH_SHORT).show()
        }
        holder.apply{
            bind(context, listener, data)
            itemView.tag = data
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupRecyclerAdapter.ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(R.layout.group_fragment_row, parent, false)
        return GroupRecyclerAdapter.ViewHolder(inflatedView)
    }
    class ViewHolder(v: View): RecyclerView.ViewHolder(v){
        private var view:View = v
        fun bind(context:Context, listener: View.OnClickListener, data:GroupFragmentData) {
            view.group_fragment_GroupName.text = data.userNames
            view.group_fragment_ScheduleName.text = data.scheduleName
            view.group_fragment_StartTime.text = data.startTime
            view.group_fragment_Date.text = data.dateMonth.toString() + "월" + data.date + "일"
            if(data.userIcons != "null")
                Glide.with(context).load(data.userIcons).into(view.group_fragment_image)
            else
                Glide.with(context).load(R.drawable.user_icon).into(view.group_fragment_image)

        }
    }
}
