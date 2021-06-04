package com.example.donkin.dataForm

data class PillsList(
    var eat : List<CalendarEat>,
    var no_eat : List<CalendarNoEat>,
    var expire : List<CalendarExpire>
)
