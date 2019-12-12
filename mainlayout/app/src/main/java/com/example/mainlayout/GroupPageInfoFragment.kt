package com.example.mainlayout

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class GroupPageInfoFragment : Fragment(), OnMapReadyCallback {
    lateinit var mapView:MapView
    var mlatitude:Double? = null
    var mlongitude:Double? = null
    lateinit var mUsername:String
    lateinit var mUserInfo:String
    var googleMap: GoogleMap? = null
    companion object {
        fun newInstance() = GroupPageInfoFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.group_page_info_fragment, container, false)
        val userInfo = arguments!!.getSerializable("userInfo") as UserInfo
        val infoText = rootView.findViewById<TextView>(R.id.group_page_Info_text)
        val userHomepage = rootView.findViewById<TextView>(R.id.group_page_info_userHomepage)
        val userTEL = rootView.findViewById<TextView>(R.id.group_page_info_userTEL)

        mlatitude = userInfo.locateLat
        mlongitude = userInfo.locateLng
        mUsername = userInfo.userNames
        mUserInfo = userInfo.userInfos
        infoText.text = userInfo.userInfos
        if(userInfo.userHomepage != "null") userHomepage.text = userInfo.userHomepage
        else userHomepage.text = "등록된 URL이 없습니다"
        if(userInfo.userTEL != "null") userTEL.text = userInfo.userTEL
        else userTEL.text = "등록된 전화번호가 없습니다"



        //mapView.getMapAsync(this)

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView = view.findViewById(R.id.group_page_info_map) as MapView
        if(mlatitude == null || mlongitude == null){
            mapView.visibility = View.GONE
        }
        else {
            mapView.onCreate(savedInstanceState)
            mapView.onResume()
            mapView.getMapAsync(this)
        }
    }

    override fun onMapReady(map: GoogleMap?) {
        googleMap = map
        val location = LatLng(mlongitude!!, mlatitude!!)
        val markerOptions = MarkerOptions()
        markerOptions.position(location)
        markerOptions.title(mUsername)
        markerOptions.snippet(mUserInfo)
        googleMap!!.addMarker(markerOptions)
        googleMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))


    }


}
