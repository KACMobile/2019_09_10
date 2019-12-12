package com.example.mainlayout
//포스트 관련 데이터 - 황선혁
data class Post(val postText : String, val postDate:String, val postTime:String, val postImage: String?, val postLat:String?, val postLng: String?, val userInfo: UserInfo) {
}