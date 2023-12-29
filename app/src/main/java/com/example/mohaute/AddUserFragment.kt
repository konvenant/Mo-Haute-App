package com.example.mohaute

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.mohaute.model.User
import com.example.mohaute.viewModel.UserViewModel
import kotlinx.android.synthetic.main.fragment_add_user.*
import kotlinx.android.synthetic.main.fragment_add_user.view.*
import kotlinx.android.synthetic.main.fragment_add_user.view.bicep
import kotlinx.android.synthetic.main.fragment_add_user.view.chest
import kotlinx.android.synthetic.main.fragment_add_user.view.cuff
import kotlinx.android.synthetic.main.fragment_add_user.view.customerName
import kotlinx.android.synthetic.main.fragment_add_user.view.fabUpdate
import kotlinx.android.synthetic.main.fragment_add_user.view.neck
import kotlinx.android.synthetic.main.fragment_add_user.view.shoulder
import kotlinx.android.synthetic.main.fragment_add_user.view.sleeve
import kotlinx.android.synthetic.main.fragment_add_user.view.thigh
import kotlinx.android.synthetic.main.fragment_add_user.view.timeClock2
import kotlinx.android.synthetic.main.fragment_add_user.view.tommy
import kotlinx.android.synthetic.main.fragment_add_user.view.top2
import kotlinx.android.synthetic.main.fragment_add_user.view.trouser
import kotlinx.android.synthetic.main.fragment_add_user.view.waist
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*


class AddUserFragment : Fragment() {

    private lateinit var mUserViewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_user, container, false)

        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
        val formatted = current.format(formatter)

        view.timeClock2.text = formatted.toString()

        mUserViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

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

//        val customerName = customerName?.text.toString()
//        val shoulder = shoulder.text.toString().toInt()
//        val sleeve = sleeve.text.toString().toInt()
//        val neck = neck.text.toString().toInt()
//        val chest = chest.text.toString().toInt()
//        val tommy = tommy.text.toString().toInt()
//        val top = top2.text.toString().toInt()
//        val bicep = bicep.text.toString().toInt()
//        val cuff = cuff.text.toString().toInt()
//        val waist = waist.text.toString().toInt()
//        val thigh = thigh.text.toString().toInt()
//        val trouser = trouser.text.toString().toInt()
//        val ankle = ankle.text.toString().toInt()

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
        view.fabUpdate.isEnabled = false

        view.fabUpdate.setOnClickListener {
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
            val ankle = ankle.text.toString().toDouble()

            val user = User(0,customerName,time,
                shoulder,sleeve,neck,chest,tommy,top,bicep,cuff,waist,thigh,trouser,ankle
//            Integer.parseInt(shoulder.toString()),
//                Integer.parseInt(sleeve.toString()),
//                Integer.parseInt(neck.toString()),
//                Integer.parseInt(chest.toString()),
//                Integer.parseInt(tommy.toString()),
//                Integer.parseInt(top.toString()),
//                Integer.parseInt(bicep.toString()),
//                Integer.parseInt(cuff.toString()),
//                Integer.parseInt(waist.toString()),
//                Integer.parseInt(thigh.toString()),
//                Integer.parseInt(trouser.toString()),
//                Integer.parseInt(ankle.toString())
            )

            mUserViewModel.addUser(user)
            findNavController().navigate(R.id.action_addUserFragment_to_homeFragment)
            Toast.makeText(requireContext(), "Successfully Added $customerName",Toast.LENGTH_LONG).show()
        }
//        (activity as AppCompatActivity).setSupportActionBar(view.my_toolbar)
    return view


//    private fun insertDataToDatabase(){
//
//
////        if (inputCheck(customerName,shoulder,sleeve,neck,chest,tommy,top,bicep,cuff,waist,thigh,trouser,ankle)){
////
////
//
//
//        } else {
//            Toast.makeText(requireContext(),"Add Customer Name",Toast.LENGTH_LONG).show()
//        }



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