package com.example.mostaqarapp.Profile

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.example.mostaqarapp.R
import com.example.mostaqarapp.data.FCMMessage
import com.example.mostaqarapp.data.HomeData
import com.example.mostaqarapp.fragment.UserHomesActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class EditHomeActivity : AppCompatActivity() {

    lateinit var etTitle: EditText
    lateinit var etDescription: EditText
    lateinit var etLocation: EditText
    lateinit var etPrice: EditText
    lateinit var etSpace: EditText
    lateinit var etRoomNum: EditText
    lateinit var etBathNum: EditText
    lateinit var tvImage: TextView
    lateinit var tvVideo: TextView
    lateinit var tvType: TextView
    lateinit var db: FirebaseFirestore
    lateinit var auth: FirebaseAuth
    lateinit var pathe:String
    var itemType = ""
    var uri: Uri? = null
    var imageURL: String? = null
    lateinit var home: HomeData


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_home)

        etTitle = findViewById<EditText>(R.id.etEditTitle)
        etDescription = findViewById<EditText>(R.id.etEditDescription)
        tvImage = findViewById<TextView>(R.id.etEditImg)
        tvVideo = findViewById<TextView>(R.id.etEditVideo)
        tvType = findViewById<TextView>(R.id.etEditType)
        etLocation = findViewById<EditText>(R.id.etEditLocation)
        etPrice = findViewById<EditText>(R.id.etEditPrice)
        etSpace = findViewById<EditText>(R.id.etEditSpace)
        etRoomNum = findViewById<EditText>(R.id.etEditRoomNum)
        etBathNum = findViewById<EditText>(R.id.etEditBathNum)
        val btnEdit = findViewById<Button>(R.id.Edit_btnNext)
        val imgBtnLocationMap = findViewById<ImageButton>(R.id.imgBtnEditLocationMap)

        btnEdit.setOnClickListener {
            saveData()
        }
    }

    fun saveData() {
        val builder = AlertDialog.Builder(this)
        builder.setCancelable(false)
        builder.setView(R.layout.progress_layout)
        val dialog = builder.create()
        dialog.show()

        if (uri != null) {
            val storageReference =
                FirebaseStorage.getInstance().reference.child("topicsLogo/topicLogoId" + UUID.randomUUID().toString())

            storageReference.putFile(uri!!).addOnSuccessListener { taskSnapshot ->
                val uriTask = taskSnapshot.storage.downloadUrl
                while (!uriTask.isComplete);
                val urlImage = uriTask.result
                imageURL = urlImage.toString()
                updateData()
                dialog.dismiss()
                Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                dialog.dismiss()
            }
        } else {
            updateData()
            dialog.dismiss()
            Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
        }
    }

    fun updateData() {
        val newData = hashMapOf<String, Any>(
            "title" to etTitle.text.toString(), "description" to etDescription.text.toString(),
            "bathRoom" to etBathNum.text.toString(), "room" to etRoomNum.text.toString(),
            "location" to etLocation.text.toString(), "space" to  etSpace.text.toString(),
            "price" to  etPrice.text.toString()
        )
        if (imageURL != null) {
            newData["HomeImage"] = imageURL!!
        }

        db.collection("homes").document(home.id!!)
            .update(newData)
            .addOnSuccessListener {
                Toast.makeText(this, "Update Successful", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, UserHomesActivity::class.java))

                getTokensInHome(home.id!!)

            }
            .addOnFailureListener {
                Toast.makeText(this, "Update Failed", Toast.LENGTH_SHORT).show()
            }
    }


    private fun getTokensInHome(topicId:String){

        val topic = Firebase.firestore.collection("topics")

        topic.get().addOnSuccessListener {
            for (document in it){
                if (document.id == topicId){
                    Firebase.firestore.collection("topics").document(document.id).collection("tokens").get()
                        .addOnSuccessListener {
                            for (d in it){
//                                sendFCMMessage(d.data["token"].toString(),"تم تحديث موضوع${document.data["name"]} ")

                                FCMMessage.sendFCMMessage(d.data["token"].toString()," تم تحديث المستقر ${document.data["title"]}")

                            }
                        }
                }
            }
        }
    }


}