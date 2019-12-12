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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import java.util.prefs.Preferences


class MainActivity : AppCompatActivity() {

    lateinit var dailyFragment: DailyFragment
    lateinit var weekFragment: WeekFragment
    lateinit var monthFragment: MonthFragment
    lateinit var groupFragment: GroupFragment

    private var userID:String? = "User01"

    private val firebaseDatabase = FirebaseDatabase.getInstance()

    private val databaseReference = firebaseDatabase.reference

    private var mUserInfo: UserInfo? = null

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
    lateinit var groupFab1 : FloatingActionButton
    lateinit var groupFab2 : FloatingActionButton
    lateinit var groupFab3 : FloatingActionButton


    lateinit var muserInfo: UserInfo


    private var REQUEST_TEST :Int = 1



    lateinit var mAuth :FirebaseAuth
    var  mUser: FirebaseUser? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        mUser = mAuth.currentUser
        if(mUser != null && resultCode == RESULT_OK){
            storeUserInfo()

        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {

        mAuth = FirebaseAuth.getInstance()
        mUser = mAuth.currentUser

        //로그인이 안되어 있으면 GoogleLoginActivity 실행 - 조성완
        if(mUser==null){
            var intent = Intent(this@MainActivity, GoogleLoginActivity::class.java)
            startActivityForResult(intent, REQUEST_TEST)
        }else{
            storeUserInfo()

        }


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolBar)

        val listView1 : ListView = findViewById(R.id.navigation_drawer_list1)
        val listView2 : ListView = findViewById(R.id.navigation_drawer_list2)
        // 체크리스트 어댑터 및 db에서 정보 가져오기 - 안용수, 황선혁
        var gList = ArrayList<String>()
        var cList = ArrayList<Int>()

        listView1.adapter = ListAdapter(this)
        listView2.adapter = CheckListAdapter(gList, cList,this)

        val userfollow = databaseReference.child("Users/" + userID + "/Follow")
        userfollow.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                gList.clear()

                for(snapshot in dataSnapshot.children){
                    if(snapshot.key.toString() == "Groups"){
                        for(deeperSnapshot in snapshot.children){
                            gList.add(deeperSnapshot.key.toString())
                            cList.add(deeperSnapshot.value.toString().toInt())
                            (listView2.adapter as BaseAdapter).notifyDataSetChanged()
                        }
                    }
                    if(snapshot.key.toString() == "Users") {
                        for(deeperSnapshot in snapshot.children){
                            gList.add(deeperSnapshot.key.toString())
                            cList.add(deeperSnapshot.value.toString().toInt())
                            (listView2.adapter as BaseAdapter).notifyDataSetChanged()
                        }


                    }


                }
            }
            override fun onCancelled(p0: DatabaseError) {

            }
        })






        //메뉴 열고 닫기 - 안용수
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


        //fab 설정 - 안용수
        fab = findViewById(R.id.fab)
        groupFab = findViewById(R.id.groupfab)
        groupFab1 = findViewById(R.id.groupfab1)
        groupFab2 = findViewById(R.id.groupfab2)
        groupFab3 = findViewById(R.id.groupfab3)
        whichFab()

        fabOpen = AnimationUtils.loadAnimation(this, R.anim.fab_open)
        fabClose = AnimationUtils.loadAnimation(this, R.anim.fab_close)
        rotateForward = AnimationUtils.loadAnimation(this, R.anim.rotate_forward)
        rotateBackward = AnimationUtils.loadAnimation(this, R.anim.rotate_backward)
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

        //fragment관련 메뉴 설정 리스너 - 안용수
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

        val navView: NavigationView = findViewById(R.id.nav_view)
        navView.bringToFront();


        //처음에 dailyFragment실행 - 안용수
        dailyFragment = DailyFragment()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.nav_host_fragment, dailyFragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()


        //내userInfo 받아오기 - 황선혁
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



    }
    ////

    //메뉴 리스트 1
    //fragment관련 메뉴 설정 어댑터 - 안용수
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
    //유저 정보 sharedpreference로 저장 - 조성완, 황선혁
    fun storeUserInfo(){
        userID = mUser!!.displayName
        val idPreference = getSharedPreferences("UserID", Context.MODE_PRIVATE)
        val editor = idPreference.edit()
        editor.putString("UserID", userID)
        editor.commit()

        mUserInfo = UserInfo(userID!!, "좋은 하루 되세요", "Users",mAuth.currentUser!!.photoUrl.toString())
        databaseReference.child("Users").child(userID!!).child("UserInfo").setValue(mUserInfo)

    }
    //fab 컨트롤 - 안용수
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
            groupFab1.hide()
            groupFab2.hide()
            groupFab3.hide()

            groupFab.isClickable = false


        }
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
    //Notify -조성완
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
            Alarm(scheduleName, scheduleInfo, dateYear, dateMonth, date, startTimeHour, startTimeMinute, endTime)
        }

        if(alarmAble==true && CalendarInfo.currentYear ==dateYear && (CalendarInfo.currentMonth +1)==dateMonth && CalendarInfo.currentDate ==date
            && currentHour==startTimeHour && currentMinute<=startTimeMinute){
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



    }




}