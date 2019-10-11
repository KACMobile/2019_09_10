package com.example.mainlayout.ui.month

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.mainlayout.CalendarInfo
import com.example.mainlayout.R
import com.applandeo.materialcalendarview.utils.*
import kotlinx.android.synthetic.main.month_calender.*
import java.util.Calendar

class MonthFragment : Fragment() {//월간 캘린더

    private lateinit var monthViewModel: MonthViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //var mCalendar: Calendar = CalendarInfo.calendar

        //mCalendar.set
        //mCalendar.set(2022, 5, 15)
        //MycalendarView.setCurrentCal(mCalendar)
        monthViewModel =
            ViewModelProviders.of(this).get(MonthViewModel::class.java)
        val root = inflater.inflate(R.layout.month_calender, container, false)
        monthViewModel.text.observe(this, Observer {
        })
        return root
    }
}