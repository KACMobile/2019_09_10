package com.example.daycalendar

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
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

    val UserId:String= "User_daytest"

    val lastDayOfMonth = arrayOf(31,28,31,30,31,30,31,31,30,31,30,31)
    val leapYearLastDayOfMonth = arrayOf(31,29,31,30,31,30,31,31,30,31,30,31)
    var currentYear = cal.get(Calendar.YEAR)
    var currentMonth = cal.get(Calendar.MONTH)
    var currentWOM = cal.get(Calendar.WEEK_OF_MONTH)
    var currentDate = cal.get(Calendar.DATE)
    var currentDOW = cal.get(Calendar.DAY_OF_WEEK)

    var changedCell= arrayListOf<TextView>()
    lateinit var saveDataSnap: DataSnapshot //DataSnapshot을 받으면 set함

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

        val userDB = databaseReference.child("Users/" + UserId)
        userDB.addValueEventListener( object: ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                saveDataSnap = dataSnapshot
                for(snapShot in dataSnapshot.children){
                    for(deeperSnapShot in snapShot.children){
                        setScheduleOnCalendar(deeperSnapShot)
                    }
                }


            }
            override fun onCancelled(dataSnapshot: DatabaseError) {
            }
        })


        insertSchedule(
            UserId, "할 일", "서양철학의 이해", "false", "1500", "1800", "과학관 115",
            false, false, 2019, 11, 4
        )
        insertSchedule(
            UserId, "할 일", "컴퓨터 구조", "false", "1030", "1200", "과학관 110",
            false, false, 2019, 11, 4
        )
        insertSchedule(
            UserId, "할 일", "모바일 SW", "false", "900", "1300", "전자관 420",
            false, false, 2019, 11, 5
        )


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

        val dateArray = arrayOf(
            daycalendarview.dateSun,daycalendarview.dateMon,
            daycalendarview.dateTue, daycalendarview.dateWen,
            daycalendarview.dateThur, daycalendarview.dateFri,
            daycalendarview.dateSat)

        val dayText = arrayOf(
            daycalendarview.textSun,daycalendarview.textMon,
            daycalendarview.textTue, daycalendarview.textWen,
            daycalendarview.textThur, daycalendarview.textFri,
            daycalendarview.textSat
        )

        val timeRow = arrayOf(
            daycalendarview.time0, daycalendarview.time1, daycalendarview.time2,
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

        for(i in changedCell){
            i.text= null
            i.setBackgroundColor(Color.WHITE)
        }

        changedCell.clear()
        if(::saveDataSnap.isInitialized) {
            for (snapShot in saveDataSnap.children) {
                for (deeperSnapShot in snapShot.children) {
                    setScheduleOnCalendar(deeperSnapShot)
                }
            }
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

    fun insertSchedule(
        userName: String,
        tag: String,
        scheduleName: String,
        alarm: String,
        endTime: String,
        startTime: String,
        scheduleInfo: String?,
        shareAble: Boolean?,
        shareEditAble: Boolean?,
        dateYear: Int,
        dateMonth: Int,
        date: Int
    ) {
        val databaseReference = database.reference
        val schedule = Schedule(
            alarm,
            endTime,
            startTime,
            scheduleInfo,
            shareAble,
            shareEditAble,
            dateYear,
            dateMonth,
            date
        )
        databaseReference.child("Users").child(UserId).child(tag).child(scheduleName).setValue(schedule)
    }

    fun setScheduleOnCalendar(dataSnapshot: DataSnapshot){
        val dateArray = arrayOf(
            daycalendarview.dateMon,
            daycalendarview.dateTue, daycalendarview.dateWen,
            daycalendarview.dateThur, daycalendarview.dateFri,
            daycalendarview.dateSat, daycalendarview.dateSun
        )

        val startTime = dataSnapshot.child("startTime").value
        val endTime = dataSnapshot.child("endTime").value
        val scheduleInfo= dataSnapshot.child("scheduleInfo").value
        val date = dataSnapshot.child("date").value
        val dateMonth = dataSnapshot.child("dateMonth").value
        val dateYear = dataSnapshot.child("dateYear").value
        var count = startTime.toString().toInt()

        cal.set(
            dataSnapshot.child("dateYear").value.toString().toInt(),
            dataSnapshot.child("dateMonth").value.toString().toInt() - 1,
            dataSnapshot.child("date").value.toString().toInt() - 1
        )

        var idFromTime = resources.getIdentifier("day" + count, "id", context.packageName)
        var view = findViewById<TextView>(idFromTime)
        if(currentDate.toString() == date.toString() && (currentMonth + 1).toString() == dateMonth.toString() && currentYear.toString() == dateYear.toString()) {
            view.text = scheduleInfo.toString()
            while (count < endTime.toString().toInt()) {
                idFromTime = resources.getIdentifier("day" + count, "id", context.packageName)
                view = findViewById<TextView>(idFromTime)
                view.setBackgroundColor(Color.CYAN)
                changedCell.add(view)
                if (count % 100 == 0)
                    count += 30
                else
                    count += 70
            }
        }

        cal.set(currentYear,currentMonth,currentDate)
    }

}

