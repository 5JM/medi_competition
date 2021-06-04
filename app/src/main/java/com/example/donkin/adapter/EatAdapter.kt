package com.example.donkin.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.donkin.R
import com.example.donkin.dataForm.CalendarEat

class EatAdapter(var context : Context, var list : ArrayList<CalendarEat>)
    : RecyclerView.Adapter<EatAdapter.ViewHolder>() {
        inner class ViewHolder(view: View):RecyclerView.ViewHolder(view){
            var name : TextView = itemView.findViewById(R.id.eat_pills_name)
            var date : TextView = itemView.findViewById(R.id.eat_pills_date)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.eat_pills_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val result = list[position]
        holder.name.text = result.pre_medicine_name
        holder.date.text = result.does_dt
    }

    override fun getItemCount(): Int {
        return list.size
    }
}
