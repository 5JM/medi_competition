package com.example.donkin.dataForm

import java.io.Serializable

data class PharmacyListCall(
    val status : Int,
    val message : String,
    val data : List<PharmacyListForm>
):Serializable
