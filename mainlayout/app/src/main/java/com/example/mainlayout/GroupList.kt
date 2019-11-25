package com.example.mainlayout

import android.os.Bundle
import android.util.Log
import android.widget.BaseAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.mainlayout.R
import com.google.firebase.database.*

class GroupList : AppCompatActivity() {
    private val firebaseDatabase = FirebaseDatabase.getInstance()
    private val databaseReference = firebaseDatabase.reference
    var userInfos = arrayListOf<UserInfo>()
    private val userID:String = "User01"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.group_list)
        val listView = findViewById<ListView>(R.id.group_list_list)
        listView.adapter = GroupAddAdapter(this, userInfos)
        val userfollow = databaseReference.child("Users/" + userID + "/Follow")
        userfollow.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                userInfos.clear()
                for(snapshot in dataSnapshot.children){
                    if(snapshot.key.toString() == "Groups"){
                        for(deeperSnapshot in snapshot.children){
                            val groupDB =databaseReference.child("Groups/"+ deeperSnapshot.key.toString())
                            groupDB.addValueEventListener(object: ValueEventListener{
                                override fun onDataChange(groupDataSnapshot: DataSnapshot) {
                                    userInfos.add(UserInfo(groupDataSnapshot.child("UserInfo/userName").value.toString(), groupDataSnapshot.child("UserInfo/userInfo").value.toString(), groupDataSnapshot.child("UserInfo/userType").value.toString(), groupDataSnapshot.child("UserInfo/userIcon").value.toString()))
                                    (listView.adapter as BaseAdapter).notifyDataSetChanged()

                                }

                                override fun onCancelled(p0: DatabaseError) {

                                }
                            })
                        }
                    }
                    if(snapshot.key.toString() == "Users") {
                        for(deeperSnapshot in snapshot.children){
                            val userDB =databaseReference.child("Users/"+ deeperSnapshot.key.toString())
                            userDB.addValueEventListener(object: ValueEventListener{
                                override fun onDataChange(userDataSnapshot: DataSnapshot) {
                                    userInfos.add(UserInfo(userDataSnapshot.child("UserInfo/userName").value.toString(), userDataSnapshot.child("UserInfo/userInfo").value.toString(), userDataSnapshot.child("UserInfo/userType").value.toString(), userDataSnapshot.child("UserInfo/userIcon").value.toString()))
                                    (listView.adapter as BaseAdapter).notifyDataSetChanged()

                                }

                                override fun onCancelled(p0: DatabaseError) {

                                }
                            })
                        }

                    }
                }
            }

            override fun onCancelled(p0: DatabaseError) {

            }

        }

        )

    }


}