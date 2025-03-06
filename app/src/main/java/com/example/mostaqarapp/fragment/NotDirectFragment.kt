package com.example.mostaqarapp.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mostaqarapp.R
import com.example.mostaqarapp.adapter.MostaqarItemAdapter
import com.example.mostaqarapp.data.HomeData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class NotDirectFragment : Fragment() {
    lateinit var firestore:FirebaseFirestore
    lateinit var MostAdapter:MostaqarItemAdapter
    lateinit var rvNotDirect:RecyclerView
    val homeData = ArrayList<HomeData>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_not_direct, container, false)
         rvNotDirect = root.findViewById<RecyclerView>(R.id.rvNotDirect)
        rvNotDirect.layoutManager = LinearLayoutManager(requireContext())

        firestore = Firebase.firestore
        lateinit var homeData : ArrayList<HomeData>
        homeData = ArrayList<HomeData>()

        MostAdapter = MostaqarItemAdapter(requireContext(),homeData)//
        rvNotDirect.adapter = MostAdapter
        getHomesFromFirebase()

        return  root
    }
    private fun getHomesFromFirebase(){
        Log.e("getHome","in function")
        firestore.collection("home").get()
            .addOnSuccessListener { result ->//
                homeData.clear()
                Log.e("getHome","get collection")
                for (document in result){
                    Log.e("getHome","getDocument${document}")
                    val home = HomeData(
                        document.getString("id"),
                        document.getString("ownerName"),
                        document.getString("ownerId"),
                        document.getString("ownerImage"),
                        document.getString("title"),
                        document.getString("price"),
                        document.getString("location"),
                        document.getString("description"),
                        document.getString("homeImage"),
                        document.getString("homeVideo"),
                        document.getString("homeType"),
                        document.getString("space"),
                        document.getString("stayTime"),
                    )
                    homeData.add(home)
                    Toast.makeText(context, "${home}", Toast.LENGTH_SHORT).show()
                }

                rvNotDirect.adapter?.notifyDataSetChanged()
            }.addOnFailureListener { exception ->
                Log.e("homeError","error in getHome from firebase")
            }
    }


}