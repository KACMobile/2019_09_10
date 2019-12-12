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
import com.example.mainlayout.MainActivity
import com.example.mainlayout.R
import com.example.weekcalendar.WeekCalendar
import com.google.firebase.database.DataSnapshot
import kotlinx.android.synthetic.main.daily_calender.view.*
import kotlinx.android.synthetic.main.week_calender.*
import kotlinx.android.synthetic.main.week_calender.view.*

class WeekFragment : Fragment() {//주간 캘린더



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.week_calender, container, false)

        var mcontext = context!!

        var yearinfo = view.week_CalendarView.currentYear
        var monthinfo = view.week_CalendarView.currentMonth + 1

        var actionBar = (activity as MainActivity).supportActionBar
        actionBar!!.title = "" + yearinfo + "년 " +  monthinfo + "월 "



        view.setOnTouchListener(object : OnSwipeTouchListener(mcontext) {
            override fun onSwipeLeft() {
                super.onSwipeLeft()
                week_CalendarView.setNextWeek()
                week_CalendarView.calendardefaultsetting()

                var yearinfo = view.week_CalendarView.currentYear
                var monthinfo = view.week_CalendarView.currentMonth + 1

                var actionBar = (activity as MainActivity).supportActionBar
                actionBar!!.title = "" + yearinfo + "년 " + monthinfo + "월 "
            }

            override fun onSwipeRight() {
                super.onSwipeRight()
                week_CalendarView.setPreWeek()
                week_CalendarView.calendardefaultsetting()

                var yearinfo = view.week_CalendarView.currentYear
                var monthinfo = view.week_CalendarView.currentMonth + 1

                var actionBar = (activity as MainActivity).supportActionBar
                actionBar!!.title = "" + yearinfo + "년 " + monthinfo + "월 "
            }
        })



        return view
    }
}