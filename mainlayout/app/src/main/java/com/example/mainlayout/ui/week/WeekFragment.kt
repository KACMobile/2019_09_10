package com.example.mainlayout.ui.week

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.example.daycalendar.OnSwipeTouchListener
import com.example.mainlayout.MainActivity
import com.example.mainlayout.R
import com.example.weekcalendar.WeekCalendar
import com.google.firebase.database.DataSnapshot
import kotlinx.android.synthetic.main.daily_calender.view.*
import kotlinx.android.synthetic.main.week_calender.*
import kotlinx.android.synthetic.main.week_calender.view.*

class WeekFragment : Fragment() {//주간 캘린더

    private lateinit var weekViewModel: WeekViewModel

    private lateinit var saveDataSnapshot: DataSnapshot

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        weekViewModel =
            ViewModelProviders.of(this).get(WeekViewModel::class.java)
        val view = inflater.inflate(R.layout.week_calender, container, false)

        var mcontext = context!!

        var yearinfo = view.week_CalendarView.currentYear
        var monthinfo = view.week_CalendarView.currentMonth + 1
        var dateinfo = view.week_CalendarView.currentDate

        var actionBar = (activity as MainActivity).supportActionBar
        actionBar!!.title = "" + yearinfo + "년 " +  monthinfo + "월 "



        view.setOnTouchListener(object : OnSwipeTouchListener(mcontext) {
            override fun onSwipeLeft() {
                super.onSwipeLeft()
                week_CalendarView.setNextWeek()
                week_CalendarView.calendardefaultsetting()

                var yearinfo = view.week_CalendarView.currentYear
                var monthinfo = view.week_CalendarView.currentMonth + 1
                var dateinfo = view.week_CalendarView.currentDate

                var actionBar = (activity as MainActivity).supportActionBar
                actionBar!!.title = "" + yearinfo + "년 " + monthinfo + "월 "
            }

            override fun onSwipeRight() {
                super.onSwipeRight()
                week_CalendarView.setPreWeek()
                week_CalendarView.calendardefaultsetting()

                var yearinfo = view.week_CalendarView.currentYear
                var monthinfo = view.week_CalendarView.currentMonth + 1
                var dateinfo = view.week_CalendarView.currentDate

                var actionBar = (activity as MainActivity).supportActionBar
                actionBar!!.title = "" + yearinfo + "년 " + monthinfo + "월 "
            }
        })

        weekViewModel.text.observe(this, Observer {
        })


        return view
    }

    //var fragmentweekCalendar = week_CalendarView

    /*if (wCalendar.get(Calendar.DATE)!=CalendarInfo.currentDate
        || wCalendar. get(Calendar.MONTH)!=CalendarInfo.currentMonth
        || wCalendar. get(Calendar.YEAR)!=CalendarInfo.currentYear)
    {
        weekCalendar.setCurrent(CalendarInfo.currentDate, CalendarInfo.currentMonth, CalendarInfo.currentYear)
    }
    */
    //week_CalendarView.setCurrent(wCalendar.get(Calendar.YEAR), wCalendar.get(Calendar.MONTH), wCalendar.get(Calendar.DATE))
    /*override fun setArguments(args: Bundle?) {
        super.setArguments(args)

        Log.d("aa","여기는 들어옴?")
        saveDataSnapshot = args!!.getSerializable("DataSnapShot") as DataSnapshot
    }

    public fun setDataSnapShot(dataSnapshot: DataSnapshot){
        saveDataSnapshot = dataSnapshot

    }*/
}