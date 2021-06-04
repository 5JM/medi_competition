package com.example.donkin.dataForm

import java.io.Serializable

data class loginReturn(
    val status:Int,
    val message : String?,
    val data : HashMap<String, String>
):Serializable
