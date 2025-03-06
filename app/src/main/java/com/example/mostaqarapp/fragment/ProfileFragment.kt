package com.example.mostaqarapp.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.mostaqarapp.Activity.register.ChooseRegistering
import com.example.mostaqarapp.Profile.ContactUsActivity
import com.example.mostaqarapp.Profile.EditProfileActivity
import com.example.mostaqarapp.Profile.PolicyActivity
import com.example.mostaqarapp.R
import com.example.mostaqarapp.adapter.ProfileAdapter
import com.example.mostaqarapp.data.ProfileData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import java.util.*

class ProfileFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_profile, container, false)

        val lvProfile = root.findViewById<ListView>(R.id.lvProf)
        val cardViewContactUs = root.findViewById<CardView>(R.id.cardViewContactUs)
        val cardViewLogout = root.findViewById<CardView>(R.id.cardViewLogout)
        val cardViewAccount = root.findViewById<CardView>(R.id.cardViewAccount)
        val cardViewFollowList = root.findViewById<CardView>(R.id.cardViewFollowList)
//        val clEditProfile = root.findViewById<ConstraintLayout>(R.id.clEditProfile)
        val profData = ArrayList<ProfileData>()

        cardViewLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(context, ChooseRegistering::class.java))
//            Firebase.auth.signOut()
        }
        cardViewAccount.setOnClickListener {
//            startActivity(Intent(context,EditProfileActivity::class.java))
           startActivity(Intent(context, EditProfileActivity::class.java))


        }

        profData.add(ProfileData(R.drawable.keypad,"سياسة الاستخدام",R.drawable.accept_terms_rafiki))
        profData.add(ProfileData(R.drawable.baseline_privacy_tip,"سياسة الخصوصية",R.drawable.privacy_policy_amico))
        profData.add(ProfileData(R.drawable.baseline_block,"المواد المحظورة",R.drawable.warning_rafiki))
        profData.add(ProfileData(R.drawable.keypad,"مركز الأمان",R.drawable.enter_opt_amico))


        val profAdapter = ProfileAdapter(requireContext(),profData)

        lvProfile.adapter = profAdapter

        lvProfile.setOnItemClickListener { adapterView, view, i, l ->
            val policy = Intent(context,PolicyActivity::class.java)

            policy.putExtra("name",profData[i].name)
            policy.putExtra("image",profData[i].image)
            policy.putExtra("text",profData[i].text)
            startActivity(policy)
        }

        cardViewContactUs.setOnClickListener {
            startActivity(Intent(context,ContactUsActivity::class.java))
        }

        return root
    }


}