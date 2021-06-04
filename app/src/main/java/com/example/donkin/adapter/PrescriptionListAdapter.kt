package com.example.donkin.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.donkin.R
import com.example.donkin.dataForm.PrescriptionInfo
import com.example.donkin.dataForm.PrescriptionList
import com.example.donkin.dataForm.TodayDrugs
import kotlinx.android.synthetic.main.mypage_prescription_list.view.*

class PrescriptionListAdapter(var context: Context, var list : ArrayList<PrescriptionInfo>)
    :RecyclerView.Adapter<PrescriptionListAdapter.ViewHolder>(),OnItemClickListener{
    lateinit var listener: OnItemClickListener
    inner class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        var pre_date = itemView.findViewById<TextView>(R.id.mypage_prescription_date)
//        var pre_idx = itemView.findViewById<>()
        var view =itemView.setOnClickListener {
            val position =adapterPosition
            listener.OnPrescriptionListener(this, it, position)
        }
    }

    override fun OnTodayDrugChekListener(
        holder: TodayDrugsAdapter.ViewHolder,
        view: View,
        position: Int
    ) {}

    override fun OnPrescriptionListener(holder: ViewHolder, view: View, position: Int) {
        listener.OnPrescriptionListener(holder, view, position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.mypage_prescription_list,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val result = list[position]
        holder.pre_date.text = result.prescription_dt
    }

    override fun getItemCount(): Int {
        return list.size
    }
    fun setItemListener(listener : OnItemClickListener){
        this.listener = listener
    }
    fun getItem(position: Int): PrescriptionInfo {
        return list[position]
    }
}