package com.example.daycalendar

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.icu.util.ChineseCalendar
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.android.synthetic.main.daycalendar.view.*
import com.google.firebase.database.*


import java.util.*
import kotlin.math.abs

//일별 달력 - 조성완, 황선혁
class DayCalendar @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0 )
    : ConstraintLayout(context,attrs,defStyleAttr)
{
    var cal: Calendar = Calendar.getInstance()
    var cc = ChineseCalendar()

    val database = FirebaseDatabase.getInstance()
    val databaseReference = database.reference

    var userID:String = "User01"
    var checkSP: SharedPreferences


    val lastDayOfMonth = arrayOf(31,28,31,30,31,30,31,31,30,31,30,31)
    val leapYearLastDayOfMonth = arrayOf(31,29,31,30,31,30,31,31,30,31,30,31)
    var currentYear = cal.get(Calendar.YEAR)
    var currentMonth = cal.get(Calendar.MONTH)
    var currentWOM = cal.get(Calendar.WEEK_OF_MONTH)
    var currentDate = cal.get(Calendar.DATE)
    var currentDOW = cal.get(Calendar.DAY_OF_WEEK)

    val hollydays = arrayOf(hollyday(1,1, "?��?��", false), hollyday(1, 1, "?��?��", true),
        hollyday(3, 1, "?��?��?��", false), hollyday(5, 5, "?��린이?��", false),
        hollyday(4, 8, "�?처님 ?��?��?��", true),
        hollyday(6, 6, "?��충일", false), hollyday(8, 15, "광복?��", false),
        hollyday(8, 15, "추석", true), hollyday(10, 3, "개천?��", false),
        hollyday(10, 9, "?���??��", false), hollyday(12, 25, "?��리스마스", false))

    var changedCell= arrayListOf<TextView>()
    lateinit var saveDataSnap: DataSnapshot //DataSnapshot?�� 받으�? set?��
    var followListSnapshot = arrayListOf<DataSnapshot>() //followList DataSnapshot?�� 받으�? add?��

    init {
        val idPreference = context.getSharedPreferences("UserID", Context.MODE_PRIVATE)
        userID = idPreference.getString("UserID", "User01")!!
        LayoutInflater.from(context).inflate(R.layout.daycalendar,this,true)
        val scheduleColorPreference = context.getSharedPreferences("ScheduleColorInfo", Context.MODE_PRIVATE)
        calendardefaultsetting()
        checkSP = context.getSharedPreferences("Check", Context.MODE_PRIVATE)

        //내 일정추가
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
        //follow 일정추가
        val userfollow = databaseReference.child("Users/" + userID + "/Follow")
        userfollow.addValueEventListener( object: ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot in dataSnapshot.children) {
                    if (snapshot.key.toString() == "Groups") {
                        for (deeperSnapshot in snapshot.children) {
                            var isChecked = checkSP.getBoolean(deeperSnapshot.key.toString(), true)
                            if (isChecked == true) {
                                val groupBackgroundColor = deeperSnapshot.value.toString().toInt()
                                val groupDB =
                                    databaseReference.child("Groups/" + deeperSnapshot.key.toString() + "/Schedule")
                                val editor = scheduleColorPreference.edit()
                                editor.putInt(deeperSnapshot.key.toString(), groupBackgroundColor)
                                editor.commit()
                                groupDB.addValueEventListener(object : ValueEventListener {
                                    override fun onDataChange(groupDataSnapshot: DataSnapshot) {
                                        followListSnapshot.add(groupDataSnapshot)

                                        for (deeperSnapShot in groupDataSnapshot.child((currentMonth + 1).toString()).children) {
                                            setScheduleOnCalendar(
                                                deeperSnapShot.value as HashMap<String, Any>,
                                                groupBackgroundColor
                                            )

                                        }


                                    }

                                    override fun onCancelled(groupDataSnapshot: DatabaseError) {

                                    }

                                }
                                )

                            }
                        }
                    }
                    if (snapshot.key.toString() == "Users"){
                        for (deeperSnapshot in snapshot.children) {
                            var isChecked = checkSP.getBoolean(deeperSnapshot.key.toString(), true)
                            if (isChecked == true) {
                                Log.d("a", "This is here?")
                                val groupBackgroundColor = deeperSnapshot.value.toString().toInt()
                                val groupDB =
                                    databaseReference.child("Users/" + deeperSnapshot.key.toString() + "/Schedule")
                                val editor = scheduleColorPreference.edit()
                                editor.putInt(deeperSnapshot.key.toString(), groupBackgroundColor)
                                editor.commit()
                                groupDB.addValueEventListener(object : ValueEventListener {
                                    override fun onDataChange(groupDataSnapshot: DataSnapshot) {

                                        for (deeperSnapshot in groupDataSnapshot.children) {
                                            followListSnapshot.add(deeperSnapshot)
                                            for (deepestSnapshot in deeperSnapshot.child((currentMonth + 1).toString()).children) {
                                                setScheduleOnCalendar(
                                                    deepestSnapshot.value as HashMap<String, Any>,
                                                    groupBackgroundColor
                                                )

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

            }
            override fun onCancelled(dataSnapshot: DatabaseError) {
            }
        })

        //스와이프 - 외부코드
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
        checkSP.registerOnSharedPreferenceChangeListener(object: SharedPreferences.OnSharedPreferenceChangeListener{
            override fun onSharedPreferenceChanged(p0: SharedPreferences?, p1: String?) {
                Log.d("a", "This is")
                calendardefaultsetting()
            }
        })
    }
    // 달력 초기화
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
            dateArray[i].setBackgroundColor(Color.WHITE)
        }

        for (i in 0..23) {
            timeRow[i].setBackgroundColor(Color.WHITE)
        }

        dayText[currentDOW-1].setBackgroundColor(Color.YELLOW)

        timeRow[currentTime].setBackgroundColor(Color.YELLOW)

        dateArray[0].setTextColor(Color.RED)

        for(i in 1..7) {
            var date = currentDate - currentDOW  + i

            for(j in hollydays){
                if(j.islunar == false){
                    if(date>currentLastDayOfMonth) {
                        if(currentMonth==11){
                            dateArray[i - 1].text = (date - currentLastDayOfMonth).toString()
                            if(j.holydate.toString() == (dateArray[i-1].text) && j.holymonth == 1) {
                                dateArray[i - 1].setBackgroundColor(Color.RED)
                                if(i==1) dateArray[i-1].setTextColor(Color.BLACK)
                            }
                        }else{
                            dateArray[i - 1].text = (date - currentLastDayOfMonth).toString()
                            if(j.holydate.toString() == (dateArray[i-1].text) && j.holymonth == currentMonth+2) {
                                dateArray[i - 1].setBackgroundColor(Color.RED)
                                if(i==1) dateArray[i-1].setTextColor(Color.BLACK)
                            }
                        }

                    }
                    else if(date<1)
                        if(currentYear%4 == 0) {
                            if(currentMonth==0){
                                dateArray[i - 1].text = (leapYearLastDayOfMonth[11] - abs(date)).toString()
                                if(j.holydate.toString() == (dateArray[i-1].text) && j.holymonth == currentMonth){
                                    dateArray[i - 1].setBackgroundColor(Color.RED)
                                    if(i==1) dateArray[i-1].setTextColor(Color.BLACK)
                                }
                            }else{
                                dateArray[i - 1].text = (leapYearLastDayOfMonth[currentMonth - 1] - abs(date)).toString()
                                if(j.holydate.toString() == (dateArray[i-1].text) && j.holymonth == currentMonth){
                                    dateArray[i - 1].setBackgroundColor(Color.RED)
                                    if(i==1) dateArray[i-1].setTextColor(Color.BLACK)
                                }
                            }

                        }
                        else {
                            if(currentMonth==0){
                                dateArray[i-1].text = (lastDayOfMonth[11] - abs(date) ).toString()
                                if(j.holydate.toString() == (dateArray[i-1].text) && j.holymonth == currentMonth){
                                    dateArray[i - 1].setBackgroundColor(Color.RED)
                                    if(i==1) dateArray[i-1].setTextColor(Color.BLACK)
                                }
                            }else{
                                dateArray[i-1].text = (lastDayOfMonth[currentMonth-1] - abs(date) ).toString()
                                if(j.holydate.toString() == (dateArray[i-1].text) && j.holymonth == currentMonth){
                                    dateArray[i - 1].setBackgroundColor(Color.RED)
                                    if(i==1) dateArray[i-1].setTextColor(Color.BLACK)
                                }
                            }

                        }
                    else {
                        dateArray[i-1].text =  date.toString()
                        if(j.holydate.toString() == (dateArray[i-1].text) && j.holymonth == currentMonth+1){
                            dateArray[i - 1].setBackgroundColor(Color.RED)
                            if(i==1) dateArray[i-1].setTextColor(Color.BLACK)
                        }
                    }
                }else{
                    var suncal = Calendar.getInstance()
                    suncal.setTimeInMillis(cc.timeInMillis)

                    cc.set( ChineseCalendar.EXTENDED_YEAR, currentYear + 2637);
                    cc.set( ChineseCalendar.MONTH, j.holymonth - 1);
                    cc.set( ChineseCalendar.DAY_OF_MONTH,  j.holydate);

                    val ccdate = suncal.get(Calendar.DAY_OF_MONTH)
                    val ccmonth = suncal.get(Calendar.MONTH)+1
                    val ccyear = suncal.get(Calendar.YEAR)

                    if(ccyear == currentYear){
                        if(ccdate>currentLastDayOfMonth) {
                            if(currentMonth==11){
                                dateArray[i - 1].text = (ccdate - currentLastDayOfMonth).toString()
                                if(ccdate.toString() == (dateArray[i-1].text) && ccmonth == currentMonth+2 && ccyear == currentYear) {
                                    dateArray[i - 1].setBackgroundColor(Color.RED)
                                    if(i==1) dateArray[i-1].setTextColor(Color.BLACK)
                                }
                            }else{
                                dateArray[i - 1].text = (ccdate - currentLastDayOfMonth).toString()
                                if(ccdate.toString() == (dateArray[i-1].text) && ccmonth == currentMonth+2 && ccyear == currentYear) {
                                    dateArray[i - 1].setBackgroundColor(Color.RED)
                                    if(i==1) dateArray[i-1].setTextColor(Color.BLACK)
                                }
                            }

                        }
                        else if(date<1)
                            if(currentYear%4 == 0) {
                                if(currentMonth==0){
                                    dateArray[i - 1].text = (leapYearLastDayOfMonth[11] - abs(date)).toString()
                                    if(ccdate.toString() == (dateArray[i-1].text) && ccmonth == currentMonth && ccyear == currentYear){
                                        dateArray[i - 1].setBackgroundColor(Color.RED)
                                        if(i==1) dateArray[i-1].setTextColor(Color.BLACK)
                                    }
                                }else{
                                    dateArray[i - 1].text = (leapYearLastDayOfMonth[currentMonth - 1] - abs(date)).toString()
                                    if(ccdate.toString() == (dateArray[i-1].text) && ccmonth == currentMonth && ccyear == currentYear){
                                        dateArray[i - 1].setBackgroundColor(Color.RED)
                                        if(i==1) dateArray[i-1].setTextColor(Color.BLACK)
                                    }
                                }

                            }
                            else {
                                if(currentMonth==0) {
                                    dateArray[i - 1].text =
                                        (lastDayOfMonth[11] - abs(date)).toString()
                                    if (ccdate.toString() == (dateArray[i - 1].text) && ccmonth == currentMonth && ccyear == currentYear) {
                                        dateArray[i - 1].setBackgroundColor(Color.RED)
                                        if (i == 1) dateArray[i - 1].setTextColor(Color.BLACK)
                                    }
                                }else{
                                    dateArray[i-1].text = (lastDayOfMonth[currentMonth-1] - abs(date) ).toString()
                                    if(ccdate.toString() == (dateArray[i-1].text) && ccmonth == currentMonth && ccyear == currentYear){
                                        dateArray[i - 1].setBackgroundColor(Color.RED)
                                        if(i==1) dateArray[i-1].setTextColor(Color.BLACK)
                                    }
                                }

                            }
                        else {
                            dateArray[i-1].text =  date.toString()
                            if(ccdate.toString() == (dateArray[i-1].text) && ccmonth == currentMonth+1 && ccyear == currentYear){
                                dateArray[i - 1].setBackgroundColor(Color.RED)
                                if(i==1) dateArray[i-1].setTextColor(Color.BLACK)
                            }
                        }
                    }
                }



            }


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

                var isChecked = checkSP.getBoolean(deeperSnapShot.child("userID").value.toString(), true)
                if(isChecked == true) {
                    groupBackgroundColor = scheduleColorPreference.getInt(
                        deeperSnapShot.child("userID").value.toString(),
                        Color.BLUE
                    )
                    setScheduleOnCalendar(
                        deeperSnapShot.value as HashMap<String, Any>,
                        groupBackgroundColor
                    )
                }
            }
        }

    }
    // 전날, 다음날
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

    //스케줄 표기
    fun setScheduleOnCalendar(schedule: HashMap<String, Any>,color:Int){
        val scheduleName = schedule.get("scheduleName").toString()
        val scheduleInfo= schedule.get("scheduleInfo").toString()
        val startTime = schedule.get("startTime").toString()
        val endTime = schedule.get("endTime").toString()
        val dateYear = schedule.get("dateYear").toString().toInt()
        val dateMonth = schedule.get("dateMonth").toString().toInt()
        val date = schedule.get("date").toString().toInt()
        Log.d("a", "This is here?" + scheduleName)

        var count = startTime.toInt()
        val schedulePeriod = endTime.toInt()-startTime.toInt()
        var idFromTime = resources.getIdentifier("day" + count, "id", context.packageName)
        var view = findViewById<TextView>(idFromTime)
        if (currentDate.toString() == date.toString() && (currentMonth + 1).toString() == dateMonth.toString() && currentYear.toString() == dateYear.toString()) {
            while (count < endTime.toInt()) {
                if(count == 0)
                    idFromTime = resources.getIdentifier("day" + "000", "id", context.packageName)
                else if(count == 30)
                    idFromTime = resources.getIdentifier("day" + "030", "id", context.packageName)
                else
                    idFromTime = resources.getIdentifier("day" + count, "id", context.packageName)
                view = findViewById<TextView>(idFromTime)
                Log.d("a", "This is!?" +count)
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

