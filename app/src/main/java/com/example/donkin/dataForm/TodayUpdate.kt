package com.example.donkin.dataForm

import java.io.Serializable

data class TodayUpdate(
    var status : Int,
    var message : String,
    var data : TodayDoesList
):Serializable
