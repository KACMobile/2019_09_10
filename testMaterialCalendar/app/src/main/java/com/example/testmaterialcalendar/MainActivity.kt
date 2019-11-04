package com.example.testmaterialcalendar

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.applandeo.materialcalendarview.CalendarView
import com.applandeo.materialcalendarview.EventDay
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    var events = arrayListOf<EventDay>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        insertEvents(2019,11,5)
        insertEvents(2019,11,15)
        insertEvents(2019,12,25)



    }
    public fun insertEvents(year:Int, month:Int, date:Int){
        val myCalendar = findViewById<CalendarView>(R.id.MycalendarVIew)
        var cal = Calendar.getInstance()
        cal.set(year,month-1,date)
        events.add(EventDay(cal,R.drawable.sample_three_icons))
        myCalendar.setEvents(events)


    }
}
