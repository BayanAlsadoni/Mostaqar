package com.example.mostaqarapp.Activity.chat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.ListView
import com.example.mostaqarapp.R
import com.example.mostaqarapp.adapter.UsersMessageAdapter
import com.example.mostaqarapp.data.UsersMessageData
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*

class UsersMessagesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_users_messages)

        val etSearch = findViewById<EditText>(R.id.message_etSearch)
        val imgBack = findViewById<ImageView>(R.id.message_imgBack)
        val lvMessage = findViewById<ListView>(R.id.lvMessage)

        val dbFirestore = Firebase.firestore
        val auth = Firebase.auth

        val usersList = ArrayList<UsersMessageData>()

        val msgAdapter= UsersMessageAdapter(this,usersList)
        lvMessage.adapter = msgAdapter

        lvMessage.setOnItemClickListener { adapterView, view, i, l ->
            startActivity(Intent(this, ChatActivity::class.java))
        }

        imgBack.setOnClickListener {

        }

        dbFirestore.collection("users").get().addOnSuccessListener {querySnapshot ->
            usersList.clear()
            for (document in querySnapshot){
                if (auth.currentUser!!.uid != document.data["uid"].toString()){
                    val data = document.data
                    val name = data["name"].toString()
                    val uid = data["uid"].toString()
                    val img = data["image"].toString()
                    usersList.add(UsersMessageData(uid=uid, senderImage = img, senderName = name, textMessage = "", timestamp = ""))
                }
            }
            msgAdapter.notifyDataSetChanged()

        }

    }
}