package com.example.daycalendar

import android.graphics.Color
import android.media.Image
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

import kotlinx.android.synthetic.main.daycalendar.*
import java.time.DayOfWeek
import java.time.Month
import java.util.*
import kotlin.math.abs


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}

