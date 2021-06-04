package com.example.donkin.retrofit

import com.example.donkin.dataForm.*
import retrofit2.Call
import retrofit2.http.*

interface RetrofitService {
    @GET("/mypage/medicine")
    fun getMyMedicine(
        @Header("authorization") authorization : String?,
        @Query("prescriptionIdx") prescriptionIdx : Int
    ):Call<MyMedicine>

    @GET("/mypage/prescription")
    fun getMyPrescription(
        @Header("authorization") authorization : String?
    ):Call<PrescriptionList>

    @GET("/mypage/info")
    fun getMyPageInfo(
        @Header("authorization") authorization : String?
    ):Call<MyPageInfo>

    @GET("/calendar/eat")
    fun getCalendarEat(
        @Header("authorization") authorization : String?
    ):Call<CalendarEatList>

    @GET("/calendar/noeat")
    fun getCalendarNoEat(
        @Header("authorization") authorization : String?
    ):Call<CalendarNoEatList>

    @FormUrlEncoded
    @PUT("/main/check")
    fun checkTodayDrug(
        @Header("authorization") authorization : String?,
        @Field("doesCheckIdx") doesCheckIdx : Int
    ):Call<NormalDataForm>

//    @FormUrlEncoded
//    @PUT("/main/check/cancel")
//    fun checkTodayDrugCancel(
//        @Header("authorization") authorization : String?,
//        @Field("doesCheckIdx") doesCheckIdx : Int
//    ):Call<NormalDataForm>

    @FormUrlEncoded
    @POST("/main/prescription")
    fun addDrugs(
        @Header("authorization") authorization : String?,
        @Field("pre_medicine_name") pre_medicine_name : String?,
        @Field("total_does_dt") total_does_dt : Int?,
        @Field("total_does_count") total_does_count : Int?,
        @Field("prescription_dt") prescription_dt : String?
    ):Call<NormalDataForm>

    @GET("/main/check")
    fun getToday(
        @Header("authorization") authorization : String?
    ):Call<TodayUpdate>

    @GET("/return/expire")
    fun getExpire(
        @Header("authorization") authorization : String?
    ):Call<ExpireList>

    @GET("/return/pharmacy")
    fun getPhar(
        @Header("authorization") authorization : String?,
        @Query("longitude") longitude : String,
        @Query("latitude") latitude : String
    ):Call<PharmacyListCall>
//        Call<List<PharmacyListForm>>

//    @FormUrlEncoded
    @Headers("Content-Type:application/json")
    @POST("/auth/signin")
    fun login(
      @Body body: LoginForm
//        @Field("user_id") user_id : String,
//        @Field("user_pwd") user_pwd : String
    ):Call<loginReturn>

    @JvmSuppressWildcards
    @Headers("Content-Type:application/json")
    @POST("/auth/signup")
    fun register(
        @Body body : SignForm
    ):Call<Void>
}