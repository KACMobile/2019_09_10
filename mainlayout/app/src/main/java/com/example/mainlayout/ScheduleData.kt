package com.example.mainlayout

import java.io.Serializable

class ScheduleData(var alarm: String?,
                   var endTime: String? = "",
                   var startTime: String? = "",
                   var scheduleInfo: String? = "",
                   var shareAble: Boolean? = true,
                   var shareEditAble: Boolean? = false,
                   var dateYear: Int? = 0,
                   var dateMonth: Int? = 0,
                   var date: Int? = 0): Serializable {

}
