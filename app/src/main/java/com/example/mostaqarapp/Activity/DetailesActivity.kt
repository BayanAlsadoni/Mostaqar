package com.example.mostaqarapp.Activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mostaqarapp.R
import com.example.mostaqarapp.adapter.CommentAdapter
import com.example.mostaqarapp.data.CommentMessage
import com.example.mostaqarapp.data.FCMMessage
import com.example.mostaqarapp.data.FCMMessage.Companion.addTokenToHome
import com.example.mostaqarapp.data.FCMMessage.Companion.sendFCMMessage
import com.example.mostaqarapp.data.HomeData
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.squareup.picasso.Picasso
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.BufferedOutputStream
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

class DetailesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detailes)

        val home = intent.getParcelableExtra<HomeData>("home")
        var name =""

        val ref = FirebaseDatabase.getInstance().getReference()
        val uid = Firebase.auth.uid
        val messageList = ArrayList<CommentMessage>()
        val adapter = CommentAdapter(this, messageList)

        val rvComments = findViewById<RecyclerView>(R.id.rvComments)
        val etComment = findViewById<EditText>(R.id.etComment)
        val btnSendComment = findViewById<ImageView>(R.id.btnSendComment)
        val rvHomeImages = findViewById<RecyclerView>(R.id.rvHomeImages)

        val tvTitle = findViewById<TextView>(R.id.tvDetailesTitle)
        val tvPrice = findViewById<TextView>(R.id.tvDetailesPrice)
        val tvLocation = findViewById<TextView>(R.id.tvDetailsLocation)
        val tvDescription = findViewById<TextView>(R.id.tvDetailsDescription)
        val tvType = findViewById<TextView>(R.id.tvDetailesType)
        val tvSpace = findViewById<TextView>(R.id.tvDetailesSpace)
        val tvPublisherName = findViewById<TextView>(R.id.tvPublisherName)
        val userAccountImage = findViewById<ImageView>(R.id.userAccountImage)
        val imgBtnSave = findViewById<ImageButton>(R.id.detailsImgBtnSave)
        val fabtnNotifiDetails = findViewById<FloatingActionButton>(R.id.fabtnNotifiDetails)
        val fabSharDetails = findViewById<FloatingActionButton>(R.id.fabSharDetails)
        val imgbtnPhoneNumber = findViewById<ImageButton>(R.id.imgbtnPhoneNumber)

        var homeId:String? = ""

        if (home!=null){
            tvTitle.text = home.title
            tvPrice.text = home.price
            tvLocation.text = home.location
            tvDescription.text = home.description
            tvType.text = home.homeType
            tvSpace.text = home.space
            tvPublisherName.text = home.ownerName
//            Picasso.get().load(home.ownerImage).into(userAccountImage)
            homeId = home.id
            if (!home.ownerImage.isNullOrEmpty()) {
                Picasso.get().load(home.ownerImage).into(userAccountImage)
            } else {
                userAccountImage.setImageResource(R.drawable.man_user) // Use a placeholder image
            }

        }
        var isFirstImage = true

        imgBtnSave.setOnClickListener {

            if (isFirstImage){
                imgBtnSave.setImageResource(R.drawable.ic_fill_save)
                FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                    if(!task.isSuccessful()){
                        Log.e("byn","faild to get token")
                        return@addOnCompleteListener
                    }
                    val token = task.result.toString()
                    Log.e("byn",token)
//                    sendFCMMessage(token,"أصبحت تتابع هذا الموضوع")
                    val map = hashMapOf<String,Any>("token" to token)
//                    addTokenToTopic(homeId!!.toString(),map,token)

                }
//                isFirstImage = !isFirstImage
            }else{
                imgBtnSave.setImageResource(R.drawable.ic_save)
                FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                    if(!task.isSuccessful()){
                        Log.e("byn","faild to get token")
                        return@addOnCompleteListener
                    }
                    val token = task.result
                    Log.e("byn",token.toString())
//                    sendFCMMessage(token.toString(),"لقد ألغيت متابعة هذا الموضوع")
//                    deleteTokenFromTopic(homeId!!.toString(), token.toString())
                }
            }
            isFirstImage = !isFirstImage
        }


        rvComments.layoutManager = LinearLayoutManager(this)
        if (home != null) {
            name = home.ownerName!!
        }
        messageList.clear()
        ref.child("messageComment").child(name)
            .addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    val message = snapshot.getValue(CommentMessage::class.java)
                    if (message != null) {
                        messageList.add(message)
                        adapter.notifyDataSetChanged()
                        adapter.notifyItemInserted(messageList.size - 1)
                        rvComments.scrollToPosition(messageList.size - 1)

                    }
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                }

                override fun onCancelled(error: DatabaseError) {
                }

            })

        btnSendComment.setOnClickListener {
            val message = etComment.text.toString()
            val timestamp = System.currentTimeMillis()
            val seconds = (timestamp / 1000) % 60
            val minutes = (timestamp / (1000 * 60) % 60)
            val hours = (timestamp / (1000 * 60 * 60) % 24)
            val time = "$hours:$minutes:$seconds"

            Firebase.firestore.collection("users").get().addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot) {
                    val data = document.data
                    val useruid = data["uid"].toString()
                    if (uid == useruid) {
                        val username = data["name"].toString()
                        val messageObject = CommentMessage(uid, username, message, time)
                        if (etComment.text.isEmpty()) return@addOnSuccessListener
                        ref.child("messageComment").child(name).push()
                            .setValue(messageObject)
                        etComment.setText("")
                    }

                }

            }

        }

        fabtnNotifiDetails.setOnClickListener {
            FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                if(!task.isSuccessful()){
                    Log.e("byn","faild to get token")
                    return@addOnCompleteListener
                }
                val token = task.result.toString()
                Log.e("byn",token)
                sendFCMMessage(token,"أصبحت تتابع هذا الموضوع")
                val map = hashMapOf<String,Any>("token" to token)
                addTokenToHome(home?.id.toString(),map,token)

            }
        }
        imgbtnPhoneNumber.setOnClickListener {
            Firebase.firestore.collection("users").get().addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot) {
                    val data = document.data
                    val useruid = data["uid"].toString()
                    if (uid == useruid) {
                        val phone = data["phone"]
                        val i = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))
                        startActivity(i)
                    }

                }

            }

        }

        fabSharDetails.setOnClickListener {
            val share = Intent(Intent.ACTION_SEND)
        }

    }




}