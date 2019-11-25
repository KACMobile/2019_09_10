package com.example.mainlayout

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import java.util.*
import kotlin.collections.HashMap


class GroupFragment : Fragment() {
    private val firebaseDatabase = FirebaseDatabase.getInstance()
    private val databaseReference = firebaseDatabase.reference
    var fragmentDataList = arrayListOf<GroupFragmentData>()
    private val userID:String = "User01"
    val cal = Calendar.getInstance()
    val todayDate = cal.get(Calendar.DATE)
    val todayMonth = cal.get(Calendar.MONTH)



    companion object {
        fun newInstance() = GroupFragment()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.group_fragment, container, false)
        val recyclerView = rootView.findViewById<RecyclerView>(R.id.group_fragment_recyclerView)
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
                                    fragmentDataList.sortWith(Comparator { data1, data2 -> data1.startTime.toString().toInt() - data2.startTime.toString().toInt()})
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
                            Log.d("A", "This is wow??"+ groupDB.key.toString())
                            groupDB.addValueEventListener(object : ValueEventListener {
                                override fun onDataChange(groupDataSnapshot: DataSnapshot) {
                                    for(deeperUserSnapshot in groupDataSnapshot.child("Schedule").children) {
                                        for (deepestSnapshot in deeperUserSnapshot.child((todayMonth + 1).toString()).children) {
                                            Log.d("A", "This is wow!!!!!!!!!!!!!!!!"+deepestSnapshot.child("date").value)
                                            if(deepestSnapshot.child("date").value.toString() == todayDate.toString()) {
                                                Log.d("A", "This is wow!!" + deeperUserSnapshot.child("dateYear").value.toString())
                                                fragmentDataList.add(GroupFragmentData(
                                                    groupDataSnapshot.child("UserInfo/userName").value.toString(),
                                                    groupDataSnapshot.child("UserInfo/userInfo").value.toString(),
                                                    groupDataSnapshot.child("UserInfo/userType").value.toString(),
                                                    groupDataSnapshot.child("UserInfo/userIcon").value.toString(),
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
                                    fragmentDataList.sortWith(Comparator { data1, data2 -> data1.startTime.toString().toInt() - data2.startTime.toString().toInt()})
                                    recyclerView.adapter!!.notifyDataSetChanged()


                                }

                                override fun onCancelled(groupDataSnapshot: DatabaseError) {

                                }

                            }
                            )

                        }
                    }
                }


            }
            override fun onCancelled(dataSnapshot: DatabaseError) {
            }
        })




        return rootView

        //val listView = view?.findViewById<ListView>(R.id.main_listview)
        //listView?.adapter = MyCustomAdapter(this)

    }
    private lateinit var viewModel: GroupViewModel

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(GroupViewModel::class.java)

    }





}