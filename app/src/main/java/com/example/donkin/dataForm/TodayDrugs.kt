package com.example.donkin.dataForm

import java.io.Serializable

data class TodayDrugs(
    var idx : Int,
    var name : String,
    var check : Boolean? = false
):Serializable
