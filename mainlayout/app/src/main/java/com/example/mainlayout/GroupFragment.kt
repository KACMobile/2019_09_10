package com.example.mainlayout

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import java.util.*


class GroupFragment : Fragment() {
    private val firebaseDatabase = FirebaseDatabase.getInstance()
    private val databaseReference = firebaseDatabase.reference
    var fragmentDataList = arrayListOf<GroupFragmentData>()
    var postArray = arrayListOf<Post>()
    private val userID:String = "User01"
    val cal = Calendar.getInstance()
    val todayDate = cal.get(Calendar.DATE)
    val todayMonth = cal.get(Calendar.MONTH)
    lateinit var userInfo:UserInfo



    companion object {
        fun newInstance() = GroupFragment()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.group_fragment, container, false)
        val recyclerView = rootView.findViewById<RecyclerView>(R.id.group_fragment_schedule_recyclerView)
        val postRecyclerView = rootView.findViewById<RecyclerView>(R.id.group_fragment_post_recyclerView)
        postRecyclerView.adapter = PostRecyclerAdapter(activity!!, postArray)
        recyclerView.adapter = GroupRecyclerAdapter(activity!!, fragmentDataList)

        val userfollow = databaseReference.child("Users/" + userID + "/Follow")
        userfollow.addValueEventListener( object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                fragmentDataList.clear()
                for (snapshot in dataSnapshot.children) {
                    if (snapshot.key.toString() == "Groups") {
                        for (deeperSnapshot in snapshot.children) {
                            val groupDB = databaseReference.child("Groups/" + deeperSnapshot.key.toString())
                            groupDB.addValueEventListener(object : ValueEventListener {
                                override fun onDataChange(groupDataSnapshot: DataSnapshot) {
                                    for (deeperGroupSnapshot in groupDataSnapshot.child("Schedule/" + (todayMonth + 1).toString()).children) {
                                        if(deeperGroupSnapshot.child("date").value.toString() == todayDate.toString().toString()) {
                                            fragmentDataList.add(GroupFragmentData(
                                                groupDataSnapshot.child("UserInfo/userName").value.toString(),
                                                groupDataSnapshot.child("UserInfo/userInfo").value.toString(),
                                                groupDataSnapshot.child("UserInfo/userType").value.toString(),
                                                groupDataSnapshot.child("UserInfo/userIcon").value.toString(),
                                                deeperGroupSnapshot.child("scheduleName").value.toString(),
                                                deeperGroupSnapshot.child("scheduleInfo").value.toString(),
                                                deeperGroupSnapshot.child("dateYear").value.toString().toInt(),
                                                deeperGroupSnapshot.child("dateMonth").value.toString().toInt(),
                                                deeperGroupSnapshot.child("date").value.toString().toInt(),
                                                deeperGroupSnapshot.child("startTime").value.toString()
                                            ))
                                        }



                                    }
                                    for(deeperDataSnapshot in groupDataSnapshot.child("Post").children){

                                        userInfo = UserInfo(groupDataSnapshot.child("UserInfo/userName").value.toString(),
                                            groupDataSnapshot.child("UserInfo/userInfo").value.toString(),
                                            groupDataSnapshot.child("UserInfo/userType").value.toString(),
                                            groupDataSnapshot.child("UserInfo/userIcon").value.toString(),
                                            null, null, null, null)
                                        postArray.add(Post(deeperDataSnapshot.child("postText").value.toString(), deeperDataSnapshot.child("postDate").value.toString(), deeperDataSnapshot.child("postTime").value.toString(), deeperDataSnapshot.child("postImage").value.toString(), deeperDataSnapshot.child("postLat").value.toString(), deeperDataSnapshot.child("postLng").value.toString(),userInfo))
                                    }
                                    postArray.sortWith(Comparator { data1, data2 -> data2.postTime.toInt() - data1.postTime.toInt()})
                                    postArray.sortWith(Comparator { data1, data2 -> data2.postDate.toInt() - data1.postDate.toInt()})
                                    fragmentDataList.sortWith(Comparator { data1, data2 -> data1.startTime.toString().toInt() - data2.startTime.toString().toInt()})
                                    postRecyclerView.adapter!!.notifyDataSetChanged()
                                    recyclerView.adapter!!.notifyDataSetChanged()

                                }

                                override fun onCancelled(groupDataSnapshot: DatabaseError) {

                                }

                            }
                            )

                        }
                    }
                    if (snapshot.key.toString() == "Users"){
                        for (deeperSnapshot in snapshot.children) {
                            val groupDB = databaseReference.child("Users/" + deeperSnapshot.key.toString())
                            groupDB.addValueEventListener(object : ValueEventListener {
                                override fun onDataChange(userDataSnapshot: DataSnapshot) {
                                    for(deeperUserSnapshot in userDataSnapshot.child("Schedule").children) {
                                        for (deepestSnapshot in deeperUserSnapshot.child((todayMonth + 1).toString()).children) {
                                            if(deepestSnapshot.child("date").value.toString() == todayDate.toString()) {
                                                fragmentDataList.add(GroupFragmentData(
                                                    userDataSnapshot.child("UserInfo/userName").value.toString(),
                                                    userDataSnapshot.child("UserInfo/userInfo").value.toString(),
                                                    userDataSnapshot.child("UserInfo/userType").value.toString(),
                                                    userDataSnapshot.child("UserInfo/userIcon").value.toString(),
                                                    deepestSnapshot.child("scheduleName").value.toString(),
                                                    deepestSnapshot.child("scheduleInfo").value.toString(),
                                                    deepestSnapshot.child("dateYear").value.toString().toInt(),
                                                    deepestSnapshot.child("dateMonth").value.toString().toInt(),
                                                    deepestSnapshot.child("date").value.toString().toInt(),
                                                    deepestSnapshot.child("startTime").value.toString()
                                                ))
                                            }
                                        }
                                    }
                                    for(deeperDataSnapshot in userDataSnapshot.child("Post").children){
                                        userInfo = UserInfo(userDataSnapshot.child("UserInfo/userName").value.toString(),
                                            userDataSnapshot.child("UserInfo/userInfo").value.toString(),
                                            userDataSnapshot.child("UserInfo/userType").value.toString(),
                                            userDataSnapshot.child("UserInfo/userIcon").value.toString(),
                                            null, null, null, null)

                                        postArray.add(Post(deeperDataSnapshot.child("postText").value.toString(), deeperDataSnapshot.child("postDate").value.toString(), deeperDataSnapshot.child("postTime").value.toString(), deeperDataSnapshot.child("postImage").value.toString(), deeperDataSnapshot.child("postLat").value.toString(), deeperDataSnapshot.child("postLng").value.toString(),userInfo))
                                    }
                                    postArray.sortWith(Comparator { data1, data2 -> data2.postTime.toInt() - data1.postTime.toInt()})
                                    postArray.sortWith(Comparator { data1, data2 -> data2.postDate.toInt() - data1.postDate.toInt()})
                                    fragmentDataList.sortWith(Comparator { data1, data2 -> data1.startTime.toString().toInt() - data2.startTime.toString().toInt()})
                                    postRecyclerView.adapter!!.notifyDataSetChanged()
                                    recyclerView.adapter!!.notifyDataSetChanged()


                                }

                                override fun onCancelled(groupDataSnapshot: DatabaseError) {

                                }

                            }
                            )

                        }
                    }


                    postArray.sortWith(Comparator { data1, data2 -> data1.postTime.toInt() - data2.postTime.toInt()})
                    postArray.sortWith(Comparator { data1, data2 -> data1.postDate.toInt() - data2.postDate.toInt()})
                }


            }
            override fun onCancelled(dataSnapshot: DatabaseError) {
            }
        })




        return rootView
    }






    }
