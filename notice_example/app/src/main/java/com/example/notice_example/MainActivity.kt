package com.example.notice_example

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import java.util.Calendar.*


class MainActivity:AppCompatActivity() {
    //private val ONE_MINUTE = 5626

    override fun onCreate(savedInstanceState:Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Alarm()
    }

    fun Alarm() {
        val intent = Intent(this@MainActivity, BroadCastD::class.java)
        val sender = PendingIntent.getBroadcast(this, 0, intent, 0)
        val calendar = getInstance()
        calendar.set(calendar.get(YEAR), calendar.get(MONTH), calendar.get(DATE), 11, 34, 30)

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            Intent(this, MainActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val am = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        am.set(AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            pendingIntent)
        am.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, sender)
    }

}

