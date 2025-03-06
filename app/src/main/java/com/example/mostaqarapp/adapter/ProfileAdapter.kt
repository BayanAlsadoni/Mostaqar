package com.example.mostaqarapp.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.mostaqarapp.Profile.PolicyActivity
import com.example.mostaqarapp.R
import com.example.mostaqarapp.data.ProfileData
import java.security.Policy
import java.util.*

class ProfileAdapter (val contex: Context, val data: ArrayList<ProfileData>)
    :BaseAdapter(){

    override fun getCount(): Int {
        return data.size
    }

    override fun getItem(p0: Int): Any {
        return data[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
           var adapterView = p1
        if(p1 == null) {
            adapterView = LayoutInflater.from(contex).inflate(R.layout.profile_item, null, false)
        }
        val iv = adapterView!!.findViewById<ImageView>(R.id.imgProfile)
        val tv = adapterView.findViewById<TextView>(R.id.nameOfprofileSetting)

        val prof = data[p0]
        iv.setImageResource(prof.icon)
        tv.text = prof.name
//        adapterView.setOnClickListener {
//            val i =Intent(contex, PolicyActivity::class.java)
//            i.putExtra("text",tv.text)
//            i.putExtra("image",iv)
//        }
//        btn.setOnClickListener{
//            Toast.makeText(context,"Button clicked",Toast.LENGTH_SHORT).show()
//        }
        return adapterView

    }

}


