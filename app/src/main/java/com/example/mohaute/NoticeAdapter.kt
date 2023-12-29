package com.example.mohaute

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.mohaute.data.Notice
import com.example.mohaute.model.User
import kotlinx.android.synthetic.main.custom_row.view.noticeName
import kotlinx.android.synthetic.main.custom_row.view.rvRecylerView
import kotlinx.android.synthetic.main.custom_row.view.shoulderText
import kotlinx.android.synthetic.main.custom_row.view.timeStamp
import kotlinx.android.synthetic.main.notice_custom.view.noticeDetails

class NoticeAdapter: RecyclerView.Adapter<NoticeAdapter.MyViewHolder>() {
    private var notice = emptyList<Notice>()

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.notice_custom, parent, false))
    }

    override fun onBindViewHolder(holder: NoticeAdapter.MyViewHolder, position: Int) {
        val currentItem = notice[position]

        holder.itemView.noticeName.text = currentItem.name.toString()
        holder.itemView.noticeDetails.text = currentItem.details.toString()
        holder.itemView.timeStamp.text = currentItem.date.toString()


//        holder.itemView.rvRecylerView.setOnClickListener{
//            val action = HomeFragmentDirections.actionHomeFragmentToUpdateFragment(currentItem)
//            holder.itemView.findNavController().navigate(action)
//        }
    }

    override fun getItemCount(): Int {
        return notice.size
    }

    fun setData(notice: List<Notice>){
        this.notice = notice
        notifyDataSetChanged()
    }
}