package com.example.donkin.retrofit

import android.app.Application
import android.content.Context
import okhttp3.Interceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MasterApplication : Application() {
    lateinit var service: RetrofitService
    override fun onCreate() {
        super.onCreate()
        createRetrofit()
    }
    fun createRetrofit(){
        //app에서 나가는 통신 가로챔
        val header = Interceptor{
            //original에서 개조
            val original = it.request()
            if(checkIsLogin()){
                getUserToken().let { token ->
                    val request =  original.newBuilder()
                        .header("X-AUTH-TOKEN","token $token")//check
                        .build()
                    it.proceed(request)
                }
            }else{
                it.proceed(original)
            }
        }
        val retrofit = Retrofit.Builder()
            .baseUrl("http://3.36.0.106:3000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        service = retrofit.create(RetrofitService::class.java)
    }
    fun checkIsLogin():Boolean{
        val sp = getSharedPreferences("login_token",Context.MODE_PRIVATE)
        val token = sp.getString("login_token","token_null")
        if(token!="token_null") return true
        return false
    }
    fun getUserToken():String?{
        val sp = getSharedPreferences("login_token",Context.MODE_PRIVATE)
        val token = sp.getString("login_token","token_null")
        return if(token!="token_null") null
        else token
    }
}