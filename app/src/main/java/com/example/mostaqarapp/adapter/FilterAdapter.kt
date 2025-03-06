package com.example.mostaqarapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mostaqarapp.R
import java.util.ArrayList

class FilterAdapter(val context: Context, val data: ArrayList<String>):  RecyclerView.Adapter<FilterAdapter.AdapterHolder>() {
    class AdapterHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var tvFliter = itemView.findViewById<TextView>(R.id.tvFilterItem)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FilterAdapter.AdapterHolder {
        val root = LayoutInflater.from(context).inflate(R.layout.filter_item,parent,false)
        return AdapterHolder(root)
    }
    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: FilterAdapter.AdapterHolder, position: Int) {
        data[position]
        holder.tvFliter!!.text = data[position]
        var filter=""
        holder.tvFliter.setOnClickListener {
            holder.tvFliter.setBackgroundResource(R.color.blue)
            holder.tvFliter.setTextColor(R.drawable.filter)
            filter=holder.tvFliter.text.toString()
        }
    }

}


