package com.example.mainlayout

import java.io.Serializable

data class UserInfo(var userNames: String, var userInfos: String, var userTypes: String, var userIcons: String?, var userHomepage: String?, var userTEL:String?, var locateLat:Double?, var locateLng:Double?): Serializable {
}