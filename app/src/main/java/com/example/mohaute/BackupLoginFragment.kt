package com.example.mohaute

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.ProgressDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.mohaute.data.AddMeasurements
import com.example.mohaute.data.BackUp
import com.example.mohaute.data.UserEmail
import com.example.mohaute.model.ErrorResponse
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_sign_up.view.backClick
import kotlinx.android.synthetic.main.fragment_sign_up.view.email_et
import kotlinx.android.synthetic.main.fragment_sign_up.view.password_et
import kotlinx.android.synthetic.main.fragment_sign_up.view.sign_in_et
import kotlinx.android.synthetic.main.fragment_sign_up.view.tvPasswordhint
import kotlinx.android.synthetic.main.fragment_sign_up.view.tvSignin
import kotlinx.android.synthetic.main.fragment_sign_up.view.tvemailhint
import kotlinx.coroutines.launch


class BackupLoginFragment : Fragment() {

    private lateinit var apiService: ApiService
    private lateinit var progressDialog: ProgressDialog
    private val args by  navArgs<BackupLoginFragmentArgs>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_backup_login, container, false)

        progressDialog = ProgressDialog(requireContext())
        progressDialog.setMessage("Loading...")
        view.sign_in_et.setOnClickListener {
            val email = view.email_et.text.toString()
            val password = view.password_et.text.toString()
            performApiCall(email,password)
        }

        view.backClick.setOnClickListener {
            findNavController().navigateUp()
        }
        view.tvSignin.setOnClickListener {
            findNavController().navigate(R.id.action_backupLoginFragment_to_signUpFragment)
        }
        view.tvSignin.isClickable = true
        view.sign_in_et.isEnabled = false
        val textWatcher = object: TextWatcher {
            @SuppressLint("UseCompatLoadingForDrawables", "SetTextI18n")
            override fun afterTextChanged(p0: Editable?) {
                val email = view.email_et.text.toString().trim()
                val password = view.password_et.text.toString().trim()
                val buttonSign = view.sign_in_et

                val emailEt = view.email_et
                val passwordEt = view.password_et

                val emailHint = view.tvemailhint
                val passwordHint = view.tvPasswordhint




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


                if ( password.isEmpty() || email.isEmpty() ){
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
                    val data = response.body()
                    val measure = args.backup
                    val backup = BackUp(data!!,measure)
                        val action = BackupLoginFragmentDirections.actionBackupLoginFragmentToBackupAddMeasurementFragment(backup)
                        Log.d("response","user: $data" )
                        findNavController().navigate(action)

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