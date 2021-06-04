package com.example.donkin.dataForm

import java.io.Serializable

data class SignForm(
    var user_id : String,
    var user_name : String,
    var user_pwd : String
):Serializable
