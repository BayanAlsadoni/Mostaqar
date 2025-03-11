package com.example.mostaqarapp.Activity.register

import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.mostaqarapp.Activity.HomeActivity
import com.example.mostaqarapp.R
import com.example.mostaqarapp.data.HomeData
import com.example.mostaqarapp.data.UserData
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import java.util.Timer
import java.util.TimerTask
import java.util.concurrent.TimeUnit

class VerificationActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private var verificationCodeBySystem: String = ""
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private var user: UserData? = null
    private lateinit var tvTimeout: TextView
    private lateinit var multiFactorSession: MultiFactorSession
    private var timeoutSeconds: Long = 60L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_varification)

        auth = FirebaseAuth.getInstance()
        FirebaseApp.initializeApp(this)

        user = intent.getParcelableExtra<UserData>("user")
        val isMail = intent.getBooleanExtra("isMail",false)
        val phoneNumber = user!!.phone!!

        val etVerify1 = findViewById<EditText>(R.id.etVerify1)
        val etVerify2 = findViewById<EditText>(R.id.etVerify2)
        val etVerify3 = findViewById<EditText>(R.id.etVerify3)
        val etVerify4 = findViewById<EditText>(R.id.etVerify4)
        val etVerify5 = findViewById<EditText>(R.id.etVerify5)
        val etVerify6 = findViewById<EditText>(R.id.etVerify6)
        val btnVerify = findViewById<Button>(R.id.btnVerfy)
        val linearLayoutVerify = findViewById<LinearLayout>(R.id.linearLayoutVerify)
        tvTimeout = findViewById(R.id.tvTimeout)
        val resendCode = findViewById<TextView>(R.id.sedAgain)
        val tvFerifyTitle = findViewById<TextView>(R.id.tvFerifyTitle)

        if (isMail){
            linearLayoutVerify.visibility = View.INVISIBLE
            tvTimeout.visibility = View.INVISIBLE
            btnVerify.visibility = View.INVISIBLE
            auth.createUserWithEmailAndPassword(user!!.email!!,user!!.password!!).addOnCompleteListener {task->
                if(task.isSuccessful){
                    auth.currentUser?.sendEmailVerification()?.addOnCompleteListener {
                        tvFerifyTitle.setText( "يرجى التحقق من رابط التحقق المرسل إلى بريدك")
//                        if(auth.currentUser!!.isEmailVerified){
                            startActivity(Intent(this,ConfirmActivity::class.java))
                            addUser(auth.currentUser!!.uid,user!!.name,user!!.email,user!!.password,user!!.location,user!!.phone)
                            addUserToken(auth.currentUser!!.uid, user!!.name, user!!.email)
//                        }
                    }
                }else{
                    Toast.makeText(this, "verification faild", Toast.LENGTH_SHORT).show()
                }
            }
        }else{
            tvFerifyTitle.text = "يرجى التحقق من رابط التحقق المرسل إلى بريدك"
            startPhoneNumberVerification(phoneNumber)
    //        FirebaseAuth.getInstance().getFirebaseAuthSettings().forceRecaptchaFlowForTesting(true)
            btnVerify.setOnClickListener {
                val userCode = etVerify1.text.toString() + etVerify2.text.toString() +etVerify3.text.toString() +
                        etVerify4.text.toString()+ etVerify5.text.toString()+ etVerify6.text.toString()
                Log.e("phone","use enter code is $userCode")

                if (userCode.length < 6) {
                    Toast.makeText(this, "wrong OTP..$userCode", Toast.LENGTH_SHORT).show()
                } else {
                    verifyCode(userCode)
                }
            }

            resendCode.setOnClickListener {
                resendVerificationCode(phoneNumber)
            }
        }
    }

    private fun startPhoneNumberVerification(phoneNumber: String) {
        startResendTimer()

        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
//            .setMultiFactorSession(multiFactorSession)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                    verificationCodeBySystem = verificationId
                    resendToken = token
                    Toast.makeText(applicationContext, "OTP Sent Successfully!", Toast.LENGTH_SHORT).show()
                }

                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    Log.d("complete", "onVerificationCompleted:$credential")
                    signInWithPhoneAuthCredential(credential)
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    Toast.makeText(applicationContext, "Verification failed: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            })
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun verifyCode(codeByUser: String) {
        val credential = PhoneAuthProvider.getCredential(verificationCodeBySystem, codeByUser)
        signInWithPhoneAuthCredential(credential)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                val u = task.result?.user
                user?.uid = u?.uid
                addUser(u?.uid,user!!.name,user!!.email,user!!.password,user!!.location,user!!.phone)
                addUserToken(u?.uid, user!!.name, user!!.email)
                Toast.makeText(this, "Authentication Successful", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Verification failed. Try again!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun resendVerificationCode(phoneNumber: String) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(timeoutSeconds, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                    verificationCodeBySystem = verificationId
                    resendToken = token
                    Toast.makeText(applicationContext, "OTP Resent!", Toast.LENGTH_SHORT).show()
                }

                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    credential.smsCode?.let { verifyCode(credential.smsCode!!) } ?: signInWithPhoneAuthCredential(credential)
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    Toast.makeText(applicationContext, "Failed to resend OTP: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            })
            .setForceResendingToken(resendToken)
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun startResendTimer() {
        tvTimeout.isEnabled = false
        val timer = Timer()
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                runOnUiThread {
                    timeoutSeconds -= 1
                    tvTimeout.text = "Resend OTP in $timeoutSeconds seconds"

                    if (timeoutSeconds <= 0) {
                        timer.cancel()
                        timeoutSeconds = 60L
                        tvTimeout.isEnabled = true
                        tvTimeout.text = "Resend OTP"
                    }
                }
            }
        }, 0, 1000)
    }

    private fun addUser(uid: String?, name: String?, email: String?, password: String?, location: String?, phone: String?) {
        val user = hashMapOf(
            "uid" to uid,
            "name" to name,
            "email" to email,
            "password" to password,
            "location" to location,
            "phone" to phone
        )

        Firebase.firestore.collection("users").add(user)
            .addOnSuccessListener {
                startActivity(Intent(this, ConfirmActivity::class.java))
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to create account", Toast.LENGTH_SHORT).show()
            }
    }

    private fun addUserToken(uid: String?, name: String?, email: String?) {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.e("token", "Failed to get token")
            }
            val token = task.result.toString()
            val userToken = hashMapOf("uid" to uid, "token" to token, "name" to name, "email" to email)
            Firebase.firestore.collection("tokens").add(userToken)
        }
    }
}
