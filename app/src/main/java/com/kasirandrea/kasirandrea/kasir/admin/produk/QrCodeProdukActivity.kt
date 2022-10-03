package com.kasirandrea.kasirandrea.kasir.admin.produk

import android.app.ProgressDialog
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.kasirandrea.kasirandrea.R
import com.kasirandrea.kasirandrea.databinding.ActivityQrCodeProdukBinding
import com.kasirandrea.kasirandrea.kasir.admin.UtamaActivity
import com.kasirandrea.kasirandrea.kasir.model.produk.DetailProdukResponse
import com.kasirandrea.kasirandrea.kasir.owner.OwnerMenuActivity
import com.kasirandrea.kasirandrea.kasir.webservice.ApiClient
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class QrCodeProdukActivity : AppCompatActivity(),AnkoLogger {
    private lateinit var codeScanner: CodeScanner
    lateinit var binding : ActivityQrCodeProdukBinding
    var is_status = 0
    var api = ApiClient.instance()
    lateinit var progressDialog: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_qr_code_produk)
        binding.lifecycleOwner  =this
        progressDialog = ProgressDialog(this)

        setupPermissions()
        codeScanner()
    }


    private fun codeScanner() {
        codeScanner = CodeScanner(this, binding.scn)

        codeScanner.apply {
            camera = CodeScanner.CAMERA_BACK
            formats = CodeScanner.ALL_FORMATS

            autoFocusMode = AutoFocusMode.SAFE
            scanMode = ScanMode.CONTINUOUS
            isAutoFocusEnabled = true
            isFlashEnabled = false

            decodeCallback = DecodeCallback {
                runOnUiThread {
                    loading(true)
                    hasilqrcode = it.text
                    if (is_status ==0){
                        is_status = 1
                        api.detail_produk(it.text).enqueue(object : Callback<DetailProdukResponse>{
                            override fun onResponse(call: Call<DetailProdukResponse>, response: Response<DetailProdukResponse>) {
                                try {
                                    if (response.body()!!.status == 1) {
                                        loading(false)
                                        val gson = Gson()
                                        val noteJson = gson.toJson(response.body()!!.data)
                                        startActivity<OrderAdminActivity>("produk" to noteJson)
                                        finish()

                                    } else {
                                        loading(false)
                                        toast("Kode tidak ada yang cocok")
                                        finish()

                                    }


                                }catch (e : Exception){
                                    loading(false)
                                    info { "dinda ${e.message }${response.code()} " }
                                    finish()
                                }

                            }

                            override fun onFailure(call: Call<DetailProdukResponse>, t: Throwable) {
                                loading(false)
                                info { "dinda ${t.message}" }
                                toast("silahkan coba lagi")
                                finish()

                            }

                        })

                    }

                }
            }

            errorCallback = ErrorCallback {
                runOnUiThread {
                    info { "dinda error codescanner ${it.message}" }
                }
            }

            binding.scn.setOnClickListener {
                codeScanner.startPreview()
            }

        }
    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }

    private fun setupPermissions() {
        val permission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            makeRequest()
        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(
            this, arrayOf(android.Manifest.permission.CAMERA),
            CAMERA_REQ
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CAMERA_REQ -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(
                        this,
                        "You need the camera permission to use this app",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
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

    companion object {
        private const val CAMERA_REQ = 101
        var hasilqrcode : String? = null
        var no : String? = null
        var kodehydrant : String? = null
        var lokasi : String? = null

    }
}