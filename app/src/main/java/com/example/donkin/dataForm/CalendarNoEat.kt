package com.example.donkin.dataForm

import java.io.Serializable

data class CalendarNoEat(
//    var name : String? = null,
//    var make_date : String? = null,
//    var late_date : String? = null
    var preMedicineIdx : Int,
    var pre_medicine_name : String,
    var does_dt : String
):Serializable
