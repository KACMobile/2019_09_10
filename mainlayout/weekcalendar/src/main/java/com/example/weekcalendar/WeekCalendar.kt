package com.example.weekcalendar

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.weekcalendar.*
import kotlinx.android.synthetic.main.weekcalendar.view.*


import org.w3c.dom.Attr
import org.w3c.dom.Text
import java.time.Month
import java.time.Year
import java.util.*
import kotlin.math.abs


class WeekCalendar @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0 )
    : ConstraintLayout(context,attrs,defStyleAttr)
{
    var cal: Calendar = Calendar.getInstance()
    val database = FirebaseDatabase.getInstance()
    val databaseReference = database.reference

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

    var scheduletext: String? = " "

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

        databaseReference.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                val dataSnapChild = dataSnapshot.child("UserId/tag/MobileProject")//그냥 경로로 임시지정 추후 수정
                setScheduleOnCalendar(dataSnapChild)

            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {

            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {

            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {

            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })

        insertSchedule("User01","할 일", "MobileProject", "false", "1600", "1530", "Mobile",
            false, false, 2019, 10,17)






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
        val textsample = findViewById<TextView>(R.id.thur000)
        textsample.text = scheduletext.toString()

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
        view.text= scheduleName.toString()
        while(count < endTime.toString().toInt())
        {
            idFromTime = resources.getIdentifier("thur"+count,"id", context.packageName)
            view = findViewById<TextView>(idFromTime)
            view.setBackgroundColor(Color.CYAN)
            if(count%100==0)
                count+=30
            else
                count+=70

        }

    }

}



