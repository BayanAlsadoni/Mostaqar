package com.example.mostaqarapp.Activity.onboarding

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.mostaqarapp.R
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator

class OnboardingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)
        val vpOnboarding = findViewById<ViewPager2>(R.id.vpOnboarding)
        var adapter = OnboardingAdapter(this)
        vpOnboarding.adapter = adapter


        /*
        val btnNext = findViewById<Button>(R.id.btnNext)
        btnNext.setOnClickListener {
            val current = viewPager.currentItem + 1
            if (current < layouts.size) {
                viewPager.currentItem = current
            } else {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
        * */

        val dotsIndicator = findViewById<DotsIndicator>(R.id.dots_indicator)
//        val viewPager = findViewById<ViewPager>(R.id.view_pager)
////        val adapter = ViewPagerAdapter()
//        viewPager.adapter = adapter
        dotsIndicator.attachTo(vpOnboarding)

        //yourTextView.setMovementMethod(new ScrollingMovementMethod());


    }
    class OnboardingAdapter(var fragmentActivity: FragmentActivity):FragmentStateAdapter(fragmentActivity){
        override fun getItemCount(): Int {
            return TOTAL_COUNT
        }

        override fun createFragment(position: Int): Fragment {
            var fragment = Fragment()
            when(position){
                0 -> fragment = Onboarding1Fragment()
                1 -> fragment = Onboarding2Fragment()
                2 -> fragment = Onboarding3Fragment()
            }
            return fragment
        }

    }
    companion object{
        val TOTAL_COUNT = 3
    }
}