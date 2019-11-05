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
import kotlinx.android.synthetic.main.month_calender.*
import kotlinx.android.synthetic.main.week_calender.*

import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import androidx.fragment.app.FragmentActivity
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration

    private val userID:String = "User01"

    private val firebaseDatabase = FirebaseDatabase.getInstance()

    private val databaseReference = firebaseDatabase.reference

    lateinit var saveDataSnap: DataSnapshot
    var dataArray = arrayListOf<Schedule>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            insertSchedule(
                userID, "할 일", "1", false, "400", "200", "Mob",
                false, false, 2019, 11, 3
            )
            insertSchedule(
                userID, "할 일", "2", false, "400", "100", "test",
                false, false, 2019, 11, 5
            )
            insertSchedule(
                userID, "할 일", "3", false, "400", "100", "test",
                false, false, 2019, 11, 13
            )
            insertSchedule(
                userID, "시간표", "컴퓨터 구조", false, "1200", "1000",
                "과학관 110",false, false, 2019, 11, 4
            )

            insertSchedule(
                userID, "시간표", "모바일 SW", false, "1300", "900",
                "전자관 420", false, false, 2019, 11, 5
            )

            insertSchedule(
                userID, "할 일","코딩", false, "1300", "1100",
                "전자관 420", false, false, 2019, 11, 5
            )
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

        val userDB = databaseReference.child("Users/" + userID)
        userDB.addValueEventListener( object: ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                saveDataSnap = dataSnapshot
            }
            override fun onCancelled(dataSnapshot: DatabaseError) {
            }
        })
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

    @IgnoreExtraProperties

    public data class Schedule(
        var scheduleName:String = "",
        var scheduleInfo: String? = "",
        var dateYear: Int = 0,
        var dateMonth: Int = 0,
        var date: Int = 0,
        var startTime: String? = "",
        var endTime: String? = "",
        var alarm: Boolean? = false,
        var shareAble: Boolean? = true,
        var shareEditAble: Boolean? = false
    )



    fun insertSchedule(
        userName: String,
        tag: String,
        scheduleName: String,
        alarm: Boolean,
        endTime: String,
        startTime: String,
        scheduleInfo: String?,
        shareAble: Boolean?,
        shareEditAble: Boolean?,
        dateYear: Int,
        dateMonth: Int,
        date: Int
    ) {
        val databaseReference = firebaseDatabase.reference
        val schedule = Schedule(
            scheduleName,
            scheduleInfo,
            dateYear,
            dateMonth,
            date,
            startTime,
            endTime,
            alarm,
            shareAble,
            shareEditAble
        )
        dataArray.add(schedule)
        var a: String = schedule.scheduleName
        databaseReference.child("Users").child(userName).child(tag).child(dateMonth.toString()).setValue(dataArray)


    }



}