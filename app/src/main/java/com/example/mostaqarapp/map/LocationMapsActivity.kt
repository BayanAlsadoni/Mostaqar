package com.example.mostaqarapp.map

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mostaqarapp.R
import com.example.mostaqarapp.databinding.ActivityLocationMapsBinding

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.firebase.firestore.GeoPoint

class LocationMapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityLocationMapsBinding
//    var homeLocation = ArrayList<GeoPoint>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLocationMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.location_map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val lat = intent.getDoubleExtra("latitude",3.0)
        val lng = intent.getDoubleExtra("longitude",3.0)
//        val loc = document["mapLocation"] as GeoPoint
        googleMap.addMarker(MarkerOptions().icon(
            BitmapDescriptorFactory.defaultMarker(
                BitmapDescriptorFactory.HUE_ROSE)).position(LatLng(lat,lng)).title("home location"))
//        homeLocation.add(loc)

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }
}