package com.example.mostaqarapp.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.mostaqarapp.Activity.DetailesActivity
import com.example.mostaqarapp.R
import com.example.mostaqarapp.data.HomeData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import java.util.*

//import com.squareup.picasso.Picasso

class MostaqarItemAdapter(val context: Context, val data: ArrayList<HomeData>):RecyclerView.Adapter<MostaqarItemAdapter.AdapterHolder>() {
    class AdapterHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val tvHomeTitle = itemView.findViewById<TextView>(R.id.tvHomeTitle)
        val tvHomeLocation = itemView.findViewById<TextView>(R.id.tvHomeLocation)
        val tvHomePrice = itemView.findViewById<TextView>(R.id.tvHomePrice)
//        val tvpublishTime = itemView.findViewById<TextView>(R.id.tvpublishTime)
//        val tvPublisherName = itemView.findViewById<TextView>(R.id.tvPublisherName)
//        var publisherImage = itemView.findViewById<ImageView>(R.id.publisherImage)
        var homeImage = itemView.findViewById<ImageView>(R.id.homeImage)
        var imgBtnSave = itemView.findViewById<ImageButton>(R.id.imgBtnSave)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterHolder {
        val homeLayout = LayoutInflater.from(context).inflate(R.layout.mostaqar_item,parent,false)
        return AdapterHolder(homeLayout)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: AdapterHolder, position: Int) {
        val home = data[position]

        home.ownerId
        home.ownerImage
        home.title

        home.description
        home.homeImage
        home.homeVideo

        home.space
        home.stayTime
        Picasso.get().load(home.homeImage).placeholder(R.drawable.home_details) // Add a default image in `res/drawable`
            .error(R.drawable.homeone).into(holder.homeImage)
//        Picasso.get().load(home.personImage).into()

        holder.tvHomeLocation.text = home.location
        holder.tvHomePrice.text = home.price
        holder.tvHomeTitle.text = home.title

        holder.itemView.setOnClickListener {
            val gotToDitailesActivity = Intent(context, DetailesActivity::class.java)//DetailesActivity fraggment
            gotToDitailesActivity.putExtra("home",home)
            context.startActivity(gotToDitailesActivity)

        }
        var isSaved = false

        holder.imgBtnSave.setOnClickListener {
            val userId = FirebaseAuth.getInstance().currentUser?.uid
            val firestore = FirebaseFirestore.getInstance()
            val savedRef = firestore.collection("saved_items").document(userId!!).collection("items")

            if(!isSaved){
                isSaved= true
                savedRef.document(home.id!!).set(home)
                    .addOnSuccessListener {
                        Toast.makeText(context, "Saved successfully!", Toast.LENGTH_SHORT).show()
                        holder.imgBtnSave.setImageResource(R.drawable.ic_fill_save)
                    }
                    .addOnFailureListener {
                        Toast.makeText(context, "Failed to save!", Toast.LENGTH_SHORT).show()
                    }
            }else{
                isSaved=false
                savedRef.document(home.id!!).delete().addOnSuccessListener {
                    holder.imgBtnSave.setImageResource(R.drawable.ic_save)
                    Toast.makeText(context, "delete from save successfully!", Toast.LENGTH_SHORT).show()
                }
                    .addOnFailureListener {
                        Toast.makeText(context, "Failed to delete!", Toast.LENGTH_SHORT).show()
                    }

            }


        }

    }
}