package com.example.daycalendar

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.size
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.daycalendar.*
import kotlinx.android.synthetic.main.daycalendar.view.*
import com.google.firebase.database.*

import org.w3c.dom.Attr
import java.sql.Types.NULL
import java.util.*
import kotlin.math.abs


class DayCalendar @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0 )
    : ConstraintLayout(context,attrs,defStyleAttr)
{
    var cal: Calendar = Calendar.getInstance()
    val database = FirebaseDatabase.getInstance()
    val databaseReference = database.reference

    val lastDayOfMonth = arrayOf(31,28,31,30,31,30,31,31,30,31,30,31)
    val leapYearLastDayOfMonth = arrayOf(31,29,31,30,31,30,31,31,30,31,30,31)
    val todayMonth = cal.get(Calendar.MONTH)
    var todayDOW = cal.get(Calendar.DAY_OF_WEEK)
    val todayDate = cal.get(Calendar.DATE)
    val thisYear = cal.get(Calendar.YEAR)
    var currentYear = cal.get(Calendar.YEAR)
    var currentMonth = cal.get(Calendar.MONTH)
    var currentWOM = cal.get(Calendar.WEEK_OF_MONTH)
    var currentDate = cal.get(Calendar.DATE)
    var currentDOW = cal.get(Calendar.DAY_OF_WEEK)


    init {
        LayoutInflater.from(context).inflate(R.layout.daycalendar,this,true)
        calendardefaultsetting()

        this.setOnTouchListener(object : OnSwipeTouchListener(context) {
            override fun onSwipeLeft() {
                super.onSwipeLeft()
                setNextDay()
                calendardefaultsetting()
            }

            override fun onSwipeRight() {
                super.onSwipeRight()
                setPreDay()
                calendardefaultsetting()
            }
        } )

        this.dayTableScroll.setOnTouchListener(object : OnSwipeTouchListener(context) {
            override fun onSwipeLeft() {
                super.onSwipeLeft()
                setNextDay()
                calendardefaultsetting()
            }

            override fun onSwipeRight() {
                super.onSwipeRight()
                setPreDay()
                calendardefaultsetting()
            }
        } )
    }

    fun calendardefaultsetting() {
        var currentLastDayOfMonth = cal.getActualMaximum(Calendar.DATE)
        var currentTime = cal.get(Calendar.HOUR_OF_DAY)
        val dateArray = arrayOf(daycalendarview.dateSun,daycalendarview.dateMon,
            daycalendarview.dateTue, daycalendarview.dateWen,
            daycalendarview.dateThur, daycalendarview.dateFri,
            daycalendarview.dateSat)
        val dayText = arrayOf(daycalendarview.textSun,daycalendarview.textMon,
            daycalendarview.textTue, daycalendarview.textWen,
            daycalendarview.textThur, daycalendarview.textFri,
            daycalendarview.textSat
        )
        val timeRow = arrayOf(daycalendarview.time0, daycalendarview.time1, daycalendarview.time2,
            daycalendarview.time3, daycalendarview.time4, daycalendarview.time5,
            daycalendarview.time6, daycalendarview.time7, daycalendarview.time8,
            daycalendarview.time9, daycalendarview.time10, daycalendarview.time11,
            daycalendarview.time12, daycalendarview.time13, daycalendarview.time14,
            daycalendarview.time15, daycalendarview.time16, daycalendarview.time17,
            daycalendarview.time18, daycalendarview.time19, daycalendarview.time20,
            daycalendarview.time21, daycalendarview.time22, daycalendarview.time23)

        val currentDOW = cal.get(Calendar.DAY_OF_WEEK)

        if(currentYear%4!=0) currentLastDayOfMonth = leapYearLastDayOfMonth[currentMonth]

        for (i in 0..6) {
            dayText[i].setBackgroundColor(Color.WHITE)
        } // 요일 배경색 초기화

        for (i in 0..23) {
            timeRow[i].setBackgroundColor(Color.WHITE)
        } // 시간 배경색 초기화

        dayText[currentDOW-1].setBackgroundColor(Color.YELLOW) // current 요일 배경색 설정

        timeRow[currentTime].setBackgroundColor(Color.YELLOW) // current 시간 배경색 설정

        for(i in 1..7) {
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

    fun setPreDay() {
        cal.add(Calendar.DATE,-1)
        currentYear = cal.get(Calendar.YEAR)
        currentMonth = cal.get(Calendar.MONTH)
        currentWOM = cal.get(Calendar.WEEK_OF_MONTH)
        currentDate = cal.get(Calendar.DATE)
        currentDOW = cal.get(Calendar.DAY_OF_WEEK)
    }

    fun setNextDay() {
        cal.add(Calendar.DATE,+1)
        currentYear = cal.get(Calendar.YEAR)
        currentMonth = cal.get(Calendar.MONTH)
        currentWOM = cal.get(Calendar.WEEK_OF_MONTH)
        currentDate = cal.get(Calendar.DATE)
        currentDOW = cal.get(Calendar.DAY_OF_WEEK)
    }

    public fun setCurrent(year: Int, month: Int, date: Int){
        currentYear = year
        currentMonth = month
        currentDate = date
    }

    @IgnoreExtraProperties

    data class Schedule(
        var alarm: String? = "",
        var endTime: String?="",
        var startTime: String?="",
        var scheduleInfo: String?="",
        var shareAble: Boolean? = true,
        var shareEditAble: Boolean? = false,
        var dateYear: Int? = 0,
        var dateMonth: Int? = 0,
        var date: Int? = 0
    )

    fun insertSchedule(userName:String, tag: String,scheduleName: String, alarm: String, endTime: String,
                       startTime:String, scheduleInfo:String?, shareAble:Boolean?,shareEditAble:Boolean?,dateYear:Int, dateMonth:Int, date:Int){
        val schedule = Schedule(alarm,endTime,startTime, scheduleInfo, shareAble, shareEditAble, dateYear, dateMonth, date)
        databaseReference.child("Users").child("UserId").child("tag").child(scheduleName).setValue(schedule)
    }

    fun setScheduleOnCalendar(dataSnapshot: DataSnapshot){
        val startTime = dataSnapshot.child("startTime").value
        val endTime = dataSnapshot.child("endTime").value
        val scheduleName= dataSnapshot.child("scheduleInfo").value
        var count = startTime.toString().toInt()
        var idFromTime = resources.getIdentifier("thur"+startTime.toString(),"id", context.packageName)
        var view = findViewById<TextView>(idFromTime)
        cal.set(dataSnapshot.child("dateYear").value.toString().toInt(), dataSnapshot.child("dateMonth").value.toString().toInt(),dataSnapshot.child("date").value.toString().toInt())
        //var dOW = dateToDOW()
        view.text= scheduleName.toString()
        while(count < endTime.toString().toInt())
        {
            idFromTime = resources.getIdentifier("day"+count,"id", context.packageName)
            view = findViewById<TextView>(idFromTime)
            view.setBackgroundColor(Color.CYAN)
            if(count%100==0)
                count+=30
            else
                count+=70
        }
    }
    /*
    fun dateToDOW():String{
        val DOW: Int = cal.get(Calendar.DAY_OF_WEEK)
        if(DOW==0)
            return "sun"
        else if(DOW==1)
            return "mon"
        else if(DOW==2)
            return "tue"
        else if(DOW==3)
            return "wen"
        else if(DOW==4)
            return "thur"
        else if(DOW==5)
            return "fri"
        else if(DOW==6)
            return "sat"
        else
            return " "
    }

     */
}

