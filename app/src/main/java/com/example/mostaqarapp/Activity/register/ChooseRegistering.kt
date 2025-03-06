package com.example.mostaqarapp.Activity.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.mostaqarapp.R

class ChooseRegistering : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_registering)

        val buttonLoginChooseRegistering = findViewById<Button>(R.id.btnChoosLogin)
        val buttonRegisterChooseRegistering = findViewById<Button>(R.id.btnCntinue)


        buttonLoginChooseRegistering.setOnClickListener {
            val goToLoginActivity = Intent(this, LoginActivity::class.java)
            startActivity(goToLoginActivity)
        }

        buttonRegisterChooseRegistering.setOnClickListener {
            val goToRegisterActivity = Intent(this, RegisterActivity::class.java)
            startActivity(goToRegisterActivity)
        }

    }
}