package com.kasirandrea.kasirandrea.kasir.admin

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.kasirandrea.kasirandrea.R
import com.kasirandrea.kasirandrea.databinding.ActivityProdukAdminBinding
import com.kasirandrea.kasirandrea.databinding.ActivityUtamaBinding
import com.kasirandrea.kasirandrea.kasir.admin.produk.ProdukAdminActivity
import com.kasirandrea.kasirandrea.kasir.auth.LoginActivity
import com.kasirandrea.kasirandrea.kasir.model.produk.PostProdukResponse
import com.kasirandrea.kasirandrea.kasir.session.SessionManager
import com.kasirandrea.kasirandrea.kasir.webservice.ApiClient
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.support.v4.startActivity
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UtamaActivity : AppCompatActivity(),AnkoLogger {
    lateinit var binding : ActivityUtamaBinding
    var api = ApiClient.instance()
    lateinit var progressDialog: ProgressDialog
    lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_utama)
        binding.lifecycleOwner = this
        sessionManager = SessionManager(this)
        progressDialog = ProgressDialog(this)
        binding.btninputpesanan.setOnClickListener {
            startActivity<ProdukAdminActivity>()
        }

        binding.imgfoto.setOnClickListener {
            logout()
        }

    }
    fun logout(){
        loading(true)
        api.logout().enqueue(object : Callback<PostProdukResponse> {
            override fun onResponse(
                call: Call<PostProdukResponse>,
                response: Response<PostProdukResponse>
            ) {
                if (response.isSuccessful){
                    if (response.body()!!.status == 1){
                        loading(false)
                        sessionManager.setLogin(false)
                        startActivity<LoginActivity>()
                        finish()
                    }
                }
            }

            override fun onFailure(call: Call<PostProdukResponse>, t: Throwable) {
                loading(false)
                info { "dinda ${t.message}" }
                toast("Kesalahan jaringan")
            }

        })
    }

    fun loading(status: Boolean) {
        if (status) {
            progressDialog.setTitle("Loading...")
            progressDialog.setCanceledOnTouchOutside(false)
            progressDialog.show()
        } else {
            progressDialog.dismiss()
        }
    }
}