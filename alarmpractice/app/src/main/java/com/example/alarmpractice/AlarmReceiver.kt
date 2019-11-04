package com.example.alarmpractice

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

public class AlarmReceiver: BroadcastReceiver(){
    override fun onReceive(context: Context, intent: Intent?) {
        val mServiceintent = Intent(context, AlarmSoundService::class.java)
        Toast.makeText(context,"알람 리시버", Toast.LENGTH_SHORT).show()
        context.startService(mServiceintent)


    }
}