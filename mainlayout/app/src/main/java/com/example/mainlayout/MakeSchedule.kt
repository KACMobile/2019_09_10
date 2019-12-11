package com.example.mainlayout

import android.app.AlertDialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.make_schedule.*

import android.widget.Button
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.Calendar
import android.widget.Toast



class MakeSchedule :AppCompatActivity(){


    private val firebaseDatabase = FirebaseDatabase.getInstance()

    private val databaseReference = firebaseDatabase.reference
    var dataArray = arrayListOf<Any?>()
    var userID:String = "User01"

    override fun onCreate(savedInstanceState: Bundle?) {
        val idPreference = getSharedPreferences("UserID", Context.MODE_PRIVATE)
        userID = idPreference.getString("UserID", "User01")!!
        super.onCreate(savedInstanceState)
        setContentView(R.layout.make_schedule)

        val saveBtn = findViewById<Button>(R.id.savebutton)

        val cal = Calendar.getInstance()

        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)

        var cb_alarm : CheckBox = findViewById(R.id.checkBox3)

        val nameText : TextInputEditText = findViewById(R.id.nameTextView)
        val infoText : TextInputEditText = findViewById(R.id.infoTextView)

        val tagText : TextView = findViewById(R.id.tag_text)

        //일정 데이터
        var alarm : Boolean = false
        var isitShare : Boolean = false
        var dateYear : Int = 2019
        var dateMonth : Int = 1
        var dateDay : Int = 1
        var startTime : String = " "
        var endTime : String = " "
        var scheduleInfo : String = " "
        var scheduleName : String = "무제"
        var tag : String = "할 일"

        tag_text.setOnClickListener {
            showdialog()
        }


        /*val userDB = databaseReference.child("Users/" + userID + "/Schedule")
        userDB.addValueEventListener( object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for(snapShot in dataSnapshot.children){
                    for(deeperSnapShot in snapShot.child((CalendarInfo.currentMonth +1).toString()).children){
                        dataArray.add(deeperSnapShot.value)//DB값을 다시 Array에 저장
                    }
                }
            }
            override fun onCancelled(dataSnapshot: DatabaseError) {
            }
        })*/

        editDateStart.setOnClickListener {
            val dpd = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { view, mYear, mMonth, mDay ->
                    editDateStart.setText("" + mYear + "/" + (mMonth + 1) + "/" + mDay)
                    editDateEnd.setText("" + mYear + "/" + (mMonth + 1) + "/" + mDay)
                    dateYear = mYear
                    dateMonth = mMonth
                    dateDay = mDay
                },
                year,
                month,
                day
            )
            dpd.show()
        }

        editTimeStart.setOnClickListener {
            val timeSetListener = TimePickerDialog.OnTimeSetListener{timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY,hour)
                cal.set(Calendar.MINUTE,minute)
                editTimeStart.setText("" + hour + "시 " + minute + "분")
                editTimeEnd.setText("" + (hour+1) + "시 " + minute + "분")
                var minuteString: String? = null
                if(minute<30) {
                    minuteString = "00"
                }
                else {
                    minuteString = "30"
                    endTime = (100 * (hour+1)+30).toString()

                }
                startTime = hour.toString() + minuteString
                if(endTime == " ")
                    endTime = (hour+1).toString() +minuteString
            }
            TimePickerDialog(this, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE),
                true).show()
        }

        editDateEnd.setOnClickListener {
            val dpd = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { view, mYear, mMonth, mDay ->
                    editDateEnd.setText("" + mYear + "/" + (mMonth + 1) + "/" + mDay)
                },
                year,
                month,
                day
            )
            dpd.show()
        }

        editTimeEnd.setOnClickListener {
            val timeSetListener = TimePickerDialog.OnTimeSetListener{timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY,hour)
                cal.set(Calendar.MINUTE,minute)
                editTimeEnd.setText("" + hour + ":시 " + minute + "분")
                var minuteString:String = " "
                if(minute<30) {
                     minuteString = "00"
                }
                else {
                    minuteString = "30"
                }
                endTime = hour.toString() + minuteString
            }
            TimePickerDialog(this, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE),
                true).show()
        }


        saveBtn.setOnClickListener {

            if(cb_alarm.isChecked)
                alarm = true

            scheduleName = nameText.text.toString()
            scheduleInfo = infoText.text.toString()
            tag = tag_text.text.toString()
            if(tag_text.text == "Tag 선택"){
                tag = "할 일"
            }

            val userDB = databaseReference.child("Users/" + userID + "/Schedule")
            userDB.addValueEventListener( object: ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for(snapShot in dataSnapshot.children){
                        for(deeperSnapShot in snapShot.child((dateMonth +1).toString()).children){
                            dataArray.add(deeperSnapShot.value)//DB값을 다시 Array에 저장
                        }
                    }
                }
                override fun onCancelled(dataSnapshot: DatabaseError) {
                }
            })

            insertSchedule(
                userID, tag, scheduleName, alarm, startTime.toString(), endTime, scheduleInfo,
                isitShare, dateYear, dateMonth+1, dateDay
            )



            finish()
        }


    }
    private fun showdialog() {
        val array = arrayOf("할 일", "시간표", "일정", "알림")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Tag")
        builder.setItems(array,{_, which ->
            val selected = array[which]

            try {
                tag_text.setText(selected)

            }catch (e:IllegalArgumentException){
                Toast.makeText(applicationContext, "Tag is not Selected", Toast.LENGTH_LONG ).show()
            }
        })

        val dialog = builder.create()

        dialog.show()
    }

    fun insertSchedule(
        userName: String,
        tag: String,
        scheduleName: String,
        alarm: Boolean,
        startTime: String,
        endTime: String,
        scheduleInfo: String?,
        shareAble: Boolean?,
        dateYear: Int,
        dateMonth: Int,
        date: Int
    ) {
        val schedule = Schedule(
            userName,
            scheduleName,
            scheduleInfo,
            dateYear,
            dateMonth,
            date,
            startTime,
            endTime,
            alarm,
            shareAble)
        dataArray.add(schedule)

        var dataHashMap = HashMap<String,Any>()
        dataHashMap["/Users/"+userName +"/Schedule/"+tag+"/" + dateMonth.toString()] = dataArray
        databaseReference.updateChildren(dataHashMap)


    }


}


