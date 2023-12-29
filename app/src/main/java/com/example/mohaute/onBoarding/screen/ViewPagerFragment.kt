package com.example.mohaute.onBoarding.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mohaute.R
import com.example.mohaute.onBoarding.FirstSplashFragment
import com.example.mohaute.onBoarding.SecondSplashFragment
import com.example.mohaute.onBoarding.ThirdSplashFragment
import kotlinx.android.synthetic.main.fragment_view_pager.view.*


class ViewPagerFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_view_pager, container, false)

        val fragmentList = arrayListOf<Fragment>(
            FirstSplashFragment(),
            SecondSplashFragment(),
            ThirdSplashFragment()
        )

        val adapter = ViewPagerAdapter(
            fragmentList,
            requireActivity().supportFragmentManager,
            lifecycle
        )

        view.placeView.adapter = adapter



    return view
    }

}