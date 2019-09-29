package com.example.mainlayout.ui.month

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.mainlayout.R

class MonthFragment : Fragment() {//월간 캘린더

    private lateinit var monthViewModel: MonthViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        monthViewModel =
            ViewModelProviders.of(this).get(MonthViewModel::class.java)
        val root = inflater.inflate(R.layout.month_calender, container, false)
        val textView: TextView = root.findViewById(R.id.text_month)
        monthViewModel.text.observe(this, Observer {
            textView.text = it
        })
        return root
    }
}