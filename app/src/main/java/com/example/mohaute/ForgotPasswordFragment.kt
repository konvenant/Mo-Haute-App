package com.example.mohaute

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.ProgressDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_forgot_password.forgot_email_et
import kotlinx.android.synthetic.main.fragment_forgot_password.view.btn_forgot_password
import kotlinx.android.synthetic.main.fragment_forgot_password.view.forgot_email_et
import kotlinx.android.synthetic.main.fragment_sign_up.view.backClick
import kotlinx.android.synthetic.main.fragment_sign_up.view.email_et
import kotlinx.android.synthetic.main.fragment_sign_up.view.password_et
import kotlinx.android.synthetic.main.fragment_sign_up.view.sign_in_et
import kotlinx.android.synthetic.main.fragment_sign_up.view.tvPasswordhint
import kotlinx.android.synthetic.main.fragment_sign_up.view.tvSignin
import kotlinx.android.synthetic.main.fragment_sign_up.view.tvemailhint


class ForgotPasswordFragment : Fragment() {
    private lateinit var onlineListViewModel: OnlineListViewModel
    private lateinit var progressDialog: ProgressDialog
    private lateinit var apiService: ApiService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_forgot_password, container, false)

        onlineListViewModel = ViewModelProvider(this).get(OnlineListViewModel::class.java)
        val retrofitHelper = RetrofitHelper()
        progressDialog = ProgressDialog(requireContext())
        progressDialog.setMessage("Loading...")
        apiService = retrofitHelper.createService(ApiService::class.java)

        view.backClick.setOnClickListener {
            findNavController().navigateUp()
        }

        view.tvSignin.setOnClickListener {
            findNavController().navigate(R.id.action_forgotPasswordFragment_to_loginFragment)
        }
        view.btn_forgot_password.setOnClickListener {
            val url = "forgot"
            val email = view.forgot_email_et.text.toString()
            forgotPassword(url,email)
        }

        view.tvSignin.isClickable = true
        view.btn_forgot_password.isEnabled = false
        val textWatcher = object: TextWatcher {
            @SuppressLint("UseCompatLoadingForDrawables", "SetTextI18n")
            override fun afterTextChanged(p0: Editable?) {
                val email = view.forgot_email_et.text.toString().trim()
                val buttonSign = view.btn_forgot_password
                val emailEt = view.forgot_email_et
                val emailHint = view.tvemailhint


                if(emailEt.isFocused){
                    if (email.isEmpty()){
                        buttonSign.isEnabled = false
                        emailHint.text = "Email is required"
                        buttonSign.background = resources.getDrawable(R.drawable.button2_drawable)
                        emailEt.background = resources.getDrawable(R.drawable.edit_draawable3)
                    } else{
                        buttonSign.isEnabled = true
                        emailHint.text = ""
                        buttonSign.background = resources.getDrawable(R.drawable.logo_drawable)
                        emailEt.background = resources.getDrawable(R.drawable.edit_draawable)
                    }
                }


                if (email.isEmpty() ){
                    buttonSign.isEnabled = false
                    buttonSign.background = resources.getDrawable(R.drawable.button2_drawable)
                } else{
                    buttonSign.isEnabled = true
                    buttonSign.background = resources.getDrawable(R.drawable.logo_drawable)
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
        }

        val email = view.forgot_email_et
        email.addTextChangedListener(textWatcher)

        return view
    }

    private fun forgotPassword(url: String, email: String) {
      progressDialog.show()
        onlineListViewModel.forgoPassword(apiService,url, email)
        onlineListViewModel.forgotStatus.observe(viewLifecycleOwner, Observer {success ->
            if (success){
                progressDialog.dismiss()
                val builder = AlertDialog.Builder(requireContext())
                builder.setPositiveButton("Close"){ _,_ ->
                    findNavController().navigate(R.id.action_forgotPasswordFragment_to_loginFragment)
                }
                builder.setTitle("Email Sent")
                builder.setMessage("password has been said to $email")
                builder.setIcon(R.drawable.baseline_check_circle_24)
                builder.create().show()
            } else{
                progressDialog.dismiss()
                val builder = AlertDialog.Builder(requireContext())
                builder.setPositiveButton("Close"){ _,_ ->

                }
                builder.setTitle("Error")
                builder.setMessage("Error Occured, Try again")
                builder.setIcon(R.drawable.baseline_error_24)
                builder.create().show()
            }
        })

        onlineListViewModel.forgotNotFound.observe(viewLifecycleOwner, Observer { err ->
            if (err){
                progressDialog.dismiss()
                val builder = AlertDialog.Builder(requireContext())
                builder.setPositiveButton("Close"){ _,_ ->
                  forgot_email_et.setText("")
                }
                builder.setTitle("Email Not Found")
                builder.setMessage("This email ($email) is not registered")
                builder.setIcon(R.drawable.baseline_error_24)
                builder.create().show()
            }
        })

    }


}