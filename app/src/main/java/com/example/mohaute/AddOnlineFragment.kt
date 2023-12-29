package com.example.mohaute

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.mohaute.data.AddMeasurements
import com.example.mohaute.data.UserDetails
import com.example.mohaute.data.UserId
import com.example.mohaute.model.User
import kotlinx.android.synthetic.main.fragment_add_user.ankle
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
import kotlinx.android.synthetic.main.fragment_add_user.view.ankle
import kotlinx.android.synthetic.main.fragment_add_user.view.bicep
import kotlinx.android.synthetic.main.fragment_add_user.view.chest
import kotlinx.android.synthetic.main.fragment_add_user.view.cuff
import kotlinx.android.synthetic.main.fragment_add_user.view.customerName
import kotlinx.android.synthetic.main.fragment_add_user.view.fabUpdate
import kotlinx.android.synthetic.main.fragment_add_user.view.icBack
import kotlinx.android.synthetic.main.fragment_add_user.view.neck
import kotlinx.android.synthetic.main.fragment_add_user.view.shoulder
import kotlinx.android.synthetic.main.fragment_add_user.view.sleeve
import kotlinx.android.synthetic.main.fragment_add_user.view.thigh
import kotlinx.android.synthetic.main.fragment_add_user.view.timeClock2
import kotlinx.android.synthetic.main.fragment_add_user.view.tommy
import kotlinx.android.synthetic.main.fragment_add_user.view.top2
import kotlinx.android.synthetic.main.fragment_add_user.view.trouser
import kotlinx.android.synthetic.main.fragment_add_user.view.waist
import kotlinx.android.synthetic.main.fragment_add_user.waist
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Date


class AddOnlineFragment : Fragment() {
    private val adapter: OnlineAdapter by lazy { OnlineAdapter()}
    private val args by  navArgs<AddOnlineFragmentArgs>()
    private lateinit var onLineViewModel: OnlineListViewModel
    private lateinit var userId: UserId
    private lateinit var progressDialog: ProgressDialog
    private lateinit var apiService: ApiService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_online, container, false)


        val retrofitHelper = RetrofitHelper()
        progressDialog = ProgressDialog(requireContext())
        progressDialog.setMessage("Loading...")
        apiService = retrofitHelper.createService(ApiService::class.java)
        userId = args.userid


        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
        val formatted = current.format(formatter)

        view.timeClock2.text = formatted.toString()


        val textWatcher = object : TextWatcher {
            @SuppressLint("ResourceAsColor")
            override fun afterTextChanged(p0: Editable?) {
                val customerName = view.customerName.text.toString().trim()
                val sdf = SimpleDateFormat("dd MMM,yyyy-HH:mm")
                val time: String = sdf.format(Date())
                val shoulder = view.shoulder.text.toString().trim()
                val sleeve = view.sleeve.text.toString().trim()
                val neck = view.neck.text.toString().trim()
                val chest = view.chest.text.toString().trim()
                val tommy = view.tommy.text.toString().trim()
                val top = view.top2.text.toString().trim()
                val bicep = view.bicep.text.toString().trim()
                val cuff = view.cuff.text.toString().trim()
                val waist = view.waist.text.toString().trim()
                val thigh = view.thigh.text.toString().trim()
                val trouser = view.trouser.text.toString().trim()
                val ankle = view.ankle.text.toString().trim()

                if (customerName.length == 0 || shoulder.length == 0 || sleeve.length == 0 || neck.length == 0 || chest.length == 0
                    || tommy.length == 0 || top.length == 0 || bicep.length == 0 || cuff.length == 0 || waist.length == 0
                    || thigh.length == 0 || trouser.length == 0 || ankle.length == 0
                ){
                    view.fabUpdate.isEnabled = false
                    view.fabUpdate.setBackgroundTintList(ColorStateList.valueOf(resources.getColor(R.color.gray)))


                } else{
                    view.fabUpdate.isEnabled = true
                    view.fabUpdate.setBackgroundTintList(ColorStateList.valueOf(resources.getColor(R.color.white)))
                }

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            @SuppressLint("ResourceAsColor")
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {



            }
        }

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
        view.ankle.addTextChangedListener(textWatcher)
        view.icBack.setOnClickListener{
            findNavController().navigateUp()
        }

        view.fabUpdate.setOnClickListener {
            progressDialog.show()
            val customerName = view.customerName.text.toString()
            val sdf = SimpleDateFormat("dd MMM,yyyy-HH:mm")
            val time: String = sdf.format(Date())
            val shoulder = view.shoulder.text.toString()
            val sleeve = view.sleeve.text.toString()
            val neck = view.neck.text.toString()
            val chest = view.chest.text.toString()
            val tommy = view.tommy.text.toString()
            val top = view.top2.text.toString()
            val bicep = view.bicep.text.toString()
            val cuff = view.cuff.text.toString()
            val waist = view.waist.text.toString()
            val thigh = view.thigh.text.toString()
            val trouser = view.trouser.text.toString()
            val ankle = view.ankle.text.toString()
            val endpointUrl = "customer/${userId.userId}"

//            val user = AddMeasurements(customerName.toString(),shoulder.toString(),sleeve.toString(),
//                neck.toString(), chest.toString(), tommy.toString(), top.toString(), bicep.toString(),
//                cuff.toString(), waist.toString(), thigh.toString(),
//                trouser.toString(), ankle.toString(), time)
//            Log.d("useradded", "$user")
            onLineViewModel = ViewModelProvider(this).get(OnlineListViewModel::class.java)
            onLineViewModel.addNewMeasurement(apiService,endpointUrl,customerName,shoulder,
                sleeve,neck,chest,tommy,top,bicep,cuff,waist,thigh,trouser,ankle,time)
            onLineViewModel.measurementAdded.observe(viewLifecycleOwner, Observer { success ->
                if (success) {
                    progressDialog.dismiss()
                    Toast.makeText(requireContext(), "Successfully Added $customerName shoulder: $shoulder", Toast.LENGTH_LONG).show()
                    findNavController().navigateUp()
                } else{
                    progressDialog.dismiss()
                    Toast.makeText(requireContext(), "Failed,Try Again", Toast.LENGTH_LONG).show()
                }
            })


        }









        return view
    }

}