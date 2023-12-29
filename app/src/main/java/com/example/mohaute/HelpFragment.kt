package com.example.mohaute

import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import kotlinx.android.synthetic.main.fragment_help.view.account_indicator
import kotlinx.android.synthetic.main.fragment_help.view.account_options
import kotlinx.android.synthetic.main.fragment_help.view.card_indicator
import kotlinx.android.synthetic.main.fragment_help.view.card_options
import kotlinx.android.synthetic.main.fragment_help.view.choose_card
import kotlinx.android.synthetic.main.fragment_help.view.enter_account_options
import kotlinx.android.synthetic.main.fragment_help.view.enter_transfer_options
import kotlinx.android.synthetic.main.fragment_help.view.expand_account
import kotlinx.android.synthetic.main.fragment_help.view.expand_account_content
import kotlinx.android.synthetic.main.fragment_help.view.expand_card
import kotlinx.android.synthetic.main.fragment_help.view.expand_card_content
import kotlinx.android.synthetic.main.fragment_help.view.transfer_indicator
import kotlinx.android.synthetic.main.fragment_help.view.transfer_options
import kotlinx.android.synthetic.main.fragment_help.view.tv_profile_name
import kotlinx.android.synthetic.main.fragment_sign_up.view.backClick


class HelpFragment : Fragment() {

    private val args by  navArgs<HelpFragmentArgs>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_help, container, false)

        view.tv_profile_name.text = "Hello ${args.user.businessName}, How can we help You"
        view.backClick.setOnClickListener {
            findNavController().navigateUp()
        }

        view.expand_card.setOnClickListener {
            view.expand_card_content.visibility = View.VISIBLE
            it.visibility = View.GONE
        }

        view.expand_account.setOnClickListener {
            view.expand_account_content.visibility = View.VISIBLE
            it.visibility = View.GONE
        }
        view.choose_card.setOnClickListener {
            view.card_options.visibility = View.VISIBLE
            view.card_indicator.visibility = View.VISIBLE
            view.account_indicator.visibility = View.GONE
            view.transfer_indicator.visibility = View.GONE
            view.transfer_options.visibility = View.GONE
            view.account_options.visibility = View.GONE
            view.choose_card.setTypeface(view.choose_card.typeface,Typeface.BOLD)
            view.enter_transfer_options.setTypeface(view.choose_card.typeface,Typeface.NORMAL)
            view.enter_account_options.setTypeface(view.choose_card.typeface,Typeface.NORMAL)
        }

        view.enter_transfer_options.setOnClickListener {
            view.transfer_options.visibility = View.VISIBLE
            view.transfer_indicator.visibility = View.VISIBLE
            view.account_indicator.visibility = View.GONE
            view.card_indicator.visibility = View.GONE
            view.card_options.visibility = View.GONE
            view.account_options.visibility = View.GONE
            view.choose_card.setTypeface(view.choose_card.typeface,Typeface.NORMAL)
            view.enter_transfer_options.setTypeface(view.choose_card.typeface,Typeface.BOLD)
            view.enter_account_options.setTypeface(view.choose_card.typeface,Typeface.NORMAL)
        }

        view.enter_account_options.setOnClickListener {
            view.account_options.visibility = View.VISIBLE
            view.account_indicator.visibility = View.VISIBLE
            view.card_indicator.visibility = View.GONE
            view.transfer_indicator.visibility = View.GONE
            view.transfer_options.visibility = View.GONE
            view.card_options.visibility = View.GONE
            view.choose_card.setTypeface(view.choose_card.typeface,Typeface.NORMAL)
            view.enter_transfer_options.setTypeface(view.choose_card.typeface,Typeface.NORMAL)
            view.enter_account_options.setTypeface(view.choose_card.typeface,Typeface.BOLD)
        }

        return view
    }
}