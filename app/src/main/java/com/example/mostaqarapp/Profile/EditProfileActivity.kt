package com.example.mostaqarapp.Profile

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.mostaqarapp.R
import com.example.mostaqarapp.data.UserData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.squareup.picasso.Picasso
//import kotlinx.coroutines.flow.internal.NoOpContinuation.context
import java.util.*

class EditProfileActivity : AppCompatActivity() {

    lateinit var pathe:String
    var uri:Uri? = null
    lateinit var editName:EditText
    lateinit var editEmail:EditText
    lateinit var editBio:EditText
    lateinit var editLocation:EditText
    lateinit var editPhone:EditText
    lateinit var editImage:ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        editName = findViewById<EditText>(R.id.etEditName)
        editEmail = findViewById<EditText>(R.id.etEditEmail)
        editBio = findViewById<EditText>(R.id.etEditBio)
        editLocation = findViewById<EditText>(R.id.etEditLocation)
        editPhone= findViewById<EditText>(R.id.etEditPhone)
        val btnEdit= findViewById<Button>(R.id.btnEdit)
        editImage= findViewById<ImageView>(R.id.imgSenderChat)

        val db = Firebase.firestore
        val storage = Firebase.storage
        val auth = FirebaseAuth.getInstance()
        var user = UserData()
//        var imageUrl = ""


        editName.setText(user.name)
        editEmail.setText(user.email)
        editLocation.setText(user.location)
        editPhone.setText(user.phone)
        editBio.setText(user.bio)

        if (!user.image.isNullOrEmpty()) {
            Picasso.get().load(user.image).into(editImage)
        } else {
            editImage.setImageResource(R.drawable.click_account)
        }

        val activityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                val data = result.data
                uri = data?.data
                editImage.setImageURI(uri)
            } else {
                Toast.makeText(this, "No Image Selected", Toast.LENGTH_SHORT).show()
            }
        }

        editImage.setOnClickListener {
            val photoPicker = Intent(Intent.ACTION_GET_CONTENT)
            photoPicker.type = "profile/*"
            activityResultLauncher.launch(photoPicker)
            uploadImageData(uri!!)
        }
        getUserProfile()

        btnEdit.setOnClickListener {
            updateUserInformation(uri.toString())
        }

    }


    private fun uploadImageData(imageUri: Uri) {
        val storage = FirebaseStorage.getInstance()
        storage.reference.child("profileImage/${UUID.randomUUID()}").putFile(uri!!).addOnSuccessListener{ taskSnapshot ->
            val downloadUri = taskSnapshot.storage.downloadUrl
            taskSnapshot.storage.downloadUrl.addOnCompleteListener {
                pathe= it.toString()
                Log.e("byn",pathe)
            }

            val imageUrl = downloadUri.result.toString()
        }
            .addOnFailureListener { exception ->
                Log.e("AddTopic", "Error uploading image: ${exception.message}")
//                Toast.makeText(context, "Failed to upload image", Toast.LENGTH_SHORT).show()
            }
    }

    private fun getUserProfile(){
        Firebase.firestore.collection("users").whereEqualTo("uid",Firebase.auth.currentUser!!.uid).get()
            .addOnSuccessListener {querySnapshot ->
                editEmail.setText(querySnapshot.documents.get(0).get("email").toString())
                editName.setText(querySnapshot.documents.get(0).get("name").toString())
                editLocation.setText(querySnapshot.documents.get(0).get("location").toString())
                editPhone.setText(querySnapshot.documents.get(0).get("phone").toString())
//                Picasso.get().load(querySnapshot.documents.get(0).get("image").toString()).into(userProfileImage)
            }.addOnFailureListener { exception ->
                Toast.makeText(this, "fail to get user $exception", Toast.LENGTH_SHORT).show()
            }
    }

    private fun updateUserInformation(imageUrl:String){
        Firebase.firestore.collection("users").whereEqualTo("uid",Firebase.auth.currentUser!!.uid).get()
            .addOnSuccessListener { snapshot->
                val hash = hashMapOf<String,Any>("name" to editName.text.toString(),"email" to editEmail.text.toString(),
                    "location" to editLocation.text.toString(), "phone" to editPhone.text.toString(), "bio" to editBio, "imageProfile" to imageUrl
                )
                Firebase.firestore.collection("users").document(snapshot.documents.get(0).id).update(hash)
                    .addOnSuccessListener { finish() }
                Toast.makeText(this, "update successfully", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener { error ->
                Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show()
            }
    }






}