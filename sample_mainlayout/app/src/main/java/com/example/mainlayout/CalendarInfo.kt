package com.example.mainlayout

import java.util.*
//현제 보고있는 일, 월, 달, 년 표기
class CalendarInfo(){
    companion object {
        var calendar = Calendar.getInstance()
        var currentYear = calendar.get(Calendar.YEAR)
        var currentMonth = calendar.get(Calendar.MONTH)
        var currentWOM = calendar.get(Calendar.WEEK_OF_MONTH)
        var currentDate = calendar.get(Calendar.DATE)
    }
}