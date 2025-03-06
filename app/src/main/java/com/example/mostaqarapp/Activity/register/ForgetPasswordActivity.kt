package com.example.mostaqarapp.Activity.register

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.example.mostaqarapp.R

class ForgetPasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_password)
        val btnSendCode = findViewById<Button>(R.id.forger_btnSendCode)
        val forget_etPoneNum = findViewById<EditText>(R.id.forget_etPoneNum)
        btnSendCode.setOnClickListener {

        }
    }
}