package com.example.notice_example

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.util.Calendar.*


class MainActivity:AppCompatActivity() {
    override fun onCreate(savedInstanceState:Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val alarmBt = findViewById<Button>(R.id.alarmBt)

        Alarm()

        alarmBt.setOnClickListener{
            alarmBt.text="알람이 꺼졌어요"
            Toast.makeText(this,"버튼을 눌렀네요", Toast.LENGTH_SHORT).show()
            //createNotice()
            //am.cancel(sender)
        }

    }



    fun Alarm() {
        val intent = Intent(this@MainActivity, BroadCastD::class.java) //BroadCastD 클래스로 보낼 intent
        val sender = PendingIntent.getBroadcast(
            this, // context 정보
            0, // 여러개의 알람을 등록하기 위한 primary id 값 세팅
            intent, // 정보가 담긴 intent
            0)
        val calendar = getInstance()
        calendar.set(calendar.get(YEAR), calendar.get(MONTH), calendar.get(DATE),
            2, 23, 30)

        val am = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        am.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, sender)
    }

}

