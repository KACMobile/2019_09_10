package com.example.notice_example

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PowerManager
import android.widget.Button
import android.widget.Toast
import androidx.core.app.NotificationCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.util.Calendar.*


class MainActivity:AppCompatActivity() {

    val NOTIFICATION_CHANNEL_ID = "10001"
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
        val calendar = getInstance()
        val notificationmanager
                = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notyintent = Intent(this, BroadCastD::class.java) //BroadCastD 클래스로 보낼 intent
        notyintent.putExtra("notificationId", calendar.get(YEAR) + calendar.get(MONTH) + calendar.get(DATE) + 20 + 41 + 30)
        notyintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)

        val pendingIntent = PendingIntent.getActivity(
            this, 0, notyintent, PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = Notification.Builder(this,NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_btn_speak_now)
            .setWhen(System.currentTimeMillis())
            .setNumber(1)
            .setContentTitle("상태바 타이틀")
            .setContentText("상태바 서브타이틀")
            .setColor(Color.RED)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        if (Build.VERSION.SDK_INT >= 26){
            val channelName :CharSequence = "noty_channel"
            //val description :String = "upper oreo"
            val importance :Int = NotificationManager.IMPORTANCE_HIGH

            val channel:NotificationChannel = NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName , importance)
            //channel.setDescription(description)

            notificationmanager.createNotificationChannel(channel)
        }

        val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
        val wakeLock = powerManager.newWakeLock(
            PowerManager.FULL_WAKE_LOCK or PowerManager.ACQUIRE_CAUSES_WAKEUP or PowerManager.ON_AFTER_RELEASE,
            "My:Tag") // 스마트폰 화면이 꺼져있으면 화면 켜고 알림 울리기위해서
        wakeLock.acquire(5000)

        notificationmanager.notify(calendar.get(YEAR) + calendar.get(MONTH) + calendar.get(DATE) + 23 + 21 + 30, builder.build())


        val sender = PendingIntent.getBroadcast(
            this, // context 정보
            0, // 여러개의 알람을 등록하기 위한 primary id 값 세팅
            intent, // 정보가 담긴 intent
            0)
        //val calendar = getInstance()
        calendar.set(calendar.get(YEAR), calendar.get(MONTH), calendar.get(DATE),
            23, 21, 30)

        val am = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        am.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, sender)
    }

}

