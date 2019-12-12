package com.example.mainlayout

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import android.content.Context.NOTIFICATION_SERVICE
import android.graphics.Color
import android.os.Build.VERSION.SDK_INT
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.media.MediaPlayer
import android.os.Build
import android.os.Handler
import androidx.core.app.NotificationCompat




class AlarmService :Service(){

    private var notificationId:Int = 1111
    private var notischeduleName = "알람 이름"
    private var notischeduleInfo = "알람 정보"

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("Service", "들어옴3!!!!!!!!!!!1")
        val extras = intent!!.extras!!
        notificationId = extras.getInt("notificationId")
        notischeduleName = extras.getString("scheduleName")!!
        notischeduleInfo = extras.getString("scheduleInfo")!!

        val serviceIntent:Intent = Intent(this, BroadCastD::class.java)

        val pendingIntent = PendingIntent.getBroadcast(
            this, // context 정보
            0, // 여러개의 알람을 등록하기 위한 primary id 값 세팅
            serviceIntent, // 정보가 담긴 intent
            PendingIntent.FLAG_UPDATE_CURRENT)

        val notificationmanager
                = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val builder = Notification.Builder(this,"default")
        //builder.setSmallIcon(R.drawable.ic_btn_speak_now)
        builder.setWhen(System.currentTimeMillis())
        builder.setNumber(1)
        builder.setContentTitle(notischeduleName)
        builder.setContentText(notischeduleInfo)
        builder.setColor(Color.RED)
        builder.setContentIntent(pendingIntent)
        builder.setAutoCancel(true)

        return START_NOT_STICKY
    }
}
    /*
class myServiceHandler: Handler {
    public void handleMessage(android.os.Message msg) {
        Intent intent = new Intent(MyService.this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(MyService.this, 0, intent,PendingIntent.FLAG_UPDATE_CURRENT);

        Notifi = new Notification.Builder(getApplicationContext())
            .setContentTitle("Content Title")
            .setContentText("Content Text")
            .setSmallIcon(R.drawable.logo)
            .setTicker("알림!!!")
            .setContentIntent(pendingIntent)
            .build();

        //소리추가
        Notifi.defaults = Notification.DEFAULT_SOUND;

        //알림 소리를 한번만 내도록
        Notifi.flags = Notification.FLAG_ONLY_ALERT_ONCE;

        //확인하면 자동으로 알림이 제거 되도록
        Notifi.flags = Notification.FLAG_AUTO_CANCEL;


        Notifi_M.notify( 777 , Notifi);

        //토스트 띄우기
        Toast.makeText(MyService.this, "뜸?", Toast.LENGTH_LONG).show();
    }
};

     */

/*
abstract class AlarmService: Service() {
    override fun onStartCommand(intent: Intent, flags:Int, startId:Int):Int {
        Log.d("in Service", "들어옴3")
        Toast.makeText(this, "알람", Toast.LENGTH_SHORT).show()
        return START_NOT_STICKY
    }
}

 */