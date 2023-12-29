package com.example.mohaute

import android.annotation.SuppressLint
import android.app.Application
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mohaute.data.UserDatabase
import com.example.mohaute.repository.Repository
import com.example.mohaute.viewModel.UserViewModel
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.fragment_home.view.my_toolbar
import kotlinx.android.synthetic.main.fragment_update.view.*


class HomeFragment : Fragment() {
    private lateinit var mUserViewModel: UserViewModel
    private val adapter: ListAdapter by lazy { ListAdapter()}


    @SuppressLint("SuspiciousIndentation")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)



        val recyclerView = view.placeRecycleView

//        if(recyclerView.canScrollVertically(-1)){
//            my_toolbar.elevation = 0f
//        } else {
//            my_toolbar.elevation = 50f
//        }


        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val controller = AnimationUtils.loadLayoutAnimation(requireContext(), R.anim.layout_right_to_left)
        recyclerView.layoutAnimation = controller
            recyclerView.scheduleLayoutAnimation()


        mUserViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        mUserViewModel.readAllData.observe(viewLifecycleOwner, Observer { user ->
            adapter.setData(user)
        })


        view.fabAdd.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_addUserFragment)
        }


        view.menuClick.setOnClickListener {
            showPopUp(menuClick)
        }


        view.fabAdd.startAnimation(AnimationUtils.loadAnimation(requireContext(),R.anim.continous_zoom_in_and_out))
        
        view.svSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null){
                    searchDatabase(newText)
                }

                return false
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
//                if (newText != null){
//                    searchDatabase(newText)
//                }
//
                return true
            }
        })
//        (activity as AppCompatActivity).setSupportActionBar(view.my_toolbar)
    return view
    }

    private fun showPopUp(view: View) {
        val popup = PopupMenu(requireContext(),view)
        popup.inflate(R.menu.nav_menu)
        popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item: MenuItem? ->
            when (item!!.itemId) {
                R.id.premium -> {
                    findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
                }
                R.id.Setting -> {
                    Toast.makeText(requireContext(), "Setting",Toast.LENGTH_SHORT).show()
                }
                R.id.help -> {
                    Toast.makeText(requireContext(), "Help",Toast.LENGTH_SHORT).show()
                }
            }
            true
        })
        popup.show()
    }
    private fun searchDatabase(query: String){
        val searchQuery = "%$query%"

        mUserViewModel.searchQuery(searchQuery).observe(this) { list ->
            list.let {
                adapter.setData(it)
            }

        }
    }

}