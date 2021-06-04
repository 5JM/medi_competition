package com.example.donkin.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.donkin.R
import com.example.donkin.dataForm.CalendarNoEat

class NoEatAdapter(var context : Context, var list : ArrayList<CalendarNoEat>)
    : RecyclerView.Adapter<NoEatAdapter.ViewHolder>() {
    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        var name : TextView = itemView.findViewById(R.id.no_eat_pills_name)
        var date : TextView = itemView.findViewById(R.id.no_eat_pills_date)
//        var late : TextView = itemView.findViewById(R.id.no_eat_pills_late)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.no_eat_pills_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val result = list[position]
        holder.name.text = result.pre_medicine_name
        holder.date.text = result.does_dt
//        holder.late.text = result.late_date
    }

    override fun getItemCount(): Int {
        return list.size
    }
}
