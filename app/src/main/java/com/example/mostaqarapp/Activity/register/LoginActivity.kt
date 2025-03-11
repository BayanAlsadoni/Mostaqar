package com.example.mostaqarapp.Activity.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import com.example.mostaqarapp.Activity.HomeActivity
import com.example.mostaqarapp.R
import com.example.mostaqarapp.data.UserData
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.ArrayList

class LoginActivity : AppCompatActivity() {
    private lateinit var auth:FirebaseAuth
    private lateinit var firestore:FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val etEmail = findViewById<EditText>(R.id.login_etEmail)
        val etPassword = findViewById<EditText>(R.id.login_etPassword)
        val buttonLogin = findViewById<Button>(R.id.login_btinSubmit)
        val buttonBackLogin = findViewById<ImageView>(R.id.btnBack)
        val tvForgetPassword = findViewById<TextView>(R.id.tvForgetPassword)
        val tvRegister = findViewById<Button>(R.id.login_btnRegister)
        val btnGoogle = findViewById<ImageView>(R.id.login_btnGoogle)
        val imgLoginPhone = findViewById<ImageView>(R.id.imgLoginPhone)

        auth = Firebase.auth
        firestore = Firebase.firestore
        val list = ArrayList<UserData>()
        val errorMessage = "الرجاء تعبئة هذا الحقل"

        tvForgetPassword.setOnClickListener {

            startActivity(Intent(this, ForgetPasswordActivity::class.java))
            Log.e("login", "forget Password")
        }
        tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }

        firestore.collection("users").get()
            .addOnSuccessListener { result ->
                list.clear()
                for (document in result) {
                    list.add(
                        UserData(
                            email = document.getString("email").toString(),
                            password = document.getString("password").toString()
                        )
                    )
                }
            }.addOnFailureListener {
                Log.e("firestor", "error getting documents")
            }


        buttonBackLogin.setOnClickListener {
            finish()
        }

        tvForgetPassword.setOnClickListener {
            startActivity(Intent(this, ForgetPasswordActivity::class.java))
        }

        buttonLogin.setOnClickListener {
            if (etEmail.text.isEmpty()) {
                etEmail.error = errorMessage
                etEmail.requestFocus()
            } else if (etPassword.text.isEmpty()) {
                etPassword.error = errorMessage
                etPassword.requestFocus()
            } else {
                userLogin(etEmail.text.toString(), etPassword.text.toString())
            }
        }

        btnGoogle.setOnClickListener {
            val signInRequest = BeginSignInRequest.builder()
                .setGoogleIdTokenRequestOptions(
                    BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        // Your server's client ID, not your Android client ID.
//                        .setServerClientId(getString(R.string.your_web_client_id))
                        // Only show accounts previously used to sign in.
                        .setFilterByAuthorizedAccounts(true)
                        .build())
                .build()
        }
        imgLoginPhone.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

    }

    fun userLogin(email:String, password:String){
        val auth = Firebase.auth
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {task ->
            if(task.isSuccessful){
                if(auth.currentUser!!.isEmailVerified){
                    val goToHome = Intent(this, HomeActivity::class.java)
                    startActivity(goToHome)
                }else{
                    Toast.makeText(this, "please verify your email address", Toast.LENGTH_SHORT).show()
                }

            }else {
                Toast.makeText(this, "فشل تسجيل الدخول", Toast.LENGTH_SHORT).show()
            }

        }
    }

}