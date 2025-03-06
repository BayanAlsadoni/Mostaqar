package com.example.mostaqarapp.Activity.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.example.mostaqarapp.Activity.HomeActivity
import com.example.mostaqarapp.R
import com.example.mostaqarapp.data.UserData
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging

class ConfirmActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm)
        val btnCntinue= findViewById<Button>(R.id.btnCntinue)
        btnCntinue.setOnClickListener {
            startActivity(Intent(this,HomeActivity::class.java))
            finish()

        }
    }





}