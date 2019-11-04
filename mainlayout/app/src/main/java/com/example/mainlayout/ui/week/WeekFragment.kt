package com.example.mainlayout.ui.week

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.mainlayout.CalendarInfo
import com.example.mainlayout.R
import com.example.weekcalendar.WeekCalendar
import com.google.firebase.database.DataSnapshot
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.week_calender.*
import java.util.*

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
        val root = inflater.inflate(R.layout.week_calender, container, false)


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
        val view = root.findViewById<WeekCalendar>(R.id.week_CalendarView)


        return root
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