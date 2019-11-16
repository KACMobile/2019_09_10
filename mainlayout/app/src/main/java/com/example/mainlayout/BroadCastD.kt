package com.example.mainlayout


import android.content.Context
import android.app.NotificationManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


class BroadCastD : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.mainlayout.R.layout.activity_main)

        var noti_id:Int = 0
        var noti_year:Int = 2019
        var noti_month:Int = 11
        var noti_date:Int = 19

        val extras = intent.extras
        if (extras == null) {}
        else {
            noti_id = extras.getInt("notificationId")
            noti_year=noti_id.toString().slice(IntRange(0,3)).toInt()
            noti_month=noti_id.toString().slice(IntRange(4,5)).toInt()
            noti_year=noti_id.toString().slice(IntRange(6,7)).toInt()
        }

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.cancel(noti_id)     //노티피케이션 제거
    }

}