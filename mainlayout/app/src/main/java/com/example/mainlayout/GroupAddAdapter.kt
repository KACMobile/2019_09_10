package com.example.mainlayout

import android.app.AlertDialog
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
import kotlin.random.Random

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
        nameTextView.text = userInfosArr[position].userName


        val infoTextView = rowMain.findViewById<TextView>(R.id.add_group_info)
        infoTextView.text = userInfosArr[position].userInfo

        val followBtn = rowMain.findViewById<Button>(R.id.add_group_button)
        val userFollowDB =databaseReference.child("Users/" + userID + "/Follow/"+ userInfosArr[position].userType + "/" + userInfosArr[position].userName)
        val icImageView = rowMain.findViewById<ImageView>(R.id.add_group_icon)
        if(userInfosArr[position].userIcon != "null") {
            Glide.with(mContext).load(userInfosArr[position].userIcon).into(icImageView)
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
        if(databaseReference.child("Users").child(userID).child("Follow").child(userInfosArr[position].userName) == null) {
            followBtn.text = ("구독중")
            followBtn.setBackgroundColor(Color.GRAY)
        }

        followBtn.setOnClickListener{
            if(followBtn.text == "구독"){
                val rnd = Random
                val randomColor = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))

                databaseReference.child("Users").child(userID).child("Follow").child(userInfosArr[position].userType).child(userInfosArr[position].userName).setValue(randomColor)
                followBtn.text = ("구독중")
                Toast.makeText(it.context, "구독되었습니다.", Toast.LENGTH_SHORT).show()
                followBtn.setBackgroundColor(Color.GRAY)
            }
            else{
                val alertDialog = AlertDialog.Builder(mContext, R.style.Theme_AppCompat_Light_Dialog)
                alertDialog.setMessage("구독을 해지하시겠습니까?")

                alertDialog.setPositiveButton("확인"){ _, _ ->
                    databaseReference.child("Users").child(userID).child("Follow").child(userInfosArr[position].userType).child(userInfosArr[position].userName).setValue(null)
                    followBtn.text = ("구독")
                    Toast.makeText(it.context, "구독을 해지하셨습니다.", Toast.LENGTH_SHORT).show()
                    followBtn.setBackgroundColor(Color.WHITE)
                }
                alertDialog.setNegativeButton("취소"){ _ , _ ->

                }
                alertDialog.show()


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