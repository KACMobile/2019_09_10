package com.example.mainlayout

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.make_schedule.*

import android.widget.Button
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.Calendar

class MakeSchedule :AppCompatActivity(){




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.make_schedule)

        val saveBtn = findViewById<Button>(R.id.savebutton)

        val cal = Calendar.getInstance()

        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)

        var cb_Share : CheckBox = findViewById(R.id.checkBox)
        var cb_ShareEdit : CheckBox = findViewById(R.id.checkBox2)
        var cb_alarm : CheckBox = findViewById(R.id.checkBox3)

        val nameText : TextInputEditText = findViewById(R.id.nameTextView)
        val infoText : TextInputEditText = findViewById(R.id.infoTextView)

        //일정 데이터
        var alarm : Boolean = false
        var isitShare : Boolean = false
        var isitShareEdit : Boolean = false
        var dateYear : Int = 2019
        var dateMonth : Int = 1
        var dateDay : Int = 1
        var startTime : Int = 0
        var endTime : Int = 0
        var scheduleInfo : String = " "
        var scheduleName : String = "무제"

        editDateStart.setOnClickListener {
            val dpd = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { view, mYear, mMonth, mDay ->
                    editDateStart.setText("" + mDay + "/" + (mMonth + 1) + "/" + mYear)
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
                editTimeStart.setText("" + hour + ":" + minute)
                if(minute<30)
                    startTime = 0
                else
                    startTime = 30

                startTime += 100*hour
            }
            TimePickerDialog(this, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE),
                true).show()
        }

        editDateEnd.setOnClickListener {
            val dpd = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { view, mYear, mMonth, mDay ->
                    editDateEnd.setText("" + mDay + "/" + (mMonth + 1) + "/" + mYear)
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
                editTimeEnd.setText("" + hour + ":" + minute)
                if(minute<30)
                    endTime = 0
                else
                    endTime = 30

                endTime += 100*hour
            }
            TimePickerDialog(this, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE),
                true).show()
        }


        saveBtn.setOnClickListener {
            if(cb_Share.isChecked)
                isitShare = true
            if(cb_ShareEdit.isChecked)
                isitShareEdit = true

            if(cb_alarm.isChecked)
                alarm = true

            scheduleName = nameText.text.toString()
            scheduleInfo = infoText.text.toString()

            /*insertSchedule(
                userID, "할 일", scheduleName, alarm, endTime, startTime, scheduleInfo,
                isitShare, isitShareEdit, dateYear, dateMonth, dateDay
            )*/



            val intent = Intent(this, MainActivity::class.java)

            startActivity(intent)
        }


    }


}


