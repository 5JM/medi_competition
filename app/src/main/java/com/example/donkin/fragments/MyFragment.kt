package com.example.donkin.fragments

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.donkin.R
import com.example.donkin.adapter.MedicineDialogAdapter
import com.example.donkin.adapter.OnItemClickListener
import com.example.donkin.adapter.PrescriptionListAdapter
import com.example.donkin.adapter.TodayDrugsAdapter
import com.example.donkin.dataForm.*
import com.example.donkin.retrofit.MasterApplication
import com.skydoves.expandablelayout.ExpandableLayout
import kotlinx.android.synthetic.main.fragment_my.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.concurrent.timer

class MyFragment : Fragment() {
//    lateinit var user_name : String
//    lateinit var user_id : String
    lateinit var expandable : ExpandableLayout
    var preList = ArrayList<PrescriptionInfo>()
    var medicineList = ArrayList<MyMedicineInfo>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_my, container, false)
        val context = view.context
        val sp = context?.getSharedPreferences("login_token", Context.MODE_PRIVATE)
        val token = sp?.getString("login_token","null")

        view.findViewById<TextView>(R.id.mypage_logout).setOnClickListener {
            val dialog = View.inflate(context, R.layout.logout_dialog, null)
            val al = AlertDialog.Builder(context)
            al.setView(dialog)
            al.setPositiveButton("네"){dialog, which ->
                activity?.finish()
            }
            al.setNegativeButton("돌아가기",null)
            al.show()
        }

        loadUserInfo(token)
        loadUserPrescription(token)

        val recyclerView = view.findViewById<RecyclerView>(R.id.mypage_recyclerview)
        val adapter = PrescriptionListAdapter(context,preList)

        adapter.setItemListener(object : OnItemClickListener{
            override fun OnTodayDrugChekListener(
                holder: TodayDrugsAdapter.ViewHolder,
                view: View,
                position: Int
            ) {

            }

            override fun OnPrescriptionListener(
                holder: PrescriptionListAdapter.ViewHolder,
                view: View,
                position: Int
            ) {
                val item = adapter.getItem(position)
                loadPersonalMedicine(token, item.prescriptionIdx)

                Log.e("MedicineTest>>","prescription idx -> ${item.prescriptionIdx}")
                val dialog = View.inflate(context, R.layout.prescription_dialog,null)
                val al = AlertDialog.Builder(context)

                val dialog_re = dialog.findViewById<RecyclerView>(R.id.prescription_dialog_recyclerview)
                val dialog_adapter =MedicineDialogAdapter(context, medicineList)
                dialog_re.layoutManager = LinearLayoutManager(context)
                dialog_re.adapter = dialog_adapter

                var i =0
                timer(period = 1000, initialDelay = 100) {
                    // 작업
                    Log.e("MedicineTest>>", "${i++}")
                    activity?.runOnUiThread {
                        // UI 조작
                        dialog_re.adapter?.notifyDataSetChanged()
                        if (i == 5) {
                            Log.e("MedicineTest>>", "${i}, timer stop")
                            cancel()
                        }
                    }
                }

                al.setView(dialog)
                al.setPositiveButton("확인",null)
                al.show()
            }
        })

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        var i =0
        timer(period = 1000, initialDelay = 500) {
            // 작업
            Log.e("prescription test>>", "${i++}")
            activity?.runOnUiThread {
                // UI 조작
                recyclerView.adapter?.notifyDataSetChanged()
                if (i == 5) {
                    Log.e("prescription test>>", "${i}, timer stop")
                    cancel()
                }
            }
        }

        expandable = view.findViewById(R.id.expandable)
        expandable.spinnerColor = Color.BLACK
        expandable.setOnExpandListener {
            if(it){
                Log.e("MypageTest>>","expandable")
//                expandable.spinnerColor = Color.BLACK
            }else{
                Log.e("MypageTest>>","collapse")
            }
        }

        expandable.parentLayout.setOnClickListener { expandable.toggleLayout() }
//        expandable.secondLayout.findViewById<TextView>(R.id.mypage_userid)
//            .text = ""
//        expandable.secondLayout.findViewById<TextView>(R.id.mypage_useremail)
//            .text = ""

        val expandable2 = view.findViewById<ExpandableLayout>(R.id.expandable2)
        expandable2.spinnerColor = Color.BLACK
        expandable2.setOnExpandListener {
            if(it){
                Log.e("MypageTest>>","expandable")
//                expandable.spinnerColor = Color.BLACK
            }else{
                Log.e("MypageTest>>","collapse")
            }
        }

        expandable2.parentLayout.setOnClickListener { expandable2.toggleLayout() }
//        expandable2.secondLayout.findViewById<RecyclerView>(R.id.mypage_recyclerview)
//            .setOnClickListener { Log.e("MypageTest>>","button0") }

        return view
    }
    private fun loadUserInfo(token : String?){
        ((activity?.application)as MasterApplication).service.getMyPageInfo(
            token
        ).enqueue(object : Callback<MyPageInfo>{
            override fun onResponse(call: Call<MyPageInfo>, response: Response<MyPageInfo>) {
                if(response.isSuccessful){
                    Log.e("MyPageTest>>","user info load suc")
                    var i =0
                    timer(period = 500, initialDelay = 200) {
                        // 작업
                        Log.e("MyPageTest>>","${i++}")
                        activity?.runOnUiThread{
                            // UI 조작
                            expandable.secondLayout.findViewById<TextView>(R.id.mypage_userid)
                                .text = response.body()?.data?.user_name.toString()
                            expandable.secondLayout.findViewById<TextView>(R.id.mypage_useremail)
                                .text = response.body()?.data?.user_id.toString()
                            if( i == 5) {
                                Log.e("MyPageTest>>","${i}, timer stop")
                                Log.e("MyPageTest>>","${response.body()?.data}")
                                cancel()
                            }
                        }
                    }
                }else{
                    Log.e("MyPageTest>>","user info load err")
                }

            }

            override fun onFailure(call: Call<MyPageInfo>, t: Throwable) {
                Log.e("MyPageTest>>","user info load failure")
            }
        })
    }
    private fun loadUserPrescription(token : String?){
        ((activity?.application)as MasterApplication).service.getMyPrescription(
            token
        ).enqueue(object : Callback<PrescriptionList>{
            override fun onResponse(
                call: Call<PrescriptionList>,
                response: Response<PrescriptionList>
            ) {
                if(response.isSuccessful){
                    Log.e("prescription test>>","${response.body()?.data}")
                    if(response.body()?.data!=null){
                        preList.addAll(response.body()?.data!!)
                    }
                    else Log.e("prescription test>>","data is null")
                }else{
                    Log.e("prescription test>>","prescription err")
                }
            }

            override fun onFailure(call: Call<PrescriptionList>, t: Throwable) {
                Log.e("prescription test>>","prescription fail")
            }
        })
    }
    private fun loadPersonalMedicine(token : String?, idx : Int){
        ((activity?.application)as MasterApplication).service.getMyMedicine(
            token, idx
        ).enqueue(object : Callback<MyMedicine>{
            override fun onResponse(call: Call<MyMedicine>, response: Response<MyMedicine>) {
                if(response.isSuccessful){
                    Log.e("MedicineTest>>", "Medicine suc")
                    if(response.body()!=null) {
                        medicineList.addAll(response.body()!!.data)
                        Log.e("MedicineTest>>", "MedicineList-> ${medicineList}")
                    }
                }else{
                    Log.e("MedicineTest>>", "Medicine err")
                }
            }

            override fun onFailure(call: Call<MyMedicine>, t: Throwable) {
                Log.e("MedicineTest>>", "Medicine fail")
            }
        })
    }
}