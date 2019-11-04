package com.example.mainlayout.ui

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment

import com.example.mainlayout.R

class MakeSchedule : Fragment() {

    companion object {
        fun newInstance() = MakeSchedule()
    }

    private lateinit var viewModel: MakeScheduleViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.make_schedule_fragment, container, false)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {//툴바의 아이템 선택됐을 때
        when (item.itemId) {

        }
        return super.onOptionsItemSelected(item)
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.make_schedule, menu)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MakeScheduleViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
