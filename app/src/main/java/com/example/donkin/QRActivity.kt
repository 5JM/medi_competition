package com.example.donkin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.donkin.dataForm.NormalDataForm
import com.example.donkin.retrofit.MasterApplication
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.activity_qractivity.*
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class QRActivity : AppCompatActivity() {
    lateinit var qrScan : IntentIntegrator
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qractivity)

        qrScan =IntentIntegrator(this)
        //scan option
        qrScan.setPrompt("Scanning...")
        //qrScan.setOrientationLocked(false);
        qrScan.initiateScan()
        //button onClick
        buttonScan.setOnClickListener {
            //scan option
            qrScan.setPrompt("Scanning...")
            //qrScan.setOrientationLocked(false);
            qrScan.initiateScan()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val sp = getSharedPreferences("login_token", Context.MODE_PRIVATE)
        val token = sp?.getString("login_token","null")
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            //qrcode 가 없으면
            if (result.contents == null) {
                Toast.makeText(this, "취소!", Toast.LENGTH_SHORT).show()
            } else {
                //qrcode 결과가 있으면
                Toast.makeText(this, "스캔완료!", Toast.LENGTH_SHORT).show()
                try {
                    //data를 json으로 변환
                    val obj = JSONObject(result.contents)
                    val pre_medicine_name = obj.getString("pre_medicine_name")
                    val total_does_dt = obj.getInt("total_does_dt")
                    val total_does_count = obj.getInt("total_does_count")
                    val prescription_dt = obj.getString("prescription_dt")
                    Log.e("QR>>","QR_Test >> ${pre_medicine_name}")
                    Log.e("QR>>","QR_Test >> ${total_does_dt}")
                    Log.e("QR>>","QR_Test >> ${total_does_count}")
                    Log.e("QR>>","QR_Test >> ${prescription_dt}")

                    uploadData(token, pre_medicine_name, total_does_dt, total_does_count, prescription_dt)
//                    textViewName.text = obj.getString("name")
//                    textViewAddress.text = obj.getString("address")
                } catch (e: JSONException) {
                    e.printStackTrace()
                    //Toast.makeText(MainActivity.this, result.getContents(), Toast.LENGTH_LONG).show();
//                    textViewResult.text = result.contents
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onBackPressed() {
        finish()
        overridePendingTransition(R.anim.translate_stay, R.anim.translate_down)
    }
    private fun uploadData(token:String?, pre_medicine_name : String,
                           total_does_dt:Int,total_does_count:Int,
                           prescription_dt:String){
        (application as MasterApplication).service.addDrugs(
            token,pre_medicine_name,total_does_dt,total_does_count,prescription_dt
        ).enqueue(object : Callback<NormalDataForm>{
            override fun onResponse(
                call: Call<NormalDataForm>,
                response: Response<NormalDataForm>
            ) {
                if(response.isSuccessful){
                    Log.e("QR","QR_sending..suc")
                }else{
                    Log.e("QR","QR_sending..err")
                }
            }

            override fun onFailure(call: Call<NormalDataForm>, t: Throwable) {
                Log.e("QR","QR_sending..fail")
            }
        })
    }
}