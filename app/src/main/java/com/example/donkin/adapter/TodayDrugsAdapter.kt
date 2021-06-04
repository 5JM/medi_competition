package com.example.donkin.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.donkin.R
import com.example.donkin.dataForm.TodayDrugTest
import com.example.donkin.dataForm.TodayDrugs

class TodayDrugsAdapter(var context: Context, var list : ArrayList<TodayDrugs>)
    :RecyclerView.Adapter<TodayDrugsAdapter.ViewHolder>(), OnItemClickListener{
    lateinit var listener: OnItemClickListener
        inner class ViewHolder(view: View):RecyclerView.ViewHolder(view){
            var name = itemView.findViewById<TextView>(R.id.drug_name)
            var check = itemView.findViewById<CheckBox>(R.id.drug_checkbox)
            var comment = itemView.findViewById<TextView>(R.id.drug_comment)
            var view =itemView.setOnClickListener {
                val position =adapterPosition
                listener.OnTodayDrugChekListener(this, it, position)
            }
        }

    override fun OnTodayDrugChekListener(holder: ViewHolder, view: View, position: Int) {
            listener.OnTodayDrugChekListener(holder, view, position)
    }

    override fun OnPrescriptionListener(
        holder: PrescriptionListAdapter.ViewHolder,
        view: View,
        position: Int
    ) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.today_pills, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val result = list[position]
//        result.idx
        holder.name.text = result.name
        holder.check.isChecked = result.check!!
        when {
            position%3==0 -> holder.comment.text = "복용했나요?"
            position%3==1 -> holder.comment.text = "까먹지 마세요!"
            else -> holder.comment.text = "차례입니다!"
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setItemListener(listener : OnItemClickListener){
        this.listener = listener
    }
    fun getItem(position: Int):TodayDrugs{
        return list[position]
    }
}