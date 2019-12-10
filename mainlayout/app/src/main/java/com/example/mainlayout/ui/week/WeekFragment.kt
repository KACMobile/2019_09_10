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

class WeekFragment : Fragment() {//주간 캘린더

    private lateinit var weekViewModel: WeekViewModel

    private lateinit var saveDataSnapshot: DataSnapshot

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        weekViewModel =
            ViewModelProviders.of(this).get(WeekViewModel::class.java)
        val view = inflater.inflate(R.layout.week_calender, container, false)


        val year = view.findViewById<View>(R.id.curr_year) as TextView
        val yearinfo = year.text.toString()

        val month = view.findViewById<View>(R.id.curr_month) as TextView
        val monthinfo = month.text.toString()

        val date = view.findViewById<View>(R.id.curr_date) as TextView
        val dateinfo = date.text.toString()

        var actionBar = (activity as MainActivity).supportActionBar
        actionBar!!.title = yearinfo + "년 " +  monthinfo + "월 "

        //var fragmentweekCalendar = week_CalendarView

        /*if (wCalendar.get(Calendar.DATE)!=CalendarInfo.currentDate
            || wCalendar. get(Calendar.MONTH)!=CalendarInfo.currentMonth
            || wCalendar. get(Calendar.YEAR)!=CalendarInfo.currentYear)
        {
            weekCalendar.setCurrent(CalendarInfo.currentDate, CalendarInfo.currentMonth, CalendarInfo.currentYear)
        }
        */
        //week_CalendarView.setCurrent(wCalendar.get(Calendar.YEAR), wCalendar.get(Calendar.MONTH), wCalendar.get(Calendar.DATE))
        weekViewModel.text.observe(this, Observer {
        })
        view.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_MOVE) {
                val year = view.findViewById<View>(R.id.curr_year) as TextView
                val yearinfo = year.text.toString()

                val month = view.findViewById<View>(R.id.curr_month) as TextView
                val monthinfo = month.text.toString()

                val date = view.findViewById<View>(R.id.curr_date) as TextView
                val dateinfo = date.text.toString()

                var actionBar = (activity as MainActivity).supportActionBar
                actionBar!!.title = yearinfo + "년 " +  monthinfo + "월 "

            }
            true
        }


        return view
    }

    /*override fun setArguments(args: Bundle?) {
        super.setArguments(args)

        Log.d("aa","여기는 들어옴?")
        saveDataSnapshot = args!!.getSerializable("DataSnapShot") as DataSnapshot
    }

    public fun setDataSnapShot(dataSnapshot: DataSnapshot){
        saveDataSnapshot = dataSnapshot

    }*/
}