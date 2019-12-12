package com.example.mainlayout

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*
import kotlin.Comparator


class GroupPageScheduleFragment : Fragment() {

    companion object {
        fun newInstance() = GroupPageScheduleFragment()
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.group_schedule_fragment, container, false)
        val userInfo = arguments!!.getSerializable("userInfo") as UserInfo
        val databaseReference = FirebaseDatabase.getInstance().reference
        var fragmentDataList = arrayListOf<GroupFragmentData>()
        val cal = Calendar.getInstance()
        val todayDate = cal.get(Calendar.DATE)
        val todayMonth = cal.get(Calendar.MONTH)
        val groupDB = databaseReference.child(userInfo.userTypes + "/" + userInfo.userNames)
        Log.d("a","This is"  + userInfo.toString())
        val recyclerView = rootView.findViewById<RecyclerView>(R.id.group_schedule_recyclerview)
        recyclerView.adapter = GroupRecyclerAdapter(activity!!, fragmentDataList)

        groupDB.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                fragmentDataList.clear()
                if(userInfo.userTypes == "Groups") {
                    for (groupSnapshot in dataSnapshot.child("Schedule/" + (todayMonth + 1).toString()).children) {
                        if (groupSnapshot.child("date").value.toString().toInt() >= todayDate.toString().toInt()) {
                            fragmentDataList.add(
                                GroupFragmentData(
                                    userInfo.userNames,
                                    userInfo.userInfos,
                                    userInfo.userTypes,
                                    userInfo.userIcons,
                                    groupSnapshot.child("scheduleName").value.toString(),
                                    groupSnapshot.child("scheduleInfo").value.toString(),
                                    groupSnapshot.child("dateYear").value.toString().toInt(),
                                    groupSnapshot.child("dateMonth").value.toString().toInt(),
                                    groupSnapshot.child("date").value.toString().toInt(),
                                    groupSnapshot.child("startTime").value.toString()
                                )
                            )
                        }


                    }
                }
                else{
                    for(UserDatasnapShot in dataSnapshot.child("Schedule").children) {
                        for (deeperUserDataSnapshot in UserDatasnapShot.child((CalendarInfo.currentMonth + 1).toString()).children) {
                            if (deeperUserDataSnapshot.child("date").value.toString().toInt() >= todayDate.toString().toInt()) {
                                fragmentDataList.add(
                                    GroupFragmentData(
                                        userInfo.userNames,
                                        userInfo.userInfos,
                                        userInfo.userTypes,
                                        userInfo.userIcons,
                                        deeperUserDataSnapshot.child("scheduleName").value.toString(),
                                        deeperUserDataSnapshot.child("scheduleInfo").value.toString(),
                                        deeperUserDataSnapshot.child("dateYear").value.toString().toInt(),
                                        deeperUserDataSnapshot.child("dateMonth").value.toString().toInt(),
                                        deeperUserDataSnapshot.child("date").value.toString().toInt(),
                                        deeperUserDataSnapshot.child("startTime").value.toString()
                                    )
                                )


                            }
                        }






                    }

                }
                fragmentDataList.sortWith(Comparator { data1, data2 -> data1.startTime.toString().toInt() - data2.startTime.toString().toInt()})
                fragmentDataList.sortWith(Comparator { data1, data2 -> data1.date.toString().toInt() - data2.date.toString().toInt()})
                recyclerView.adapter!!.notifyDataSetChanged()

            }

            override fun onCancelled(groupDataSnapshot: DatabaseError) {

            }

        }
        )




        return rootView
    }


}
