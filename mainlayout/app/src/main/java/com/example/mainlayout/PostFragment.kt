package com.example.mainlayout

import android.content.Context
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

//포스트 표기관련 fragment - 황선혁
class PostFragment : Fragment() {
    var userID:String = "User01"

    companion object {
        fun newInstance() = PostFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val idPreference = context!!.getSharedPreferences("UserID", Context.MODE_PRIVATE)
        userID = idPreference.getString("UserID", "User01")!!
        val rootView = inflater.inflate(R.layout.post_fragment, container, false)
        val recyclerView = rootView.findViewById<RecyclerView>(R.id.post_RecyclerView)
        val userInfo = arguments!!.getSerializable("userInfo") as UserInfo
        val postArray = arrayListOf<Post>()
        val databaseReference = FirebaseDatabase.getInstance().reference

        recyclerView.adapter = PostRecyclerAdapter(activity!!, postArray)

        val userFollowDB =databaseReference.child(userInfo.userType + "/" +  userInfo.userName + "/" + "Post")
        userFollowDB.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                postArray.clear()
                for(deeperDataSnapshot in dataSnapshot.children){

                    postArray.add(Post(deeperDataSnapshot.child("postText").value.toString(), deeperDataSnapshot.child("postDate").value.toString(), deeperDataSnapshot.child("postTime").value.toString(), deeperDataSnapshot.child("postImage").value.toString(),deeperDataSnapshot.child("postLat").value.toString(),deeperDataSnapshot.child("postLng").value.toString(),userInfo))
                }
                postArray.sortWith(Comparator { data1, data2 -> data2.postTime.toInt() - data1.postTime.toInt()})
                postArray.sortWith(Comparator { data1, data2 -> data2.postDate.toInt() - data1.postDate.toInt()})
                recyclerView.adapter!!.notifyDataSetChanged()
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })



        
        return rootView
    }




}
