package com.example.mainlayout

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.TextView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.make_schedule.view.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

//메뉴 리스트2, group checkBox
class CheckListAdapter(var names : ArrayList<String>, var colorArray: ArrayList<Int>, val context: Activity): BaseAdapter(){

    val database = FirebaseDatabase.getInstance()
    val databaseReference = database.reference
    val userID: String = "User01"

    var followListSnapshot = arrayListOf<DataSnapshot>() //followList DataSnapshot을 받으면 add함
    val scheduleColorPreference = context.getSharedPreferences("ScheduleColorInfo", Context.MODE_PRIVATE)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var inflater = context.layoutInflater
        var view2 : View = inflater.inflate(R.layout.fragment_list_row2, null)

        val textView : TextView = view2.findViewById(R.id.checkBox_text)
        val checkBox : CheckBox = view2.findViewById(R.id.checkBox_group)
        val name = names[position]
        val colorView = view2.findViewById<View>(R.id.show_color)
        val sharedPreferences: SharedPreferences = context.getSharedPreferences("Check", Context.MODE_PRIVATE)
        //checkBox.setTag(position)
        //checkBox.setText(names[position])
        colorView.setBackgroundColor(colorArray[positions])
        checkBox.isChecked = sharedPreferences.getBoolean(name, true)
        textView.setText(name)





        checkBox.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(buttomView:CompoundButton, isChecked : Boolean){
                val editor = sharedPreferences.edit()
                if (checkBox.isChecked){
                    editor.putBoolean(name, true)
                }
                else
                    editor.putBoolean(name, false)
                editor.commit()





            }
        })

        return view2
    }

    override fun getItem(position: Int): Any {
        return names.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
    override fun getCount(): Int {
        return names.size
    }


}