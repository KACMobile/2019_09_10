package com.example.mainlayout

import java.io.Serializable
//유저 인포 저장 data class - 황선혁
data class UserInfo(var userName: String, var userInfo: String, var userType: String, var userIcon: String?= null, var userHomepage: String? = null, var userTEL:String? = null, var locateLat:Double? = null, var locateLng:Double? = null): Serializable {
}