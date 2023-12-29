package com.example.mohaute

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.mohaute.data.UserEmail
import kotlinx.android.synthetic.main.fragment_sign_in_two.view.*
import kotlinx.android.synthetic.main.fragment_sign_up.view.*


class SignInTwoFragment : Fragment() {
    private val args by  navArgs<SignInTwoFragmentArgs>()
    private lateinit var onlineListViewModel: OnlineListViewModel
    private lateinit var progressDialog: ProgressDialog
    private lateinit var apiService: ApiService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_sign_in_two, container, false)
        onlineListViewModel = ViewModelProvider(this).get(OnlineListViewModel::class.java)
        val retrofitHelper = RetrofitHelper()
        progressDialog = ProgressDialog(requireContext())
        progressDialog.setMessage("Loading...")
        apiService = retrofitHelper.createService(ApiService::class.java)


        view.next.isEnabled = false

        view.goBackClick.setOnClickListener {
            findNavController().navigateUp()
        }
        val textWatcher = object: TextWatcher {
            @SuppressLint("UseCompatLoadingForDrawables", "SetTextI18n")
            override fun afterTextChanged(p0: Editable?) {
                val businessName = view.buiness_name_et.text.toString().trim()
                val phone = view.phone_et.text.toString().trim()
                val buttonSign = view.next

                val businessNameEt= view.buiness_name_et
                val phoneEt = view.phone_et

                val businessHint = view.tvBusinessnamehint
                val phoneHint = view.tvPhonehint


                if(businessNameEt.isFocused){
                    if ( businessName.isEmpty()){
                        buttonSign.isEnabled = false
                        businessHint.text = "Business name is required"
                        buttonSign.background = resources.getDrawable(R.drawable.button2_drawable)
                        businessNameEt.background = resources.getDrawable(R.drawable.edit_draawable3)
                    } else{
                        buttonSign.isEnabled = true
                        businessHint.text = ""
                        buttonSign.background = resources.getDrawable(R.drawable.logo_drawable)
                        businessNameEt.background = resources.getDrawable(R.drawable.edit_draawable)
                    }
                }

                if(phoneEt.isFocused){
                    if ( phone.isEmpty()){
                        buttonSign.isEnabled = false
                        phoneHint.text = "Phone Number is required"
                        buttonSign.background = resources.getDrawable(R.drawable.button2_drawable)
                        phoneEt.background = resources.getDrawable(R.drawable.edit_draawable3)
                    } else{
                        buttonSign.isEnabled = true
                        phoneHint.text = ""
                        buttonSign.background = resources.getDrawable(R.drawable.logo_drawable)
                        phoneEt.background = resources.getDrawable(R.drawable.edit_draawable)
                    }
                }


                if (businessName.isEmpty() || phone.isEmpty()){
                    buttonSign.isEnabled = false
                    buttonSign.background = resources.getDrawable(R.drawable.button2_drawable)
                } else{
                    buttonSign.isEnabled = true
                    buttonSign.background = resources.getDrawable(R.drawable.logo_drawable)
                }
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
        }

        val businessName = view.buiness_name_et
        val phone = view.phone_et
        businessName.addTextChangedListener(textWatcher)
        phone.addTextChangedListener(textWatcher)

        view.next.setOnClickListener {
            val url = "users/${args.email.email}"
            addNumAndName(url,apiService,businessName.text.toString(),phone.text.toString())
        }
        return view
    }

    private fun addNumAndName(url: String,apiService: ApiService,name:String,number:String) {
        progressDialog.show()
        onlineListViewModel.addNumAndName(apiService, url, name, number)
        onlineListViewModel.numAndNameAdded.observe(viewLifecycleOwner, Observer {success ->
            if (success){
                progressDialog.dismiss()
                Toast.makeText(requireContext(),"Added Number And Business Name", Toast.LENGTH_SHORT).show()
                onlineListViewModel.userUpdated.observe(viewLifecycleOwner, Observer { user->
                    val actions = SignInTwoFragmentDirections.actionSignInTwoFragmentToOnlineHomeFragment(user.user)
                    findNavController().navigate(actions)
                })
            } else{
                progressDialog.dismiss()
                Toast.makeText(requireContext(),"Error Occured", Toast.LENGTH_SHORT).show()
            }
        })
    }


}