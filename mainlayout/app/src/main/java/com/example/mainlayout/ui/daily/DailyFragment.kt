package com.example.mainlayout.ui.daily

import android.app.ActionBar
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.mainlayout.R
import com.example.mainlayout.MainActivity





class DailyFragment : Fragment() {//?ùºÍ∞? Ï∫òÎ¶∞?çî

    private lateinit var dailyViewModel: DailyViewModel


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dailyViewModel = ViewModelProviders.of(this).get(DailyViewModel::class.java)
        val view = inflater.inflate(R.layout.daily_calender, container, false)

        dailyViewModel.text.observe(this, Observer {})

        val year = view.findViewById<View>(R.id.curr_year) as TextView
        val yearinfo = year.text.toString()

        val month = view.findViewById<View>(R.id.curr_month) as TextView
        val monthinfo = month.text.toString()

        val date = view.findViewById<View>(R.id.curr_date) as TextView
        val dateinfo = date.text.toString()

        var actionBar = (activity as MainActivity).supportActionBar
        actionBar!!.title = yearinfo + "?ÖÑ " +  monthinfo + "?õî " + dateinfo + "?ùº"

        view.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_MOVE) {
                val year = view.findViewById<View>(R.id.curr_year) as TextView
                val yearinfo = year.text.toString()

                val month = view.findViewById<View>(R.id.curr_month) as TextView
                val monthinfo = month.text.toString()

                val date = view.findViewById<View>(R.id.curr_date) as TextView
                val dateinfo = date.text.toString()

                var actionBar = (activity as MainActivity).supportActionBar
                actionBar!!.title = yearinfo + "?ÖÑ " +  monthinfo + "?õî " + dateinfo + "?ùº"

            }
            true
        }

        return view





    }









}