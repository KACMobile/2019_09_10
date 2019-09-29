package com.example.mainlayout.ui.daily

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.mainlayout.R

class DailyFragment : Fragment() {//일간 캘린더

    private lateinit var dailyViewModel: DailyViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dailyViewModel =
            ViewModelProviders.of(this).get(DailyViewModel::class.java)
        val root = inflater.inflate(R.layout.daily_calender, container, false)
        val textView: TextView = root.findViewById(R.id.text_daily)
        dailyViewModel.text.observe(this, Observer {
            textView.text = it
        })
        return root
    }
}