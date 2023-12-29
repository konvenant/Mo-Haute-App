package com.example.mohaute

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_choice.view.*


class ChoiceFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_choice, container, false)

        view.local.setOnClickListener {
            findNavController().navigate(R.id.action_choiceFragment_to_homeFragment)
        }
        view.login.setOnClickListener {
            findNavController().navigate(R.id.action_choiceFragment_to_signUpFragment)
        }
        return view
    }
}
