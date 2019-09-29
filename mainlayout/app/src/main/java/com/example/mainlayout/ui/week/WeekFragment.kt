package com.example.mainlayout.ui.week

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.mainlayout.R

class WeekFragment : Fragment() {//주간 캘린더

    private lateinit var weekViewModel: WeekViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        weekViewModel =
            ViewModelProviders.of(this).get(WeekViewModel::class.java)
        val root = inflater.inflate(R.layout.week_calender, container, false)
        val textView: TextView = root.findViewById(R.id.text_week)
        weekViewModel.text.observe(this, Observer {
            textView.text = it
        })
        return root
    }
}