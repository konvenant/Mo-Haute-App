package com.example.mohaute.onBoarding

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.mohaute.R
import kotlinx.android.synthetic.main.fragment_third_splash.view.*


class ThirdSplashFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_third_splash, container, false)

        val viewpager = activity?.findViewById<ViewPager2>(R.id.viewPagerFragment)

        view.next3.setOnClickListener {
            findNavController().navigate(R.id.action_viewPagerFragment_to_choiceFragment)
             onBoardingFinished()
        }
    return view
    }

    private fun onBoardingFinished () {
        val sharedPref = requireActivity().getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putBoolean("Finished", true)
        editor.apply()
    }

}