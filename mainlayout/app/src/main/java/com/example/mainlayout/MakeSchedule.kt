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
    var userID:String = "User01"
    var malarm : Boolean = false
    var misitShare : Boolean = false
    var mdateYear : Int = 2019
    var mdateMonth : Int = 1
    var mdateDay : Int = 1
    var mstartTime : String = " "
    var mendTime : String = " "
    var mscheduleInfo : String = " "
    var mscheduleName : String = "무제"
    var mtag : String = "할 일"

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
                    mdateYear = mYear
                    mdateMonth = mMonth
                    mdateDay = mDay
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
                    mendTime = (100 * (hour+1)+30).toString()

                }
                mstartTime = hour.toString() + minuteString
                if(mendTime == " ")
                    mendTime = (hour+1).toString() +minuteString
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
                mendTime = hour.toString() + minuteString
            }
            TimePickerDialog(this, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE),
                true).show()
        }


        saveBtn.setOnClickListener {

            if(cb_alarm.isChecked)
                malarm = true

            mscheduleName = nameText.text.toString()
            mscheduleInfo = infoText.text.toString()




            insertSchedule(
                userID, mtag, mscheduleName, malarm, mstartTime, mendTime, mscheduleInfo,
                misitShare, mdateYear, mdateMonth+1, mdateDay
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
                mtag = tag_text.text.toString()

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
        databaseReference.child("Users/"+userName +"/Schedule/"+tag+"/" + dateMonth.toString()).push().setValue(schedule)


    }


}


