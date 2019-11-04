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
    var INTENT_ACTION :String = Intent.ACTION_BOOT_COMPLETED

    override fun onReceive(context: Context, intent: Intent) {
        val notificationmanager
                = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            notificationmanager.createNotificationChannel(NotificationChannel(
                "default","기본 채널",NotificationManager.IMPORTANCE_DEFAULT))
        }

        val pendingIntent = PendingIntent.getActivity(
            context, 0, Intent(context, MainActivity::class.java), PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = Notification.Builder(context,"default")
        builder.setSmallIcon(R.drawable.ic_btn_speak_now)
        builder.setWhen(System.currentTimeMillis())
        builder.setNumber(1)
        builder.setContentTitle("알람")
        builder.setContentText("알람시작")
        builder.setColor(Color.RED)
        builder.setContentIntent(pendingIntent)
        builder.setAutoCancel(true)

        notificationmanager.notify(1, builder.build())
    }

}