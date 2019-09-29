package com.example.mainlayout.ui.month

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MonthViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is month calender"
    }
    val text: LiveData<String> = _text
}