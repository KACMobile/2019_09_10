package com.example.alarmpractice

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.widget.Toast

class AlarmSoundService : Service() {

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Toast.makeText(this,"이거슨 알람테스트", Toast.LENGTH_SHORT).show()
        return START_NOT_STICKY
    }
}
