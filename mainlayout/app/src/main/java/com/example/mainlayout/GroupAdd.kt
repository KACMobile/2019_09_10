package com.example.mainlayout

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.contentValuesOf
import com.example.mainlayout.R

class GroupAdd : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.group_add)

        val listView = findViewById<ListView>(R.id.group_add_list)

        listView.adapter = MyCustomAdapter(this) //custom adapter

    }
    private class MyCustomAdapter(context: Context) : BaseAdapter(){

        private val mContext : Context

        private val names = arrayListOf<String>(
            "Group1", "Group2", "Group3","Group4","Group5"
        )

        init {
            mContext = context
        }

        override fun getCount(): Int {
            return names.size
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getItem(position: Int): Any {
            return "Test"
        }
        //각각의 줄 렌더링
        override fun getView(position: Int, convertView: View?, viewGroup: ViewGroup?): View {

            val layoutInflater = LayoutInflater.from(mContext)
            val rowMain = layoutInflater.inflate(R.layout.group_add_row, viewGroup, false)

            val nameTextView = rowMain.findViewById<TextView>(R.id.add_group_name)
            nameTextView.text = names.get(position)

            //val addBtn = rowMain.findViewById<Button>(R.id.add_group_button)


            val positionTextView = rowMain.findViewById<TextView>(R.id.add_group_info)
            positionTextView.text = "group info $position"


            return rowMain

//            val textView = TextView(mContext)
//            textView.text = "this is list row"
//            return textView
        }


    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        val inflater = menuInflater
        inflater.inflate(R.menu.add_group_menu, menu)

        //검색어 저장 변수
        var searchData : String

        val searchItem = menu.findItem(R.id.search)
        val searchView = searchItem.actionView as SearchView


        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                if (newText!!.isNotEmpty())
                {
                    searchData = newText
                    Toast.makeText(this@GroupAdd, "You search $searchData", Toast.LENGTH_LONG).show()
                }
                return true
            }
        })

        return true
    }
}