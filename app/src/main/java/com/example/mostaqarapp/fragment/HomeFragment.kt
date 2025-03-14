package com.example.mostaqarapp.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mostaqarapp.Activity.NotificationsActivity
import com.example.mostaqarapp.R
import com.example.mostaqarapp.adapter.FilterAdapter
import com.example.mostaqarapp.adapter.MostaqarItemAdapter
import com.example.mostaqarapp.data.HomeData
import com.example.mostaqarapp.map.MapsFragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.ArrayList

class HomeFragment : Fragment() {

    lateinit var firestore : FirebaseFirestore

    lateinit var homeList:ArrayList<HomeData>
    var isDuplicate = false
    lateinit var MostAdapter:MostaqarItemAdapter
    lateinit var rvHome:RecyclerView
    val homeData = ArrayList<HomeData>()

    var displayList :  ArrayList<HomeData> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root =  inflater.inflate(R.layout.fragment_home, container, false)

        val imgNotifi = root.findViewById<ImageView>(R.id.home_imgNotifi)
        val imgFilter = root.findViewById<ImageView>(R.id.imgFilter)
        rvHome = root.findViewById<RecyclerView>(R.id.rvHome)
        val search = root.findViewById<SearchView>(R.id.searchViewHome)

        val auth = Firebase.auth
        firestore = Firebase.firestore
        val dbRef = FirebaseDatabase.getInstance().getReference("tasks")

        val homeList = ArrayList<HomeData>()
        val desplayhome = ArrayList<HomeData>()
        var loc=""

        search.clearFocus()

        imgFilter.setOnClickListener {

            val bottomSheetDialog = BottomSheetDialog(requireContext())
            val bottomSheetLayout = LayoutInflater.from(requireContext()).inflate(R.layout.bottom_sheet,null)
            bottomSheetDialog.setContentView(bottomSheetLayout)
            bottomSheetDialog.show()
            var prog=200
            val tvFilterLocation =bottomSheetLayout.findViewById<TextView>(R.id.tvFilterLocation)
            val tvFilterPrice =bottomSheetLayout.findViewById<TextView>(R.id.tvFilterPrice)
            val lvFilter =bottomSheetLayout.findViewById<RecyclerView>(R.id.lvSortFilter)
            val lvFilterType =bottomSheetLayout.findViewById<RecyclerView>(R.id.lvFilterType)
            val seekBar =bottomSheetLayout.findViewById<SeekBar>(R.id.seekBar)

            val sortList= ArrayList<String>()
            sortList.add("ترتيب تصاعدي")
            sortList.add("ترتيب تنازلي")
            val typeList = ArrayList<String>()
            typeList.add("شقة")
            typeList.add("منزل")
            typeList.add("غرفة")

            lvFilter.adapter= FilterAdapter(requireContext(),sortList)
            lvFilter.layoutManager= LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)

            lvFilterType.adapter= FilterAdapter(requireContext(),typeList)
            lvFilterType.layoutManager= LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)

            seekBar.setOnSeekBarChangeListener(object :SeekBar.OnSeekBarChangeListener{
                override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                    tvFilterPrice.text = " السعر  $p1"
                    prog = p1
                }

                override fun onStartTrackingTouch(p0: SeekBar?) {

                }

                override fun onStopTrackingTouch(p0: SeekBar?) {

                }

            })
            tvFilterLocation.setOnClickListener {
                val popup = PopupMenu(context,tvFilterLocation)
                popup.menuInflater.inflate(R.menu.location_menu,popup.menu)
                popup.setOnMenuItemClickListener {item ->
                    Toast.makeText(context, "item$item", Toast.LENGTH_SHORT).show()
                    when(item.itemId){
                        R.id.flat_item -> loc="شقة"
                        R.id.room_item -> loc="غرفة"
                        R.id.home_item -> loc="منزل"
                    }
                    tvFilterLocation.text = loc
                    true
                }
                popup.show()

            }

    }

        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                desplayhome.clear()
                val searchQuery = newText?.lowercase(Locale.getDefault())?:""
//                if (searchQuery.isNotEmpty()) {
//                    desplayhome.addAll(homeData.filter {
//                        it.title?.lowercase(Locale.getDefault())?.contains(searchQuery)==true
//                    })}else {
//                        desplayhome.addAll(homeData)
//                }
//                homeRecyclerView.adapter?.notifyDataSetChanged()
                return true
            }
        })

        imgNotifi.setOnClickListener {
            startActivity(Intent(requireContext(), NotificationsActivity::class.java))
        }



        getHomesFromFirebase()

        return root
    }


    private fun sortAscending() {
//        displayList.
//        homeData.sortBy { it.title }
//        homeRecyclerView.adapter!!.notifyDataSetChanged()
    }
    private fun sortDescending() {
//        homeData.sortByDescending { it.title }
//        homeRecyclerView.adapter!!.notifyDataSetChanged()
    }

    private fun getHomesFromFirebase(){
        Log.e("getHome","in function")
        firestore.collection("home").get()
            .addOnSuccessListener { result ->
                homeData.clear()
                Log.e("getHome","get collection")
                for (document in result){
                    Log.e("getHome","getDocument${document}")
                    var location = document.getGeoPoint("mapLocation")
                    if(location == null){
                        location = GeoPoint(3.0,3.0)
                    }
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
                        document.getString("room"),
                        document.getString("bath"),
                        location.latitude,
                        location.longitude
                    )
                    homeData.add(home)
                    Toast.makeText(context, "${home}", Toast.LENGTH_SHORT).show()
                }
                MostAdapter = MostaqarItemAdapter(requireContext(),homeData)//
                rvHome.layoutManager = LinearLayoutManager(requireContext())
                rvHome.adapter = MostAdapter

                rvHome.adapter?.notifyDataSetChanged()
            }.addOnFailureListener { exception ->
                Log.e("homeError","error in getHome from firebase")
            }
    }


}