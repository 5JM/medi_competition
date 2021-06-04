package com.example.donkin.dataForm

import java.io.Serializable

data class MyPageInfo(
    var status : Int,
    var message : String?,
    var data : MyPageUserInfoData
):Serializable
