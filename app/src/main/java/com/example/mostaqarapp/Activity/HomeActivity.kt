package com.example.mostaqarapp.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import com.example.mostaqarapp.Activity.chat.ChatActivity
import com.example.mostaqarapp.Activity.chat.UsersMessagesActivity
import com.example.mostaqarapp.R
import com.example.mostaqarapp.fragment.AddHomeFragment
//import com.example.mostaqarapp.addHome.AddHomeFirstActivity
import com.example.mostaqarapp.fragment.HomeFragment
import com.example.mostaqarapp.fragment.ProfileFragment
import com.example.mostaqarapp.fragment.SaveFragment
import com.example.mostaqarapp.map.MapsFragment

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val layoutContent = R.id.layoutContent
        val btnHome = findViewById<ImageButton>(R.id.btnHome)
        val btnSave = findViewById<ImageButton>(R.id.btnSave)
        val btnAddHome = findViewById<ImageButton>(R.id.btnAddHome)
        val btnChat = findViewById<ImageButton>(R.id.btnChat)
        val btnProfile = findViewById<ImageButton>(R.id.btnProfile)

        supportFragmentManager.beginTransaction().replace(layoutContent,HomeFragment()).commit()

        btnHome.setOnClickListener {
            supportFragmentManager.beginTransaction().replace(layoutContent,HomeFragment()).commit()
            btnHome.setImageResource(R.drawable.click_home)
        }

        btnSave.setOnClickListener {
            supportFragmentManager.beginTransaction().replace(layoutContent,SaveFragment()).commit()
            Toast.makeText(this, "add clicked", Toast.LENGTH_SHORT).show()
            btnSave.setImageResource(R.drawable.ic_fill_save)

        }

        btnAddHome.setOnClickListener {
//            startActivity(Intent(this, AddHomeFirstActivity::class.java))
            supportFragmentManager.beginTransaction().replace(layoutContent,AddHomeFragment()).commit()
            Toast.makeText(this, "add clicked", Toast.LENGTH_SHORT).show()
            btnAddHome.setImageResource(R.drawable.click_add)

        }

        btnChat.setOnClickListener {
//            supportFragmentManager.beginTransaction().replace(layoutContent, MapsFragment()).commit()
            startActivity(Intent(this, UsersMessagesActivity::class.java))
            btnChat.setImageResource(R.drawable.click_chat)
        }

        btnProfile.setOnClickListener {
            supportFragmentManager.beginTransaction().replace(layoutContent,ProfileFragment()).commit()
            btnProfile.setImageResource(R.drawable.click_account)
        }

    }
}