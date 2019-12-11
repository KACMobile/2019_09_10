package com.example.mainlayout

import android.R
import android.app.NotificationManager
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


class BroadCastD :AppCompatActivity(){
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
/*
class BroadCastD : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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

 */
/*
class BroadCastD :BroadcastReceiver(){
    override fun onReceive(context: Context, intent: Intent) {
        Log.d("Broadcase", "들어옴2!!!!!")
        val mServiceIntent = Intent(context, AlarmService::class.java)

        val extras = intent.extras!!
        val notificationId = extras.getInt("notificationId")
        val notischeduleName = extras.getString("scheduleName")
        var notischeduleInfo = extras.getString("scheduleInfo")

        mServiceIntent.putExtra("notificationId", notificationId)
        mServiceIntent.putExtra("scheduleName", notischeduleName)
        mServiceIntent.putExtra("scheduleInfo", notischeduleInfo)

        context.startService(mServiceIntent)
    }
}

 */
/*
class BroadCastD : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent){
        Log.d("in Broadcast", "들어옴2")
        val mServiceIntent : Intent = Intent(context, AlarmService::class.java)
        context.startService(mServiceIntent)

    }
}


 */



    //var INTENT_ACTION :String = Intent.ACTION_BOOT_COMPLETED
    /*
    override fun onReceive(context: Context, intent: Intent) {

        val notificationmanager
                = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        Log.d("In Broadcast", "after notimanager")

        val extras = intent.extras!!
        val notificationId = extras.getInt("notificationId")
        val notischeduleName = extras.getString("scheduleName")
        var notischeduleInfo = extras.getString("scheduleInfo")

        /*
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            notificationmanager.createNotificationChannel(
                NotificationChannel(
                    "default","기본 채널",NotificationManager.IMPORTANCE_DEFAULT)
            )
        }
         */

        val pendingIntent = PendingIntent.getActivity(
            context, notificationId, Intent(context, MainActivity::class.java), PendingIntent.FLAG_UPDATE_CURRENT)
        Log.d("In Broadcast", "after pendingIntent")

        val builder = Notification.Builder(context,"default")
        builder.setSmallIcon(R.drawable.ic_btn_speak_now)
        builder.setWhen(System.currentTimeMillis())
        builder.setNumber(1)
        builder.setContentTitle(notischeduleName)
        builder.setContentText(notischeduleInfo)
        builder.setColor(Color.RED)
        builder.setContentIntent(pendingIntent)
        builder.setAutoCancel(true)

        Log.d("In Broadcast", "after builder")

        notificationmanager.notify(notificationId, builder.build())
        Log.d("In Broadcast", "after notify")
    }
}

     */

