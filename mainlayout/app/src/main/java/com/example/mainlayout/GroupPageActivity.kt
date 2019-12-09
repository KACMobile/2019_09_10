package com.example.mainlayout

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
    val userID:String = "User01"

    override fun onCreate(savedInstanceState: Bundle?) {
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
        val userFollowDB =databaseReference.child("Users/" + userID + "/Follow/"+ userInfo.userTypes + "/" + userInfo.userNames)
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


        userNameView.text=userInfo.userNames
        if(userInfo.userIcons != "null")
            Glide.with(this).load(userInfo.userIcons).into(userImageView)
        subscribeButton.setOnClickListener{
            if(subscribeButton.text == "구독"){
                databaseReference.child("Users").child(userID).child("Follow").child(userInfo.userTypes).child(userInfo.userNames).setValue(Color.RED)
                subscribeButton.text = ("구독중")
                Toast.makeText(it.context, "구독되었습니다.", Toast.LENGTH_SHORT).show()
                subscribeButton.setBackgroundColor(Color.GRAY)
            }
            else{
                databaseReference.child("Users").child(userID).child("Follow").child(userInfo.userTypes).child(userInfo.userNames).setValue(null)
                subscribeButton.text = ("구독")
                Toast.makeText(it.context, "구독을 해지하셨습니다.", Toast.LENGTH_SHORT).show()
                subscribeButton.setBackgroundColor(Color.WHITE)

            }

        }
        closeButton.setOnClickListener {
            finish()
        }


        viewPager.adapter = GroupPagePagerAdapter(supportFragmentManager,3, userInfo)
        tabLayout.setupWithViewPager(viewPager)




    }
}
