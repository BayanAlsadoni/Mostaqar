package com.example.mostaqarapp.fragment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.mostaqarapp.R
import com.example.mostaqarapp.adapter.MostaqarItemAdapter
import com.example.mostaqarapp.data.HomeData
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class UserHomesActivity : AppCompatActivity() {
    val homeData = ArrayList<HomeData>()
    lateinit var firestore:FirebaseFirestore
    lateinit var adapter: MostaqarItemAdapter
    lateinit var rvUserHome: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_homes)
         rvUserHome = findViewById<RecyclerView>(R.id.rvUserHome)
        firestore = Firebase.firestore

        adapter = MostaqarItemAdapter(this,homeData)//
        rvUserHome.adapter = adapter
        getHomesFromFirebase()

    }

    private fun getHomesFromFirebase(){
        Log.e("getHome","in function")
        firestore.collection("home").whereEqualTo("ownerId",Firebase.auth.currentUser!!.uid).get()
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
                    Toast.makeText(this, "${home}", Toast.LENGTH_SHORT).show()
                }

                rvUserHome.adapter?.notifyDataSetChanged()
            }.addOnFailureListener { exception ->
                Log.e("homeError","error in getHome from firebase")
            }
    }



}