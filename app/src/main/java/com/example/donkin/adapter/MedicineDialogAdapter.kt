package com.example.donkin.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.donkin.R
import com.example.donkin.dataForm.MyMedicineInfo

class MedicineDialogAdapter(var context: Context, var list : ArrayList<MyMedicineInfo>)
    : RecyclerView.Adapter<MedicineDialogAdapter.ViewHolder>() {
        inner class ViewHolder(view : View):RecyclerView.ViewHolder(view){
            val drug_name = itemView.findViewById<TextView>(R.id.medicine_drug_name)
            val drug_total_date = itemView.findViewById<TextView>(R.id.medicine_total_dates)
            val drug_one_day = itemView.findViewById<TextView>(R.id.medicine_total_oneday)
            val drug_my_total = itemView.findViewById<TextView>(R.id.medicine_my_total_dates)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.prescription_dialog_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val result = list[position]
        holder.drug_name.text = result.pre_medicine_name
        holder.drug_total_date.text = result.total_does_count.toString()
        holder.drug_my_total.text = result.my_does_dt.toString()
        holder.drug_one_day.text = result.total_does_count.toString()
    }

    override fun getItemCount(): Int {
       return list.size
    }
}