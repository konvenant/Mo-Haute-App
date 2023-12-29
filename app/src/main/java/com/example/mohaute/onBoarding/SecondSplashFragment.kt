package com.example.mohaute.onBoarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.example.mohaute.R
import kotlinx.android.synthetic.main.fragment_second_splash.view.*
import kotlinx.android.synthetic.main.fragment_third_splash.view.*


class SecondSplashFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_second_splash, container, false)

        val viewpager = activity?.findViewById<ViewPager2>(R.id.placeView)

        view.next2.setOnClickListener {
            viewpager?.currentItem = 2
        }

    return view
    }


}