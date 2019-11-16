package com.example.notice_example

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.R
import android.app.Notification
import android.app.NotificationChannel
import android.app.PendingIntent
import android.app.NotificationManager
import android.graphics.Color
import android.os.Build

class BroadCastD : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val notificationmanager
                = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            notificationmanager.createNotificationChannel(NotificationChannel(
                "default","기본 채널",NotificationManager.IMPORTANCE_DEFAULT))
        }

        val text:String? = intent.getStringExtra("text")
        val id = intent.getIntExtra("id", 0)


        val pendingIntent = PendingIntent.getActivity(
            context, 0, Intent(context, MainActivity::class.java), PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = Notification.Builder(context,"default").setSmallIcon(R.drawable.ic_btn_speak_now)
        .setWhen(System.currentTimeMillis()).setNumber(1).setContentTitle(text).setContentText(text).setColor(Color.RED)
        .setContentIntent(pendingIntent).setAutoCancel(true)

        notificationmanager.notify(id, builder.build())
    }

}