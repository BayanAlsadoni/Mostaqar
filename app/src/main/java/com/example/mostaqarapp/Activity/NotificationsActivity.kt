package com.example.mostaqarapp.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import com.example.mostaqarapp.Profile.PolicyActivity
import com.example.mostaqarapp.R
import com.example.mostaqarapp.adapter.ProfileAdapter
import com.example.mostaqarapp.data.NotificationData
import com.example.mostaqarapp.data.ProfileData
import java.util.*

class NotificationsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifications)

        val lvNotification = findViewById<ListView>(R.id.lvNotification)
        val notifiData = ArrayList<NotificationData>()
//        val notifiAdapter = ProfileAdapter(this,notifiData::class.java)

//        lvNotification.adapter = notifiAdapter
//        notifiData.add(ProfileData(R.drawable.keypad,"سياسة الاستخدام",R.drawable.accept_terms_rafiki))
        lvNotification.setOnItemClickListener { adapterView, view, i, l ->
//            val policy = Intent(this, PolicyActivity::class.java)
//
//            policy.putExtra("name",profData[i].name)
//            policy.putExtra("image",profData[i].image)
//            policy.putExtra("text",profData[i].text)
//            startActivity(policy)
        }

    }
}