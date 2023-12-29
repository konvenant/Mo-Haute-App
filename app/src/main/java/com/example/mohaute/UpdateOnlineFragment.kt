package com.example.mohaute

import android.app.ProgressDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.mohaute.data.AddMeasurements
import com.example.mohaute.data.UpdateMeasurements
import com.example.mohaute.data.UserId
import com.example.mohaute.viewModel.UserViewModel
import kotlinx.android.synthetic.main.fragment_add_user.bicep
import kotlinx.android.synthetic.main.fragment_add_user.chest
import kotlinx.android.synthetic.main.fragment_add_user.cuff
import kotlinx.android.synthetic.main.fragment_add_user.customerName
import kotlinx.android.synthetic.main.fragment_add_user.neck
import kotlinx.android.synthetic.main.fragment_add_user.shoulder
import kotlinx.android.synthetic.main.fragment_add_user.sleeve
import kotlinx.android.synthetic.main.fragment_add_user.thigh
import kotlinx.android.synthetic.main.fragment_add_user.tommy
import kotlinx.android.synthetic.main.fragment_add_user.top2
import kotlinx.android.synthetic.main.fragment_add_user.trouser
import kotlinx.android.synthetic.main.fragment_add_user.waist
import kotlinx.android.synthetic.main.fragment_update.trouser2
import kotlinx.android.synthetic.main.fragment_update.view.bicep
import kotlinx.android.synthetic.main.fragment_update.view.chest
import kotlinx.android.synthetic.main.fragment_update.view.cuff
import kotlinx.android.synthetic.main.fragment_update.view.customerName
import kotlinx.android.synthetic.main.fragment_update.view.delete
import kotlinx.android.synthetic.main.fragment_update.view.fabUpdate
import kotlinx.android.synthetic.main.fragment_update.view.ibBack
import kotlinx.android.synthetic.main.fragment_update.view.neck
import kotlinx.android.synthetic.main.fragment_update.view.shoulder
import kotlinx.android.synthetic.main.fragment_update.view.sleeve
import kotlinx.android.synthetic.main.fragment_update.view.thigh
import kotlinx.android.synthetic.main.fragment_update.view.timeClock2
import kotlinx.android.synthetic.main.fragment_update.view.tommy
import kotlinx.android.synthetic.main.fragment_update.view.top2
import kotlinx.android.synthetic.main.fragment_update.view.trouser
import kotlinx.android.synthetic.main.fragment_update.view.trouser2
import kotlinx.android.synthetic.main.fragment_update.view.waist
import java.text.SimpleDateFormat
import java.util.Date


class UpdateOnlineFragment : Fragment() {

    private val args by  navArgs<UpdateOnlineFragmentArgs>()
    private lateinit var onlineListViewModel: OnlineListViewModel
    private lateinit var progressDialog: ProgressDialog
    private lateinit var apiService: ApiService
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_update_online, container, false)
        onlineListViewModel = ViewModelProvider(this).get(OnlineListViewModel::class.java)
        val retrofitHelper = RetrofitHelper()
        progressDialog = ProgressDialog(requireContext())
        progressDialog.setMessage("Loading...")
        apiService = retrofitHelper.createService(ApiService::class.java)

        val textWatcher = object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

                val customerName = customerName.text.toString()
                val sdf = SimpleDateFormat("dd MMM,yyyy-HH:mm")
                val time: String = sdf.format(Date())
                val shoulder = shoulder.text.toString()
                val sleeve = sleeve.text.toString()
                val neck = neck.text.toString()
                val chest = chest.text.toString()
                val tommy = tommy.text.toString()
                val top = top2.text.toString()
                val bicep = bicep.text.toString()
                val cuff = cuff.text.toString()
                val waist = waist.text.toString()
                val thigh = thigh.text.toString()
                val trouser = trouser.text.toString()
                val ankle = trouser2.text.toString()

                if (customerName.isEmpty() || shoulder.isEmpty() || sleeve.isEmpty() || neck.isEmpty() || chest.isEmpty()
                    || tommy.isEmpty() || top.isEmpty() || bicep.isEmpty() || cuff.isEmpty() || waist.isEmpty()
                    || thigh.isEmpty() || trouser.isEmpty()  || ankle.isEmpty()
                ){
                    view.fabUpdate.isEnabled = false
                    Toast.makeText(requireContext(),"All measurement is required", Toast.LENGTH_LONG).show()

                } else{
                    view.fabUpdate.isEnabled = true
                }


            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {


            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
        }

        view.customerName.setText(args.update.name.toString())
        view.timeClock2.setText(args.update.date.toString())
        view.shoulder.setText(args.update.shoulder.toString())
        view.sleeve.setText(args.update.sleeve.toString())
        view.neck.setText(args.update.neck.toString())
        view.chest.setText(args.update.chest.toString())
        view.tommy.setText(args.update.tommy.toString())
        view.top2.setText(args.update.top.toString())
        view.bicep.setText(args.update.bicep.toString())
        view.cuff.setText(args.update.cuff.toString())
        view.waist.setText(args.update.waist.toString())
        view.thigh.setText(args.update.thigh.toString())
        view.trouser.setText(args.update.trouser.toString())
        view.trouser2.setText(args.update.ankle.toString())


        view.customerName.addTextChangedListener(textWatcher)
        view.shoulder.addTextChangedListener(textWatcher)
        view.sleeve.addTextChangedListener(textWatcher)
        view.neck.addTextChangedListener(textWatcher)
        view.chest.addTextChangedListener(textWatcher)
        view.tommy.addTextChangedListener(textWatcher)
        view.top2.addTextChangedListener(textWatcher)
        view.bicep.addTextChangedListener(textWatcher)
        view.cuff.addTextChangedListener(textWatcher)
        view.waist.addTextChangedListener(textWatcher)
        view.thigh.addTextChangedListener(textWatcher)
        view.trouser.addTextChangedListener(textWatcher)
        view.trouser2.addTextChangedListener(textWatcher)
        view.ibBack.setOnClickListener{
            findNavController().navigateUp()
        }

        view.fabUpdate.setOnClickListener {
            updateMeasuremnt(args.update._id,apiService)
        }


        view.delete.setOnClickListener {
            deleteUser(apiService,args.update._id)
        }
        return view
    }

    private fun deleteUser(apiService: ApiService,measurementId: String) {
        progressDialog.show()
        val endpointUrl = "customer/${measurementId}"
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes"){ _,_ ->
            onlineListViewModel.deleteMeasurement(measurementId,apiService)
            onlineListViewModel.measurementDeleted.observe(viewLifecycleOwner, Observer {success ->
                if (success){
                    progressDialog.dismiss()
                    Toast.makeText(requireContext(),"Successfully Deleted: ${args.update.name}",Toast.LENGTH_SHORT).show()
                    findNavController().navigateUp()
                } else{
                    progressDialog.dismiss()
                    Toast.makeText(requireContext(),"Error Occurred, Try again",Toast.LENGTH_SHORT).show()
                }
            })

        }
        builder.setNegativeButton("No") {_,_ ->

        }
        builder.setTitle("Delete ${args.update.name}?")
        builder.setMessage("Are you sure you want to delete ${args.update.name}?")
        builder.setIcon(R.drawable.ic_person)
        builder.create().show()
    }

    fun updateMeasuremnt(measurementId: String, apiService: ApiService){
        progressDialog.show()
        val customerName = customerName.text.toString()
        val sdf = SimpleDateFormat("dd MMM,yyyy-HH:mm")
        val time: String = sdf.format(Date()).toString()
        val shoulder = shoulder.text.toString()
        val sleeve = sleeve.text.toString()
        val neck = neck.text.toString()
        val chest = chest.text.toString()
        val tommy = tommy.text.toString()
        val top = top2.text.toString()
        val bicep = bicep.text.toString()
        val cuff = cuff.text.toString()
        val waist = waist.text.toString()
        val thigh = thigh.text.toString()
        val trouser = trouser.text.toString()
        val ankle = trouser2.text.toString()
        val endpointUrl = "customer/${measurementId}"

//        val user = UpdateMeasurements(customerName.toString(),
//            shoulder.toString(),sleeve.toString(),neck.toString(),
//            chest.toString(),tommy.toString(),
//            top.toString(),bicep.toString(),cuff.toString(),waist.toString(),thigh.toString(),trouser.toString(),ankle.toString(),time)

        onlineListViewModel.updateMeasurement(apiService,endpointUrl,customerName,
            shoulder,sleeve,neck,chest,tommy,top,bicep,cuff,waist,thigh,trouser,ankle,time)
        onlineListViewModel.measurementUpdated.observe(viewLifecycleOwner, Observer { success ->
            if (success) {
                progressDialog.dismiss()
                Toast.makeText(requireContext(), "Successfully Updated $customerName", Toast.LENGTH_LONG).show()
                findNavController().navigateUp()
            } else{
                progressDialog.dismiss()
                Toast.makeText(requireContext(), "Failed,Try Again", Toast.LENGTH_LONG).show()
            }


        })

    }

}