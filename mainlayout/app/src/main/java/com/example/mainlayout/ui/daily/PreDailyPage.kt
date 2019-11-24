package com.example.mainlayout.ui.daily

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

class PreDailyPage : Fragment() {

    private lateinit var dailyViewModel: DailyViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        //dailyViewModel = ViewModelProviders.of(this).get(DailyViewModel::class.java)
        val view = inflater.inflate(R.layout.daily_calender, container, false)

        //dailyViewModel.text.observe(this, Observer {})

        val mContext = getContext()

        return view
    }
}