package com.example.mostaqarapp.map

import android.content.Intent
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mostaqarapp.R
import com.example.mostaqarapp.fragment.AddHomeFragment

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class AddLocationMapFragment : Fragment() {

    private val callback = OnMapReadyCallback { googleMap ->

        val sydney = LatLng(31.32535135135135, 34.29015667840477)
        googleMap.addMarker(MarkerOptions().icon(
            BitmapDescriptorFactory.defaultMarker(
                BitmapDescriptorFactory.HUE_ROSE)).position(sydney).title("Marker in Sydney"))
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney,9f))

        googleMap.uiSettings.isMyLocationButtonEnabled=true

        googleMap.setOnMapClickListener{point ->
            val lat= point.latitude
            val lon= point.longitude
            val i= Intent(requireContext(),AddHomeFragment::class.java)
            i.putExtra("home_latitude",lat)
            i.putExtra("home_longitude",lon)
            Log.e("byn", lat.toString()+ lon.toString())
            googleMap.addMarker( MarkerOptions().position(LatLng(point.latitude, point.longitude))
//                .anchor(0.5f, 0.1f)
                .title("product")
                .snippet("your product location"))

        }

    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_location_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

}