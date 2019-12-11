package com.example.mainlayout

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

import android.os.Build
import android.os.PowerManager
import android.util.Log
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.google.firebase.database.*
import java.util.*

import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.net.Uri
import android.provider.Settings
import android.view.*
import android.widget.*

import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.fragment.app.FragmentTransaction
import com.example.mainlayout.CalendarInfo.Companion.calendar
import com.example.mainlayout.ui.daily.DailyFragment
import com.example.mainlayout.ui.month.MonthFragment
import com.example.mainlayout.ui.week.WeekFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*


class MainActivity : AppCompatActivity() {

    lateinit var dailyFragment: DailyFragment
    lateinit var weekFragment: WeekFragment
    lateinit var monthFragment: MonthFragment
    lateinit var groupFragment: GroupFragment

    private lateinit var appBarConfiguration: AppBarConfiguration

    private val userID:String = "User01"

    private val firebaseDatabase = FirebaseDatabase.getInstance()

    private val databaseReference = firebaseDatabase.reference

    lateinit var saveDataSnap: DataSnapshot
    var dataArray = arrayListOf<Any>()

    lateinit var fabOpen : Animation
    lateinit var fabClose : Animation
    lateinit var rotateBackward : Animation
    lateinit var rotateForward : Animation

    var isGroupFragment : Boolean = false
    var isOpen : Boolean = false

    val NOTIFICATION_CHANNEL_ID = "10001"

    lateinit var fab : FloatingActionButton
    lateinit var groupFab : FloatingActionButton

    lateinit var muserInfo: UserInfo

    var yearInfo = 2019
    var monthInfo = 12
    var dateInfo = 12

    public fun setActionBarTitle(str:String){
        actionBar?.title = str
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolBar)

        setSupportActionBar(toolBar)

        val actionBar = supportActionBar
        setActionBarTitle("" + yearInfo + monthInfo + dateInfo)

        val listView1 : ListView = findViewById(R.id.navigation_drawer_list1)
        val listView2 : ListView = findViewById(R.id.navigation_drawer_list2)

        var gList = ArrayList<String>()
        gList.add("Group1")
        gList.add("Group2")
        gList.add("Group3")
        gList.add("Group4")

        listView1.adapter = ListAdapter(this)
        listView2.adapter = CheckListAdapter(gList, this)





        val drawerToggle : ActionBarDrawerToggle = object : ActionBarDrawerToggle(
            this,
            drawer_layout,
            toolBar,
            (R.string.navigation_drawer_open),
            (R.string.navigation_drawer_close)
        ){

        }
        drawerToggle.isDrawerIndicatorEnabled = true
        drawer_layout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()


        fab = findViewById(R.id.fab)
        groupFab = findViewById(R.id.groupfab)
        val groupFab1: FloatingActionButton = findViewById(R.id.groupfab1)
        val groupFab2: FloatingActionButton = findViewById(R.id.groupfab2)
        val groupFab3: FloatingActionButton = findViewById(R.id.groupfab3)
        whichFab()

        fabOpen = AnimationUtils.loadAnimation(this, R.anim.fab_open)
        fabClose = AnimationUtils.loadAnimation(this, R.anim.fab_close)
        rotateForward = AnimationUtils.loadAnimation(this, R.anim.rotate_forward)
        rotateBackward = AnimationUtils.loadAnimation(this, R.anim.rotate_backward)
        //val userFollow = databaseReference.child("Users/" + userID + "/Follow")
        //databaseReference.child("Users").child(userID).child("Follow").child("Groups").child("KAU").setValue(Color.RED)//DB에 임시추가
        //val currentFragment : DailyFragment = FragmentManager
        fun animateFab()
        {
            if (isOpen)
            {
                groupFab.startAnimation(rotateForward)
                groupFab1.startAnimation(fabClose)
                groupFab2.startAnimation(fabClose)
                groupFab3.startAnimation(fabClose)
                groupFab1.hide()
                groupFab2.hide()
                groupFab3.hide()
                isOpen = false

            }
            else
            {
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
        fab.setOnClickListener { view ->
            val nextIntent = Intent(this, MakeSchedule::class.java)
            startActivity(nextIntent)
        }
        groupFab.setOnClickListener{
            animateFab()
        }

        groupFab1.setOnClickListener{view ->
            val Intent = Intent( this, GroupAdd::class.java)
            startActivity(Intent)
        }
        groupFab2.setOnClickListener{view ->
            val Intent = Intent( this, GroupList::class.java)
            startActivity(Intent)
        }
        groupFab3.setOnClickListener{view ->
            val Intent = Intent( this, MyPageActivity::class.java)
            if(::muserInfo.isInitialized) Intent.putExtra("userInfo",muserInfo)
            startActivity(Intent)
        }


        listView1.setOnItemClickListener { parent: AdapterView<*>, view:View, position:Int, id ->
            if (position == 0){
                dailyFragment = DailyFragment()
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.nav_host_fragment, dailyFragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit()

                isGroupFragment = false
                whichFab()
            }
            if (position == 1){
                weekFragment = WeekFragment()
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.nav_host_fragment, weekFragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit()

                isGroupFragment = false
                whichFab()
            }
            if (position == 2){
                monthFragment = MonthFragment()
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.nav_host_fragment, monthFragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit()

                isGroupFragment = false
                whichFab()
            }
            if (position == 3){
                groupFragment = GroupFragment()
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.nav_host_fragment, groupFragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit()

                isGroupFragment = true
                whichFab()
            }
            drawer_layout.closeDrawer(GravityCompat.START)
        }
        /*insertGroup(
            "KAU", "KAU", "모바일SW", false, "1000", "1300", "13주",
            false, false, 2019, 11, 19
        )
        insertGroup(
            "KAU", "KAU", "모바일SW", false, "1000", "1300", "14주",
            false, false, 2019, 11, 26
        )*/
        /*insertGroup(
            "KBO", "프리미어12", "결승", false, "1900", "2300", "대한민국 VS 일본",
            false, false, 2019, 11, 17
        )*/
        //insertGroupInfo("User01", "User01")
        /*insertSchedule(
            userID, "할 일", "Test", false, "300", "600", "Test",
            false, false, 2019, 11, 23
        )*/
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

        //navView.setNavigationItemSelectedListener(this)
        navView.bringToFront();



        dailyFragment = DailyFragment()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.nav_host_fragment, dailyFragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()

        /*appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.daily_calender, R.id.week_calender, R.id.month_calender, R.id.group_fragment
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)*/

        //navView.setupWithNavController(navController)사용할 경우 onNavigationItemSelected메소드 사용불

        val userDB = databaseReference.child("Users/" + userID)
        userDB.addValueEventListener( object: ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var lat:Double? = null
                var lng:Double? = null
                if(dataSnapshot.child("UserInfo/locateLat").value != null && dataSnapshot.child("UserInfo/locateLng").value != null){
                    lat  = dataSnapshot.child("UserInfo/locateLat").value as Double
                    lng = dataSnapshot.child("UserInfo/locateLng").value as Double
                }
                muserInfo = UserInfo(dataSnapshot.child("UserInfo/userName").value.toString(), dataSnapshot.child("UserInfo/userInfo").value.toString(),
                    dataSnapshot.child("UserInfo/userType").value.toString(), dataSnapshot.child("UserInfo/userIcon").value.toString(),
                    dataSnapshot.child("UserInfo/userHomePage").value.toString(), dataSnapshot.child("UserInfo/userTEL").value.toString(),
                    lat, lng)
                saveDataSnap = dataSnapshot.child("Schedule")
                for(snapShot in dataSnapshot.child("Schedule").children){
                    for(deeperSnapShot in snapShot.child((CalendarInfo.currentMonth +1).toString()).children){
                        setAlarmScheduleOnCalendar(deeperSnapShot.value as HashMap<String, Any>)

                    }
                }
            }
            override fun onCancelled(dataSnapshot: DatabaseError) {
            }
        })

        if(::saveDataSnap.isInitialized) {
            for (snapShot in saveDataSnap.children) {
                for (deeperSnapShot in snapShot.child((Calendar.getInstance().get(Calendar.MONTH)+1).toString()).children) {
                    setAlarmScheduleOnCalendar(deeperSnapShot.value as HashMap<String, Any>)
                }
            }
        }


        /*//Switch관련 저장 및 처리
        val userFollow = databaseReference.child("Users/" + userID + "Follow")
        val checkPreference = getSharedPreferences("CheckPreference", Context.MODE_PRIVATE)
        val editor = checkPreference.edit()
        val groupSwitch: Switch = findViewById(R.id.check_group)
        val switchSample = CheckSwitch("KAU", groupSwitch,this)
        userFollow.child("Users").child(userID).child("KAU").setValue(null)//DB에 임시추가
        groupSwitch.setOnCheckedChangeListener { checkButton, isChecked ->

            if(isChecked)
                editor.putBoolean("KAU", true)
            else
                editor.putBoolean("KAU", false)
        }
        groupSwitch.isChecked = checkPreference.getBoolean("KAU",true)*/
    }
    ////

    //메뉴 리스트 1

    class ListAdapter(private val context: Activity) : BaseAdapter(){
        var names = arrayOf("일간", "주간", "월간", "그룹")

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            var inflater = context.layoutInflater
            var view1 = inflater.inflate(R.layout.fragment_list_row1, null)

            var fTitle = view1.findViewById<TextView>(R.id.fragment_title)

            fTitle.setText(names[position])
            return view1
        }

        override fun getItem(position: Int): Any {
            return position
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return 4
        }
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when(item?.itemId){
            R.id.app_bar_setting ->{
                //전체 세팅
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun whichFab()
    {
        if (isGroupFragment)
        {
            fab.hide()
            groupFab.show()
            groupFab.isClickable = true

        }
        else //groupFragment = false
        {
            fab.show()
            groupFab.hide()
            groupFab.isClickable = false

        }
    }

    /*override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.group_fragment ->{
                groupFragment = GroupFragment()
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.nav_host_fragment, groupFragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit()

                isGroupFragment = true
                whichFab()

            }
            R.id.daily_calender ->{
                dailyFragment = DailyFragment()
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.nav_host_fragment, dailyFragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit()

                isGroupFragment = false
                whichFab()

            }
            R.id.week_calender ->{
                weekFragment = WeekFragment()
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.nav_host_fragment, weekFragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit()

                isGroupFragment = false
                whichFab()



            }
            R.id.month_calender ->{
                monthFragment = MonthFragment()
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.nav_host_fragment, monthFragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit()

                isGroupFragment = false
                whichFab()

            }
        }
        drawer_layout.closeDrawer(GravityCompat.START)

        return true
    }*/

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onContentChanged() {
        super.onContentChanged()
    }

    /*override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }*/



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
        val databaseReference = firebaseDatabase.reference
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
            shareAble
        )
        dataArray.add(schedule)
        var a: String = schedule.scheduleName
        databaseReference.child("Users").child(userName).child("Schedule").child(tag).child(dateMonth.toString()).setValue(dataArray)


    }
    fun insertGroup(
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
        val databaseReference = firebaseDatabase.reference
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
            shareAble
        )
        dataArray.add(schedule)
        var a: String = schedule.scheduleName
        databaseReference.child("Groups").child(userName).child("Schedule").child(dateMonth.toString()).setValue(dataArray)


    }
    public data class Info(var userName:String = "",
                           var userInfo:String? = null,
                           var userType:String? = "Group") {

    }
    fun insertGroupInfo(
        userName: String,
        userInfo: String?
    ) {
        val databaseReference = firebaseDatabase.reference
        val groupInfo = Info(
            userName,
            userInfo
        )
        databaseReference.child("Users").child(userName).child("UserInfo").setValue(groupInfo)


    }

    fun setAlarmScheduleOnCalendar(schedule: HashMap<String, Any>) {
        val scheduleName = schedule.get("scheduleName").toString()
        val scheduleInfo = schedule.get("scheduleInfo").toString()
        val startTime = schedule.get("startTime").toString()
        val endTime = schedule.get("endTime").toString()
        val dateYear = schedule.get("dateYear").toString().toInt()
        val dateMonth = schedule.get("dateMonth").toString().toInt()
        val date = schedule.get("date").toString().toInt()

        val alarmAble = schedule.get("alarm")

        val currentHour = CalendarInfo.calendar.get(Calendar.HOUR_OF_DAY)
        val currentMinute = CalendarInfo.calendar.get(Calendar.MINUTE)

        val startTimeHour= if (startTime.length>3) startTime.slice(IntRange(0, 1)).toInt() else startTime.slice(IntRange(0, 0)).toInt()
        val startTimeMinute = if (startTime.length>3) startTime.slice(IntRange(2, 3)).toInt() else startTime.slice(IntRange(1, 2)).toInt()


        if(alarmAble==true && CalendarInfo.currentYear ==dateYear && (CalendarInfo.currentMonth +1)==dateMonth && CalendarInfo.currentDate ==date
            && currentHour<startTimeHour) {
            Log.d("알람1", "들어옴")
            Alarm(scheduleName, scheduleInfo, dateYear, dateMonth, date, startTimeHour, startTimeMinute, endTime)
        }

        if(alarmAble==true && CalendarInfo.currentYear ==dateYear && (CalendarInfo.currentMonth +1)==dateMonth && CalendarInfo.currentDate ==date
            && currentHour==startTimeHour && currentMinute<=startTimeMinute){
            Log.d("알람1", "들어옴")
            Alarm(scheduleName, scheduleInfo, dateYear, dateMonth, date, startTimeHour, startTimeMinute, endTime)
        }

    }

    fun Alarm(scheduleName: String, scheduleInfo: String?, dateYear: Int, dateMonth: Int, date: Int,
              startTimeHour: Int, startTimeMinute:Int, endTime: String) {

        val notificationmanager
                = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notificationId = dateYear + dateMonth + date + startTimeHour + startTimeMinute + endTime.toInt()
        val notyintent = Intent(this@MainActivity, BroadCastD::class.java) //BroadCastD 클래스로 보낼 intent
        notyintent.putExtra("notificationId", notificationId)
        notyintent.putExtra("scheduleName", scheduleName)
        notyintent.putExtra("scheduleInfo", scheduleInfo)
        notyintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)

        val alcal = Calendar.getInstance()
        alcal.set(dateYear, dateMonth, date, startTimeHour, startTimeMinute)

        val pendingIntent = PendingIntent.getActivity(
            this, // context 정보
            notificationId, // 여러개의 알람을 등록하기 위한 primary id 값 세팅
            notyintent, // 정보가 담긴 intent
            PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = Notification.Builder(this,NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_btn_speak_now)
            .setWhen(alcal.timeInMillis)
            .setNumber(dateYear + dateMonth + date + startTimeHour + startTimeMinute + endTime.toInt())
            .setContentTitle(scheduleName)
            .setContentText(scheduleInfo)
            .setColor(Color.RED)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        if (Build.VERSION.SDK_INT >= 26){
            val channelName :CharSequence = "noty_channel"
            val importance :Int = NotificationManager.IMPORTANCE_HIGH

            val channel: NotificationChannel = NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName , importance)

            notificationmanager.createNotificationChannel(channel)
        }

        notificationmanager.notify(notificationId,builder.build())


        //val am = getSystemService(Context.ALARM_SERVICE) as AlarmManager



        //Log.d("In Alarm", "Id : " + notificationId + " scheduleName : "+ scheduleName + " scheduleInfo : "+ scheduleInfo)

        /*
        val sender = Pendin
        gIntent.getBroadcast(
            this, // context 정보
            notificationId, // 여러개의 알람을 등록하기 위한 primary id 값 세팅
            intent, // 정보가 담긴 intent
            0)

         */


        //calendar.set(2019,11,19,2,59)

        //val notificationmanager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


        /*
        val builder = Notification.Builder(this,NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_btn_speak_now)
            .setWhen(calendar.timeInMillis)
            .setNumber(dateYear + dateMonth + date + startTimeHour + startTimeMinute + endTime.toInt())
            .setContentTitle(scheduleName)
            .setContentText(scheduleInfo)
            .setColor(Color.RED)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
         */


        /*
        if (Build.VERSION.SDK_INT >= 26){
            val channelName :CharSequence = "noty_channel"
            val importance :Int = NotificationManager.IMPORTANCE_HIGH

            val channel: NotificationChannel = NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName , importance)

            notificationmanager.createNotificationChannel(channel)
        }
         */



        /*
        val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
        val wakeLock = powerManager.newWakeLock(
            PowerManager.FULL_WAKE_LOCK or PowerManager.ACQUIRE_CAUSES_WAKEUP or PowerManager.ON_AFTER_RELEASE,
            "My:Tag") // 스마트폰 화면이 꺼져있으면 화면 켜고 알림 울리기위해서 (FULL_WAKE_LOCK)
        wakeLock.acquire(5000)
         */

        //notificationmanager.notify(dateYear + dateMonth + date + startTimeHour + startTimeMinute + endTime.toInt(), builder.build())
        /*
        am.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
        am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, sender)
        Log.d("In Alarm", "after am set")

         */
    }




}