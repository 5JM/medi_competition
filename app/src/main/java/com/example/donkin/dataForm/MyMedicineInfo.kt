package com.example.donkin.dataForm

import java.io.Serializable

data class MyMedicineInfo(
//    "userIdx": 8,
//    "prescriptionIdx": 3,
//"preMedicineIdx": 27,
//"pre_medicine_name": "가아아ㅏ",
//"total_does_dt": 4,
//"my_does_dt": 0,
//"total_does_count": 5
    var userIdx : Int,
    var prescriptionIdx : Int,
    var preMedicineIdx : Int,
    var pre_medicine_name : String,
    var total_does_dt : Int,
    var my_does_dt : Int,
    var total_does_count : Int
):Serializable
