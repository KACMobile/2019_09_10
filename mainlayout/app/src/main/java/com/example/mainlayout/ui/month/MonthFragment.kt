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
import com.applandeo.materialcalendarview.*
import com.example.mainlayout.MainActivity

class MonthFragment : Fragment() {//월간 캘린더



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.month_calender, container, false)
        var actionBar = (activity as MainActivity).supportActionBar
        actionBar!!.title = " "
        return root
    }
}