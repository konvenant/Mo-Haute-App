package com.example.mohaute

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mohaute.data.UserDetails
import kotlinx.android.synthetic.main.fragment_notice.noticeRecycleView
import kotlinx.android.synthetic.main.fragment_notice.view.noticeRecycleView
import kotlinx.android.synthetic.main.fragment_online_home.view.onlineRecycleView

class NoticeFragment : Fragment() {
    private val adapter: NoticeAdapter by lazy { NoticeAdapter()}
    private lateinit var onLineViewModel: OnlineListViewModel
    private lateinit var progressDialog: ProgressDialog
    private lateinit var apiService: ApiService
    private val args by  navArgs<NoticeFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_notice, container, false)
        val retrofitHelper = RetrofitHelper()
        progressDialog = ProgressDialog(requireContext())
        progressDialog.setMessage("Loading...")
        apiService = retrofitHelper.createService(ApiService::class.java)
        progressDialog.show()

        val recyclerView = view.noticeRecycleView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    Log.e("email","${args.email.email}")
        onLineViewModel = ViewModelProvider(this).get(OnlineListViewModel::class.java)
        onLineViewModel.performNoticeApiCall(args.email.email,apiService)
        onLineViewModel.notice.observe(viewLifecycleOwner, Observer {data ->
            progressDialog.dismiss()
            data?.let { adapter.setData(it) }
            Log.e("responseNotice","notice: ${data.toString()}" )
        })

        onLineViewModel.noticeCatchError.observe(viewLifecycleOwner, Observer {err ->
            if(err){
                progressDialog.dismiss()
                Toast.makeText(requireContext(),"server Error", Toast.LENGTH_SHORT).show()
                onLineViewModel.noticeErrorMessage.observe(viewLifecycleOwner, Observer { message ->
                    Toast.makeText(requireContext(),"${message}",Toast.LENGTH_SHORT).show()
                    Log.e("err","${message}")
                })
            }
        })

        onLineViewModel.noticeOtherError.observe(viewLifecycleOwner, Observer {err ->
            if(err){
                progressDialog.dismiss()
                Toast.makeText(requireContext(),"Notice Error", Toast.LENGTH_SHORT).show()
            }
        })
        return view
    }
}