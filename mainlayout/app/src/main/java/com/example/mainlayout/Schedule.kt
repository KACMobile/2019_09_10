package com.example.mainlayout
//스케줄 자료 저장용 data class - 황선혁
data class Schedule(var userID:String = "",
                           var scheduleName:String = "",
                           var scheduleInfo: String? = "",
                           var dateYear: Int = 0,
                           var dateMonth: Int = 0,
                           var date: Int = 0,
                           var startTime: String? = "",
                           var endTime: String? = "",
                           var alarm: Boolean? = false,
                           var shareAble: Boolean? = true,
                           var shareEditAble: Boolean? = false) {

}