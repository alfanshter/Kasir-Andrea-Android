package com.kasirandrea.kasirandrea.kasir.admin.datapembeli

import android.app.Activity
import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.kasirandrea.kasirandrea.R
import com.kasirandrea.kasirandrea.databinding.ActivityDataPembeliBinding
import com.kasirandrea.kasirandrea.databinding.ActivityUtamaBinding
import com.kasirandrea.kasirandrea.kasir.admin.detailpesanan.DetailPesananActivity
import com.kasirandrea.kasirandrea.kasir.admin.ongkir.OngkirActivity
import com.kasirandrea.kasirandrea.kasir.admin.produk.ProdukAdminActivity
import com.kasirandrea.kasirandrea.kasir.model.pesanan.PesananResponse
import com.kasirandrea.kasirandrea.kasir.model.post.PostPesanan
import com.kasirandrea.kasirandrea.kasir.session.SessionManager
import com.kasirandrea.kasirandrea.kasir.webservice.ApiClient
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DataPembeliActivity : AppCompatActivity(), AnkoLogger {
    lateinit var binding: ActivityDataPembeliBinding
    var total_belanja: Int? = null
    var modal: Int? = null

    companion object {
        var activity: Activity? = null
    }

    var api = ApiClient.instance()
    lateinit var sessionManager: SessionManager
    lateinit var progressDialog: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_data_pembeli)
        binding.lifecycleOwner = this
        activity = this
        progressDialog = ProgressDialog(this)
        sessionManager = SessionManager(this)
        val bundle: Bundle? = intent.extras
        total_belanja = bundle!!.getInt("total_belanja")
        modal = bundle.getInt("modal")

        binding.btnselanjutnya.setOnClickListener {

            val nama = binding.edtnama.text.toString().trim()
            val telepon = binding.edttelepon.text.toString().trim()
            val alamat = binding.edtalamat.text.toString().trim()
            val kurir = binding.edtekspedisi.text.toString().trim()
            val ongkir = binding.edtongkir.text.toString().trim()

            if (nama.isNotEmpty() && telepon.isNotEmpty() && alamat.isNotEmpty() && kurir.isNotEmpty() && ongkir.isNotEmpty()) {
                loading(true)
                api.tambah_transaksi(
                    PostPesanan(
                        nama,
                        ongkir.toInt(),
                        total_belanja!! + ongkir.toInt(),
                        total_belanja!!,
                        telepon,
                        sessionManager.getid_user(),
                        kurir,
                        alamat,
                        modal
                    )
                ).enqueue(object : Callback<PesananResponse> {
                    override fun onResponse(
                        call: Call<PesananResponse>,
                        response: Response<PesananResponse>
                    ) {
/*                        try {*/
                        if (response.isSuccessful) {
                            if (response.body()!!.status == 1) {
                                loading(false)
                                val gson = Gson()
                                val noteJson = gson.toJson(response.body()!!.data)
                                startActivity<DetailPesananActivity>(
                                    "pesanan" to noteJson
                                )
                                finish()
                            } else if (response.body()!!.status == 2) {
                                loading(false)
                                toast("jangan kosongi kolom")
                            } else {
                                loading(false)
                                toast("silahkan ulangi lagi")

                            }
                        }
/*                        } catch (e: Exception) {
                            loading(false)
                            info { "dinda cath ${e.message}" }

                        }*/
                    }

                    override fun onFailure(call: Call<PesananResponse>, t: Throwable) {
                        loading(false)
                        info { "dinda failure ${t.message}" }
                        toast("silahkan ulangi lagi")
                    }

                })
            } else {
                Snackbar.make(it, "Jangan kosongi kolom", 3000).show()
            }
        }

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