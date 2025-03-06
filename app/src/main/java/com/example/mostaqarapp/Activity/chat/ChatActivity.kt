package com.example.mostaqarapp.Activity.chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mostaqarapp.R
import com.example.mostaqarapp.adapter.ChatAdapter
import com.example.mostaqarapp.data.ChatData
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase

class ChatActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val rcChat = findViewById<RecyclerView>(R.id.rcChat)
        val btnSendMessage = findViewById<FloatingActionButton>(R.id.btnSendMessage)
        val etSendMessage = findViewById<EditText>(R.id.etSendMessage)

        val uidReceiver = intent.getStringExtra("sender_id")
        val name = intent.getStringExtra("sender_name")
        val image = intent.getIntExtra("sender_image",R.drawable.ic_person_outline)


        val uidSender = Firebase.auth.currentUser?.uid
        val senderRoom = uidReceiver + uidSender
        val receiverRoom = uidSender+uidReceiver
        supportActionBar?.title = name///
        val dbRef = FirebaseDatabase.getInstance().getReference()


//add uid in login and register

        val chatData = ArrayList<ChatData>()
        val chatAdapter = ChatAdapter(this,chatData)

        rcChat.layoutManager = LinearLayoutManager(this)
        rcChat.adapter = chatAdapter

        dbRef.child("chats").child(senderRoom).child("messages")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot){
                    chatData.clear()

                    for(postSnapshot in snapshot.children) {
                        val message = postSnapshot.getValue(ChatData::class.java)
                        chatData.add(message!!)
                    }
                    chatAdapter.notifyDataSetChanged()
                chatAdapter.notifyItemInserted(chatData.size -1)
                rcChat.scrollToPosition(chatData.size -1)
                }

                override fun onCancelled(p0: DatabaseError) {

                }
            })

        btnSendMessage.setOnClickListener{
            val message = etSendMessage.text.toString()
            val timestamp = System.currentTimeMillis()
            val seconds = (timestamp / 1000) % 60
            val minutes = (timestamp / (1000 * 60) % 60)
            val hours = (timestamp / (1000 * 60 * 60) % 24)
            val time = "$hours:$minutes:$seconds"

            val messageObject = ChatData(image,name!!,message,uidSender!!,uidReceiver!!,time)
            dbRef.child("chats").child(senderRoom).child("messages").push()
                .setValue(messageObject).addOnSuccessListener {
                    dbRef.child("chats").child(receiverRoom).child("messages").push()
                        .setValue(messageObject)
                }
            etSendMessage.setText("")
        }

    }
}

/*

  chatData.add(ChatData(
            R.drawable.ic_account_circle,"AhmedAli",
            "مرحبا كيف حالك","1","2","08:47AM"))
        chatData.add(ChatData(
            R.drawable.ic_account_circle,"AhmedAli",
            "بخير وأنت","2","1","08:47AM"))
        chatData.add(ChatData(
            R.drawable.ic_account_circle,"AhmedAli",
            "الحمد لله","1","2","08:47AM"))
        chatData.add(ChatData(
            R.drawable.ic_account_circle,"AhmedAli",
            "أريد أن أسألك بخصوص المنزل","2","1","08:47AM"))
        chatData.add(ChatData(
            R.drawable.ic_account_circle,"AhmedAli",
            "تفضل كيف يمكنني أن أخدمك","1","2","08:47AM"))

 */