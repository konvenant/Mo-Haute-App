package com.example.mohaute

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.FileUtils
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.room.util.FileUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.mohaute.data.UserDetails
import kotlinx.android.synthetic.main.fragment_online_home.view.profileImage
import kotlinx.android.synthetic.main.fragment_sign_in_two.view.buiness_name_et
import kotlinx.android.synthetic.main.fragment_sign_in_two.view.goBackClick
import kotlinx.android.synthetic.main.fragment_sign_in_two.view.next
import kotlinx.android.synthetic.main.fragment_sign_in_two.view.phone_et
import kotlinx.android.synthetic.main.fragment_sign_in_two.view.tvBusinessnamehint
import kotlinx.android.synthetic.main.fragment_sign_in_two.view.tvPhonehint
import kotlinx.android.synthetic.main.fragment_update_user.view.btnSelectImage
import kotlinx.android.synthetic.main.fragment_update_user.view.btnUpdate
import kotlinx.android.synthetic.main.fragment_update_user.view.buiness_name
import kotlinx.android.synthetic.main.fragment_update_user.view.businessnamehint
import kotlinx.android.synthetic.main.fragment_update_user.view.detail_business
import kotlinx.android.synthetic.main.fragment_update_user.view.detail_number
import kotlinx.android.synthetic.main.fragment_update_user.view.email_tv
import kotlinx.android.synthetic.main.fragment_update_user.view.linear_update
import kotlinx.android.synthetic.main.fragment_update_user.view.password
import kotlinx.android.synthetic.main.fragment_update_user.view.passwordhint
import kotlinx.android.synthetic.main.fragment_update_user.view.phone
import kotlinx.android.synthetic.main.fragment_update_user.view.phonehint
import kotlinx.android.synthetic.main.fragment_update_user.view.tv_click
import kotlinx.android.synthetic.main.fragment_update_user.view.userImage
import java.io.File
import java.util.jar.Manifest


class UpdateUserFragment : Fragment() {
    private val args by  navArgs<UpdateUserFragmentArgs>()
    private lateinit var onLineViewModel: OnlineListViewModel
    private lateinit var progressDialog: ProgressDialog
    private lateinit var apiService: ApiService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_update_user, container, false)
        onLineViewModel = ViewModelProvider(this).get(OnlineListViewModel::class.java)
        val retrofitHelper = RetrofitHelper()
        progressDialog = ProgressDialog(requireContext())
        progressDialog.setMessage("Loading...")
        apiService = retrofitHelper.createService(ApiService::class.java)


        val profileImageView = view.userImage
        val imageUri = args.detail.image


        Glide.with(this)
            .load(imageUri)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(profileImageView)

        view.btnUpdate.isEnabled = false
        view.goBackClick.setOnClickListener {
            findNavController().navigateUp()
        }

        view.btnSelectImage.setOnClickListener {
           if (hasStoragePermission()){
               selectImage()
           } else{
               requestStoragePermission()
           }

        }
        onLineViewModel.uploadStatus.observe(viewLifecycleOwner, Observer {status ->
            when(status){
                is OnlineListViewModel.UploadStatus.InProgress ->{
                    progressDialog.show()
                }
                is OnlineListViewModel.UploadStatus.Success ->{
                    progressDialog.dismiss()
                    Toast.makeText(requireContext(),"image updated",Toast.LENGTH_SHORT).show()
                    onLineViewModel.uploadImage.observe(viewLifecycleOwner, Observer { user ->
                        val actions = UpdateUserFragmentDirections.actionUpdateUserFragmentToOnlineHomeFragment(user.user)
                         findNavController().navigate(actions)
                    })
                }
                is OnlineListViewModel.UploadStatus.Failure ->{
                    progressDialog.dismiss()
                    Toast.makeText(requireContext(),status.errorMessage,Toast.LENGTH_SHORT).show()
                }
            }
        })


        view.email_tv.text = args.detail.email
        view.detail_business.text = args.detail.businessName
        view.detail_number.text = args.detail.number
        view.buiness_name.setText(args.detail.businessName)
        view.phone.setText(args.detail.number)
        view.password.setText(args.detail.password)

        val textWatcher = object: TextWatcher {
            @SuppressLint("UseCompatLoadingForDrawables", "SetTextI18n")
            override fun afterTextChanged(p0: Editable?) {
                val businessName = view.buiness_name.text.toString().trim()
                val phone = view.phone.text.toString().trim()
                val password = view.password.text.toString().trim()
                val buttonSign = view.btnUpdate

                val businessNameEt= view.buiness_name
                val phoneEt = view.phone
                val passwordEt = view.password

                val businessHint = view.businessnamehint
                val phoneHint = view.phonehint
                val passwordHint = view.passwordhint


                if(businessNameEt.isFocused){
                    if ( businessName.isEmpty()){
                        buttonSign.isEnabled = false
                        businessHint.text = "Business name cannot be empty"
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

                if(passwordEt.isFocused){
                    if ( password.isEmpty()){
                        buttonSign.isEnabled = false
                        passwordHint.text = "Password cannot be empty"
                        buttonSign.background = resources.getDrawable(R.drawable.button2_drawable)
                        phoneEt.background = resources.getDrawable(R.drawable.edit_draawable3)
                    } else{
                        buttonSign.isEnabled = true
                        passwordHint.text = ""
                        buttonSign.background = resources.getDrawable(R.drawable.logo_drawable)
                        passwordEt.background = resources.getDrawable(R.drawable.edit_draawable)
                    }
                }


                if (businessName.isEmpty() || phone.isEmpty() || password.isEmpty()){
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

        val businessName = view.buiness_name
        val phone = view.phone
        val passwords = view.password
        businessName.addTextChangedListener(textWatcher)
        phone.addTextChangedListener(textWatcher)
       passwords.addTextChangedListener(textWatcher)



        view.btnUpdate.setOnClickListener {
            val url = "users/${args.detail.email}"
            val name = businessName.text.toString()
            val number = phone.text.toString()
            val password = passwords.text.toString()
            Log.d("detail", " name: ${name} number: $number password: $password")
            updateUser(apiService,url, name, number, password)
        }

        view.tv_click.setOnClickListener {
            if(view.linear_update.visibility == View.VISIBLE){
                val fadeOut = AnimationUtils.loadAnimation(requireContext(),R.anim.fade_out)
                fadeOut.setAnimationListener(object : Animation.AnimationListener{
                    override fun onAnimationEnd(p0: Animation?) {
                        view.linear_update.visibility = View.GONE
                    }
                    override fun onAnimationRepeat(p0: Animation?) {
                    }
                    override fun onAnimationStart(p0: Animation?) {
                    }
                })
                view.linear_update.startAnimation(fadeOut)
            } else{
                val fadeIn = AnimationUtils.loadAnimation(requireContext(),R.anim.fade_in)
                fadeIn.setAnimationListener(object : Animation.AnimationListener{
                    override fun onAnimationRepeat(p0: Animation?) {}

                    override fun onAnimationEnd(p0: Animation?) {}

                    override fun onAnimationStart(p0: Animation?) {
                        view.linear_update.visibility = View.VISIBLE
                    }
                })
                view.linear_update.startAnimation(fadeIn)
            }
        }

        return view
    }

    private fun selectImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_IMAGE_PICKER)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_PICKER && resultCode == RESULT_OK && data != null){
            val selectedImageUri : Uri? = data?.data
            val contentResolver = requireContext().contentResolver
            selectedImageUri?.let {
                val selectedImageFie = createFileFromUri(it)
                val url = "upload/${args.detail.email}"
                val fileType = contentResolver.getType(selectedImageUri)
                if (fileType == "image/jpeg" || fileType == "image/png"){
                    onLineViewModel.updateImage(apiService,url,selectedImageFie)
                } else{
                    Toast.makeText(requireContext(),"Only Accept jpeg and png",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun createFileFromUri(uri: Uri): File {
        val contentResolver = requireContext().contentResolver
        val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(uri,filePathColumn,null,null,null)
        cursor?.moveToFirst()
            val columnIndex = cursor?.getColumnIndex(filePathColumn[0])
            val filePath = cursor?.getString(columnIndex ?:0)
            cursor?.close()

        return File(filePath ?: "")
    }

    private fun hasStoragePermission(): Boolean{
        val readPermission = ContextCompat.checkSelfPermission(
            requireContext(),android.Manifest.permission.READ_EXTERNAL_STORAGE
        )
        val writePermission = ContextCompat.checkSelfPermission(
            requireContext(),android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        return readPermission == PackageManager.PERMISSION_GRANTED &&
                writePermission == PackageManager.PERMISSION_GRANTED
    }

    private fun requestStoragePermission(){
        ActivityCompat.requestPermissions(
            requireActivity(), arrayOf(
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ),
            REQUEST_PERMISSION_STORAGE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permission: Array<String>,
        grantResults: IntArray
    ){
        super.onRequestPermissionsResult(requestCode,permission,grantResults)

        if (requestCode == REQUEST_PERMISSION_STORAGE){
            if (grantResults.isNotEmpty() &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                grantResults[1] == PackageManager.PERMISSION_GRANTED
            ){
            selectImage()
            } else{
                Toast.makeText(requireContext(),"Storage permission denied, cannot select image",Toast.LENGTH_SHORT).show()
            }
        }
    }


    companion object{
        private const val REQUEST_PERMISSION_STORAGE = 1
        private const val REQUEST_IMAGE_PICKER = 2
    }

    private fun updateUser(apiService: ApiService,url: String,name:String,number: String,password:String) {
        progressDialog.show()
        onLineViewModel.updateUser(apiService,url, name, number, password)
        onLineViewModel.updateUser.observe(viewLifecycleOwner, Observer { success ->
            if (success){
                progressDialog.dismiss()
                Toast.makeText(requireContext(),"User Details Updated", Toast.LENGTH_SHORT).show()
                onLineViewModel.updateUserDetails.observe(viewLifecycleOwner, Observer { user ->
                    val actions = UpdateUserFragmentDirections.actionUpdateUserFragmentToOnlineHomeFragment(user.user)
                    findNavController().navigate(actions)
                })
            } else{
                progressDialog.dismiss()
                Toast.makeText(requireContext(),"Error Occured", Toast.LENGTH_SHORT).show()
            }
        })

    }



}