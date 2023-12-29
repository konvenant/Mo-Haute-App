package com.example.mohaute

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.mohaute.data.Measurements
import kotlinx.android.synthetic.main.custom_row.view.noticeName
import kotlinx.android.synthetic.main.custom_row.view.rvRecylerView
import kotlinx.android.synthetic.main.custom_row.view.shoulderText
import kotlinx.android.synthetic.main.custom_row.view.timeStamp

class OnlineAdapter: RecyclerView.Adapter<OnlineAdapter.MyViewHolder>() {
    private var userList = emptyList<Measurements>()
    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.custom_row, parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = userList[position]

        holder.itemView.noticeName.text = currentItem.name.toString()
        holder.itemView.shoulderText.text = currentItem.shoulder.toString()
        holder.itemView.timeStamp.text = currentItem.date.toString()


        holder.itemView.rvRecylerView.setOnClickListener{
            val action = OnlineHomeFragmentDirections.actionOnlineHomeFragmentToUpdateOnlineFragment(currentItem)
            holder.itemView.findNavController().navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    fun setData(measure: List<Measurements>){
        this.userList = measure
        notifyDataSetChanged()
    }
}