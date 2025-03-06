package com.example.mostaqarapp.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mostaqarapp.Activity.register.LoginActivity
import com.example.mostaqarapp.R
import com.example.mostaqarapp.data.HomeData

class DetailsAdapter (val context: Context, val data: ArrayList<String>): RecyclerView.Adapter<DetailsAdapter.AdapterHolder>() {
    class AdapterHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var imghome = itemView.findViewById<ImageView>(R.id.imgHomeDteaile)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DetailsAdapter.AdapterHolder {
        val homeLayout = LayoutInflater.from(context).inflate(R.layout.home_details,parent,false)
        return AdapterHolder(homeLayout)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: DetailsAdapter.AdapterHolder, position: Int) {
        val home = data[position]

//        holder.homeImage.setImageResource(home.homeImage)
//        holder.imghome.setImageResource()
}
}