package com.example.mainlayout

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.example.mainlayout.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MyPageActivity : AppCompatActivity() {
    private val userID:String = "User01"
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
        val fab =findViewById<FloatingActionButton>(R.id.group_page_fab)

        val intent = intent
        val userInfo = intent.getSerializableExtra("userInfo") as UserInfo
        subscribeButton.visibility = View.GONE
        alarmImage.visibility = View.GONE


        viewPager.adapter = GroupPagePagerAdapter(supportFragmentManager,3, userInfo)
        tabLayout.setupWithViewPager(viewPager)
        userNameView.text=userInfo.userNames
        if(userInfo.userIcons != "null")
            Glide.with(this).load(userInfo.userIcons).into(userImageView)

        closeButton.setOnClickListener {
            finish()
        }

        fab.setOnClickListener{
            val intent = Intent(this, NewPostActivity::class.java)
            intent.putExtra("userInfo", userInfo)
            this.startActivity(intent)
        }


    }

}