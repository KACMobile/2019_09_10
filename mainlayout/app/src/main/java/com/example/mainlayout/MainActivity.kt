package com.example.mainlayout

import android.os.Bundle
import android.util.Log
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.Menu
import com.applandeo.materialcalendarview.utils.*
import com.applandeo.materialcalendarview.CalendarView
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.month_calender.*
import kotlinx.android.synthetic.main.week_calender.*

import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import androidx.fragment.app.FragmentActivity

import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import com.google.firebase.database.DatabaseReference

import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T

import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import com.google.firebase.database.*

import com.google.firebase.database.ChildEventListener
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.view.MenuItem


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
   /* private var calendarInfo: CalendarInfo = CalendarInfo()

    private val firebaseDatabase = FirebaseDatabase.getInstance()

    private val databaseReference = firebaseDatabase.reference*/

    private val firebaseDatabase = FirebaseDatabase.getInstance()

    private val databaseReference = firebaseDatabase.reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.daily_calender, R.id.week_calender, R.id.month_calender
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

       /* insertSchedule("User01","할 일", "MobileProject", "false", "1600", "1530", "김철기 교수님 skype",
            false, false, 2019, 10,17)


        databaseReference.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                val sample = dataSnapshot.getValue(Schedule::class.java)?.scheduleInfo

            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {

            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {

            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {

            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })*/
        /*val database = FirebaseDatabase.getInstance()
        val endTime_String = database.getReference("Users/UserID/Tag/Schedule/endTime_String")
        val startTime_String = database.getReference("Users/UserID/Tag/Schedule/startTime_String")
        val date_String = database.getReference("Users/UserID/Tag/Schedule/date_String")
        val month_String = database.getReference("Users/UserID/Tag/Schedule/month_String")
        val year_String = database.getReference("Users/UserID/Tag/Schedule/year_String")
        endTime_String.setValue("1530")
        startTime_String.setValue("1600")
        date_String.setValue("17")
        month_String.setValue("10")
        year_String.setValue("2019")


        FirebaseDatabase.getInstance().reference.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                Log.d("MainActivity", "ChildEventListener - onChildAdded : " + dataSnapshot.value!!)
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {
                Log.d("MainActivity", "ChildEventListener - onChildChanged : " + s!!)
            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                Log.d("MainActivity", "ChildEventListener - onChildRemoved : " + dataSnapshot.key!!)
            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {
                Log.d("MainActivity", "ChildEventListener - onChildMoved" + s!!)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d("MainActivity", "ChildEventListener - onCancelled" + databaseError.message)
            }
        })*/


    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

  /*  fun insertSchedule(userName:String, tag: String,scheduleName: String, alarm: String, endTime: String,
                       startTime:String, scheduleInfo:String?, shareAble:Boolean?,shareEditAble:Boolean?,dateYear:Int, dateMonth:Int, date:Int){
        val schedule = Schedule(alarm,endTime,startTime, scheduleInfo, shareAble, shareEditAble, dateYear, dateMonth, date)

        databaseReference.child("Users").child(userName).child(tag).child(scheduleName).setValue(schedule)

    }

    @IgnoreExtraProperties

    data class Schedule(
        var alarm: String? = "",
        var endTime: String?="",
        var startTime: String?="",
        var scheduleInfo: String?="",
        var shareAble: Boolean? = true,
        var shareEditAble: Boolean? = false,
        var dateYear: Int? = 0,
        var dateMonth: Int? = 0,
        var date: Int? = 0

    )*/



}