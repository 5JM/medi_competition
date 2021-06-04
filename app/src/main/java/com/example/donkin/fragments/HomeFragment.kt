package com.example.donkin.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.donkin.MainActivity
import com.example.donkin.QRActivity
import com.example.donkin.R
import com.example.donkin.adapter.OnItemClickListener
import com.example.donkin.adapter.PrescriptionListAdapter
import com.example.donkin.adapter.TodayDrugsAdapter
import com.example.donkin.dataForm.NormalDataForm
import com.example.donkin.dataForm.TodayDrugTest
import com.example.donkin.dataForm.TodayDrugs
import com.example.donkin.dataForm.TodayUpdate
import com.example.donkin.retrofit.MasterApplication
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_home.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.concurrent.timer

class HomeFragment : Fragment() {
    var drugList = ArrayList<TodayDrugs>()
    var pre_medicine_name : String? = null
    var total_does_dt: Int? = null
    var total_does_count: Int? = null
    var prescription_dt: String? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val context = view.context

        view.findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
            startActivity(Intent(activity, QRActivity::class.java))
            activity?.overridePendingTransition(R.anim.translate_up,R.anim.translate_stay)
        }

        updateList()
        val recyclerView = view.findViewById<RecyclerView>(R.id.home_recyclerview)
        val layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager

        Log.e("Today>>","adapter에 들어가기 직전\n ->${drugList}")

        val adapter = TodayDrugsAdapter(context,drugList)

        recyclerView.adapter = adapter

        val refreshView = view.findViewById<SwipeRefreshLayout>(R.id.home_refreshView)
        refreshView.setOnRefreshListener {
            updateList()
            recyclerView.adapter?.notifyDataSetChanged()
            refreshView.isRefreshing = false
            Log.e("Today>>","Refresh")
        }

        adapter.setItemListener(object : OnItemClickListener{
            override fun OnPrescriptionListener(
                holder: PrescriptionListAdapter.ViewHolder,
                view: View,
                position: Int
            ) {}

            override fun OnTodayDrugChekListener(
                holder: TodayDrugsAdapter.ViewHolder,
                view: View,
                position: Int
            ) {
                val sp = context?.getSharedPreferences("login_token", Context.MODE_PRIVATE)
                val token = sp?.getString("login_token","null")
                val item = adapter.getItem(position)
                Log.e("TodayTest>>","test : drug_idx -> ${item.idx}")
                val dialog = View.inflate(context, R.layout.drug_take_check_dialog, null)
                val al = AlertDialog.Builder(context)
                dialog.findViewById<TextView>(R.id.take_drug_name).text = item.name
                al.setView(dialog)
                al.setPositiveButton("복용 완료"){ dialog, which ->
                    (((activity as MainActivity).application) as MasterApplication).service.checkTodayDrug(
                        token, item.idx
                    ).enqueue(object : Callback<NormalDataForm>{
                        override fun onResponse(
                            call: Call<NormalDataForm>,
                            response: Response<NormalDataForm>
                        ) {
                            if(response.isSuccessful){
                                Log.e("AddTest>>","Add suc")
                                updateList()
                                var i =0
                                timer(period = 1000, initialDelay = 500) {
                                    // 작업
                                    Log.e("TodayTest>>","${i++}")
                                    activity?.runOnUiThread{
                                        // UI 조작
                                        if(drugList.isNotEmpty()) {
                                            recyclerView.adapter?.notifyDataSetChanged()
                                            Log.e("TodayTest>>","${i}, timer stop")
                                            cancel()
                                        }else if( i == 5) {
                                            Log.e("TodayTest>>","${i}, timer stop")
                                            cancel()
                                        }
                                    }
                                }
//                                recyclerView.adapter?.notifyDataSetChanged()
                            }else{
                                Log.e("AddTest>>","Add err")
                                Log.e("AddTest>>","Add err1 : ${response.body()}")
                            }
                        }

                        override fun onFailure(call: Call<NormalDataForm>, t: Throwable) {
                            Log.e("AddTest>>","Add fail")
                        }
                    })
                }
                al.setNegativeButton("취소",null)
                al.show()
            }
        })

        var i = 0
        timer(period = 1000, initialDelay = 1000) {
            // 작업
            Log.e("TodayTest>>","${i++}")
            activity?.runOnUiThread{
                // UI 조작
                if(drugList.isNotEmpty()) {
                    recyclerView.adapter?.notifyDataSetChanged()
                    Log.e("TodayTest>>","${i}, timer stop")
                    cancel()
                }else if( i == 15) {
                    Log.e("TodayTest>>","${i}, timer stop")
                    cancel()
                }
            }
        }

        val addDrug = view.findViewById<Button>(R.id.home_add_drugs)
        addDrug.setOnClickListener {
            val dialogView = View.inflate(context,R.layout.today_drug_dialog,null)
            val al = AlertDialog.Builder(context)
            al.setView(dialogView)
            al.setPositiveButton("추가"
            ) { dialog, which ->
                pre_medicine_name =
                    dialogView.findViewById<EditText>(R.id.dialog_drug_name).text.toString()
                total_does_dt =
                    dialogView.findViewById<EditText>(R.id.dialog_drug_total).text.toString().toInt()
                total_does_count =
                    dialogView.findViewById<EditText>(R.id.dialog_drug_day).text.toString().toInt()
                prescription_dt =
                    dialogView.findViewById<EditText>(R.id.dialog_drug_prescription_date).text.toString()
                Log.e("TodayTest>>","${pre_medicine_name}")
                Log.e("TodayTest>>","${total_does_dt}")
                Log.e("TodayTest>>","${total_does_count}")
                Log.e("TodayTest>>","${prescription_dt}")

                addDrugs()
                var k=0
                timer(period = 500, initialDelay = 0){
                    // 작업
                    Log.e("TodayTest>>","${k++}")
                    Log.e("TodayTest>>","${drugList}")
                    updateList()
                    if( k == 5) {
                        Log.e("TodayTest>>", "${k}, timer stop")
                        cancel()
                    }

                }
                var j=0
                timer(period = 500, initialDelay = 500){
                    // 작업
                    Log.e("TodayTest>>","${j++}")
                    Log.e("TodayTest>>","${drugList}")
                    activity?.runOnUiThread{
                        // UI 조작
                        recyclerView.adapter?.notifyDataSetChanged()
                        if( j == 7) {
                            Log.e("TodayTest>>", "${j}, timer stop")
                            cancel()
                        }
                    }
                }
            }
            al.setNegativeButton("취소",null)
            al.show()
        }
        return view
    }
    private fun updateList(){
        val sp = context?.getSharedPreferences("login_token", Context.MODE_PRIVATE)
        val token = sp?.getString("login_token","null")
        Log.e("TodayTest>>","token -> ${token}")
        (((activity as MainActivity).application) as MasterApplication).service.getToday(
            token
        ).enqueue(object : Callback<TodayUpdate>{
            override fun onResponse(call: Call<TodayUpdate>, response: Response<TodayUpdate>) {
                if(response.isSuccessful){
                    Log.e("TodayTest>>","${response.body()?.data}")
                    drugList.clear()
                    if(response.body()?.data?.todayDoesList != null) {
                        for (i in response.body()?.data?.todayDoesList!!) {
                            drugList.add(TodayDrugs(i.doesCheckIdx, i.pre_medicine_name))
                        }
                    }
                    Log.e("TodayTest>>","Update list suc : ${drugList}")
                }else{
                    Log.e("TodayTest>>","Update list err")
                    Log.e("TodayTest>>","Update list err1 : ${response.message()}")
                    Log.e("TodayTest>>","Update list err2 : ${response.body()?.message}")
                }
            }

            override fun onFailure(call: Call<TodayUpdate>, t: Throwable) {
                Log.e("TodayTest>>","Update list Fail")
            }
        })
    }

    private fun addDrugs(){//ad : TodayDrugsAdapter
        val sp = context?.getSharedPreferences("login_token", Context.MODE_PRIVATE)
        val token = sp?.getString("login_token","null")
        Log.e("TodayTest>>","${token}")
        (((activity as MainActivity).application) as MasterApplication).service.addDrugs(
            token,pre_medicine_name, total_does_dt, total_does_count, prescription_dt
        ).enqueue(object : Callback<NormalDataForm>{
            override fun onResponse(
                call: Call<NormalDataForm>,
                response: Response<NormalDataForm>
            ) {
                if(response.isSuccessful){
                    Log.e("TodayTest>>","suc")
                    Log.e("TodayTest>>","${pre_medicine_name}")
                    Log.e("TodayTest>>","${total_does_dt}")
                    Log.e("TodayTest>>","${total_does_count}")
                    Log.e("TodayTest>>","${prescription_dt}")
//                    drugList.add(TodayDrugs(null,pre_medicine_name!!,false))
//                    drugList2.add(TodayDrugTest(pre_medicine_name!!,false))
//                    ad.notifyDataSetChanged()
                }else{
                    Log.e("TodayTest>>","err")
                }

            }

            override fun onFailure(call: Call<NormalDataForm>, t: Throwable) {
                Log.e("TodayTest>>","Fail")
            }
        })
    }
}