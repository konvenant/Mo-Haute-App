package com.example.mohaute

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.example.mohaute.data.Measurements
import com.example.mohaute.data.OnlineUser
import com.example.mohaute.data.UserDetails
import com.example.mohaute.data.UserEmail
import com.example.mohaute.model.ErrorResponse
import com.example.mohaute.viewModel.UserViewModel
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_forgot_password.view.btn_forgot_password
import kotlinx.android.synthetic.main.fragment_login.view.checkBox
import kotlinx.android.synthetic.main.fragment_login.view.email_et
import kotlinx.android.synthetic.main.fragment_login.view.password_et
import kotlinx.android.synthetic.main.fragment_login.view.tvForgetPassword
import kotlinx.android.synthetic.main.fragment_sign_up.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class LoginFragment : Fragment() {
    private lateinit var apiService: ApiService
    private lateinit var progressDialog: ProgressDialog
    private lateinit var sharedPref: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
      val view =  inflater.inflate(R.layout.fragment_login, container, false)

        sharedPref = requireActivity().getPreferences(MODE_PRIVATE)

        val savedEmail = sharedPref.getString("email",null)
        val savedPassword = sharedPref.getString("password",null)
        val savedRememberMe = sharedPref.getBoolean("rememberMe",false)

        view.email_et.setText(savedEmail)
        view.password_et.setText(savedPassword)
        view.checkBox.isChecked = savedRememberMe

        progressDialog = ProgressDialog(requireContext())
        progressDialog.setMessage("Loading...")
        view.sign_in_et.setOnClickListener {
            val email = view.email_et.text.toString()
            val password = view.password_et.text.toString()
            val rememberMe = view.checkBox.isChecked
            performApiCall(email,password)


            if (rememberMe){
                with(sharedPref.edit()){
                    putString("email",email)
                    putString("password",password)
                    putBoolean("rememberMe",rememberMe)
                    apply()
                }
            } else{
                with(sharedPref.edit()){
                    remove("email")
                    remove("password")
                    remove("rememberMe")
                    apply()
                }
            }
        }

        view.tvForgetPassword.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_forgotPasswordFragment)
        }

        view.backClick.setOnClickListener {
            findNavController().navigateUp()
        }
        view.tvSignin.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
        }
        view.tvSignin.isClickable = true
        view.sign_in_et.isEnabled = false
        val textWatcher = object: TextWatcher {
            @SuppressLint("UseCompatLoadingForDrawables", "SetTextI18n")
            override fun afterTextChanged(p0: Editable?) {
                val email = view.email_et.text.toString().trim()
                val password = view.password_et.text.toString().trim()
                val buttonSign = view.sign_in_et
                val remember = view.checkBox

                val emailEt = view.email_et
                val passwordEt = view.password_et

                val emailHint = view.tvemailhint
                val passwordHint = view.tvPasswordhint




                if(emailEt.isFocused){
                    if (email.isEmpty()){
                        buttonSign.isEnabled = false
                        remember.isEnabled = false
                        emailHint.text = "Email is required"
                        buttonSign.background = resources.getDrawable(R.drawable.button2_drawable)
                        emailEt.background = resources.getDrawable(R.drawable.edit_draawable3)
                    } else{
                        buttonSign.isEnabled = true
                        remember.isEnabled = true
                        emailHint.text = ""
                        buttonSign.background = resources.getDrawable(R.drawable.logo_drawable)
                        emailEt.background = resources.getDrawable(R.drawable.edit_draawable)
                    }
                }


                if (passwordEt.isFocused){
                    if (password.isEmpty()){
                        buttonSign.isEnabled = false
                        remember.isEnabled = false
                        passwordHint.text = "password cannot be empty"
                        buttonSign.background = resources.getDrawable(R.drawable.button2_drawable)
                        passwordEt.background = resources.getDrawable(R.drawable.edit_draawable3)
                    } else{
                        buttonSign.isEnabled = true
                        remember.isEnabled = true
                        passwordHint.text = ""
                        buttonSign.background = resources.getDrawable(R.drawable.logo_drawable)
                        passwordEt.background = resources.getDrawable(R.drawable.edit_draawable)
                    }
                }


                if ( password.isEmpty() || email.isEmpty() ){
                    buttonSign.isEnabled = false
                    remember.isEnabled = false
                    buttonSign.background = resources.getDrawable(R.drawable.button2_drawable)

                } else{
                    buttonSign.isEnabled = true
                    remember.isEnabled = true
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

        email.addTextChangedListener(textWatcher)
        password.addTextChangedListener(textWatcher)
        return view
    }

    private fun performApiCall(email:String, password:String) {
        val retrofitHelper = RetrofitHelper()
        apiService = retrofitHelper.createService(ApiService::class.java)
        lifecycleScope.launch() {
            try{
                progressDialog.show()
                val response = apiService.getUserByIdAndPassword(email,password)
                if(response.isSuccessful){
                    val data = response.body()?.user

                    if (data!!.number == null || data!!.businessName == null) {
                        val email = UserEmail(data!!.email,data!!.password)
                        val actions = LoginFragmentDirections.actionLoginFragmentToSignInTwoFragment(email)
                        findNavController().navigate(actions)
                    } else{
                        val action = LoginFragmentDirections.actionLoginFragmentToOnlineHomeFragment(
                            data!!
                        )
                        Log.d("response","user: $data" )
                        findNavController().navigate(action)
                    }
                } else{
                    val error = Gson().fromJson(response.errorBody()?.string(), ErrorResponse::class.java)
                    val errorMessage = error?.message ?: "Unknown error"
                    if(response.code() == 401 && errorMessage == "Incorrect Password"){
                        val builder = AlertDialog.Builder(requireContext())
                        builder.setPositiveButton("Close"){ _,_ ->

                        }
                        builder.setTitle("Login error")
                        builder.setMessage("Incorrect Password, Try again")
                        builder.setIcon(R.drawable.baseline_error_24)
                        builder.create().show()
                    } else{
                        val builder = AlertDialog.Builder(requireContext())
                        builder.setPositiveButton("Close"){ _,_ ->

                        }
                        builder.setTitle("Error: Invalid Credentials")
                        builder.setMessage("Email not found, enter a valid email")
                        builder.setIcon(R.drawable.baseline_error_24)
                        builder.create().show()
                    }

                }
            } catch (e:Exception){
                val builder = AlertDialog.Builder(requireContext())
                builder.setPositiveButton("Try again"){ _,_ ->

                }
                builder.setTitle("Failed to Login")
                builder.setMessage("Unable to Login Now, Try again ")
                builder.setIcon(R.drawable.baseline_error_24)
                builder.create().show()
            } finally {
                progressDialog.dismiss()
            }
        }

    }

}




