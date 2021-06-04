package com.example.donkin.dataForm

import java.io.Serializable

data class LoginForm(
    var user_id : String,
    var user_pwd : String
):Serializable
