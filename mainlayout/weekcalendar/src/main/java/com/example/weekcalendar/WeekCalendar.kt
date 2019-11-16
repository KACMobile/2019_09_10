package com.example.weekcalendar

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
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
    : ConstraintLayout(context,attrs,defStyleAttr) {
    var cal: Calendar = Calendar.getInstance()
    val database = FirebaseDatabase.getInstance()
    val databaseReference = database.reference

    val UserId: String = "User02"

    val lastDayOfMonth = arrayOf(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)
    val leapYearLastDayOfMonth = arrayOf(31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)
    val todayMonth = cal.get(Calendar.MONTH)
    val todayDOW = cal.get(Calendar.DAY_OF_WEEK)
    val todayDate = cal.get(Calendar.DATE)
    val thisYear = cal.get(Calendar.YEAR)
    val thisWOM = cal.get(Calendar.WEEK_OF_MONTH)
    var currentYear = cal.get(Calendar.YEAR)
    var currentMonth = cal.get(Calendar.MONTH)
    var currentWOM = cal.get(Calendar.WEEK_OF_MONTH)
    var currentDate = cal.get(Calendar.DATE)
    var changedCell= arrayListOf<TextView>()
    lateinit var saveDataSnap: DataSnapshot //DataSnapshot을 받으면 set함

    init {
        LayoutInflater.from(context).inflate(R.layout.weekcalendar, this, true)
        calendardefaultsetting()

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


        })

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
        /*insertSchedule(
            UserId, "할 일", "1", "false", "400", "200", "Mob",
            false, false, 2019, 11, 3
        )
        insertSchedule(
            UserId, "할 일", "2", "false", "400", "100", "test",
            false, false, 2019, 11, 5
        )
        insertSchedule(
            UserId, "할 일", "3", "false", "400", "100", "test",
            false, false, 2019, 11, 13
        )*/


    }

    //달력 기본 설정
    fun calendardefaultsetting() {
        var currentLastDayOfMonth = cal.getActualMaximum(Calendar.DATE)
        var currentTime = cal.get(Calendar.HOUR_OF_DAY)
        val dateArray = arrayOf(
            weekcalendarview.dateSun, weekcalendarview.dateMon,
            weekcalendarview.dateTue, weekcalendarview.dateWen,
            weekcalendarview.dateThur, weekcalendarview.dateFri,
            weekcalendarview.dateSat
        )
        val dayText = arrayOf(
            weekcalendarview.textSun, weekcalendarview.textMon,
            weekcalendarview.textTue, weekcalendarview.textWen,
            weekcalendarview.textThur, weekcalendarview.textFri,
            weekcalendarview.textSat
        )
        val timeRow = arrayOf(
            weekcalendarview.time0, weekcalendarview.time1, weekcalendarview.time2,
            weekcalendarview.time3, weekcalendarview.time4, weekcalendarview.time5,
            weekcalendarview.time6, weekcalendarview.time7, weekcalendarview.time8,
            weekcalendarview.time9, weekcalendarview.time10, weekcalendarview.time11,
            weekcalendarview.time12, weekcalendarview.time13, weekcalendarview.time14,
            weekcalendarview.time15, weekcalendarview.time16, weekcalendarview.time17,
            weekcalendarview.time18, weekcalendarview.time19, weekcalendarview.time20,
            weekcalendarview.time21, weekcalendarview.time22, weekcalendarview.time23
        )

        val currentDOW = cal.get(Calendar.DAY_OF_WEEK)

        if (currentYear % 4 != 0) currentLastDayOfMonth = leapYearLastDayOfMonth[currentMonth]


        if (currentMonth == todayMonth && thisYear == currentYear && thisWOM ==currentWOM)
            dayText[todayDOW - 1].setBackgroundColor(Color.YELLOW)
        else
            dayText[todayDOW - 1].setBackgroundColor(Color.WHITE)
        timeRow[currentTime].setBackgroundColor(Color.YELLOW)


        for (i in 1..7) {
            var date = currentDate - currentDOW + i

            if (date > currentLastDayOfMonth)
                dateArray[i - 1].text = (date - currentLastDayOfMonth).toString()
            else if (date < 1)
                if (currentYear % 4 == 0) dateArray[i - 1].text =
                    (leapYearLastDayOfMonth[currentMonth - 1] - abs(date)).toString()
                else dateArray[i - 1].text =
                    (lastDayOfMonth[currentMonth - 1] - abs(date)).toString()
            else
                dateArray[i - 1].text = date.toString()
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

    fun setPreWeek() {
        cal.add(Calendar.DATE, -7)
        currentYear = cal.get(Calendar.YEAR)
        currentMonth = cal.get(Calendar.MONTH)
        currentWOM = cal.get(Calendar.WEEK_OF_MONTH)
        currentDate = cal.get(Calendar.DATE)
    }

    fun setNextWeek() {
        cal.add(Calendar.DATE, +7)
        currentYear = cal.get(Calendar.YEAR)
        currentMonth = cal.get(Calendar.MONTH)
        currentWOM = cal.get(Calendar.WEEK_OF_MONTH)
        currentDate = cal.get(Calendar.DATE)
    }

    public fun setCurrent(year: Int, month: Int, date: Int) {
        currentYear = year
        currentMonth = month
        currentDate = date
    }

    @IgnoreExtraProperties

    data class Schedule(
        var alarm: String? = "",
        var endTime: String? = "",
        var startTime: String? = "",
        var scheduleInfo: String? = "",
        var shareAble: Boolean? = true,
        var shareEditAble: Boolean? = false,
        var dateYear: Int? = 0,
        var dateMonth: Int? = 0,
        var date: Int? = 0

    )

    /////////////////////////
    //스케줄추가(임시)
    public fun setDataSnapShot(dataSnapshot: DataSnapshot){
        saveDataSnap = dataSnapshot
        setScheduleOnCalendar(saveDataSnap)
    }
    /*
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
        databaseReference.child("Users").child(userName).child(tag).child(scheduleName).setValue(schedule)


    }
    */


    public fun setScheduleOnCalendar(dataSnapshot: DataSnapshot) {
        val dateArray = arrayOf(
            weekcalendarview.dateMon,
            weekcalendarview.dateTue, weekcalendarview.dateWen,
            weekcalendarview.dateThur, weekcalendarview.dateFri,
            weekcalendarview.dateSat, weekcalendarview.dateSun
        )
        val startTime = dataSnapshot.child("startTime").value
        val endTime = dataSnapshot.child("endTime").value
        val scheduleName = dataSnapshot.key
        var count = startTime.toString().toInt()
        /*val yearData = dataSnapshot.child("dateYear").value.toString().toInt()
        val monthData = dataSnapshot.child("dateMonth").value.toString().toInt() - 1
        val dateData =dataSnapshot.child("date").value.toString().toInt() - 1*/

        cal.set(
            dataSnapshot.child("dateYear").value.toString().toInt(),
            dataSnapshot.child("dateMonth").value.toString().toInt() - 1,
            dataSnapshot.child("date").value.toString().toInt() - 1
        )
        var dOW = dateToDOW()
        var idFromTime = resources.getIdentifier(dOW + count, "id", context.packageName)
        var view = findViewById<TextView>(idFromTime)
        if (dateArray[cal.get(Calendar.DAY_OF_WEEK)-1].text == (cal.get(Calendar.DATE) + 1).toString()) {
            view.text = scheduleName.toString()
            while (count < endTime.toString().toInt()) {
                idFromTime = resources.getIdentifier(dOW + count, "id", context.packageName)
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

    fun dateToDOW(): String {
        val DOW: Int = cal.get(Calendar.DAY_OF_WEEK)
        if (DOW == 7)
            return "sun"
        else if (DOW == 1)
            return "mon"
        else if (DOW == 2)
            return "tue"
        else if (DOW == 3)
            return "wen"
        else if (DOW == 4)
            return "thur"
        else if (DOW == 5)
            return "fri"
        else if (DOW == 6)
            return "sat"
        else
            return " "


    }
}



