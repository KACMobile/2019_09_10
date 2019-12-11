package com.example.mainlayout

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.Glide
import com.example.mainlayout.GroupAdd
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class GroupAddAdapter(context: Context, val userInfosArr: ArrayList<UserInfo>) : BaseAdapter(){
    private var userID:String = "User01"

    private val mContext : Context

    init {
        mContext = context
        val idPreference = context.getSharedPreferences("UserID", Context.MODE_PRIVATE)
        userID = idPreference.getString("UserID", "User01")!!
    }

    override fun getCount(): Int {
        return userInfosArr.size
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
        nameTextView.text = userInfosArr[position].userNames


        val infoTextView = rowMain.findViewById<TextView>(R.id.add_group_info)
        infoTextView.text = userInfosArr[position].userInfos

        val followBtn = rowMain.findViewById<Button>(R.id.add_group_button)
        val userFollowDB =databaseReference.child("Users/" + userID + "/Follow/"+ userInfosArr[position].userTypes + "/" + userInfosArr[position].userNames)
        val icImageView = rowMain.findViewById<ImageView>(R.id.add_group_icon)
        if(userInfosArr[position].userIcons != "null") {
            Glide.with(mContext).load(userInfosArr[position].userIcons).into(icImageView)
        }
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
        if(databaseReference.child("Users").child(userID).child("Follow").child(userInfosArr[position].userNames) == null) {
            followBtn.text = ("구독중")
            followBtn.setBackgroundColor(Color.GRAY)
        }

        followBtn.setOnClickListener{
            if(followBtn.text == "구독"){
                databaseReference.child("Users").child(userID).child("Follow").child(userInfosArr[position].userTypes).child(userInfosArr[position].userNames).setValue(Color.RED)
                followBtn.text = ("구독중")
                Toast.makeText(it.context, "구독되었습니다.", Toast.LENGTH_SHORT).show()
                followBtn.setBackgroundColor(Color.GRAY)
            }
            else{
                databaseReference.child("Users").child(userID).child("Follow").child(userInfosArr[position].userTypes).child(userInfosArr[position].userNames).setValue(null)
                followBtn.text = ("구독")
                Toast.makeText(it.context, "구독을 해지하셨습니다.", Toast.LENGTH_SHORT).show()
                followBtn.setBackgroundColor(Color.WHITE)

            }

        }
        rowMain.setOnClickListener {
            val groupPageIntent = Intent(mContext, GroupPageActivity::class.java)
            groupPageIntent.putExtra("userInfo",userInfosArr[position])
            mContext.startActivity(groupPageIntent)
        }





        return rowMain
    }


}