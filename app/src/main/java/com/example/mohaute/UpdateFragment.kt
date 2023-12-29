package com.example.mohaute

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.mohaute.data.AddMeasurements
import com.example.mohaute.model.User
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
import kotlinx.android.synthetic.main.fragment_update.*
import kotlinx.android.synthetic.main.fragment_update.view.*
import kotlinx.android.synthetic.main.fragment_update.view.bicep
import kotlinx.android.synthetic.main.fragment_update.view.chest
import kotlinx.android.synthetic.main.fragment_update.view.cuff
import kotlinx.android.synthetic.main.fragment_update.view.customerName
import kotlinx.android.synthetic.main.fragment_update.view.fabUpdate
import kotlinx.android.synthetic.main.fragment_update.view.neck
import kotlinx.android.synthetic.main.fragment_update.view.shoulder
import kotlinx.android.synthetic.main.fragment_update.view.sleeve
import kotlinx.android.synthetic.main.fragment_update.view.thigh
import kotlinx.android.synthetic.main.fragment_update.view.timeClock2
import kotlinx.android.synthetic.main.fragment_update.view.tommy
import kotlinx.android.synthetic.main.fragment_update.view.top2
import kotlinx.android.synthetic.main.fragment_update.view.trouser
import kotlinx.android.synthetic.main.fragment_update.view.waist
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*


class UpdateFragment : Fragment() {


    private val args by  navArgs<UpdateFragmentArgs>()
    private lateinit var mUserViewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_update, container, false)


        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
        val formatted = current.format(formatter)

        view.timeClock2.text = formatted.toString()

        mUserViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

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
                    view.backup.isEnabled = false
                } else{
                    view.fabUpdate.isEnabled = true
                    view.backup.isEnabled = true
                }


            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {


            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
        }

        view.customerName.setText(args.update.customerName.toString())
        view.timeClock2.setText(args.update.currentDate.toString())
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
            updateItem()
        }
        view.delete.setOnClickListener {
            deleteUser()
        }

        view.backup.setOnClickListener{
            backUpMeasurement()
        }
        return view
    }

    private fun deleteUser(){
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes"){ _,_ ->
            mUserViewModel.deleteUser(args.update)
            Toast.makeText(requireContext(),"Successfully Deleted: ${args.update.customerName}",Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_updateFragment_to_homeFragment)
        }
        builder.setNegativeButton("No") {_,_ ->

        }
        builder.setTitle("Delete ${args.update.customerName}?")
        builder.setMessage("Are you sure you want to delete ${args.update.customerName}?")
        builder.setIcon(R.drawable.ic_person)
        builder.create().show()
    }



    private fun updateItem(){
        val customerName = customerName.text.toString()
        val sdf = SimpleDateFormat("dd MMM,yyyy-HH:mm")
        val time: String = sdf.format(Date())
        val shoulder = shoulder.text.toString().toDouble()
        val sleeve = sleeve.text.toString().toDouble()
        val neck = neck.text.toString().toDouble()
        val chest = chest.text.toString().toDouble()
        val tommy = tommy.text.toString().toDouble()
        val top = top2.text.toString().toDouble()
        val bicep = bicep.text.toString().toDouble()
        val cuff = cuff.text.toString().toDouble()
        val waist = waist.text.toString().toDouble()
        val thigh = thigh.text.toString().toDouble()
        val trouser = trouser.text.toString().toDouble()
        val ankle = trouser2.text.toString().toDouble()

        if (inputCheck(customerName,shoulder,sleeve,neck,chest,tommy,top,bicep,cuff,waist,thigh,trouser,ankle)){

            val updateUser = User(args.update.id, customerName,time,
                shoulder,sleeve,neck,chest,tommy,top,bicep,cuff,waist,thigh,trouser,ankle
            )
            mUserViewModel.updateUser(updateUser)
            Toast.makeText(requireContext(), "$customerName Measurement Updated Successfully", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_updateFragment_to_homeFragment)
        } else{
            Toast.makeText(requireContext(),"Pls enter all field", Toast.LENGTH_SHORT).show()
        }
    }

    private fun backUpMeasurement(){
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

        if (inputCheck(customerName,shoulder.toDouble(),sleeve.toDouble(),neck.toDouble(),
                chest.toDouble(),tommy.toDouble(),top.toDouble(),bicep.toDouble(),cuff.toDouble(),
                waist.toDouble(),thigh.toDouble(),trouser.toDouble(),ankle.toDouble())){
            val backupUser = AddMeasurements(customerName,
                shoulder,sleeve,neck,chest,tommy,top,bicep,cuff,waist,thigh,trouser,ankle,time
            )

            val builder = AlertDialog.Builder(requireContext())
            builder.setPositiveButton("Yes"){ _,_ ->
                Toast.makeText(requireContext(),"You need to Login",Toast.LENGTH_SHORT).show()
                val actions = UpdateFragmentDirections.actionUpdateFragmentToBackupLoginFragment(backupUser)
                findNavController().navigate(actions)
            }
            builder.setNegativeButton("No") {_,_ ->
                Toast.makeText(requireContext(),"Cancelled",Toast.LENGTH_SHORT).show()
            }
            builder.setTitle("Backup ${args.update.customerName}?")
            builder.setMessage("to continue backup, you need internet connection, do you wish to continue?")
            builder.setIcon(R.drawable.ic_person)
            builder.create().show()


        } else{
            Toast.makeText(requireContext(),"Pls enter all field", Toast.LENGTH_SHORT).show()
        }
    }
    private fun inputCheck(
        customerName: String, shoulder: Double, sleeve: Double,
        neck: Double, chest:Double,
        tommy: Double, top:Double,
        bicep:Double,cuff:Double,
        waist:Double, thigh:Double,
        trouser: Double, ankle:Double) : Boolean{
        return !(
                TextUtils.isEmpty(customerName) &&
                        TextUtils.isEmpty(shoulder.toString()) &&
                        TextUtils.isEmpty(sleeve.toString()) &&
                        TextUtils.isEmpty(neck.toString()) &&
                        TextUtils.isEmpty(chest.toString()) &&
                        TextUtils.isEmpty(tommy.toString()) &&
                        TextUtils.isEmpty(top.toString()) &&
                        TextUtils.isEmpty(bicep.toString()) &&
                        TextUtils.isEmpty(cuff.toString()) &&
                        TextUtils.isEmpty(waist.toString()) &&
                        TextUtils.isEmpty(thigh.toString()) &&
                        TextUtils.isEmpty(trouser.toString()) &&
                        TextUtils.isEmpty(ankle.toString())
                )
    }


}