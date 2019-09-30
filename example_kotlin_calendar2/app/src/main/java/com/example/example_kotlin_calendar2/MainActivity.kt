package com.example.example_kotlin_calendar2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.weekcalendar.*
import kotlinx.android.synthetic.main.weekcalendar.view.*
import com.applandeo.materialcalendarview.CalendarView
import com.applandeo.materialcalendarview.EventDay
import java.util.*


class MainActivity : AppCompatActivity() {

    val RESULT = "result"
    val EVENT = "event"

    //lateinit var CalendarViewAdapter: CalendarPageAdapter

    //private var mCalendarView: CalendarView? = null
    //private val mEventDays : List<EventDay> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_constriant_main)

        //val mCalendarView = findViewById<View>(R.id.calendarView) as CalendarView
        val events = ArrayList<EventDay>()

        val calendar = Calendar.getInstance()
        calendar.set(2019, 10, 1)

        events.add(EventDay(calendar, R.drawable.ic_arrow_left))

        val calendarView = findViewById<View>(R.id.MycalendarView) as CalendarView

        calendarView.setEvents(events)

        calendarView.setOnDayClickListener { eventDay ->
            val calendar = eventDay.calendar
            /*
            val s =
                calendar.get(Calendar.MONTH).toString() + "/" + calendar.get(Calendar.DAY_OF_MONTH)
            Toast.makeText(this@MainActivity, s, Toast.LENGTH_SHORT)
                .show()

             */
            val intent1 = Intent(this, MoveDayCalendar::class.java)
            startActivity(intent1)
        }

}
}
