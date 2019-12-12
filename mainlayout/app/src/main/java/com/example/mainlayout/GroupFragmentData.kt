package com.example.mainlayout
//그룹 fragment data
data class GroupFragmentData(var userNames: String,
                             var userInfos: String,
                             var userTypes: String,
                             var userIcons: String?,
                             var scheduleName:String = "",
                             var scheduleInfo: String? = "",
                             var dateYear: Int = 0,
                             var dateMonth: Int = 0,
                             var date: Int = 0,
                             var startTime: String? = "")