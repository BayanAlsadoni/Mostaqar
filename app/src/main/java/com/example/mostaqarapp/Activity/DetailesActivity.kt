package com.example.mostaqarapp.Activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mostaqarapp.Activity.chat.ChatActivity
import com.example.mostaqarapp.R
import com.example.mostaqarapp.adapter.ChatAdapter
import com.example.mostaqarapp.adapter.CommentAdapter
import com.example.mostaqarapp.data.ChatData
import com.example.mostaqarapp.data.CommentMessage
import com.example.mostaqarapp.data.FCMMessage
import com.example.mostaqarapp.data.FCMMessage.Companion.addTokenToHome
import com.example.mostaqarapp.data.FCMMessage.Companion.sendFCMMessage
import com.example.mostaqarapp.data.HomeData
import com.example.mostaqarapp.map.LocationMapsActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.squareup.picasso.Picasso
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
        val uid = Firebase.auth.currentUser!!.uid
        val messageList = ArrayList<CommentMessage>()


        val rvComments = findViewById<RecyclerView>(R.id.rvComments)
        val etComment = findViewById<EditText>(R.id.etComment)
        val btnSendComment = findViewById<FloatingActionButton>(R.id.btnSendComment)
        val rvHomeImages = findViewById<RecyclerView>(R.id.rvHomeImages)

        val tvTitle = findViewById<TextView>(R.id.tvDetailesTitle)
        val tvPrice = findViewById<TextView>(R.id.tvDetailesPrice)
        val tvLocation = findViewById<TextView>(R.id.tvDetailsLocation)
        val tvDescription = findViewById<TextView>(R.id.tvDetailsDescription)
        val tvType = findViewById<TextView>(R.id.tvDetailesType)
        val tvSpace = findViewById<TextView>(R.id.tvDetailesSpace)
        val tvPublisherName = findViewById<TextView>(R.id.tvPublisherName)
        val details_tvBedroomNom = findViewById<TextView>(R.id.details_tvBedroomNom)
        val details_tvBathroomNom = findViewById<TextView>(R.id.details_tvBathroomNom)
        val tvSpaceDetails = findViewById<TextView>(R.id.tvSpaceDetails)
        val tvRoomDetails = findViewById<TextView>(R.id.tvRoomDetails)
        val tvBathDetails = findViewById<TextView>(R.id.tvBathDetails)
        val userAccountImage = findViewById<ImageView>(R.id.userAccountImage)
        val imgHomeDetails = findViewById<ImageView>(R.id.imgHomeDetails)
        val imgBtnSave = findViewById<ImageButton>(R.id.detailsImgBtnSave)
        val fabtnNotifiDetails = findViewById<FloatingActionButton>(R.id.fabtnNotifiDetails)
        val fabVideo = findViewById<FloatingActionButton>(R.id.fabSharDetails)
        val imgbtnPhoneNumber = findViewById<ImageButton>(R.id.imgbtnPhoneNumber)
        val imgBtnEditHome = findViewById<ImageButton>(R.id.imgBtnEditHome)
        val imgBtnDeleteHome = findViewById<ImageButton>(R.id.imgBtnDeleteHome)
        val imgBtnMessagingDetails = findViewById<ImageButton>(R.id.imgBtnMessegingDetails)
        val cardViewLocation = findViewById<CardView>(R.id.cardViewLocation)

        var homeId:String? = ""
        val isHomeOwner = intent.getBooleanExtra("isHomeOwner",false)
        if (isHomeOwner){
            imgBtnEditHome.visibility = View.VISIBLE
            imgBtnDeleteHome.visibility = View.VISIBLE
        }

        if (home!=null){
            tvTitle.text = home.title
            tvPrice.text = home.price
            tvLocation.text = home.location
            tvDescription.text = home.description
            tvType.text = home.homeType
            tvSpace.text = home.space
            tvSpaceDetails.text = "${home.space}M²"
            tvPublisherName.text = home.ownerName
            details_tvBathroomNom.text = home.bathroom
            tvBathDetails.text = "${home.bathroom} حمام "
            details_tvBedroomNom.text = home.room
            tvRoomDetails.text = "${home.room} غرف "

            homeId = home.id
            Picasso.get().load(home.homeImage).placeholder(R.drawable.home_details)
                .error(R.drawable.homeone).into(imgHomeDetails)
            Picasso.get().load(home.ownerImage).placeholder(R.drawable.home_details)
                .error(R.drawable.click_account).into(userAccountImage)

        }
        var isFirstImage = true

        imgBtnEditHome.setOnClickListener {

        }
        imgBtnDeleteHome.setOnClickListener {
            Firebase.firestore.collection("homes").document(home?.id.toString()).delete()
        }

        imgBtnMessagingDetails.setOnClickListener {
            val gotToChat = Intent(this,ChatActivity::class.java)
            gotToChat.putExtra("sender_id",home?.ownerId)
            gotToChat.putExtra("sender_name",home?.ownerName)
            gotToChat.putExtra("sender_image",home?.ownerImage)
            startActivity(gotToChat)
        }

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
                        val adapter = CommentAdapter(this@DetailesActivity, messageList)
                        adapter.notifyDataSetChanged()
                        adapter.notifyItemInserted(messageList.size - 1)
                        rvComments.layoutManager = LinearLayoutManager(this@DetailesActivity)
                        rvComments.adapter = adapter
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
        val chatData = ArrayList<ChatData>()
        val chatAdapter = ChatAdapter(this,chatData)
        ref.child("messageComment").child(home!!.id!!).child("comment")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot){
                    chatData.clear()

                    for(postSnapshot in snapshot.children) {
                        val message = postSnapshot.getValue(ChatData::class.java)
                        chatData.add(message!!)
                    }
                    chatAdapter.notifyDataSetChanged()
                    chatAdapter.notifyItemInserted(chatData.size -1)
                    rvComments.scrollToPosition(chatData.size -1)
                }

                override fun onCancelled(p0: DatabaseError) {

                }
            })

        btnSendComment.setOnClickListener {
            Toast.makeText(this, "btn send clicked", Toast.LENGTH_SHORT).show()
            val message = etComment.text.toString()
            val timestamp = System.currentTimeMillis()
            val seconds = (timestamp / 1000) % 60
            val minutes = (timestamp / (1000 * 60) % 60)
            val hours = (timestamp / (1000 * 60 * 60) % 24)
            val time = "$hours:$minutes:$seconds"
            Toast.makeText(this, "id ${home.id}", Toast.LENGTH_SHORT).show()
            Firebase.firestore.collection("users").whereEqualTo("uid",Firebase.auth.currentUser?.uid).get()
              .addOnSuccessListener { querySnapshot ->
                Toast.makeText(this, "get users", Toast.LENGTH_SHORT).show()
//                for(doc in querySnapshot){
//                    if(doc["id"]==home.id){
                        Toast.makeText(this, "get current hoem", Toast.LENGTH_SHORT).show()
//                        val username = querySnapshot.documents.get(0).get("ownerName").toString() //doc["ownerName"].toString()
                        val username = querySnapshot.documents.get(0).get("name").toString()
                        val messageObject = CommentMessage(uid, username, message, time)
                        if (etComment.text.isEmpty()) return@addOnSuccessListener
                        ref.child("messageComment").child(home.id!!).child("comment").push()
                            .setValue(messageObject)
                        etComment.setText("")
//                    }
//                }


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

        fabVideo.setOnClickListener {
            val goToVideo = Intent(this,HomeVideoActivity::class.java)
            goToVideo.putExtra("videoUrl",home?.homeVideo)
            startActivity(goToVideo)
        }
        if(home.lat ==3.0 && home.lng ==3.0){
            cardViewLocation.visibility = View.GONE
        }
        cardViewLocation.setOnClickListener {
            val goToMap = Intent(this,LocationMapsActivity::class.java)
            goToMap.putExtra("latitude",home.lat)
            goToMap.putExtra("longitude",home.lng)
            startActivity(goToMap)
        }

    }




}