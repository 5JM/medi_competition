package com.example.donkin

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.style.UnderlineSpan
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import com.example.donkin.dataForm.LoginForm
import com.example.donkin.dataForm.loginReturn
import com.example.donkin.retrofit.MasterApplication
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    lateinit var email:String
    lateinit var password:String
    lateinit var customProgress : ProgressDialog
    lateinit var container_view : View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        container_view = findViewById(R.id.login_container)

        customProgress = ProgressDialog(this)
        customProgress.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        customProgress.setCancelable(false)

        val original = login_text.text
        val spstring = SpannableString(original)
        spstring.setSpan(UnderlineSpan(),0, original.length,0)

        login_text.text = spstring
        //email, password update
        login_email_edittext.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                email = login_email_edittext.text.toString()
            }
        })
        login_password_edittext.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                password = login_password_edittext.text.toString()
            }
        })

        login_loginbtn.setOnClickListener {
            Log.e("loginLog>>","send{$email++$password}")
            login(this, email, password)
        }

        login_signbtn.setOnClickListener {
            startActivity(Intent(this@LoginActivity, SignActivity::class.java))
        }
    }
    fun login(activity: Activity, email:String, password:String){
        customProgress.show()
        val login = LoginForm(email, password)
//        Log.e("loginLog>>","{$login}")
        Log.e("loginLog>>","login{$email++$password}")
        (application as MasterApplication).service.login(
              login
//         email, password
        ).enqueue(
            object :Callback<loginReturn>{
                override fun onResponse(call: Call<loginReturn>, response: Response<loginReturn>) {
                    if(response.isSuccessful){
                        Log.e("loginLog>>","isSuccessful")
                        //Log.e("loginLog>>","${response.body()?.data}")
                        val token = response.body()?.data?.get("token")
                        Log.e("loginLog","{$token}")

                        saveToken(token,activity)
                        customProgress.dismiss()
                        (application as MasterApplication).createRetrofit()
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    }else{
                        Log.e("loginLog>>","isError, code : ${response.code()}")
                        Log.e("loginLog>>","isError, code : ${response.message()}")
                        customProgress.dismiss()
                        Snackbar.make(container_view,"비밀번호는 6자이상 입력해주세요.", Snackbar.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<loginReturn>, t: Throwable) {
                    Log.e("loginLog>>","onFailure")
                    customProgress.dismiss()
                    Snackbar.make(container_view,"통신 실패.",Snackbar.LENGTH_SHORT).show()
                }
            }
        )
    }
    fun saveToken(token:String?, activity: Activity){
        val sp = activity.getSharedPreferences("login_token", 0)
        val editor = sp.edit()
        editor.putString("login_token", token).apply()
        Log.e("loginLog>>","=========saveToken=========")
    }
}