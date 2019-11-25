package com.example.mainlayout

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class GroupFragmentAdapter(context: Context): BaseAdapter() {

    private val mContext: Context

    private val names = arrayListOf<String>(
        "Group1", "Group2", "Group3", "Group4", "Group5"
    )

    init {
        mContext = context
        Log.d("A", "This is 여기옴?")
    }

    // responsible for how many rows in my list
    override fun getCount(): Int {
        Log.d("a","This is 여기여기"+ names.size.toString())
        return names.size
    }

    // you can also ignore this
    override fun getItemId(position: Int): Long {
        Log.d("a","This is 여기여기??"+ names.size.toString())
        return position.toLong()
    }

    // you can ignore this for now
    override fun getItem(position: Int): Any {
        Log.d("a","This is 여기여기는"+ names.size.toString())
        return names[position]
    }

    // responsible for rendering out each row
    override fun getView(position: Int, convertView: View?, viewGroup: ViewGroup?): View {
        Log.d("A", "This is 여기옴??")
        val layoutInflater = LayoutInflater.from(mContext)
        val rowMain = layoutInflater.inflate(R.layout.group_fragment_row, viewGroup,false)
        Log.d("A", "This is 여기옴???")

        val nameTextView = rowMain.findViewById<TextView>(R.id.name_textView)
        nameTextView.text = "abc"

        val positionTextView = rowMain.findViewById<TextView>(R.id.position_textview)
        positionTextView.text = "abc"


        return rowMain

    }

}