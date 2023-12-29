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
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.mohaute.data.UserEmail
import kotlinx.android.synthetic.main.fragment_add_user.view.*
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_sign_up.view.*


class SignUpFragment : Fragment() {
    private lateinit var onlineListViewModel: OnlineListViewModel
    private lateinit var progressDialog: ProgressDialog
    private lateinit var apiService: ApiService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_sign_up, container, false)
        onlineListViewModel = ViewModelProvider(this).get(OnlineListViewModel::class.java)
        val retrofitHelper = RetrofitHelper()
        progressDialog = ProgressDialog(requireContext())
        progressDialog.setMessage("Loading...")
        apiService = retrofitHelper.createService(ApiService::class.java)


        view.backClick.setOnClickListener {
            findNavController().navigateUp()
        }

        view.sign_in_et.isEnabled = false
        val textWatcher = object: TextWatcher {
            @SuppressLint("UseCompatLoadingForDrawables", "SetTextI18n")
            override fun afterTextChanged(p0: Editable?) {
                val email = view.email_et.text.toString().trim()
                val password = view.password_et.text.toString().trim()
                val confirm = view.confirm_et.text.toString().trim()
                val buttonSign = view.sign_in_et

                val emailEt = view.email_et
                val passwordEt = view.password_et
                val confirmEt = view.confirm_et


                val emailHint = view.tvemailhint
                val passwordHint = view.tvPasswordhint
                val confirmHint = view.tvConfirmPasswordhint


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



                if (confirmEt.isFocused){
                    if ( confirm.isEmpty()){
                        buttonSign.isEnabled = false
                        confirmHint.text = "Field cannot be empty"
                        buttonSign.background = resources.getDrawable(R.drawable.button2_drawable)
                        confirmEt.background = resources.getDrawable(R.drawable.edit_draawable3)
                    } else{
                        buttonSign.isEnabled = true
                        confirmHint.text = ""
                        buttonSign.background = resources.getDrawable(R.drawable.logo_drawable)
                        confirmEt.background = resources.getDrawable(R.drawable.edit_draawable)
                    }
                }

                if (passwordEt.isFocused){
                    if (password.isEmpty()){
                        buttonSign.isEnabled = false
                        passwordHint.text = "password cannot be empty"
                        buttonSign.background = resources.getDrawable(R.drawable.button2_drawable)
                        passwordEt.background = resources.getDrawable(R.drawable.edit_draawable3)
                    } else{
                        buttonSign.isEnabled = true
                        passwordHint.text = ""
                        buttonSign.background = resources.getDrawable(R.drawable.logo_drawable)
                        passwordEt.background = resources.getDrawable(R.drawable.edit_draawable)
                    }
                }

                if (passwordEt.isFocused){
                    if (password.length < 4){
                        buttonSign.isEnabled = false
                        passwordHint.text = "four digit required"
                        buttonSign.background = resources.getDrawable(R.drawable.button2_drawable)
                        passwordEt.background = resources.getDrawable(R.drawable.edit_draawable3)
                    } else{
                        buttonSign.isEnabled = true
                        passwordHint.text = ""
                        buttonSign.background = resources.getDrawable(R.drawable.logo_drawable)
                        passwordEt.background = resources.getDrawable(R.drawable.edit_draawable)
                    }
                }

                if (confirmEt.isFocused){
                    if (password.toString() != confirm.toString() ){
                        buttonSign.isEnabled = false
                        confirmHint.text = "password don't match"
                        buttonSign.background = resources.getDrawable(R.drawable.button2_drawable)
                    } else{
                        buttonSign.isEnabled = true
                        confirmHint.text = ""
                        buttonSign.background = resources.getDrawable(R.drawable.logo_drawable)
                    }
                }

                if (confirm.isEmpty() || password.isEmpty() || email.isEmpty() ){
                    buttonSign.isEnabled = false
                    buttonSign.background = resources.getDrawable(R.drawable.button2_drawable)
                } else{
                    buttonSign.isEnabled = true
                    buttonSign.background = resources.getDrawable(R.drawable.logo_drawable)
                }

                if (confirm != password || password.isEmpty() || confirm.isEmpty()){
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

        val email = view.email_et
        val password = view.password_et
        val confirm = view.confirm_et
        val tvsign = view.tvSignin


        email.addTextChangedListener(textWatcher)
        password.addTextChangedListener(textWatcher)
        confirm.addTextChangedListener(textWatcher)

        view.sign_in_et.setOnClickListener {
            val url = "user"
            addUser(url,email.text.toString(),password.text.toString(),apiService)
        }

        tvsign.setOnClickListener {
            findNavController().navigate(R.id.action_signUpFragment_to_loginFragment)
        }


        return  view
    }

    private fun addUser(url:String,email:String,password:String,apiService:ApiService) {
        progressDialog.show()
        onlineListViewModel.addUser(apiService,url, email,password)
        onlineListViewModel.userAdded.observe(viewLifecycleOwner, Observer { success->
            if (success){
                progressDialog.dismiss()
                Toast.makeText(requireContext(),"Added User",Toast.LENGTH_SHORT).show()
                val emailHolder = UserEmail(view?.email_et?.text.toString(), view?.password_et?.text.toString())
                val actions = SignUpFragmentDirections.actionSignUpFragmentToSignInTwoFragment(emailHolder)
                findNavController().navigate(actions)
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
        onlineListViewModel.foundUser.observe(viewLifecycleOwner, Observer { err ->
            if (err){
                progressDialog.dismiss()
                val builder = AlertDialog.Builder(requireContext())
                builder.setPositiveButton("Close"){ _,_ ->

                }
                builder.setTitle("Error: Duplicate User Found")
                builder.setMessage("User: $email Already Exist")
                builder.setIcon(R.drawable.baseline_error_24)
                builder.create().show()
            }
        })
    }

}