package com.example.donkin.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.donkin.MainActivity
import com.example.donkin.R
import com.example.donkin.adapter.EatAdapter
import com.example.donkin.adapter.ExpireAdapter
import com.example.donkin.adapter.NoEatAdapter
import com.example.donkin.dataForm.*
import com.example.donkin.retrofit.MasterApplication
import com.prolificinteractive.materialcalendarview.CalendarDay
import kotlinx.android.synthetic.main.fragment_cale_test.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.timer

class CalendarFragment : Fragment() {
    var eatList  = ArrayList<CalendarEat>()
    var noEatList  = ArrayList<CalendarNoEat>()
//    var expireList  = ArrayList<ExpireListInfo>()
    var reEat = ArrayList<CalendarEat>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var i = 0
        timer(period = 1000, initialDelay = 200) {
            // 작업
            Log.e("caltest>>","${i++}")
            activity?.runOnUiThread{
                // UI 조작
                if(noEatList.isEmpty()) {
                    updateList()
//                    Log.e("caltest>>","${i}, timer stop")
//                    cancel()
                }else if( i == 5) {
                    Log.e("caltest>>","${i}, timer stop")
                    cancel()
                }else{
                    Log.e("caltest>>","${i}, timer stop")
                    Log.e("caltest>>","<><><><><>${noEatList}")
                    updateCalendar(noEatList)
                    cancel()
                }
            }
        }
        calendar_view.selectedDate = CalendarDay.today()
//        calendar_view.addDecorator(
//            SundayDeco()
//        )
        //        Test용
//        calendar_view.setDateSelected(CalendarDay.from(2021,5,30),true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        val test = arguments?.getSerializable("userCalendar")
//        Log.e("CalendarTest>>","test2 : ${test}")

        val view = inflater.inflate(R.layout.fragment_cale_test, container, false)
        updateList()

        val refBtn = view.findViewById<Button>(R.id.calendar_bottom_btn)

        val context = view.context
        val recyclerView = view.findViewById<RecyclerView>(R.id.calendar_recycler1)
        val recyclerView2 = view.findViewById<RecyclerView>(R.id.calendar_recycler2)
//        val recyclerView3 = view.findViewById<RecyclerView>(R.id.calendar_recycler3)

        val layoutManager1 = LinearLayoutManager(context)
        val layoutManager2 = LinearLayoutManager(context)
//        val layoutManager3 = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager1
        recyclerView2.layoutManager = layoutManager2
//        recyclerView3.layoutManager = layoutManager3

        val eatAdapter = EatAdapter(context,eatList)
        val noEatAdapter = NoEatAdapter(context, noEatList)
//        val expireAdapter = ExpireAdapter(context, expireList)

        recyclerView.adapter = eatAdapter
        recyclerView2.adapter = noEatAdapter
//        recyclerView3.adapter = expireAdapter


        var i = 0
        timer(period = 1000, initialDelay = 200) {
            // 작업
            Log.e("caltest>>","${i++}")
            activity?.runOnUiThread{
                // UI 조작
                recyclerView.adapter?.notifyDataSetChanged()
                recyclerView2.adapter?.notifyDataSetChanged()
                if( i == 5) {
                    Log.e("caltest>>","${i}, timer stop")
                    cancel()
                }
            }
        }

        refBtn.setOnTouchListener { v, event ->
            Log.e("Calendar Log>>", "touch Test")
            recyclerView.adapter?.notifyDataSetChanged()
            true
        }
        return view
    }
    private fun updateCalendar(list : ArrayList<CalendarNoEat>){
        Log.e("CalendarTest>>","updateCalendar Start..")
        var year = 0
        var mon = 0
        var day = 0
        if(list != null) {
            for (i in list) {
//                val arr = i.does_dt.split("-")
                if(i.does_dt!=null) {
                    val arr = i.does_dt!!.split("-")
                    Log.e("CalendarTest>>", "updateCalendar ${arr[0]}")
                    year = arr[0].toInt()
                    Log.e("CalendarTest>>", "updateCalendar ${arr[1]}")
                    mon = arr[1].toInt()
                    Log.e("CalendarTest>>", "updateCalendar ${arr[2]}")
                    day = arr[2].toInt()
                    calendar_view.setDateSelected(CalendarDay.from(year, mon, day), true)
                }else{
                    Log.e("CalendarTest>>", "updateCalendar..does_dt is null}")
                }
            }
        }else{
            Log.e("CalendarTest>>", "updateCalendar...list is null ")
        }
    }
    private fun updateList(){
        val sp = context?.getSharedPreferences("login_token", Context.MODE_PRIVATE)
        val token = sp?.getString("login_token","null")
        val retrofit = ((activity as MainActivity).application as MasterApplication).service

        retrofit.getCalendarEat(
            token
        ).enqueue(object : Callback<CalendarEatList>{
            override fun onResponse(call: Call<CalendarEatList>, response: Response<CalendarEatList>) {
                if(response.isSuccessful){
                    val result = response.body()?.data
                    Log.e("Calendar Log>>","update list suc1")
                    Log.e("Calendar Log>>","update list suc2 : ${result?.size}")
                    if( result!!.isNotEmpty()){
                        eatList.clear()
                        eatList.addAll(result)
                        Log.e("Calendar Log>>","update list suc4 : ${eatList}")
                    }


                }else{
                    Log.e("Calendar Log>>","update list err : ${response.body()}")
                }
            }

            override fun onFailure(call: Call<CalendarEatList>, t: Throwable) {
                Log.e("Calendar Log>>","update list fail")
            }
        })

        retrofit.getCalendarNoEat(
            token
        ).enqueue(object : Callback<CalendarNoEatList>{
            override fun onResponse(
                call: Call<CalendarNoEatList>,
                response: Response<CalendarNoEatList>
            ) {
                if(response.isSuccessful){
                    Log.e("CalendarNoEat>>","CalendarNoEat>> suc")
                    val result = response.body()?.data
                    if( result!!.isNotEmpty()){
                        noEatList.clear()
                        noEatList.addAll(result)
                    }
                    Log.e("CalendarNoEat>>","update list suc4 : ${noEatList}")
                }else{
                    Log.e("CalendarNoEat>>","CalendarNoEat>> err")
                }
            }
            override fun onFailure(call: Call<CalendarNoEatList>, t: Throwable) {
                Log.e("CalendarNoEat>>","CalendarNoEat>> fail")
            }
        })
//        noEatList.clear()
//        noEatList.add(CalendarNoEat("abc1","2021-05-23","3"))

//        expireList.clear()
//        expireList.add(ExpireListInfo(0,0,0,"test",0,"2021-05-25",0))
    }
}