package com.example.mostaqarapp.fragment

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.mostaqarapp.R
import com.example.mostaqarapp.data.HomeData
import com.example.mostaqarapp.map.AddLocationMapFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class AddHomeFragment : Fragment() {

    lateinit var etTitle:EditText
    lateinit var etDescription:EditText
    lateinit var etLocation:EditText
    lateinit var etPrice:EditText
    lateinit var etSpace:EditText
    lateinit var etRoomNum:EditText
    lateinit var etBathNum:EditText
    lateinit var tvImage:TextView
    lateinit var tvVideo:TextView
    lateinit var tvType:TextView
    lateinit var db:FirebaseFirestore
    lateinit var auth:FirebaseAuth
    lateinit var pathe:String
    var itemType = ""

    var uri: Uri? = null
    private var videoUri: Uri? = null
    private var videoURL: String? = null
    var uname = ""
    var uimage = ""
    var uid = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        auth = Firebase.auth
        db = Firebase.firestore

        val root = inflater.inflate(R.layout.fragment_add_home, container, false)

        etTitle = root.findViewById<EditText>(R.id.etEditName)
        etDescription = root.findViewById<EditText>(R.id.etEditPhone)
        tvImage = root.findViewById<TextView>(R.id.etAddImg)
        tvVideo = root.findViewById<TextView>(R.id.etAddVideo)
        tvType = root.findViewById<TextView>(R.id.etAddType)
        etLocation = root.findViewById<EditText>(R.id.etAddLocation)
        etPrice = root.findViewById<EditText>(R.id.etEditLocation)
        etSpace = root.findViewById<EditText>(R.id.etEditBio)
        etRoomNum = root.findViewById<EditText>(R.id.etRoomNum)
        etBathNum = root.findViewById<EditText>(R.id.etBathNum)
        val btnNext = root.findViewById<Button>(R.id.add_btnNext)
        val imgBtnAddLocationMap = root.findViewById<ImageButton>(R.id.imgBtnAddLocationMap)

        val errorMessage = "الرجاء تعبئة هذا الحقل"
        btnNext.setOnClickListener {
            if(etTitle.text.isEmpty()){
                etTitle.error = errorMessage
                etTitle.requestFocus()
            }else if(etDescription.text.isEmpty()){
                etDescription.error = errorMessage
                etDescription.requestFocus()
            }else if(etLocation.text.isEmpty()){
                etLocation.error = errorMessage
                etLocation.requestFocus()
            }else if(etPrice.text.isEmpty()){
                etPrice.error = errorMessage
                etPrice.requestFocus()
            }else {
                saveData()
            }
        }

        tvType.setOnClickListener {
            val popup = PopupMenu(context,tvType)
            popup.menuInflater.inflate(R.menu.home_menu,popup.menu)
            popup.setOnMenuItemClickListener {item ->
                Toast.makeText(context, "item$item", Toast.LENGTH_SHORT).show()
                when(item.itemId){
                    R.id.flat_item -> itemType="شقة"
                    R.id.room_item -> itemType="غرفة"
                    R.id.home_item -> itemType="منزل"
                }
                tvType.text = itemType
                true
            }
            popup.show()
        }
//        tvType.text=itemType

        imgBtnAddLocationMap.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.detailsFragment,AddLocationMapFragment()).commit()

        }

        val videoHome = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                val data: Intent? = result.data
                val selectedVideoUri: Uri? = data?.data
                if (selectedVideoUri != null) {
                    videoUri = selectedVideoUri
                    videoURL = videoUri.toString()
                    Toast.makeText(context, videoUri.toString(), Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireContext(), "No Video Selected", Toast.LENGTH_SHORT).show()
            }
        }

        val activityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                uri = result.data?.data
//                image.setImageURI(uri)
            } else {
                Toast.makeText(requireContext(), "No Image Selected", Toast.LENGTH_SHORT).show()
            }
        }
        tvImage.setOnClickListener {
            val photoPicker = Intent(Intent.ACTION_GET_CONTENT)
            photoPicker.type = "image/*"
            activityResultLauncher.launch(photoPicker)
        }
        tvVideo.setOnClickListener {
            val videoPicker = Intent(Intent.ACTION_GET_CONTENT)
            videoPicker.type = "video/*"
            videoHome.launch(videoPicker)
            Toast.makeText(requireContext(), "clicked", Toast.LENGTH_SHORT).show()
        }
        return root
    }

    private fun saveData() {
        val storageReference = FirebaseStorage.getInstance().reference.child("homeVideos/${UUID.randomUUID()}")
        val builder = AlertDialog.Builder(context)
        builder.setCancelable(false)
        builder.setView(R.layout.progress_layout)
        val dialog = builder.create()
//        dialog.show()

        if(videoUri != null){
            storageReference.putFile(videoUri!!)
                .continueWithTask { task ->
                    storageReference.downloadUrl
                }
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        videoURL = task.result.toString()
                        uploadImageData(uri!!)
                        dialog.dismiss()
//                        Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show()
                        Log.e("add home","success")
                    } else {
                        dialog.dismiss()
//                        Toast.makeText(requireContext(), "Failed to upload video", Toast.LENGTH_SHORT).show()
                        Log.e("add home","fail")
                    }
                }

        }

            if (uri!=null) {
                uploadImageData(uri!!)
            }else {
//                Toast.makeText(requireContext(), "No image selected", Toast.LENGTH_SHORT).show()
                Log.e("upload image","fail")
            }

    }

    private fun uploadImageData(imageUri: Uri) {
        val storage = FirebaseStorage.getInstance()
        val storageReference = storage.reference.child("homeImage/${UUID.randomUUID()}")

        storageReference.putFile(imageUri)
            .addOnSuccessListener { taskSnapshot ->
                taskSnapshot.storage.downloadUrl.addOnSuccessListener {downloadUri->
                    val imageUrl = downloadUri.toString()
                    db.collection("users").get().addOnSuccessListener {querySnapshot ->
                        for (document in querySnapshot){
                            if (auth.currentUser!!.uid == document.data["uid"].toString()){
                                val data = document.data
                                Toast.makeText(context, "current user $data", Toast.LENGTH_SHORT).show()
                                uname = document.data["name"].toString()
                                uid = document.data["uid"].toString()
                                uimage = document.data["image"].toString()
                                saveHomeData(imageUrl,videoURL?:"")
                            }
                        }

                    }

                }
            }
            .addOnFailureListener { exception ->
                Log.e("AddTopic", "Error uploading image: ${exception.message}")
            }
    }

    private fun saveHomeData(imageUrl: String, videoUrl: String) {
        val id = UUID.randomUUID().toString()
        val title = etTitle.text.toString()
        val locat = etLocation.text.toString()
        val description = etDescription.text.toString()
        val price = etPrice.text.toString()
        val type = tvType.text.toString()
        val space = etSpace.text.toString()
        val room = etRoomNum.text.toString()
        val bath = etBathNum.text.toString()

        val la= requireActivity().intent.getDoubleExtra("product_latitude",31.32535135135135)
        val lo= requireActivity().intent.getDoubleExtra("product_longitude",34.29015667840477)


//        val home = HomeData(id=id, ownerName =uname, ownerId = uid, ownerImage = uimage, title = title
//                , price = price, location = locat, description = description, homeImage = imageUrl,
//            homeType = type, homeVideo = videoUrl, space = space)

        val home = hashMapOf<String,Any>("id" to id,"ownerName" to uname, "ownerId" to uid, "ownerImage" to uimage, "title" to title
            , "price" to price, "location" to locat, "description" to description, "homeImage" to imageUrl,
            "mapLocation" to GeoPoint(la,lo),"homeType" to type, "homeVideo" to videoUrl, "space" to space,"room" to room, "bath" to bath)

        db.collection("home")
            .add(home)
            .addOnSuccessListener { documentReference ->
                Log.e("AddHome", "DocumentSnapshot added with ID: ${documentReference.id}")
                Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show()
                requireActivity().supportFragmentManager.beginTransaction().replace(R.id.fragmentAddHome,HomeFragment()).commit()

            }
            .addOnFailureListener { e ->
                Log.e("AddTopic", "Error adding document", e)
                Toast.makeText(context, "Failed to save data", Toast.LENGTH_SHORT).show()
            }
    }

}
