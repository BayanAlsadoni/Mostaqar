package com.example.mostaqarapp.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.mostaqarapp.Activity.onboarding.OnboardingActivity
import com.example.mostaqarapp.R
import com.example.mostaqarapp.Activity.register.ChooseRegistering

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)


        Handler().postDelayed({
            val i = Intent(this, OnboardingActivity::class.java)
            startActivity(i)
            finish()
        }, 5000)

    }
}