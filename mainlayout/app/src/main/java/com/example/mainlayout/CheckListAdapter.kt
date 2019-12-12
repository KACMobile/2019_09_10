package com.example.mainlayout

import android.app.Activity
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.TextView
import kotlinx.android.synthetic.main.make_schedule.view.*
import java.util.ArrayList

//메뉴 리스트2, group checkBox
class CheckListAdapter(var names : ArrayList<String>, val context: Activity): BaseAdapter(){



    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var inflater = context.layoutInflater
        var view2 : View = inflater.inflate(R.layout.fragment_list_row2, null)

        val textView : TextView = view2.findViewById(R.id.checkBox_text)
        val checkBox : CheckBox = view2.findViewById(R.id.checkBox_group)
        //checkBox.setTag(position)

        //checkBox.setText(names[position])

        textView.setText(names[position])

        for(name in names){
            //checkBox.setText(names[position])
        }


        checkBox.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(buttomView:CompoundButton, isChecked : Boolean){
                for (i in 0..(names.size-1)){
                    if (textView.text == names[i]){
                        if (checkBox.isChecked){
                            Log.d("Tag", textView.text.toString())

                        }


                    }
                }

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