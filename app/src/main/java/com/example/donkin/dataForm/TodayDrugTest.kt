package com.example.donkin.dataForm

import java.io.Serializable

data class TodayDrugTest(
    var name : String,
    var check : Boolean? = false
):Serializable
