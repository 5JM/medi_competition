package com.example.donkin

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Location
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.donkin.adapter.PharlistAdapter
import com.example.donkin.dataForm.PharmacyListCall
import com.example.donkin.dataForm.PharmacyListForm
import com.example.donkin.retrofit.MasterApplication
import com.google.android.gms.location.*
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_list_phar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.concurrent.timer

class ListPhar : AppCompatActivity() {
    var flag:Boolean=true
    private var gpsTracker: GpsTracker? = null
    var REQUIRED_PERMISSIONS = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )
    var phar_list = ArrayList<PharmacyListForm>()
    lateinit var container_view : View
    lateinit var customProgress : ProgressDialog
    override fun onBackPressed() {
        finish()
        overridePendingTransition(R.anim.translate_stay, R.anim.translate_down)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_phar)

        container_view = findViewById(R.id.phar_container)

        customProgress = ProgressDialog(this)
        customProgress.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        customProgress.setCancelable(false)

        pharList_backbtn.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.translate_stay, R.anim.translate_down)
        }

        if (!checkLocationServicesStatus()) {
            showDialogForLocationServiceSetting()
        } else {
            checkRunTimePermission()
        }

        gpsTracker = GpsTracker(this@ListPhar)

        var latitude: Double = gpsTracker!!.latitude
        var longitude: Double = gpsTracker!!.longitude

//        var t = 0
//        timer(period = 1000, initialDelay = 1000) {
//            // 작업
//            Log.e("GpsTest>>","${t++}")
//            runOnUiThread{
//                // UI 조작
//                latitude= gpsTracker!!.latitude
//                longitude= gpsTracker!!.longitude
//                Log.e("gps","${latitude}, ${longitude}")
//                updateList(latitude.toString(),longitude.toString())
////                if(latitude == 0.toDouble() && longitude==0.toDouble()) {
////                    updateList(latitude.toString(),longitude.toString())
////                    recyclerView.adapter?.notifyDataSetChanged()
////                    Log.e("GpsTest>>","${t}, timer stop")
////                    cancel()
////                }else if( t == 6) {
//                if(t==6){
//                    Log.e("GpsTest>>","${t}, timer stop")
//                    cancel()
//                }
//            }
//        }

        Toast.makeText(
            this@ListPhar,
            "현재위치 \n위도 $latitude\n경도 $longitude",
            Toast.LENGTH_LONG
        ).show()

        Log.e("gps","${latitude}, ${longitude}")

        updateList(latitude.toString(),longitude.toString())

        val recyclerview = findViewById<RecyclerView>(R.id.pharList_recyclerview)
        Log.e("pharTest>>", "list : ${phar_list}")
        recyclerview.layoutManager = LinearLayoutManager(this)
        recyclerview.adapter = PharlistAdapter(this, phar_list)

        var i = 0

        timer(period = 1000, initialDelay = 1000) {
            // 작업
            Log.e("pharTest>>","${i++}")
            runOnUiThread{
                // UI 조작
                if(!phar_list.isEmpty()) {
                    recyclerview.adapter?.notifyDataSetChanged()
                    Log.e("pharTest>>","${i}, timer stop")
                    cancel()
                }else if( i == 10) {
                    cancel()
                }
            }
        }
    }
    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            GPS_ENABLE_REQUEST_CODE ->
                //사용자가 GPS 활성 시켰는지 검사
                if (checkLocationServicesStatus()) {
                    if (checkLocationServicesStatus()) {
                        Log.d("gps", "onActivityResult : GPS 활성화 되있음")
                        checkRunTimePermission()
                        return
                    }
                }
        }
    }
    override fun onResume() {
        super.onResume()

        if (!checkNetworkState(this)) {
            val dialogBuilder = AlertDialog.Builder(this)

            // set message of alert dialog
            dialogBuilder.setMessage("app을 사용하기 위해선 인터넷 연결이 필요합니다.")
                // if the dialog is cancelable
                .setCancelable(false)
                // positive button text and action
                .setPositiveButton("네! ", DialogInterface.OnClickListener { dialog, id ->
                    finish()
                })
                // negative button text and action
                .setNegativeButton("응!", DialogInterface.OnClickListener { dialog, id ->
                    finish()
                })

            // create dialog box
            val alert = dialogBuilder.create()
            // set title for alert dialog box
            alert.setTitle("잠깐!")
            // show alert dialog
            alert.show()
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSIONS_REQUEST_CODE && grantResults.size == REQUIRED_PERMISSIONS.size) {

            // 요청 코드가 PERMISSIONS_REQUEST_CODE 이고, 요청한 퍼미션 개수만큼 수신되었다면
            var check_result = true


            // 모든 퍼미션을 허용했는지 체크합니다.
            for (result in grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false
                    break
                }
            }
            if (check_result) {

                //위치 값을 가져올 수 있음
            } else {
                // 거부한 퍼미션이 있다면 앱을 사용할 수 없는 이유를 설명해주고 앱을 종료합니다.2 가지 경우가 있습니다.
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        REQUIRED_PERMISSIONS[0]
                    )
                    || ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        REQUIRED_PERMISSIONS[1]
                    )
                ) {
                    Toast.makeText(
                        this@ListPhar,
                        "퍼미션이 거부되었습니다.\n앱을 다시 실행하여 퍼미션을 허용해주세요.",
                        Toast.LENGTH_LONG
                    ).show()
                    finish()
                } else {
                    Toast.makeText(
                        this@ListPhar,
                        "퍼미션이 거부되었습니다.\n설정(앱 정보)에서 퍼미션을 허용해야 합니다. ",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
    private fun checkNetworkState(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val nw      = connectivityManager.activeNetwork ?: return false
            val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
            return when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        } else {
            val nwInfo = connectivityManager.activeNetworkInfo ?: return false
            return nwInfo.isConnected
        }
    }
    //여기부터는 GPS 활성화를 위한 메소드들
    private fun showDialogForLocationServiceSetting() {
        flag = false
        val builder =
            AlertDialog.Builder(this@ListPhar)
        builder.setTitle("위치 서비스 비활성화")
        builder.setMessage(
            """
                앱을 사용하기 위해서는 위치 서비스가 필요합니다.
                 위치 서비스를 활성화 한 뒤 앱을 재시작 해주세요?
                """.trimIndent()
        )
        builder.setCancelable(true)
        builder.setPositiveButton("설정") { dialog, id ->
            val callGPSSettingIntent =
                Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivityForResult(
                callGPSSettingIntent,
                GPS_ENABLE_REQUEST_CODE
            )
        }
        builder.setNegativeButton(
            "취소"
        ) { dialog, id -> dialog.cancel() }
        builder.create().show()
    }

    private fun checkRunTimePermission() {

        //런타임 퍼미션 처리
        // 1. 위치 퍼미션을 가지고 있는지 체크합니다.
        val hasFineLocationPermission = ContextCompat.checkSelfPermission(
            this@ListPhar,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        val hasCoarseLocationPermission = ContextCompat.checkSelfPermission(
            this@ListPhar,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
            hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED
        ) {

            // 2. 이미 퍼미션을 가지고 있다면
            // ( 안드로이드 6.0 이하 버전은 런타임 퍼미션이 필요없기 때문에 이미 허용된 걸로 인식합니다.)


            // 3.  위치 값을 가져올 수 있음
        } else {  //2. 퍼미션 요청을 허용한 적이 없다면 퍼미션 요청이 필요합니다. 2가지 경우(3-1, 4-1)가 있습니다.

            // 3-1. 사용자가 퍼미션 거부를 한 적이 있는 경우에는
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this@ListPhar,
                    REQUIRED_PERMISSIONS[0]
                )
            ) {

                // 3-2. 요청을 진행하기 전에 사용자가에게 퍼미션이 필요한 이유를 설명해줄 필요가 있습니다.
                Toast.makeText(this@ListPhar, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_LONG)
                    .show()
                // 3-3. 사용자게에 퍼미션 요청을 합니다. 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(
                    this@ListPhar, REQUIRED_PERMISSIONS,
                    PERMISSIONS_REQUEST_CODE
                )
            } else {
                // 4-1. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
                // 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(
                    this@ListPhar, REQUIRED_PERMISSIONS,
                    PERMISSIONS_REQUEST_CODE
                )
            }
        }
    }
    private fun checkLocationServicesStatus(): Boolean {
        val locationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
    }

    companion object {
        private const val GPS_ENABLE_REQUEST_CODE = 2001
        private const val PERMISSIONS_REQUEST_CODE = 100
    }

    private fun updateList( latitude : String, longitude : String){
        val sp = getSharedPreferences("login_token",Context.MODE_PRIVATE)
        val token = sp.getString("login_token","null")

        customProgress.show()

        Log.e("pharTest>>","${token} -- ${longitude}, ${latitude}")
        (application as MasterApplication).service.getPhar(
            token, longitude, latitude
        ).enqueue(object : Callback<PharmacyListCall>{
            override fun onResponse(
                call: Call<PharmacyListCall>,
                response: Response<PharmacyListCall>
            ) {
                if(response.isSuccessful){
                    customProgress.dismiss()
                    Log.e("pharTest>>","suc:${response.body()?.message}")
                    Log.e("pharTest>>","suc:${response.body()?.data?.size}")

//                    Log.e("pharTest>>","suc:${response.body()?.data.}")
                    if(response.body()?.data?.isEmpty() == false){
                        for (i in response.body()?.data!!){
//                            Log.e("pharTest>>","${i.pharmacy_name}")
                            phar_list.add(PharmacyListForm(
                                i.pharmacy_name,
                                i.pharmacy_number,
                                i.pharmacy_address,
                                i.pharmacy_longitude,
                                i.pharmacy_latitude))

                        }
                        Log.e("pharTest>>","통신 완료..")
                    }

//                    val rr = response.body()?.get(0)?.pharmacy_name
                }else{
                    Log.e("pharTest>>","err : ${response.body()}")
                    customProgress.dismiss()
                    Snackbar.make(container_view,"Err", Snackbar.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<PharmacyListCall>, t: Throwable) {
                Log.e("pharTest>>","fail")
                customProgress.dismiss()
                Snackbar.make(container_view,"통신실패..", Snackbar.LENGTH_SHORT).show()
            }
        })
    }
}