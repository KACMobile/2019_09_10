package com.example.settingtest

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    var fontSize = 14f
    var fontColor = Color.BLACK

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val optionPreference = getSharedPreferences("OptionInfo", Context.MODE_PRIVATE)
        val helloText: TextView = findViewById(R.id.Hello)
        /*val editor = optionPreference.edit()
        editor.putFloat("fontSize", 20f)
        editor.putInt("fontColor", Color.RED)
        editor.commit()*/
        fontSize = optionPreference.getFloat("fontSize",14f)
        fontColor = optionPreference.getInt("fontColor",Color.BLACK)
        helloText.textSize = fontSize
        helloText.setTextColor(fontColor)


        val settingbt: Button = findViewById(R.id.Settingbt)
        settingbt.setOnClickListener{
            val nextIntent = Intent(this, SettingActivity::class.java)
            startActivity(nextIntent)
        }
    }

    override fun onRestart() {
        super.onRestart()
        val optionPreference = getSharedPreferences("OptionInfo", Context.MODE_PRIVATE)
        val helloText: TextView = findViewById(R.id.Hello)
        fontSize = optionPreference.getFloat("fontSize",14f)
        fontColor = optionPreference.getInt("fontColor",Color.BLACK)
        helloText.textSize = fontSize
        helloText.setTextColor(fontColor)
    }
}
