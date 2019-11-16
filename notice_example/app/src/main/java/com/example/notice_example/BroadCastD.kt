package com.example.notice_example

import android.content.Context
import android.content.Intent
import android.R
import android.app.Notification
import android.app.NotificationChannel
import android.app.PendingIntent
import android.app.NotificationManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.PowerManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import android.widget.TextView
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T





class BroadCastD : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(com.example.notice_example.R.layout.activity_main)

        var text:String = ""
        var id:Int = 0

        val extras = intent.extras
        if (extras == null) {
            text = "값을 전달 받는데 문제 발생"
        } else
            id = extras.getInt("notificationId")

        //val textView = findViewById(R.id.textView) as TextView
        //textView.text = "$text $id"

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        //노티피케이션 제거
        notificationManager.cancel(id)
    }

}