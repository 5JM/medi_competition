package com.example.donkin.dataForm

import java.io.Serializable

data class PharmacyListForm(
    var pharmacy_name : String?,
    var pharmacy_number : String?,
    var pharmacy_address: String?,
    var pharmacy_longitude : String,
    var pharmacy_latitude : String
): Serializable
