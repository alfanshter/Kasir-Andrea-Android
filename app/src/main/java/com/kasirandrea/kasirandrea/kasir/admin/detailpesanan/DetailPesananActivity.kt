package com.kasirandrea.kasirandrea.kasir.admin.detailpesanan


import android.Manifest
import android.app.ProgressDialog
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.pdf.PdfDocument
import android.graphics.pdf.PdfDocument.PageInfo
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import android.graphics.Bitmap

import android.graphics.BitmapFactory
import androidx.databinding.DataBindingUtil
import com.google.gson.Gson
import com.kasirandrea.kasirandrea.R
import com.kasirandrea.kasirandrea.databinding.ActivityDetailPesananBinding
import com.kasirandrea.kasirandrea.kasir.adapter.KeranjangAdapter
import com.kasirandrea.kasirandrea.kasir.adapter.KeranjangSelesaiAdapter
import com.kasirandrea.kasirandrea.kasir.model.keranjang.KeranjangModel
import com.kasirandrea.kasirandrea.kasir.model.keranjang.KeranjangResponse
import com.kasirandrea.kasirandrea.kasir.model.pesanan.PesananModel
import com.kasirandrea.kasirandrea.kasir.model.produk.ProdukModel
import com.kasirandrea.kasirandrea.kasir.session.SessionManager
import com.kasirandrea.kasirandrea.kasir.webservice.ApiClient
import java.text.DecimalFormat
import java.text.NumberFormat


class DetailPesananActivity : AppCompatActivity(), AnkoLogger {
    private val api = ApiClient.instance()
    lateinit var sessionManager: SessionManager
    lateinit var progressDialog: ProgressDialog
    var nomorpesanan: String? = null
    var jumlahhalaman: Int? = null
    var listdataprintall = ArrayList<KeranjangModel>()
    private lateinit var mAdapter: KeranjangSelesaiAdapter
    var informationArray = arrayOf("Name", "Perusahaan", "Alamat")

//    private lateinit var mAdapter: KeranjangSelesaiAdapter

    lateinit var binding: ActivityDetailPesananBinding


    var pesanan: PesananModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail_pesanan)
        binding.lifecycleOwner = this
        val bundle: Bundle? = intent.extras
        ActivityCompat.requestPermissions(
            this, arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ), PackageManager.PERMISSION_GRANTED
        )

        val gson = Gson()
        pesanan = gson.fromJson(intent.getStringExtra("pesanan"), PesananModel::class.java)


        sessionManager = SessionManager(this)
        progressDialog = ProgressDialog(this)

        val sdf = SimpleDateFormat("dd MMMM yyyy hh:mm")
        val currentDate = sdf.format(Date())

        binding.txtnama.text = pesanan!!.nama.toString()
        binding.txttelepon.text = pesanan!!.telepon.toString()
        binding.txtalamat.text = pesanan!!.alamat.toString()

        val formatter: NumberFormat = DecimalFormat("#,###")
        val myNumber = pesanan!!.harga
        val harga: String = formatter.format(myNumber)

        binding.txtharga.text = "Rp. ${harga.toString()}"
        binding.txttotalharga.text = "Rp. ${formatter.format(pesanan!!.hargaTotal)}"
        binding.txtkalender.text = currentDate.toString()
        binding.txtongkoskirim.text = "Rp. ${formatter.format(pesanan!!.ongkir)}"
        binding.btncheckout.setOnClickListener {

        }
    }


    override fun onStart() {
        super.onStart()
        binding.rvpesanan.layoutManager = LinearLayoutManager(this)
        binding.rvpesanan.setHasFixedSize(true)
        (binding.rvpesanan.layoutManager as LinearLayoutManager).orientation =
            LinearLayoutManager.VERTICAL

        api.get_keranjang_pesanan(pesanan!!.nomorpesanan!!)
            .enqueue(object : Callback<KeranjangResponse> {
                override fun onResponse(
                    call: Call<KeranjangResponse>,
                    response: Response<KeranjangResponse>
                ) {
                    try {
                        if (response.isSuccessful) {
                            val notesList = mutableListOf<KeranjangModel>()
                            val data = response.body()
                            for (hasil in data!!.data!!) {
                                notesList.add(hasil)
                                mAdapter =
                                    KeranjangSelesaiAdapter(notesList)
                                binding.rvpesanan.adapter = mAdapter
                                mAdapter.notifyDataSetChanged()
                            }

                        } else {
                            toast("gagal mendapatkan response")
                        }
                    } catch (e: Exception) {
                        info { "dinda ${e.message}" }
                    }
                }

                override fun onFailure(call: Call<KeranjangResponse>, t: Throwable) {
                    info { "dinda ${t.message}" }
                }

            })

    }

    fun formatnumber(number: Int): String {
        val formatter: NumberFormat = DecimalFormat("#,###")
        val formattedNumber: String = formatter.format(number)

        return formattedNumber
    }

}