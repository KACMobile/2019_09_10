package com.example.mainlayout.ui.week

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.example.daycalendar.OnSwipeTouchListener
import com.example.mainlayout.R

class PreWeekPage : Fragment() {

    private lateinit var dailyViewModel: WeekViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        dailyViewModel =
            ViewModelProviders.of(this).get(WeekViewModel::class.java)
        val view = inflater.inflate(R.layout.week_calender, container, false)
        dailyViewModel.text.observe(this, Observer {
        })


        val mContext = getContext()


        return view
    }
}