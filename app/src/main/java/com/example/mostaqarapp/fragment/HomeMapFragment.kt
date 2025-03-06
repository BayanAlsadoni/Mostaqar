package com.example.mostaqarapp.fragment

import android.Manifest
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
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class HomeMapFragment : Fragment() {

    //in signup val ge= GeoPoint(latitude!!,longtude!!)
    lateinit var locationClient: FusedLocationProviderClient
    private var latitude: Double?=null
    private var longtude: Double?=null

    private val callback = OnMapReadyCallback { googleMap ->
//        val sydney = LatLng(-34.0, 151.0)
//        googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
//        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))

        val mMap = googleMap
        // getLastLocation()
        // Add a marker in Sydney and move the camera

        val laa= requireActivity().intent.getDoubleExtra("laa",31.37535135135135)
        val loo= requireActivity().intent.getDoubleExtra("loo",34.28015667840477)

        val userLocation = LatLng(laa, loo)
        mMap.addMarker(MarkerOptions().position(userLocation).title("your location"))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocation,10f))


        val la=requireActivity().intent.getDoubleExtra("lattpro",31.32535135135135)
        val lo=requireActivity().intent.getDoubleExtra("lontpro",34.29015667840477)


        val product = LatLng(la!!, lo!!)
        mMap.addMarker(MarkerOptions().icon(
            BitmapDescriptorFactory.defaultMarker(
                BitmapDescriptorFactory.HUE_ROSE)).position(product).title("product location"))




    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home_map, container, false)
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
                    latitude= ll.latitude
                    longtude= ll.longitude
                    Log.e("byn",longtude.toString())
                    Log.e("byn",latitude.toString())
                    Log.e("byn",ll.provider!!)
                }else{
                    Log.e("byn","location is null")
                    Log.e("byn",ll.toString())
                    Log.e("byn",ll?.latitude.toString())
                }
//                Log.e("byn",ll.latitude.toString())
                Log.e("byn","success")
            }.addOnFailureListener {
                Log.e("byn","error in location")
            }
        Log.e("byn","end")

    }

    fun getLoc(){
        val data = ArrayList<HomeData>()
        Firebase.firestore.collection("homes").get()
            .addOnSuccessListener { querSnapshot ->
                for (document in querSnapshot){
                    document.data
                    val homeItems= HomeData(document.get("productName").toString(), document.get("productRate").toString(),
                        document.get("productDescription").toString(), document.get("productLocation").toString(),
                        document.get("productPrice").toString(),document.get("ProductImage").toString(), document.get("productCategory").toString())
                    data.add(homeItems)
                }
//                rvGetUserProduct.adapter=AdapterProductUser(this,data)
            }.addOnFailureListener {
                Toast.makeText(requireContext(), "failed to get products", Toast.LENGTH_SHORT).show()
                Log.e("byn","failed to get product to user")
            }
    }


}