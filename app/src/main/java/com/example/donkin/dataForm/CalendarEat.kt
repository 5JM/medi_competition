package com.example.donkin.dataForm

import java.io.Serializable

data class CalendarEat(
//    var name : String? = null,
//    var eat_date : String? = null,
    var preMedicineIdx : Int,
    var pre_medicine_name : String?,
    var does_dt : String?
):Serializable
