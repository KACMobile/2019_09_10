package com.example.alarmpractice

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.IBinder
import android.widget.Toast
import androidx.core.app.NotificationCompat

class AlarmSoundService : Service() {

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Toast.makeText(this,"이거슨 알람테스트", Toast.LENGTH_SHORT).show()
        createNotice()
        return START_NOT_STICKY
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
            notificationManager.createNotificationChannel(
                NotificationChannel("default","기본 채널",
                    NotificationManager.IMPORTANCE_DEFAULT)
            )
        }
        notificationManager.notify(1,builder.build())

    }
}
