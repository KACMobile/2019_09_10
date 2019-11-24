package com.example.mainlayout.ui.daily

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.example.daycalendar.OnSwipeTouchListener
import com.example.mainlayout.R

class DailyFragment : Fragment() {//일간 캘린더

    private lateinit var dailyViewModel: DailyViewModel


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //dailyViewModel = ViewModelProviders.of(this).get(DailyViewModel::class.java)
        val view = inflater.inflate(R.layout.daily_calender, container, false)

        //dailyViewModel.text.observe(this, Observer {})

        val mContext = getContext()

        if (mContext != null) {
            view.setOnTouchListener(object : OnSwipeTouchListener(mContext) {
                override fun onSwipeLeft() {
                    Navigation.findNavController(view).navigate(R.id.Daily_CurrentToNext)
                    Navigation.findNavController(view).navigate(R.id.Daily_NextToCurrent)
                }

                override fun onSwipeRight() {
                    Navigation.findNavController(view).navigate(R.id.Daily_CurrentToPre)
                    Navigation.findNavController(view).navigate(R.id.Daily_PreToCurrent)
                }

            })

        }
        return view

    }
}