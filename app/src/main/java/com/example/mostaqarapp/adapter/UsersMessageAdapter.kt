package com.example.mostaqarapp.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.mostaqarapp.Activity.chat.ChatActivity
import com.example.mostaqarapp.R
import com.example.mostaqarapp.data.UsersMessageData
import java.util.*

class UsersMessageAdapter(val context: Context, val data: ArrayList<UsersMessageData>): BaseAdapter() {

    override fun getCount(): Int {
        return data.size
    }

    override fun getItem(p0: Int): Any {
        return data[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        var adapterView = p1
        if(p1 == null) {
            adapterView = LayoutInflater.from(context).inflate(R.layout.message_item, null, false)
        }
        val iv = adapterView!!.findViewById<ImageView>(R.id.imageMassegeItem)
        val tvName = adapterView.findViewById<TextView>(R.id.personNameMessageItem)
        val tvMessage = adapterView.findViewById<TextView>(R.id.messageMessageItem)
        val tvTime = adapterView.findViewById<TextView>(R.id.timeMessageItem)

        val prof = data[p0]
//        iv.setImageResource(prof.senderImage)
        tvName.text = prof.senderName
        tvMessage.text = prof.textMessage
        tvTime.text = prof.timestamp
        adapterView.setOnClickListener {
            val goToChat = Intent(context, ChatActivity::class.java)
            goToChat.putExtra("sender_id", data[p0].uid)
            goToChat.putExtra("sender_name", data[p0].senderName)
            goToChat.putExtra("sender_image", data[p0].senderImage)
            context.startActivity(goToChat)
        }
//        btn.setOnClickListener{
//            Toast.makeText(context,"Button clicked",Toast.LENGTH_SHORT).show()
//        }

        return adapterView

    }
}