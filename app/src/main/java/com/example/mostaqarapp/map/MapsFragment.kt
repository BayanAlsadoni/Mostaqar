package com.example.mostaqarapp.map

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.mostaqarapp.R
import com.example.mostaqarapp.data.HomeData
import com.example.mostaqarapp.fragment.AddHomeFragment
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MapsFragment : Fragment() {
    var homeLocation = ArrayList<GeoPoint>()

    private val callback = OnMapReadyCallback { googleMap ->
        val sydney = LatLng(-34.0, 151.0)
        googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))

        Log.e("getHome","in function")
        Firebase.firestore.collection("home").get()
            .addOnSuccessListener { result ->//
                homeLocation.clear()
                Log.e("getHome","get collection")
                for (document in result){
                    Log.e("getHome","getDocument${document}")

                    val loc = document["mapLocation"] as GeoPoint
                    googleMap.addMarker(MarkerOptions().icon(
                        BitmapDescriptorFactory.defaultMarker(
                            BitmapDescriptorFactory.HUE_ROSE)).position(LatLng(loc.latitude,loc.longitude)).title("home location"))
                    homeLocation.add(loc)
                }

            }.addOnFailureListener { exception ->
                Log.e("homeError","error in getHome from firebase")
            }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_maps, container, false)
        getLastLocation()
        return root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    private fun getLastLocation(){//
        val locaton= LocationServices.getFusedLocationProviderClient(requireContext())

        if (ActivityCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        locaton.lastLocation
            .addOnSuccessListener {ll ->
                if(ll != null){
                    val latitude= ll.latitude
                    val longtude= ll.longitude
                    Log.e("byn",longtude.toString())
                    Log.e("byn",latitude.toString())
                    Log.e("byn",ll.provider!!)
                }else{
                    Log.e("byn","location is null")
                    Log.e("byn",ll.toString())
                    Log.e("byn",ll?.latitude.toString())
                }
                Log.e("byn","success")
            }.addOnFailureListener {
                Log.e("byn","error in location")
            }
        Log.e("byn","end")

    }

}