package com.example.donkin.dataForm

import java.io.Serializable

data class MyMedicine(
    var status : Int,
    var message : String?,
    var data : List<MyMedicineInfo>
):Serializable
