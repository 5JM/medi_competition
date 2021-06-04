package com.example.donkin.dataForm

import java.io.Serializable

data class PrescriptionList(
    var status : Int,
    var message : String?,
    var data : List<PrescriptionInfo>
):Serializable
