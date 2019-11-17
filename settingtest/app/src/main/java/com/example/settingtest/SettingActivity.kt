package com.example.settingtest

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity

class SettingActivity: AppCompatActivity() {
    val settingList: Array<String> = arrayOf("글자크기", "16", "22", "28", "글자색", "black", "red", "blue")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        val list : ListView = findViewById(R.id.SettingOptions)
        val adapter = SettingAdapter(this, settingList)
        val optionPreference = getSharedPreferences("OptionInfo", Context.MODE_PRIVATE)
        val editor = optionPreference.edit()
        list.adapter = adapter

        list.setOnItemClickListener { parent, itemview, position, id ->
            when(list.adapter.getItem(position)){
                "16"  -> editor.putFloat("fontSize", 16f)
                "22"  -> editor.putFloat("fontSize", 22f)
                "28"  -> editor.putFloat("fontSize", 28f)
                "black" -> editor.putInt("fontColor", Color.BLACK)
                "red" -> editor.putInt("fontColor", Color.RED)
                "blue" -> editor.putInt("fontColor", Color.BLUE)
            }
        }

        val applybt: Button = findViewById(R.id.ApplyButton)
        applybt.setOnClickListener{
            editor.commit()
            finish()
        }



    }
}