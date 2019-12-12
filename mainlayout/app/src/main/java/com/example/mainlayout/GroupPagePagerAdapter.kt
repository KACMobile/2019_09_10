package com.example.mainlayout

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
//그룹 패이지 탭레이아웃 어댑터 - 황선혁
public class GroupPagePagerAdapter(fm: FragmentManager, tabcount: Int, userInfo: UserInfo): FragmentStatePagerAdapter(fm) {
    val mtabcount = tabcount
    val muserInfo = userInfo
    override fun getItem(position: Int): Fragment {
        val args: Bundle = Bundle()
        args.putSerializable("userInfo", muserInfo)
        val fragment = when(position){
            0-> PostFragment.newInstance()
            1-> GroupPageScheduleFragment.newInstance()
            else-> GroupPageInfoFragment.newInstance()
        }
        fragment.arguments =args
        return fragment
    }
    override fun getCount(): Int {
        return mtabcount
    }

    override fun getPageTitle(position: Int): CharSequence? {
        val title = when(position){
            0 -> "포스트"
            1 -> "일정"
            else -> "정보"
        }
        return title
    }
}