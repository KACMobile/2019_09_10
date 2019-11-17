package com.example.settingtest

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class SettingAdapter(val context : Context, val settingArray: Array<String>):BaseAdapter() {

    override fun getCount(): Int {
        return settingArray.size
    }

    override fun getItem(p0: Int): String {
        return settingArray[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view:  View = LayoutInflater.from(context).inflate(R.layout.list_element_settings,null)
        view.findViewById<TextView>(R.id.settingInfo).text = settingArray[position]


        return view


    }
}