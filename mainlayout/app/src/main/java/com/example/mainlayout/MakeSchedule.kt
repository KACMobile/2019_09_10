package com.example.mainlayout

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.make_schedule.*

import android.widget.Button
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
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


        //일정 데이터

        editDateStart.setOnClickListener {
            val dpd = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { view, mYear, mMonth, mDay ->
                    editDateStart.setText("" + mDay + "/" + (mMonth + 1) + "/" + mYear)
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
            }
            TimePickerDialog(this, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE),
                true).show()
        }


        saveBtn.setOnClickListener {



            val intent = Intent(this, MainActivity::class.java)

            startActivity(intent)
        }


    }


}


