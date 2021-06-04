package com.example.donkin.dataForm

import java.io.Serializable

data class TodayDoesList(
    var does_dt : String,
    var todayDoesList : List<TodayDoesListItems>
):Serializable
