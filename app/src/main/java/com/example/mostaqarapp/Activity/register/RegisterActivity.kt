package com.example.mostaqarapp.Activity.register

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.mostaqarapp.R
import com.example.mostaqarapp.data.UserData
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import java.util.*

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth:FirebaseAuth
    private lateinit var googleSignInClient:com.google.android.gms.auth.api.signin.GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val btbBack = findViewById<ImageView>(R.id.register_btnBack)
        val etUserName = findViewById<EditText>(R.id.register_etUserName)
        val etEmail = findViewById<EditText>(R.id.register_etEmail)
        val etPhoneNum = findViewById<EditText>(R.id.etPhoneNum)
        val etLocation = findViewById<EditText>(R.id.register_etLocation)
        val etPassword = findViewById<TextView>(R.id.register_etPassword)
        val etConfirmPassword = findViewById<TextView>(R.id.register_etConfirmPassword)
        val tvRegisterTitel = findViewById<TextView>(R.id.tvRegisterTitel)
        val tvSignin = findViewById<TextView>(R.id.register_tvSignin)
        val btnCreateAccount = findViewById<Button>(R.id.buttonCreateAccountRegister)
//        val btnFacebook = findViewById<ImageView>(R.id.register_btnFacebook)
        val btnGoogle = findViewById<ImageView>(R.id.register_btnGoogle)
        val btnMail = findViewById<ImageView>(R.id.register_btnMail)
        var isMail = false

         auth = FirebaseAuth.getInstance()
//        auth.getUsersAsync

       val signInRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    // Your server's client ID, not your Android client ID.
                    .setServerClientId(getString(R.string.default_web_client_id))
                    // Only show accounts previously used to sign in.
                    .setFilterByAuthorizedAccounts(true)
                    .build())
            .build()
//        googleSignInClient = GoogleSignIn.getClient(this, signInRequest)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this,gso)

        btbBack.setOnClickListener {
            finish()
        }
        tvSignin.setOnClickListener {
            startActivity(Intent(this,LoginActivity::class.java))
            finish()
        }

        btnMail.setOnClickListener {
            if(!isMail){
                btnMail.setImageResource(R.drawable.baseline_phone_enabled)
                isMail=true
                tvRegisterTitel.text="إنشاء حساب باستخدام البريد الإلكتروني"
            }else{
                btnMail.setImageResource(R.drawable.baseline_mail)
                isMail=false
                tvRegisterTitel.text="إنشاء حساب باستخدام رقم الهاتف"
            }

        }

        btnCreateAccount.setOnClickListener {

            if(etUserName.text.isEmpty()){
                etUserName.error = "الرجاء تعبئة هذا الحقل"
                etUserName.requestFocus()
            } else if(etEmail.text.isEmpty()){
                etEmail.error = "الرجاء تعبئة هذا الحقل"
                etEmail.requestFocus()
            }else if(etPhoneNum.text.isEmpty()){
                etPhoneNum.error = "الرجاء تعبئة هذا الحقل"
                etPhoneNum.requestFocus()
            }else if(etLocation.text.isEmpty()){
                etLocation.error = "الرجاء تعبئة هذا الحقل"
                etLocation.requestFocus()
            }else if(etConfirmPassword.text.isEmpty()){
                etConfirmPassword.error = "الرجاء تعبئة هذا الحقل"
                etConfirmPassword.requestFocus()
            }else if(etPassword.text.isEmpty()){
                etPassword.error = "الرجاء تعبئة هذا الحقل"
                etPassword.requestFocus()
           }else{
                val i = Intent(this,VerificationActivity::class.java)
                val user = UserData("",etUserName.text.toString(), etEmail.text.toString(), etPassword.text.toString(),
                    etLocation.text.toString(),etPhoneNum.text.toString())
                i.putExtra("user",user)
                if(isMail){
                    i.putExtra("isMail",isMail)
                    Toast.makeText(this, "verify by mail", Toast.LENGTH_SHORT).show()
                    startActivity(i)

               }else{
                   Toast.makeText(this, "user is ${user}", Toast.LENGTH_SHORT).show()
                    i.putExtra("isMail",isMail)
                   startActivity(i)
               }
            }
        }

        btnGoogle.setOnClickListener {
            signInGoogle()
        }

    }

    fun signInGoogle(){
        val signInIntent = googleSignInClient.signInIntent
        launcher.launch(signInIntent)
    }
    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        result->
        if(result.resultCode == Activity.RESULT_OK){
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            handleResults(task)
        }
    }
    fun handleResults(task:Task<GoogleSignInAccount>){
        if(task.isSuccessful){
            val account :GoogleSignInAccount? = task.result
            if(account != null){
                updateUI(account)
            }
        }else{
            Toast.makeText(this, task.exception.toString(), Toast.LENGTH_SHORT).show()
        }
    }
    fun updateUI(account:GoogleSignInAccount){
        val credential = GoogleAuthProvider.getCredential(account.idToken,null)
        auth.signInWithCredential(credential).addOnCompleteListener {
            if(it.isSuccessful){
                val goToConfirem = Intent(this,ConfirmActivity::class.java)
                addUser(account.id,account.displayName,account.email,"","","")
                addUserToken(account.id!!,account.displayName!!,account.email!!)
                startActivity(goToConfirem)
            }else{
                Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }


    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        var currentUser = auth.getCurrentUser()
//        updateUI(currentUser);
    }

    fun addUser(uid:String?, name:String?, email:String?, password:String?, location:String?, phone:String?){
        val user = hashMapOf("uid" to uid,"name" to name, "email" to email, "password" to password,
            "location" to location, "phone" to phone)

        val firestore = Firebase.firestore
        firestore.collection("users").add(user)
            .addOnSuccessListener {docRef ->
                val goToVerification = Intent(this, VerificationActivity::class.java)
                startActivity(goToVerification)

            }.addOnFailureListener {
                Toast.makeText(this, "فشل إنشاء حساب", Toast.LENGTH_SHORT).show()
            }
    }
    private fun addUserToken(uid:String, name:String,useremail:String){
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if(!task.isSuccessful()){
                Log.e("token","faild to get token")
                return@addOnCompleteListener
            }
            val token = task.result.toString()
            Log.e("token",token)
            val userToken = hashMapOf<String,Any>(
                "uid" to uid,
                "token" to token,
                "name" to name,
                "email" to useremail,
            )
            Firebase.firestore.collection("tokens").add(userToken)


        }
    }



}

