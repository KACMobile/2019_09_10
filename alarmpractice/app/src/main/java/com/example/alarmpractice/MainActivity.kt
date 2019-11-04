package com.example.alarmpractice

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.core.app.NotificationCompat
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val alarmBt = findViewById<Button>(R.id.alarmbt)
        alarmBt.setOnClickListener{
            alarmBt.text="AlarmStarted"
            Toast.makeText(this,"버튼을 눌렀네요", Toast.LENGTH_SHORT).show()
            //createNotice()
            setAlarm()
        }





    }
    fun setAlarm(){
        var cal = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, 14)
        cal.set(Calendar.MINUTE, 16)
        cal.set(Calendar.SECOND, 0)
        Log.d("aa", cal.get(Calendar.YEAR).toString() + "년" + cal.get(Calendar.MONTH).toString() + "월" + cal.get(Calendar.DATE).toString() + "일" + cal.get(Calendar.HOUR_OF_DAY).toString() +"시"+ cal.get(Calendar.MINUTE).toString() + "분" + cal.get(Calendar.SECOND).toString() + "초")

        val mAlarmIntent = Intent("android.intent.action.ALARM_START")
        val mPendingIntent = PendingIntent.getBroadcast(
            this,
            0,
            mAlarmIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val mAlarmMangager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        mAlarmMangager.set(
            AlarmManager.RTC_WAKEUP,
            cal.timeInMillis,
            mPendingIntent)

    }
    fun createNotice(){
        val builder = NotificationCompat.Builder(this, "default")
        builder.setSmallIcon(R.drawable.ic_arrow_left)
        builder.setContentTitle("알람")
        builder.setContentText("알람시작")
        builder.setColor(Color.RED)
        builder.setAutoCancel(true)

        val notificationManager = this.getSystemService(Context.NOTIFICATION_SERVICE)as NotificationManager
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            notificationManager.createNotificationChannel(NotificationChannel("default","기본 채널",NotificationManager.IMPORTANCE_DEFAULT))
        }
        notificationManager.notify(1,builder.build())

    }

}

