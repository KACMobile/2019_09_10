package com.example.mainlayout

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.BaseAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.mainlayout.R
import com.google.firebase.database.*
//팔로우한 그룹 리스트 - 황선혁
class GroupList : AppCompatActivity() {
    private val firebaseDatabase = FirebaseDatabase.getInstance()
    private val databaseReference = firebaseDatabase.reference
    var userInfos = arrayListOf<UserInfo>()
    private var userID:String = "User01"
    override fun onCreate(savedInstanceState: Bundle?) {
        val idPreference = getSharedPreferences("UserID", Context.MODE_PRIVATE)
        userID = idPreference.getString("UserID", "User01")!!
        super.onCreate(savedInstanceState)
        setContentView(R.layout.group_list)
        val listView = findViewById<ListView>(R.id.group_list_list)
        listView.adapter = GroupAddAdapter(this, userInfos)
        val userfollow = databaseReference.child("Users/" + userID + "/Follow")
        userfollow.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                for(snapshot in dataSnapshot.children){
                    if(snapshot.key.toString() == "Groups"){
                        for(deeperSnapshot in snapshot.children){
                            val groupDB =databaseReference.child("Groups/"+ deeperSnapshot.key.toString())
                            groupDB.addValueEventListener(object: ValueEventListener{
                                override fun onDataChange(groupDataSnapshot: DataSnapshot) {
                                    var lat:Double? = null
                                    var lng:Double? = null
                                    if(groupDataSnapshot.child("UserInfo/locateLat").value != null && groupDataSnapshot.child("UserInfo/locateLng").value != null){
                                        lat  = groupDataSnapshot.child("UserInfo/locateLat").value as Double
                                        lng = groupDataSnapshot.child("UserInfo/locateLng").value as Double
                                    }
                                    userInfos.add(UserInfo(groupDataSnapshot.child("UserInfo/userName").value.toString(), groupDataSnapshot.child("UserInfo/userInfo").value.toString(),
                                        groupDataSnapshot.child("UserInfo/userType").value.toString(), groupDataSnapshot.child("UserInfo/userIcon").value.toString(),
                                        groupDataSnapshot.child("UserInfo/userHomepage").value.toString(), groupDataSnapshot.child("UserInfo/userTEL").value.toString(),
                                        lat, lng))
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
                                    var lat:Double? = null
                                    var lng:Double? = null
                                    if(userDataSnapshot.child("UserInfo/locateLat").value != null && userDataSnapshot.child("UserInfo/locateLng").value != null){
                                        lat  = userDataSnapshot.child("UserInfo/locateLat").value as Double
                                        lng = userDataSnapshot.child("UserInfo/locateLng").value as Double
                                    }
                                    userInfos.add(UserInfo(userDataSnapshot.child("UserInfo/userName").value.toString(), userDataSnapshot.child("UserInfo/userInfo").value.toString(),
                                        userDataSnapshot.child("UserInfo/userType").value.toString(), userDataSnapshot.child("UserInfo/userIcon").value.toString(),
                                        userDataSnapshot.child("UserInfo/userHomepage").value.toString(), userDataSnapshot.child("UserInfo/userTEL").value.toString(),
                                                lat, lng))
                                    (listView.adapter as BaseAdapter).notifyDataSetChanged()

                                }

                                override fun onCancelled(p0: DatabaseError) {

                                }
                            })
                        }

                    }
                    userInfos.clear()
                }
            }

            override fun onCancelled(p0: DatabaseError) {

            }

        }

        )

    }


}