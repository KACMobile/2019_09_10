package com.example.mainlayout

import java.io.Serializable

data class UserInfo(var userNames: String, var userInfos: String, var userTypes: String, var userIcons: String?= null, var userHomepage: String? = null, var userTEL:String? = null, var locateLat:Double? = null, var locateLng:Double? = null): Serializable {
}