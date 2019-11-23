package com.example.mainlayout

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView
import com.example.mainlayout.GroupAdd
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class GroupAddAdapter(context: Context, val userNamesArr: ArrayList<String>, val userInfosArr: ArrayList<String>, val userTypesArr: ArrayList<String>) : BaseAdapter(){
    private val userID:String = "User01"

    private val mContext : Context

    init {
        mContext = context
    }

    override fun getCount(): Int {
        return userNamesArr.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItem(position: Int): Any {
        return "Test"
    }
    //각각의 줄 렌더링
    override fun getView(position: Int, convertView: View?, viewGroup: ViewGroup?): View {
        val databaseReference = FirebaseDatabase.getInstance().reference

        val layoutInflater = LayoutInflater.from(mContext)
        val rowMain = layoutInflater.inflate(R.layout.group_add_row, viewGroup, false)

        val nameTextView = rowMain.findViewById<TextView>(R.id.add_group_name)
        nameTextView.text = userNamesArr[position]


        val infoTextView = rowMain.findViewById<TextView>(R.id.add_group_info)
        infoTextView.text = userInfosArr[position]

        val followBtn = rowMain.findViewById<Button>(R.id.add_group_button)
        val userFollowDB =databaseReference.child("Users/" + userID + "/Follow/"+ userTypesArr[position] + "/" + userNamesArr[position])
        userFollowDB.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(dataSnapShot: DataSnapshot) {
                if(dataSnapShot.value != null){
                    followBtn.text = ("구독중")
                    followBtn.setBackgroundColor(Color.GRAY)
                }

            }

            override fun onCancelled(p0: DatabaseError) {
            }
        }

        )
        if(databaseReference.child("Users").child(userID).child("Follow").child(userNamesArr[position]) == null) {
            followBtn.text = ("구독중")
            followBtn.setBackgroundColor(Color.GRAY)
        }

        followBtn.setOnClickListener{
            if(followBtn.text == "구독"){
                databaseReference.child("Users").child(userID).child("Follow").child(userTypesArr[position]).child(userNamesArr[position]).setValue(Color.RED)
                followBtn.text = ("구독중")
                //followBtn.setBackgroundColor(Color.RED)
            }
            else{
                databaseReference.child("Users").child(userID).child("Follow").child(userTypesArr[position]).child(userNamesArr[position]).setValue(null)
                followBtn.text = ("구독")
                //followBtn.setBackgroundColor(Color.RED)

            }

        }





        return rowMain

//            val textView = TextView(mContext)
//            textView.text = "this is list row"
//            return textView
    }


}