package com.example.mainlayout

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.contentValuesOf
import com.example.mainlayout.R
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase

class GroupAdd : AppCompatActivity() {

    private val firebaseDatabase = FirebaseDatabase.getInstance()
    private val databaseReference = firebaseDatabase.reference
    var userNames = arrayListOf<String>()
    var userInfos = arrayListOf<String>()
    var userTypes = arrayListOf<String>()
    var userIcons = arrayListOf<String?>()
    private val userID:String = "User01"




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.group_add)
        val listView = findViewById<ListView>(R.id.group_add_list)
        listView.adapter = GroupAddAdapter(this,userNames,userInfos,userTypes,userIcons) //custom adapter*/

    }



    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        val inflater = menuInflater
        inflater.inflate(R.menu.add_group_menu, menu)

        //검색어 저장 변수
        var searchData : String

        val searchItem = menu.findItem(R.id.search)
        val searchView = searchItem.actionView as SearchView


        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(newText: String?): Boolean {
                if (newText!!.isNotEmpty())
                {
                    searchData = newText
                    //Toast.makeText(this@GroupAdd, "You search $searchData", Toast.LENGTH_LONG).show()
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val listView = findViewById<ListView>(R.id.group_add_list)
                clearArr()

                if (newText!!.isNotEmpty()) {
                    searchData = newText

                    databaseReference.child("Groups").orderByKey().startAt(searchData).endAt(searchData + "\uf8ff").addChildEventListener(object: ChildEventListener{
                        override fun onChildAdded(dataSnapshot: DataSnapshot, p1: String?) {
                            userNames.add(dataSnapshot.child("UserInfo/userName").value.toString())
                            userInfos.add(dataSnapshot.child("UserInfo/userInfo").value.toString())
                            userTypes.add(dataSnapshot.child("UserInfo/userType").value.toString())
                            userIcons.add(dataSnapshot.child("UserInfo/userIcon").value.toString())
                            (listView.adapter as BaseAdapter).notifyDataSetChanged()
                        }

                        override fun onChildChanged(p0: DataSnapshot, p1: String?) {

                        }

                        override fun onCancelled(p0: DatabaseError) {

                        }

                        override fun onChildMoved(p0: DataSnapshot, p1: String?) {

                        }

                        override fun onChildRemoved(p0: DataSnapshot) {

                        }
                    }
                    )

                    databaseReference.child("Users").orderByKey().startAt(searchData).endAt(searchData + "\uf8ff").addChildEventListener(object: ChildEventListener{
                        override fun onChildAdded(dataSnapshot: DataSnapshot, p1: String?) {
                            if(dataSnapshot.child("UserInfo/userName").value.toString()!=userID) {
                                userNames.add(dataSnapshot.child("UserInfo/userName").value.toString())
                                userInfos.add(dataSnapshot.child("UserInfo/userInfo").value.toString())
                                userTypes.add(dataSnapshot.child("UserInfo/userType").value.toString())
                                userIcons.add(dataSnapshot.child("UserInfo/userIcon").value.toString())
                                (listView.adapter as BaseAdapter).notifyDataSetChanged()
                            }
                        }

                        override fun onChildChanged(p0: DataSnapshot, p1: String?) {

                        }

                        override fun onCancelled(p0: DatabaseError) {

                        }

                        override fun onChildMoved(p0: DataSnapshot, p1: String?) {

                        }

                        override fun onChildRemoved(p0: DataSnapshot) {

                        }
                    }
                    )

                }
                (listView.adapter as BaseAdapter).notifyDataSetChanged()
                return true
            }
        })

        return true
    }

    public fun clearArr(){
        userNames.clear()
        userInfos.clear()
        userTypes.clear()
        userIcons.clear()
    }

}