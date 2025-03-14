package com.example.mostaqarapp.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CheckedTextView
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.mostaqarapp.R
import com.example.mostaqarapp.data.UserData

class SpecificNotificationAdapter(val context: Context, val userList:ArrayList<UserData>):
    RecyclerView.Adapter<SpecificNotificationAdapter.NotifViewHolder>() {

    val notifList = ArrayList<UserData>()

    class NotifViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val txt_view_notification = itemView.findViewById<TextView>(R.id.txt_view_notification)
        val NotificatioUserImage = itemView.findViewById<ImageView>(R.id.NotificatioUserImage)
        val checkBoxNotification = itemView.findViewById<CheckBox>(R.id.checkBoxNotification)
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotifViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.specific_notification_item,parent,false)
        return NotifViewHolder(view)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: NotifViewHolder, position: Int) {
        val currentUser = userList[position]
        var name = ""
//        if(currentUser.uid == 0){
//            name = currentUser.name
//            holder.NotificatioUserImage.setImageResource(R.drawable.patient)
//
//        }else if (currentUser.id == 1){
//            name = "د. ${currentUser.name}"
//            holder.NotificatioUserImage.setImageResource(R.drawable.doctor)
//
//        }
        holder.txt_view_notification.text = name
        holder.itemView.setOnClickListener {
            holder.checkBoxNotification.isChecked = !holder.checkBoxNotification.isChecked
        }
        if (holder.checkBoxNotification.isChecked){
            Toast.makeText(context, "checked true", Toast.LENGTH_SHORT).show()
        }
    }
}