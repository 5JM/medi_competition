package com.example.donkin.dataForm

import java.io.Serializable

data class CalendarNoEatList(
    var status : Int,
    var message : String?,
    var data : List<CalendarNoEat>?
):Serializable
