package com.example.donkin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.example.donkin.adapter.FragmentAdapter
import com.example.donkin.dataForm.CalendarEat
import com.example.donkin.dataForm.CalendarEatList
import com.example.donkin.fragments.CalendarFragment
import com.example.donkin.fragments.HomeFragment
import com.example.donkin.fragments.MyFragment
import com.example.donkin.fragments.PharFragment
import com.example.donkin.retrofit.MasterApplication
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.concurrent.timer


class MainActivity : AppCompatActivity() {
    var eatList  = ArrayList<CalendarEat>()
    val fragmentList = listOf(
        HomeFragment(), CalendarFragment(), PharFragment(), MyFragment()
    )
    override fun onBackPressed() {
//        super.onBackPressed()
        val dialog = View.inflate(this, R.layout.logout_dialog, null)
        val al = AlertDialog.Builder(this)
        al.setView(dialog)
        al.setPositiveButton("네"){dialog, which ->
            finish()
        }
        al.setNegativeButton("돌아가기",null)
        al.show()
//        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        updateCalendar()
        var i = 0
        timer(period = 1000, initialDelay = 1000) {
            // 작업
            Log.e("checkList>>","${i++}")
            runOnUiThread{
                // UI 조작
                if(eatList.isEmpty()) {
                    updateCalendar()
                    Log.e("checkList>>","${i}, timer stop")
                }else if( i == 5) {
                    Log.e("checkList>>","${i}, timer stop")
                    cancel()
                }else {
                    Log.e("checkList>>","${i}, timer stop")
                    Log.e("checkList","!!${eatList}")
//
//                    intentInstance()

                    cancel()
                }
            }
        }
//        Log.e("checkList","!!${eatList}")

//        val sp = getSharedPreferences("login_token",0)
//        val token = sp.getString("login_token","null")

        //checkValidToken(token)



        val adapter = FragmentAdapter(supportFragmentManager,1)
        adapter.fragmentList = fragmentList
        viewpager.adapter = adapter
        viewpager.offscreenPageLimit = 4
//        viewpager.setOnTouchListener { v, event -> true }

        bottomnavi.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.menu_home -> viewpager.currentItem = 0
                R.id.menu_calender -> viewpager.currentItem = 1
                R.id.menu_phar -> viewpager.currentItem = 2
                R.id.menu_mypage -> viewpager.currentItem = 3
            }
            true
        }

        viewpager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {}

            override fun onPageSelected(position: Int) {
                bottomnavi.menu.getItem(position).isChecked=true
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
    }
    //유효한 token인지 확인
    fun checkValidToken(token : String?){
//        (application as MasterApplication).service
    }

    private fun updateCalendar(){
        val sp = getSharedPreferences("login_token", Context.MODE_PRIVATE)
        val token = sp?.getString("login_token","null")

        (application as MasterApplication).service.getCalendarEat(
            token
        ).enqueue(object : Callback<CalendarEatList> {
            override fun onResponse(call: Call<CalendarEatList>, response: Response<CalendarEatList>) {
                if(response.isSuccessful){
                    val result = response.body()?.data
                    Log.e("checkList Log>>","update list suc1")
                    Log.e("checkList Log>>","update list suc2 : ${result?.size}")
                    if( result!!.isNotEmpty()){
                        eatList.clear()
                        eatList.addAll(result)
//                         for (i in result) {
//                             Log.e("Calendar Log>>","update list suc3 : ${i}")
//                             eatList.clear()
//                             eatList.addAll(result)
//                         }
                        Log.e("checkList Log>>","update list suc4 : ${eatList}")
                    }


                }else{
                    Log.e("checkList Log>>","update list err : ${response.body()}")
                }
            }

            override fun onFailure(call: Call<CalendarEatList>, t: Throwable) {
                Log.e("checkList Log>>","update list fail")
            }
        })

    }
//    private fun intentInstance(){
//        // 인텐트로 전달할 객체를 생성
////        val user = UserObject("abc", "111", "test")
//        //인텐트 생성
////        val intent = Intent(applicationContext, ::class.java)
////        intent.putExtra("userCalendar", eatList)
////        startActivity(intent)
//        val bundle = Bundle()
//        Log.e("CalendarTest","put?? ${eatList[0]}")
//        bundle.putSerializable("userCalendar", eatList[0])
////        fragmentList.
//        fragmentList[0].arguments = bundle
//        fragmentList[1].arguments = bundle
//        fragmentList[2].arguments = bundle
//        fragmentList[3].arguments = bundle
//    }
//    fun saveToken(token:String?, activity: Activity){
//        val sp = activity.getSharedPreferences("login_token", 0)
//        val editor = sp.edit()
//        editor.putString("login_token", token).apply()
//        Log.e("loginLog>>","=========saveToken=========")
//    }
}