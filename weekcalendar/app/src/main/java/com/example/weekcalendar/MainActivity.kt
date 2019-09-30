package com.example.weekcalendar

import android.graphics.Color
import android.media.Image
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

import kotlinx.android.synthetic.main.weekcalendar.*
import kotlinx.android.synthetic.main.weekcalendar.view.*
import java.time.DayOfWeek
import java.time.Month
import java.util.*
import kotlin.math.abs


class MainActivity : AppCompatActivity() {

    var cal: Calendar = Calendar.getInstance()

    val lastDayOfMonth = arrayOf(31,28,31,30,31,30,31,31,30,31,30,31)
    val leapYearLastDayOfMonth = arrayOf(31,29,31,30,31,30,31,31,30,31,30,31)
    val todayMonth = cal.get(Calendar.MONTH)
    val todayDOW = cal.get(Calendar.DAY_OF_WEEK)
    val todayDate = cal.get(Calendar.DATE)
    val thisYear = cal.get(Calendar.YEAR)
    var currentYear = cal.get(Calendar.YEAR)
    var currentMonth = cal.get(Calendar.MONTH)
    var currentWOM = cal.get(Calendar.WEEK_OF_MONTH)
    var currentDate = cal.get(Calendar.DATE)

    /*val dateID = arrayOf(weekcalendarview.dateSun,weekcalendarview.dateMon,
        weekcalendarview.dateTue, weekcalendarview.dateWen,
        weekcalendarview.dateThur, weekcalendarview.dateFri,
        weekcalendarview.dateSat
        )*/


    //val date = arrayOf("Sun","Mon","Tue","Wen","Thur","Fri","Sat")



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.weekcalendar)
        calendardefaultsetting()
        val preWeekButton = findViewById<ImageButton>(R.id.previousWeek)
        val nextWeekButton = findViewById<ImageButton>(R.id.nextWeek)
        preWeekButton.setOnClickListener(){
            setPreWeek()
            calendardefaultsetting()

        }
        preWeekButton.setOnClickListener(){
            setPreWeek()
            calendardefaultsetting()

        }

        nextWeekButton.setOnClickListener(){
            setNextWeek()
            calendardefaultsetting()

        }


    }

    fun calendardefaultsetting()
    {
        var currentLastDayOfMonth = cal.getActualMaximum(Calendar.DATE)
        var currentTime = cal.get(Calendar.HOUR_OF_DAY)
        val dateArray = arrayOf(weekcalendarview.dateSun,weekcalendarview.dateMon,
            weekcalendarview.dateTue, weekcalendarview.dateWen,
            weekcalendarview.dateThur, weekcalendarview.dateFri,
            weekcalendarview.dateSat)
        val dayText = arrayOf(weekcalendarview.textSun,weekcalendarview.textMon,
            weekcalendarview.textTue, weekcalendarview.textWen,
            weekcalendarview.textThur, weekcalendarview.textFri,
            weekcalendarview.textSat
        )
        val timeRow = arrayOf(weekcalendarview.time0, weekcalendarview.time1, weekcalendarview.time2,
            weekcalendarview.time3, weekcalendarview.time4, weekcalendarview.time5,
            weekcalendarview.time6, weekcalendarview.time7, weekcalendarview.time8,
            weekcalendarview.time9, weekcalendarview.time10, weekcalendarview.time11,
            weekcalendarview.time12, weekcalendarview.time13, weekcalendarview.time14,
            weekcalendarview.time15, weekcalendarview.time16, weekcalendarview.time17,
            weekcalendarview.time18, weekcalendarview.time19, weekcalendarview.time20,
            weekcalendarview.time21, weekcalendarview.time22, weekcalendarview.time23)

        val currentDOW = cal.get(Calendar.DAY_OF_WEEK)



        if(currentYear%4!=0) currentLastDayOfMonth = leapYearLastDayOfMonth[currentMonth]

        textWeekInfo.text = ((currentMonth + 1).toString() + "월" + currentWOM.toString() + "주")

        if(currentMonth == todayMonth && thisYear == currentYear && todayDate == currentDate)
            dayText[todayDOW-1].setBackgroundColor(Color.YELLOW)
        else
            dayText[todayDOW-1].setBackgroundColor(Color.WHITE)
        timeRow[currentTime].setBackgroundColor(Color.YELLOW)


        for(i in 1..7)
        {
            var date = currentDate - currentDOW  + i

            if(date>currentLastDayOfMonth)
                dateArray[i-1].text = (date-currentLastDayOfMonth).toString()
            else if(date<1)
                if(currentYear%4 == 0) dateArray[i-1].text = (leapYearLastDayOfMonth[currentMonth-1]-abs(date)).toString()
                else dateArray[i-1].text = (lastDayOfMonth[currentMonth-1] - abs(date) ).toString()
            else
                dateArray[i-1].text =  date.toString()
        }

    }
    fun setPreWeek()
    {
        cal.add(Calendar.DATE,-7)
        currentYear = cal.get(Calendar.YEAR)
        currentMonth = cal.get(Calendar.MONTH)
        currentWOM = cal.get(Calendar.WEEK_OF_MONTH)
        currentDate = cal.get(Calendar.DATE)

    }

    fun setNextWeek()
    {
        cal.add(Calendar.DATE,+7)
        currentYear = cal.get(Calendar.YEAR)
        currentMonth = cal.get(Calendar.MONTH)
        currentWOM = cal.get(Calendar.WEEK_OF_MONTH)
        currentDate = cal.get(Calendar.DATE)

    }


}

