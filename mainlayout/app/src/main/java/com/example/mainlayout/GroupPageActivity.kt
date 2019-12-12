package com.example.mainlayout

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class GroupPageActivity : AppCompatActivity() {
    var userID:String = "User01"

    override fun onCreate(savedInstanceState: Bundle?) {
        val idPreference = getSharedPreferences("UserID", Context.MODE_PRIVATE)
        userID = idPreference.getString("UserID", "User01")!!
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_page)
        val userNameView = findViewById<TextView>(R.id.group_page_username)
        val userImageView = findViewById<ImageView>(R.id.group_page_userimage)
        val subscribeButton = findViewById<Button>(R.id.group_page_add_button)
        val closeButton = findViewById<ImageView>(R.id.group_page_close)
        val tabLayout = findViewById<TabLayout>(R.id.group_page_tablayout)
        val viewPager = findViewById<ViewPager>(R.id.group_page_viewpager)
        val alarmImage = findViewById<ImageView>(R.id.group_page_alarm)
        val fab = findViewById<FloatingActionButton>(R.id.group_page_fab)

        fab.hide()


        val intent = intent
        val userInfo = intent.getSerializableExtra("userInfo") as UserInfo
        val databaseReference = FirebaseDatabase.getInstance().reference
        val userFollowDB =databaseReference.child("Users/" + userID + "/Follow/"+ userInfo.userType + "/" + userInfo.userName)
        userFollowDB.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapShot: DataSnapshot) {
                if(dataSnapShot.value != null){
                    subscribeButton.text = ("구독중")
                    subscribeButton.setBackgroundColor(Color.GRAY)
                }

            }

            override fun onCancelled(p0: DatabaseError) {
            }
        }

        )


        userNameView.text=userInfo.userName
        if(userInfo.userIcon != "null")
            Glide.with(this).load(userInfo.userIcon).into(userImageView)
        subscribeButton.setOnClickListener{
            if(subscribeButton.text == "구독"){
                databaseReference.child("Users").child(userID).child("Follow").child(userInfo.userType).child(userInfo.userName).setValue((Math.random()*16777216).toInt())
                subscribeButton.text = ("구독중")
                Toast.makeText(it.context, "구독되었습니다.", Toast.LENGTH_SHORT).show()
                subscribeButton.setBackgroundColor(Color.GRAY)
            }
            else{
                val alertDialog = AlertDialog.Builder(this, R.style.Theme_AppCompat_Light_Dialog)
                alertDialog.setMessage("구독을 해지하시겠습니까?")

                alertDialog.setPositiveButton("확인"){ _, _ ->
                    databaseReference.child("Users").child(userID).child("Follow").child(userInfo.userType).child(userInfo.userName).setValue(null)
                    subscribeButton.text = ("구독")
                    Toast.makeText(it.context, "구독을 해지하셨습니다.", Toast.LENGTH_SHORT).show()
                    subscribeButton.setBackgroundColor(Color.WHITE)
                }
                alertDialog.setNegativeButton("취소"){ _ , _ ->

                }
                alertDialog.show()


            }

        }
        closeButton.setOnClickListener {
            finish()
        }


        viewPager.adapter = GroupPagePagerAdapter(supportFragmentManager,3, userInfo)
        tabLayout.setupWithViewPager(viewPager)




    }
}
