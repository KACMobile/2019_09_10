package com.example.mainlayout

import android.content.Context
import android.view.View
import android.widget.Switch
import com.google.firebase.database.FirebaseDatabase

class CheckSwitch(switchname:String, switch:Switch, context:Context) {
    private val firebaseDatabase = FirebaseDatabase.getInstance()
    private val databaseReference = firebaseDatabase.reference
    private var userID:String = "User01"
    lateinit  var switchView:Switch
    fun CheckSwitch(switchname:String, switch:Switch, context:Context){
        val idPreference = context.getSharedPreferences("UserID", Context.MODE_PRIVATE)
        userID = idPreference.getString("UserID", "User01")!!
        val userFollow = databaseReference.child("Users/" + userID + "Follow")
        val checkPreference = context.getSharedPreferences("CheckPreference", Context.MODE_PRIVATE)
        val editor = checkPreference.edit()
        switchView = switch
        userFollow.child("Users").child(userID).child("KAU").setValue(null)//DB에 임시추가
        if(::switchView.isInitialized) {
            switchView.setOnCheckedChangeListener { checkButton, isChecked ->
                if (isChecked)
                    editor.putBoolean("KAU", true)
                else
                    editor.putBoolean("KAU", false)
            }
            editor.commit()
        }

    }
}