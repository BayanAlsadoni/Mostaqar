package com.example.mostaqarapp.map

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.example.mostaqarapp.R
import com.example.mostaqarapp.databinding.ActivityAddMapsBinding
import com.example.mostaqarapp.fragment.AddHomeFragment

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class AddMapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityAddMapsBinding
    private lateinit var btnAddMapLocation:Button
    private var lat:Double=3.0
    private var lng:Double=3.0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        btnAddMapLocation = findViewById<Button>(R.id.btnAddMapLocation)

        binding = ActivityAddMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.add_map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
//        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))

        mMap.addMarker(MarkerOptions().icon(
            BitmapDescriptorFactory.defaultMarker(
                BitmapDescriptorFactory.HUE_ROSE)).position(sydney).title("Marker in Sydney"))
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney,9f))

        mMap.uiSettings.isMyLocationButtonEnabled=true


        mMap.setOnMapClickListener{point ->
             lat= point.latitude
             lng= point.longitude

            val i= Intent(this,AddHomeFragment::class.java)
            i.putExtra("home_latitude",lat)
            i.putExtra("home_longitude",lng)
            Log.e("byn", lat.toString()+ lng.toString())

            mMap.addMarker( MarkerOptions().position(LatLng(point.latitude, point.longitude))
                //.anchor(0.5f, 0.1f)
                .title("location")
                .snippet("your home location"))


        }
//        btnAddMapLocation.setOnClickListener {
//            finish()
//        }

    }
}