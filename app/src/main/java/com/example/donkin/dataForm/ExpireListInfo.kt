package com.example.donkin.dataForm

import java.io.Serializable

data class ExpireListInfo(
    var userIdx : Int,
    var prescriptionIdx : Int,
    var preMedicineIdx : Int,
    var pre_medicine_name : String?,
    var expireIdx : Int,
    var expire_dt : String,
    var abandon_check : Int
):Serializable
