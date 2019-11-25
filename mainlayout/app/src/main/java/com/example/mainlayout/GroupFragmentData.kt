package com.example.mainlayout

data class GroupFragmentData(var userNames: String,
                             var userInfos: String,
                             var userTypes: String,
                             var userIcons: String?,
                             var scheduleName:String = "",
                             var scheduleInfo: String? = "",
                             var dateYear: Int = 0,
                             var dateMonth: Int = 0,
                             var date: Int = 0,
                             var startTime: String? = "")/*:Comparable<Int>{
    override fun compareTo(other: Int): Int {

    }
}*/