package com.example.mostaqarapp.fragment

import android.os.Bundle
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SaveFragment : Fragment() {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var savedList: ArrayList<HomeData>
    private lateinit var adapter: MostaqarItemAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_save, container, false)
        val rvSave = root.findViewById<RecyclerView>(R.id.rvSave)
        firestore = FirebaseFirestore.getInstance()
        savedList = arrayListOf()
        adapter = MostaqarItemAdapter(requireContext(),savedList)


        rvSave.layoutManager = LinearLayoutManager(requireContext())
        rvSave.adapter = adapter

        fetchSavedItems()
        return root
    }

    private fun fetchSavedItems() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val savedRef = firestore.collection("saved_items").document(userId).collection("items")

        savedRef.get()
            .addOnSuccessListener { documents ->
                savedList.clear()
                for (document in documents) {
                    val item = document.toObject(HomeData::class.java)
                    savedList.add(item)
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(requireContext(), "Error: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }
}