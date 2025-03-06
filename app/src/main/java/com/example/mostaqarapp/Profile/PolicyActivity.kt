package com.example.mostaqarapp.Profile

import android.content.Intent
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.example.mostaqarapp.R
import org.w3c.dom.Text

class PolicyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_policy)
        val name = intent.getStringExtra("name")
        val img = intent.getIntExtra("image",R.drawable.enter_opt_amico)
        val txt = intent.getStringExtra("text")

        val btnBack = findViewById<ImageView>(R.id.btnBack)
        val policytxt = findViewById<TextView>(R.id.policytxt)
        val polcyimg = findViewById<ImageView>(R.id.polcyimg)
        policytxt.text = name
        polcyimg.setImageResource(img)
        btnBack.setOnClickListener { finish() }
    }
}