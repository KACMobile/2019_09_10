package com.example.mainlayout.ui.week

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class WeekViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is week calender"
    }
    val text: LiveData<String> = _text
}