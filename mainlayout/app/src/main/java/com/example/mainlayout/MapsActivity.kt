package com.example.mainlayout

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.location.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.SearchView

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import java.io.IOException
import android.content.Intent.CATEGORY_DEFAULT
import android.content.pm.PackageManager
import android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS
import android.location.LocationManager.GPS_PROVIDER
import android.os.Build
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat



class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var geocoder: Geocoder
    private var mMarkerOptions: MarkerOptions = MarkerOptions()
    private lateinit var previousMarker: Marker
    lateinit var lm:LocationManager
    var requestCode: Int? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        val actionBar = supportActionBar
        actionBar!!.title = ""
        actionBar!!.setDisplayHomeAsUpEnabled(true)
        requestCode =intent.getIntExtra("RequestCode", 0)


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        geocoder = Geocoder(this)
        if( requestCode== 1001) {
            lm = getSystemService(Context.LOCATION_SERVICE) as LocationManager

            if (!lm.isProviderEnabled(GPS_PROVIDER)) {
                //GPS 설정화면으로 이동
                val intent = Intent(ACTION_LOCATION_SOURCE_SETTINGS)
                intent.addCategory(CATEGORY_DEFAULT)
                startActivity(intent)
                finish()
            }
            if (Build.VERSION.SDK_INT >= 23) {
                //권한이 없는 경우
                if (ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS
                    ) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    Log.d("a", "This is 2222?!?")
                    val permissionArray = arrayOf(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    )
                    ActivityCompat.requestPermissions(this, permissionArray, 1);
                }
                //권한이 있는 경우
                else {
                    requestMyLocation();
                }
            }
            //마시멜로 아래
            else {
                requestMyLocation();
            }




            mMap.setOnMapClickListener(object : GoogleMap.OnMapClickListener {
                override fun onMapClick(point: LatLng?) {
                    if (point != null) {
                        val lat = point.latitude
                        val lng = point.longitude
                        addMarkerOnMap(lat, lng, null)
                    }
                }

            })
        }
        else{
            val lat = intent.getDoubleExtra("Lat", 0.0)
            val lng = intent.getDoubleExtra("Lng", 0.0)
            Log.d("a" , "This is" + lat + lng)
            addMarkerOnMap(lat,lng,null)
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode === 1) {
            //권한받음
            if (grantResults.size > 0 && grantResults[0] === PackageManager.PERMISSION_GRANTED) {
                requestMyLocation()
            } else {
                Toast.makeText(this, "권한없음", Toast.LENGTH_SHORT).show()
                finish()
            }//권한못받음
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if( requestCode== 1001) {
            val inflater = menuInflater
            inflater.inflate(R.menu.activity_maps_menu, menu)
            val searchItem = menu.findItem(R.id.maps_search)
            val searchView = searchItem.actionView as SearchView


            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

                override fun onQueryTextSubmit(query: String?): Boolean {
                    var addressList: List<Address>? = null
                    try {
                        addressList = geocoder.getFromLocationName(query, 10)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                    if (addressList != null && addressList.size != 0) {

                        val splitStr: List<String> = addressList.get(0).toString().split(",")
                        val adress: String = splitStr[0].substring(
                            splitStr[0].indexOf("\"") + 1,
                            splitStr[0].length - 2
                        )
                        val lat: Double = addressList.get(0).latitude
                        val lng: Double = addressList.get(0).longitude

                        val point = LatLng(lat, lng)
                        addMarkerOnMap(lat, lng, adress)
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 15f))
                    } else
                        Toast.makeText(this@MapsActivity, "검색결과가 없습니다", Toast.LENGTH_LONG).show()
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return true

                }
            })
            return true
        }
        return true
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home-> {
                finish()
            }

            R.id.setlocation->{
                val resultIntent = Intent()
                resultIntent.putExtra("latitude",previousMarker.position.latitude)
                resultIntent.putExtra("longitude",previousMarker.position.longitude)
                setResult(RESULT_OK, resultIntent)
                finish()

            }


        }
        return super.onOptionsItemSelected(item)
    }



    fun requestMyLocation(){
        val locationListener = object:LocationListener{
            override fun onLocationChanged(location: Location?) {
                Log.d("a","This is 여기" )
                if(location!= null) {
                    val lat = location.latitude
                    val lng = location.longitude
                    addMarkerOnMap(lat,lng, null)

                    lm.removeUpdates(this)
                }
            }

            override fun onProviderDisabled(p0: String?) {

            }

            override fun onProviderEnabled(p0: String?) {

            }

            override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {

            }

        }

        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000,10f,locationListener)
        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,1000,10f,locationListener)

    }

    fun addMarkerOnMap(lat:Double, lng:Double, snippet:String?){
        val latLng = LatLng(lat, lng)
        mMarkerOptions.title("현재 위치")
        if(snippet!=null)
            mMarkerOptions.snippet(snippet)
        else
            mMarkerOptions.snippet(lat.toString() + ", " + lng.toString())
        mMarkerOptions.position(latLng)
        if (::previousMarker.isInitialized)
            previousMarker.remove()
        previousMarker = mMap.addMarker(mMarkerOptions)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))

    }

}
