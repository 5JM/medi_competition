package com.example.donkin.adapter

import android.view.View

interface OnItemClickListener {
    fun OnTodayDrugChekListener(holder : TodayDrugsAdapter.ViewHolder, view: View, position : Int)
    fun OnPrescriptionListener(holder : PrescriptionListAdapter.ViewHolder, view: View, position : Int)
}