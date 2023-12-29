package com.example.mohaute

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.res.ColorStateList
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
import com.example.mohaute.data.UserId
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
import kotlinx.android.synthetic.main.fragment_backup_add_measurement.view.ankles
import kotlinx.android.synthetic.main.fragment_backup_add_measurement.view.biceps
import kotlinx.android.synthetic.main.fragment_backup_add_measurement.view.chests
import kotlinx.android.synthetic.main.fragment_backup_add_measurement.view.cuffs
import kotlinx.android.synthetic.main.fragment_backup_add_measurement.view.customerNames
import kotlinx.android.synthetic.main.fragment_backup_add_measurement.view.description
import kotlinx.android.synthetic.main.fragment_backup_add_measurement.view.necks
import kotlinx.android.synthetic.main.fragment_backup_add_measurement.view.shoulders
import kotlinx.android.synthetic.main.fragment_backup_add_measurement.view.sleeves
import kotlinx.android.synthetic.main.fragment_backup_add_measurement.view.thighs
import kotlinx.android.synthetic.main.fragment_backup_add_measurement.view.timeClock2s
import kotlinx.android.synthetic.main.fragment_backup_add_measurement.view.tommys
import kotlinx.android.synthetic.main.fragment_backup_add_measurement.view.top2s
import kotlinx.android.synthetic.main.fragment_backup_add_measurement.view.trousers
import kotlinx.android.synthetic.main.fragment_backup_add_measurement.view.waists
import kotlinx.android.synthetic.main.fragment_update.view.bicep
import kotlinx.android.synthetic.main.fragment_update.view.chest
import kotlinx.android.synthetic.main.fragment_update.view.cuff
import kotlinx.android.synthetic.main.fragment_update.view.customerName
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
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Date


class BackupAddMeasurementFragment : Fragment() {
    private val args by navArgs<BackupAddMeasurementFragmentArgs>()
    private lateinit var onLineViewModel: OnlineListViewModel
    private lateinit var userId: UserId
    private lateinit var progressDialog: ProgressDialog
    private lateinit var apiService: ApiService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_backup_add_measurement, container, false)

        val retrofitHelper = RetrofitHelper()
        progressDialog = ProgressDialog(requireContext())
        progressDialog.setMessage("Loading...")
        apiService = retrofitHelper.createService(ApiService::class.java)
        userId = UserId(args.backup.userResponse.user._id)

        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
        val formatted = current.format(formatter)

        view.timeClock2s.text = formatted.toString()
        view.description.text = "Backing up as ${args.backup.userResponse.user.email}"

        val textWatcher = object : TextWatcher {
            @SuppressLint("ResourceAsColor")
            override fun afterTextChanged(p0: Editable?) {
                val customerName = view.customerNames.text.toString().trim()
                val sdf = SimpleDateFormat("dd MMM,yyyy-HH:mm")
                val time: String = sdf.format(Date())
                val shoulder = view.shoulders.text.toString().trim()
                val sleeve = view.sleeves.text.toString().trim()
                val neck = view.necks.text.toString().trim()
                val chest = view.chests.text.toString().trim()
                val tommy = view.tommys.text.toString().trim()
                val top = view.top2s.text.toString().trim()
                val bicep = view.biceps.text.toString().trim()
                val cuff = view.cuffs.text.toString().trim()
                val waist = view.waists.text.toString().trim()
                val thigh = view.thighs.text.toString().trim()
                val trouser = view.trousers.text.toString().trim()
                val ankle = view.ankles.text.toString().trim()

                if (customerName.length == 0 || shoulder.length == 0 || sleeve.length == 0 || neck.length == 0 || chest.length == 0
                    || tommy.length == 0 || top.length == 0 || bicep.length == 0 || cuff.length == 0 || waist.length == 0
                    || thigh.length == 0 || trouser.length == 0 || ankle.length == 0
                ) {
                    view.fabUpdate.isEnabled = false
                    view.fabUpdate.setBackgroundTintList(ColorStateList.valueOf(resources.getColor(R.color.gray)))


                } else {
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

        view.customerNames.addTextChangedListener(textWatcher)
        view.shoulders.addTextChangedListener(textWatcher)
        view.sleeves.addTextChangedListener(textWatcher)
        view.necks.addTextChangedListener(textWatcher)
        view.chests.addTextChangedListener(textWatcher)
        view.tommys.addTextChangedListener(textWatcher)
        view.top2s.addTextChangedListener(textWatcher)
        view.biceps.addTextChangedListener(textWatcher)
        view.cuffs.addTextChangedListener(textWatcher)
        view.waists.addTextChangedListener(textWatcher)
        view.thighs.addTextChangedListener(textWatcher)
        view.trousers.addTextChangedListener(textWatcher)
        view.ankles.addTextChangedListener(textWatcher)
        view.icBack.setOnClickListener {
            findNavController().navigateUp()
        }



        view.fabUpdate.setOnClickListener {
            progressDialog.show()
            val customerName = view.customerNames.text.toString()
            val sdf = SimpleDateFormat("dd MMM,yyyy-HH:mm")
            val time: String = sdf.format(Date())
            val shoulder = view.shoulders.text.toString()
            val sleeve = view.sleeves.text.toString()
            val neck = view.necks.text.toString()
            val chest = view.chests.text.toString()
            val tommy = view.tommys.text.toString()
            val top = view.top2s.text.toString()
            val bicep = view.biceps.text.toString()
            val cuff = view.cuffs.text.toString()
            val waist = view.waists.text.toString()
            val thigh = view.thighs.text.toString()
            val trouser = view.trousers.text.toString()
            val ankle = view.ankles.text.toString()
            val endpointUrl = "customer/${userId.userId}"

            onLineViewModel = ViewModelProvider(this).get(OnlineListViewModel::class.java)
            onLineViewModel.addNewMeasurement(apiService,endpointUrl,customerName,shoulder,
                sleeve,neck,chest,tommy,top,bicep,cuff,waist,thigh,trouser,ankle,time)
            onLineViewModel.measurementAdded.observe(viewLifecycleOwner, Observer { success ->
                if (success) {
                    progressDialog.dismiss()
                    Toast.makeText(requireContext(), "Successfully Added $customerName shoulder: $shoulder", Toast.LENGTH_LONG).show()
                   val action = BackupAddMeasurementFragmentDirections.actionBackupAddMeasurementFragmentToOnlineHomeFragment(args.backup.userResponse.user)
                    findNavController().navigate(action)
                } else{
                    progressDialog.dismiss()
                    Toast.makeText(requireContext(), "Failed,Try Again", Toast.LENGTH_LONG).show()
                }
            })
        }

        view.customerNames.setText(args.backup.addMeasurements.name .toString())
        view.timeClock2s.setText(args.backup.addMeasurements.date.toString())
        view.shoulders.setText(args.backup.addMeasurements.shoulder.toString())
        view.sleeves.setText(args.backup.addMeasurements.sleeve.toString())
        view.necks.setText(args.backup.addMeasurements.neck.toString())
        view.chests.setText(args.backup.addMeasurements.chest.toString())
        view.tommys.setText(args.backup.addMeasurements.tommy.toString())
        view.top2s.setText(args.backup.addMeasurements.top.toString())
        view.biceps.setText(args.backup.addMeasurements.bicep.toString())
        view.cuffs.setText(args.backup.addMeasurements.cuff.toString())
        view.waists.setText(args.backup.addMeasurements.waist.toString())
        view.thighs.setText(args.backup.addMeasurements.thigh.toString())
        view.trousers.setText(args.backup.addMeasurements.trouser.toString())
        view.ankles.setText(args.backup.addMeasurements.ankle.toString())

            return view
        }


    }









