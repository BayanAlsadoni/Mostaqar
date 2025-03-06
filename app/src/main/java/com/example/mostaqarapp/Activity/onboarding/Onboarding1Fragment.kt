package com.example.mostaqarapp.Activity.onboarding

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.viewpager2.widget.ViewPager2
import com.example.mostaqarapp.Activity.register.ChooseRegistering
import com.example.mostaqarapp.R


class Onboarding1Fragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_onboarding1, container, false)
        val btn = view.findViewById<Button>(R.id.buttonNext)
        val skip = view.findViewById<TextView>(R.id.skip)
        val viewPager = activity?.findViewById<ViewPager2>(R.id.vpOnboarding)
        btn.setOnClickListener {
            viewPager?.currentItem = 1
        }
        skip.setOnClickListener {
            context?.startActivity(Intent(context, ChooseRegistering::class.java))
        }
        return view
    }


}