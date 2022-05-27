package com.kasirandrea.kasirandrea.kasir.auth

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.google.android.material.snackbar.Snackbar
import com.kasirandrea.kasirandrea.R
import com.kasirandrea.kasirandrea.databinding.ActivityLoginBinding
import com.kasirandrea.kasirandrea.kasir.LoginResponse
import com.kasirandrea.kasirandrea.kasir.owner.OwnerMenuActivity
import com.kasirandrea.kasirandrea.kasir.session.SessionManager
import com.kasirandrea.kasirandrea.kasir.webservice.ApiClient
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity(),AnkoLogger {
    lateinit var  binding : ActivityLoginBinding
    lateinit var progressDialog: ProgressDialog
    var api = ApiClient.instance()
    lateinit var sessionManager: SessionManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_login)
        binding.lifecycleOwner = this
        progressDialog = ProgressDialog(this)
        sessionManager = SessionManager(this)

        binding.btnlogin.setOnClickListener {
            val username = binding.edtusername.text.toString().trim()
            val password = binding.edtpassword.text.toString().trim()
            if (username.isNotEmpty() && password.isNotEmpty()){
                login(username,password,it)
            }else{
                toast("jangan kosongi kolom")
            }
        }

    }

    fun login(username: String, password: String,view : View) {
        loading(true)
        api.login(username,password).enqueue(object : Callback<LoginResponse>{
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                try {
                    if (response.body()!!.status == 1) {
                        loading(false)
                        if (response.body()!!.data!!.role == 0){
                            toast("login berhasil")
                            sessionManager.setLoginowner(true)
                            startActivity<OwnerMenuActivity>()
                            finish()
                        }else{

                        }

                    } else {
                        loading(false)
                        Snackbar.make(view, "Email atau password salah", Snackbar.LENGTH_SHORT).show()

                    }


                }catch (e : Exception){
                    loading(false)
                    info { "dinda ${e.message }${response.code()} " }
                }

            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                loading(false)
                info { "dinda ${t.message}" }
                Snackbar.make(view, "Silahkan coba lagi", Snackbar.LENGTH_SHORT).show()

            }

        })

    }


    fun loading(status : Boolean){
        if (status){
            progressDialog.setTitle("Loading...")
            progressDialog.setCanceledOnTouchOutside(false)
            progressDialog.show()
        }else{
            progressDialog.dismiss()
        }
    }
}