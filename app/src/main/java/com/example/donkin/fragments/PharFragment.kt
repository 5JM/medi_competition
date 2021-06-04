package com.example.donkin.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.donkin.ListPhar
import com.example.donkin.R
import com.example.donkin.adapter.ExpireAdapter
import com.example.donkin.adapter.NoEatAdapter
import com.example.donkin.dataForm.CalendarNoEat
import com.example.donkin.dataForm.ExpireList
import com.example.donkin.dataForm.ExpireListInfo
import com.example.donkin.retrofit.MasterApplication
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.concurrent.timer

class PharFragment : Fragment() {
    var expireList = ArrayList<ExpireListInfo>()
    override fun onAttach(context: Context) {
        super.onAttach(context)
//        this.context = context as Activity
//        REQUIRED_PERMISSIONS.add(android.Manifest.permission.ACCESS_FINE_LOCATION)
//        REQUIRED_PERMISSIONS.add(android.Manifest.permission.ACCESS_COARSE_LOCATION)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_phar, container, false)
        val context = view.context
        val sp = context?.getSharedPreferences("login_token", Context.MODE_PRIVATE)
        val token = sp?.getString("login_token","null")
        updateList(token)
        val recyclerView = view.findViewById<RecyclerView>(R.id.phar_recyclerview)
        val layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager

        val adapter = ExpireAdapter(context,expireList)
        recyclerView.adapter = adapter

        var i =0
        timer(period = 1000, initialDelay = 100) {
            // 작업
            Log.e("ExpireListTest>>", "${i++}")
            activity?.runOnUiThread {
                // UI 조작
                recyclerView.adapter?.notifyDataSetChanged()
                if (i == 5) {
                    Log.e("ExpireListTest>>", "${i}, timer stop")
                    cancel()
                }
            }
        }

        val bot_btn = view.findViewById<Button>(R.id.phar_bot_btn)
        bot_btn.setOnClickListener {
            startActivity(Intent(activity, ListPhar::class.java))
            activity?.overridePendingTransition(R.anim.translate_up,R.anim.translate_stay)
        }
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }
    private fun updateList(token : String?) {
        expireList.clear()
        ((activity?.application)as MasterApplication).service.getExpire(
            token
        ).enqueue(object : Callback<ExpireList>{
            override fun onResponse(call: Call<ExpireList>, response: Response<ExpireList>) {
                if(response.isSuccessful){
                    Log.e("ExpireListTest>>","ExpireListTest suc")
                    if(response.body()?.data!=null) {
                        expireList.addAll(response.body()?.data!!)
                    }
                    else {
                        Log.e("ExpireListTest>>","ExpireListTest body is null")
                        expireList.add(ExpireListInfo(0,0,0,"해당 목록이 없습니다.",0,"-",0))
                    }
                }else{
                    Log.e("ExpireListTest>>","ExpireListTest err")
                }
            }

            override fun onFailure(call: Call<ExpireList>, t: Throwable) {
                Log.e("ExpireListTest>>","ExpireListTest fail")
            }
        })
//            noEatList.add(CalendarNoEat("감기약1", "2020-03-30", "1년 넘게 지났어요!"))
//            noEatList.add(CalendarNoEat("감기약2", "2020-03-30", "1년 넘게 지났어요!"))
//            noEatList.add(CalendarNoEat("감기약3", "2020-03-30", "1년 넘게 지났어요!"))
//            noEatList.add(CalendarNoEat("감기약4", "2020-03-30", "1년 넘게 지났어요!"))
        }
}


