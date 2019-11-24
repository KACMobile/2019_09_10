package com.example.daycalendar

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.android.synthetic.main.daycalendar.view.*
import com.google.firebase.database.*


import java.util.*
import kotlin.math.abs


class DayCalendar @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0 )
    : ConstraintLayout(context,attrs,defStyleAttr)
{
    var cal: Calendar = Calendar.getInstance()
    val database = FirebaseDatabase.getInstance()
    val databaseReference = database.reference

    val userID:String= "User01"

    val lastDayOfMonth = arrayOf(31,28,31,30,31,30,31,31,30,31,30,31)
    val leapYearLastDayOfMonth = arrayOf(31,29,31,30,31,30,31,31,30,31,30,31)
    var currentYear = cal.get(Calendar.YEAR)
    var currentMonth = cal.get(Calendar.MONTH)
    var currentWOM = cal.get(Calendar.WEEK_OF_MONTH)
    var currentDate = cal.get(Calendar.DATE)
    var currentDOW = cal.get(Calendar.DAY_OF_WEEK)

    var changedCell= arrayListOf<TextView>()
    lateinit var saveDataSnap: DataSnapshot //DataSnapshot을 받으면 set함
    var followListSnapshot = arrayListOf<DataSnapshot>() //followList DataSnapshot을 받으면 add함

    init {
        LayoutInflater.from(context).inflate(R.layout.daycalendar,this,true)
        val scheduleColorPreference = context.getSharedPreferences("ScheduleColorInfo", Context.MODE_PRIVATE)
        calendardefaultsetting()
        /*
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

         */

        val userDB = databaseReference.child("Users/" + userID + "/Schedule")
        userDB.addValueEventListener( object: ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                saveDataSnap = dataSnapshot
                for(snapShot in dataSnapshot.children){
                    for(deeperSnapShot in snapShot.child((currentMonth+1).toString()).children){
                        setScheduleOnCalendar(deeperSnapShot.value as HashMap<String, Any>,Color.CYAN)

                    }
                }
            }
            override fun onCancelled(dataSnapshot: DatabaseError) {
            }
        })
        //follow추가
        val userfollow = databaseReference.child("Users/" + userID + "/Follow")
        userfollow.addValueEventListener( object: ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot in dataSnapshot.children) {
                    if (snapshot.key.toString() == "Groups") {
                        for (deeperSnapshot in snapshot.children) {
                            val groupBackgroundColor = deeperSnapshot.value.toString().toInt()
                            val groupDB = databaseReference.child("Groups/" + deeperSnapshot.key.toString()+ "/Schedule")
                            val editor = scheduleColorPreference.edit()
                            editor.putInt(deeperSnapshot.key.toString(), groupBackgroundColor)
                            editor.commit()
                            groupDB.addValueEventListener(object : ValueEventListener {
                                override fun onDataChange(groupDataSnapshot: DataSnapshot) {
                                    followListSnapshot.add(groupDataSnapshot)

                                    for (deeperSnapShot in groupDataSnapshot.child((currentMonth + 1).toString()).children) {
                                        setScheduleOnCalendar(deeperSnapShot.value as HashMap<String, Any>,groupBackgroundColor)

                                    }


                                }

                                override fun onCancelled(groupDataSnapshot: DatabaseError) {

                                }

                            }
                            )

                        }
                    }
                    if (snapshot.key.toString() == "Users"){
                        for (deeperSnapshot in snapshot.children) {
                            val groupBackgroundColor = deeperSnapshot.value.toString().toInt()
                            val groupDB = databaseReference.child("Users/" + deeperSnapshot.key.toString()+"/Schedule")
                            val editor = scheduleColorPreference.edit()
                            editor.putInt(deeperSnapshot.key.toString(), groupBackgroundColor)
                            editor.commit()
                            groupDB.addValueEventListener(object : ValueEventListener {
                                override fun onDataChange(groupDataSnapshot: DataSnapshot) {

                                    for(deeperSnapshot in groupDataSnapshot.children) {
                                        followListSnapshot.add(deeperSnapshot)
                                        for (deepestSnapshot in deeperSnapshot.child((currentMonth + 1).toString()).children) {
                                            setScheduleOnCalendar(deepestSnapshot.value as HashMap<String, Any>, groupBackgroundColor)

                                        }
                                    }


                                }

                                override fun onCancelled(groupDataSnapshot: DatabaseError) {

                                }

                            }
                            )

                        }
                    }
                }

            }
            override fun onCancelled(dataSnapshot: DatabaseError) {
            }
        })


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
            i.text = null
            i.setBackgroundColor(Color.WHITE)
        }

        changedCell.clear()

        if(::saveDataSnap.isInitialized) {
            for (snapShot in saveDataSnap.children) {
                for (deeperSnapShot in snapShot.child((currentMonth+1).toString()).children) {
                    setScheduleOnCalendar(deeperSnapShot.value as HashMap<String, Any>,Color.CYAN)
                }
            }
        }
        var groupBackgroundColor = Color.RED
        for(snapShot in followListSnapshot){
            val scheduleColorPreference = context.getSharedPreferences("ScheduleColorInfo", Context.MODE_PRIVATE)
            for (deeperSnapShot in snapShot.child((currentMonth + 1).toString()).children) {
                groupBackgroundColor = scheduleColorPreference.getInt(deeperSnapShot.child("userID").value.toString(), Color.BLUE)
                setScheduleOnCalendar(deeperSnapShot.value as HashMap<String, Any>,groupBackgroundColor)
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
        databaseReference.child("Users").child(userID).child(tag).child(scheduleName).setValue(schedule)
    }

     */

    fun setScheduleOnCalendar(schedule: HashMap<String, Any>,color:Int){
        val scheduleName = schedule.get("scheduleName").toString()
        val scheduleInfo= schedule.get("scheduleInfo").toString()
        val startTime = schedule.get("startTime").toString()
        val endTime = schedule.get("endTime").toString()
        val dateYear = schedule.get("dateYear").toString().toInt()
        val dateMonth = schedule.get("dateMonth").toString().toInt()
        val date = schedule.get("date").toString().toInt()


        var count = startTime.toInt()
        val schedulePeriod = endTime.toInt()-startTime.toInt()
        var idFromTime = resources.getIdentifier("day" + count, "id", context.packageName)
        var view = findViewById<TextView>(idFromTime)
        if (currentDate.toString() == date.toString() && (currentMonth + 1).toString() == dateMonth.toString() && currentYear.toString() == dateYear.toString()) {
            while (count < endTime.toInt()) {
                idFromTime = resources.getIdentifier("day" + count, "id", context.packageName)
                view = findViewById<TextView>(idFromTime)
                if(schedulePeriod <= 30) view.text = scheduleName + scheduleInfo
                else if (count == startTime.toInt()) view.text = scheduleName
                else if(count == (startTime.toInt() + 30)) view.text = scheduleInfo
                view.setBackgroundColor(color)
                changedCell.add(view)
                if (count % 100 == 0){
                    count += 30
                }
                else
                    count += 70
            }
        }

        cal.set(currentYear,currentMonth,currentDate)
    }

}

