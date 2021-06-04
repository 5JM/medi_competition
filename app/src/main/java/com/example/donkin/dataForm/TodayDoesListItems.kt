package com.example.donkin.dataForm

import java.io.Serializable

data class TodayDoesListItems(
    var doesCheckIdx : Int,
    var pre_medicine_name : String,
    var total_does_count : Int
):Serializable
