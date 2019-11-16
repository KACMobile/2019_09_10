package com.example.mainlayout

import android.content.Intent
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
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.example.mainlayout.group.GroupAdd
import com.example.mainlayout.group.GroupList
import com.example.mainlayout.group.GroupSetting
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration

    private val userID:String = "User01"

    private val firebaseDatabase = FirebaseDatabase.getInstance()

    private val databaseReference = firebaseDatabase.reference

    lateinit var saveDataSnap: DataSnapshot
    var dataArray = arrayListOf<Schedule>()

    lateinit var fabOpen : Animation
    lateinit var fabClose : Animation
    lateinit var rotateBackward : Animation
    lateinit var rotateForward : Animation

    var isGroupFragment : Boolean = false
    var isOpen : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener { _ ->
            val groupFab: FloatingActionButton = findViewById(R.id.groupfab)
            val groupFab1: FloatingActionButton = findViewById(R.id.groupfab1)
            val groupFab2: FloatingActionButton = findViewById(R.id.groupfab2)
            val groupFab3: FloatingActionButton = findViewById(R.id.groupfab3)

            fabOpen = AnimationUtils.loadAnimation(this, R.anim.fab_open)
            fabClose = AnimationUtils.loadAnimation(this, R.anim.fab_close)
            rotateForward = AnimationUtils.loadAnimation(this, R.anim.rotate_forward)
            rotateBackward = AnimationUtils.loadAnimation(this, R.anim.rotate_backward)

            fun whichFab() {
                if (isGroupFragment) {
                    fab.show()
                    groupFab.hide()
                } else //groupFab = false
                {
                    fab.hide()
                    groupFab.show()
                }
            }

            fun animateFab() {
                if (isOpen) {
                    groupFab.startAnimation(rotateForward)
                    groupFab1.startAnimation(fabClose)
                    groupFab2.startAnimation(fabClose)
                    groupFab3.startAnimation(fabClose)
                    groupFab1.hide()
                    groupFab2.hide()
                    groupFab3.hide()
                    isOpen = false
                } else {
                    groupFab.startAnimation(rotateBackward)
                    groupFab1.startAnimation(fabOpen)
                    groupFab2.startAnimation(fabOpen)
                    groupFab3.startAnimation(fabOpen)
                    groupFab1.show()
                    groupFab2.show()
                    groupFab3.show()
                    isOpen = true
                }
            }

            whichFab()

            //var fragment : Fragment? = supportFragmentManager.findFragmentById(R.id.group_fragment)

            groupFab.setOnClickListener { view ->
                animateFab()
            }

            groupFab1.setOnClickListener { view ->
                val Intent = Intent(this, GroupList::class.java)
                startActivity(Intent)
            }
            groupFab2.setOnClickListener { view ->
                val Intent = Intent(this, GroupAdd::class.java)
                startActivity(Intent)
            }
            groupFab3.setOnClickListener { view ->
                val Intent = Intent(this, GroupSetting::class.java)
                startActivity(Intent)
            }

            fab.setOnClickListener { view ->
                val nextIntent = Intent(this, MakeSchedule::class.java)
                startActivity(nextIntent)
            }
            /*val fab: FloatingActionButton = findViewById(R.id.fab)
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
                userID, "할 일", "777", false, "400", "100", "commit",
                false, false, 2019, 11, 6
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
        }*/

            val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
            val navView: NavigationView = findViewById(R.id.nav_view)
            val navController = findNavController(R.id.nav_host_fragment)
            // Passing each menu ID as a set of Ids because each
            // menu should be considered as top level destinations.
            appBarConfiguration = AppBarConfiguration(
                setOf(
                    R.id.daily_calender,
                    R.id.week_calender,
                    R.id.month_calender,
                    R.id.group_fragment
                ), drawerLayout
            )
            setupActionBarWithNavController(navController, appBarConfiguration)
            navView.setupWithNavController(navController)

            val userDB = databaseReference.child("Users/" + userID)
            userDB.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    saveDataSnap = dataSnapshot
                }

                override fun onCancelled(dataSnapshot: DatabaseError) {
                }
            })
        }
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
            var scheduleName: String = "",
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
            databaseReference.child("Users").child(userName).child(tag).child(dateMonth.toString())
                .setValue(dataArray)


        }


}