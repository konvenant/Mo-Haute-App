package com.example.mohaute

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.Request
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.example.mohaute.data.Email
import com.example.mohaute.data.Measurements
import com.example.mohaute.data.UserDetails
import com.example.mohaute.data.UserId
import com.example.mohaute.viewModel.UserViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_home.menuClick
import kotlinx.android.synthetic.main.fragment_home.view.fabAdd
import kotlinx.android.synthetic.main.fragment_home.view.menuClick
import kotlinx.android.synthetic.main.fragment_home.view.placeRecycleView
import kotlinx.android.synthetic.main.fragment_online_home.profileImage
import kotlinx.android.synthetic.main.fragment_online_home.view.onlineRecycleView
import kotlinx.android.synthetic.main.fragment_online_home.view.profileImage
import kotlinx.android.synthetic.main.fragment_online_home.view.userEmail
import kotlinx.android.synthetic.main.fragment_online_home.view.userName

class OnlineHomeFragment : Fragment() {
    private val adapter: OnlineAdapter by lazy { OnlineAdapter()}
    private val args by  navArgs<OnlineHomeFragmentArgs>()
    private lateinit var onLineViewModel: OnlineListViewModel
    private lateinit var user: UserDetails
    private lateinit var progressDialog: ProgressDialog
    private lateinit var apiService: ApiService

    @SuppressLint("ResourceAsColor")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view =  inflater.inflate(R.layout.fragment_online_home, container, false)
        val retrofitHelper = RetrofitHelper()
        progressDialog = ProgressDialog(requireContext())
        progressDialog.setMessage("Loading...")
        apiService = retrofitHelper.createService(ApiService::class.java)
        user = args.details
        progressDialog.show()

        view.userEmail.text = user.email
        view.userName.text = user.businessName

        view.userEmail.setOnClickListener {
            val actions = OnlineHomeFragmentDirections.actionOnlineHomeFragmentToUpdateUserFragment(user)
            findNavController().navigate(actions)
        }
        view.userName.setOnClickListener {
            val actions = OnlineHomeFragmentDirections.actionOnlineHomeFragmentToUpdateUserFragment(user)
            findNavController().navigate(actions)
        }

        Log.d("image",user.image.toString())
        val profileImageView = view.profileImage
        val imageUri = user.image


        Glide.with(this)
            .load(imageUri)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(profileImageView)

        val id = user._id
        val recyclerView = view.onlineRecycleView

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        onLineViewModel = ViewModelProvider(this).get(OnlineListViewModel::class.java)
        onLineViewModel.performMeasurementApiCall(id,apiService)
        onLineViewModel.measurements.observe(viewLifecycleOwner, Observer {data ->
            progressDialog.dismiss()
            data?.let { adapter.setData(it) }
            Log.e("oooo", data.toString())
        })
        onLineViewModel.catchError.observe(viewLifecycleOwner, Observer {err ->
            if(err){
                Toast.makeText(requireContext(),"server Error",Toast.LENGTH_SHORT).show()
            }
        })
        onLineViewModel.otherError.observe(viewLifecycleOwner, Observer {err ->
            if(err){
                Toast.makeText(requireContext(),"not found Error",Toast.LENGTH_SHORT).show()
            }
        })

        view.fabAdd.setOnClickListener {
            val id = UserId(user._id)
            val addAction = OnlineHomeFragmentDirections.actionOnlineHomeFragmentToAddOnlineFragment(id)
            findNavController().navigate(addAction)
        }

        view.menuClick.setOnClickListener {
            showPopUp(menuClick)
        }

        view.profileImage.setOnClickListener {
            val actions = OnlineHomeFragmentDirections.actionOnlineHomeFragmentToUpdateUserFragment(user)
            findNavController().navigate(actions)
        }
        return view
    }

    private fun showPopUp(view: View) {
        val popup = PopupMenu(requireContext(),view)
        popup.inflate(R.menu.nav_menu2)
        popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item: MenuItem? ->
            when (item!!.itemId) {
                R.id.home -> {
                    val action = OnlineHomeFragmentDirections.actionOnlineHomeFragmentToHelpFragment(user)
                    findNavController().navigate(action)
                }
                R.id.details -> {
                    val actions = OnlineHomeFragmentDirections.actionOnlineHomeFragmentToUpdateUserFragment(user)
                    findNavController().navigate(actions)
                }
                R.id.Setting -> {
                    Toast.makeText(requireContext(), "Setting",Toast.LENGTH_SHORT).show()
                }
                R.id.help -> {
                    val action = OnlineHomeFragmentDirections.actionOnlineHomeFragmentToHelpFragment(user)
                    findNavController().navigate(action)
                }
                R.id.logout -> {
                    findNavController().navigate(R.id.action_onlineHomeFragment_to_loginFragment)
                }
                R.id.notice -> {
                    val email = Email(user.email)
                    val action = OnlineHomeFragmentDirections.actionOnlineHomeFragmentToNoticeFragment(email)
                    findNavController().navigate(action)
                }
            }
            true
        })
        popup.show()
    }

    }
