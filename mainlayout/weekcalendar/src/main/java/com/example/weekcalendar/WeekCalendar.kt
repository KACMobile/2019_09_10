package com.example.weekcalendar

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.weekcalendar.*
import kotlinx.android.synthetic.main.weekcalendar.view.*

import org.w3c.dom.Attr
import java.time.Month
import java.time.Year
import java.util.*
import kotlin.math.abs


class WeekCalendar @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0 )
    : ConstraintLayout(context,attrs,defStyleAttr)
{
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

    init {
        LayoutInflater.from(context).inflate(R.layout.weekcalendar,this,true)
        calendardefaultsetting()

        this.setOnTouchListener(object : OnSwipeTouchListener(context) {
            override fun onSwipeLeft() {
                super.onSwipeLeft()
                setNextWeek()
                calendardefaultsetting()
            }

            override fun onSwipeRight() {
                super.onSwipeRight()
                setPreWeek()
                calendardefaultsetting()

            }


        } )
        this.weekTableScroll.setOnTouchListener(object : OnSwipeTouchListener(context) {
            override fun onSwipeLeft() {
                super.onSwipeLeft()
                setNextWeek()
                calendardefaultsetting()
            }

            override fun onSwipeRight() {
                super.onSwipeRight()
                setPreWeek()
                calendardefaultsetting()

            }


        } )

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

        //textWeekInfo.text = ((currentMonth + 1).toString() + "월" + currentWOM.toString() + "주")

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
    public fun setCurrent(year: Int, month: Int, date: Int){
        currentYear = year
        currentMonth = month
        currentDate = date
    }

}



