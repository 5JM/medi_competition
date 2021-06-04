package com.example.donkin.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.donkin.R
import com.example.donkin.dataForm.PharmacyListForm

class PharlistAdapter(var context : Context, var list : ArrayList<PharmacyListForm>)
    :RecyclerView.Adapter<PharlistAdapter.ViewHolder>(){
        inner class ViewHolder(view: View):RecyclerView.ViewHolder(view){
            var name = itemView.findViewById<TextView>(R.id.phar_name)
            var number = itemView.findViewById<TextView>(R.id.phar_number)
            var address = itemView.findViewById<TextView>(R.id.phar_address)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.phar_lists_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val result = list[position]
        holder.name.text = result.pharmacy_name
        holder.number.text = result.pharmacy_number
        holder.address.text = result.pharmacy_address
    }

    override fun getItemCount(): Int {
        return list.size
    }
}