package com.example.mostaqarapp.adapter

import android.content.Context
import android.graphics.Color
import android.provider.CalendarContract.Colors
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.mostaqarapp.R
import com.example.mostaqarapp.data.CommentMessage


class CommentAdapter (val context: Context, val messageList:ArrayList<CommentMessage>):
    RecyclerView.Adapter<CommentAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val comment_tvMessage = itemView.findViewById<TextView>(R.id.comment_tvMessage)
        val tvUserName = itemView.findViewById<TextView>(R.id.comment_tvUserName)
        val time_comment = itemView.findViewById<TextView>(R.id.comment_tvTime)
        val comment_imgProfile = itemView.findViewById<ImageView>(R.id.comment_imgProfile)
//        val group_message_layout = itemView.findViewById<LinearLayout>(R.id.group_message_layout)
//        val layout_content = itemView.findViewById<ConstraintLayout>(R.id.layout_content)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.comment_item,parent,false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

//        holder.group_message_layout.gravity = Gravity.START
//        holder.layout_content.setBackgroundResource(R.drawable.comment_shap)
//        holder.name_comment_writer_txt.setTextColor(R.color.black.toInt())
//addpicasi for image
        val currentMessage = messageList[position]
        holder.comment_tvMessage.text = currentMessage.message
        holder.tvUserName.text = currentMessage.senderName
        holder.time_comment.text = currentMessage.timeStamp
        Log.d("msg", "current msg in adapter recieve ${currentMessage.message}")
        Log.d("msg", "current msg in adapter recieve text view ${holder.comment_tvMessage.text}")
    }
}