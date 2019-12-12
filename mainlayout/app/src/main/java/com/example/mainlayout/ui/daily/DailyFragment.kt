package com.example.mainlayout.ui.daily

import android.app.ActionBar
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.mainlayout.R
import com.example.mainlayout.MainActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.daily_calender.*
import kotlinx.android.synthetic.main.daily_calender.view.*




class DailyFragment : Fragment() {//

    private lateinit var dailyViewModel: DailyViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dailyViewModel = ViewModelProviders.of(this).get(DailyViewModel::class.java)
        val view = inflater.inflate(R.layout.daily_calender, container, false)

        var mcontext = context!!

        var yearinfo = view.day_CalendarView.currentYear
        var monthinfo = view.day_CalendarView.currentMonth + 1

        var actionBar = (activity as MainActivity).supportActionBar
        actionBar!!.title = "" + yearinfo + "년 " + monthinfo + "월 "

        view.setOnTouchListener(object : OnSwipeTouchListener(mcontext) {
            override fun onSwipeLeft() {
                super.onSwipeLeft()
                day_CalendarView.setNextDay()
                day_CalendarView.calendardefaultsetting()

                var yearinfo = view.day_CalendarView.currentYear
                var monthinfo = view.day_CalendarView.currentMonth + 1

                var actionBar = (activity as MainActivity).supportActionBar
                actionBar!!.title = "" + yearinfo + "년 " + monthinfo + "월 "

            }
            override fun onSwipeRight() {
                super.onSwipeRight()
                day_CalendarView.setPreDay()
                day_CalendarView.calendardefaultsetting()

                var yearinfo = view.day_CalendarView.currentYear
                var monthinfo = view.day_CalendarView.currentMonth + 1

                var actionBar = (activity as MainActivity).supportActionBar
                actionBar!!.title = "" + yearinfo + "년 " + monthinfo + "월 "
            }
        })
        dailyViewModel.text.observe(this, Observer {

        })


        return view
    }
}

