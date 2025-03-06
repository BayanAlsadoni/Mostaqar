package com.example.mostaqarapp.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mostaqarapp.R
import com.example.mostaqarapp.data.ChatData
import com.google.firebase.auth.FirebaseAuth

class ChatAdapter(val context: Context, val messageList: ArrayList<ChatData>):RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class SendViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val sendChat = itemView.findViewById<TextView>(R.id.tvSendMessage)
        init {
            if (sendChat == null) {
                Log.e("ChatAdapter", "sendChat TextView is null in SendViewHolder")
            }
        }
    }
    class ReciveViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val reciveChat = itemView.findViewById<TextView>(R.id.tvReciveMsg)
        init {
            if (reciveChat == null) {
                Log.e("ChatAdapter", "reciveChat TextView is null in ReciveViewHolder")
            }
        }
    }

    val numRecive = 1
    val numSend = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == 1){
            val view = LayoutInflater.from(context).inflate(R.layout.recive_chat_item,parent,false)
            return ReciveViewHolder(view)
        }else{
            val view = LayoutInflater.from(context).inflate(R.layout.send_chat_item,parent,false)
            return SendViewHolder(view)
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentMsg = messageList[position]
        if(holder.javaClass == SendViewHolder::class.java){
            val viewHolder = holder as SendViewHolder
            viewHolder.sendChat.text = currentMsg.message ?: "No message"
        }else if (holder.javaClass == ReciveViewHolder::class.java){
            val viewHolder= holder as ReciveViewHolder
            viewHolder.reciveChat.text = currentMsg.message ?: "No message"
        }
    }

    override fun getItemViewType(position: Int): Int {
        val currentMessage = messageList[position]
        if (FirebaseAuth.getInstance().currentUser?.uid.equals(currentMessage.senderId)){
            return numSend
        }else{
            return numRecive
        }
    }

}