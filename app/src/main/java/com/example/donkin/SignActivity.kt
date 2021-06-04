package com.example.donkin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.example.donkin.dataForm.SignForm
import com.example.donkin.retrofit.MasterApplication
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_sign.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignActivity : AppCompatActivity() {
    lateinit var email: String
    lateinit var password : String
    lateinit var name : String
    lateinit var view : View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign)

        view = findViewById(R.id.sign_container)

        sign_email_edittext.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if(checkEmail(sign_email_edittext.text.toString())){
                    email = sign_email_edittext.text.toString()
                }else{
                    Snackbar.make(view,"옳바른 이메일을 입력해주세요.",Snackbar.LENGTH_SHORT).show()
                }
            }
        })
        sign_password_edittext.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }

            override fun afterTextChanged(s: Editable?) {
                if(sign_password_edittext.text.toString().length>=6)
                    password=sign_password_edittext.text.toString()
                else
                    Snackbar.make(view,"비밀번호는 6자이상 입력해주세요.",Snackbar.LENGTH_SHORT).show()
            }
        })
        sign_name_edittext.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }

            override fun afterTextChanged(s: Editable?) {
                name = sign_name_edittext.text.toString()
            }
        })

        sign_signbtn.setOnClickListener {
            register(email, name, password)
        }
    }
    fun checkEmail(email : String?):Boolean{
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
    fun register(email : String, name : String, password : String){
        val regiBody = SignForm(email, name, password)

        sign_signbtn.setOnClickListener {
            (application as MasterApplication).service.register(
                regiBody
            ).enqueue(
                object :Callback<Void>{
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if(response.isSuccessful){
                            Log.e("signLog>>","가입 완료")
                            Toast.makeText(this@SignActivity,"회원가입 완료!",Toast.LENGTH_SHORT).show()
//                            Snackbar.make(view,"회원가입 완료!",Snackbar.LENGTH_SHORT).show()
                            finish()
                        }else{
                            if(response.code()==409){
                                Log.e("signLog>>","${response.code()}")
                                Snackbar.make(view,"중복된 이메일입니다.",Snackbar.LENGTH_SHORT).show()
                            }else{
                                Log.e("signLog>>","${response.code()}")
                                Snackbar.make(view,"다시한번 확인해주세요.",Snackbar.LENGTH_SHORT).show()
                            }
                        }
                    }
                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Log.e("signLog>>","onFailure")
                        Snackbar.make(view,"통신오류",Snackbar.LENGTH_SHORT).show()
                    }
                }
            )
        }
    }
}