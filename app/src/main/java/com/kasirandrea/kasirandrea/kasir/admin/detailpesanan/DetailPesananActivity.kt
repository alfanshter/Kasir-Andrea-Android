package com.kasirandrea.kasirandrea.kasir.admin.detailpesanan


import android.Manifest
import android.app.Activity
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
import android.net.Uri
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.google.gson.Gson
import com.kasirandrea.kasirandrea.R
import com.kasirandrea.kasirandrea.databinding.ActivityDetailPesananBinding
import com.kasirandrea.kasirandrea.kasir.adapter.KeranjangAdapter
import com.kasirandrea.kasirandrea.kasir.adapter.KeranjangSelesaiAdapter
import com.kasirandrea.kasirandrea.kasir.model.keranjang.KeranjangModel
import com.kasirandrea.kasirandrea.kasir.model.keranjang.KeranjangResponse
import com.kasirandrea.kasirandrea.kasir.model.pesanan.NotaResponse
import com.kasirandrea.kasirandrea.kasir.model.pesanan.PesananModel
import com.kasirandrea.kasirandrea.kasir.model.post.PostKeranjang
import com.kasirandrea.kasirandrea.kasir.model.produk.PostProdukResponse
import com.kasirandrea.kasirandrea.kasir.model.produk.ProdukModel
import com.kasirandrea.kasirandrea.kasir.session.SessionManager
import com.kasirandrea.kasirandrea.kasir.webservice.ApiClient
import com.mazenrashed.printooth.Printooth
import com.mazenrashed.printooth.data.printable.Printable
import com.mazenrashed.printooth.data.printable.RawPrintable
import com.mazenrashed.printooth.data.printable.TextPrintable
import com.mazenrashed.printooth.data.printer.DefaultPrinter
import com.mazenrashed.printooth.ui.ScanningActivity
import com.mazenrashed.printooth.utilities.Printing
import com.mazenrashed.printooth.utilities.PrintingCallback
import org.jetbrains.anko.startActivityForResult
import java.text.DecimalFormat
import java.text.NumberFormat
import kotlin.collections.ArrayList


class DetailPesananActivity : AppCompatActivity(), AnkoLogger{
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

    //printer blueetoh
    internal var printing : Printing? = null

    var pesanan: PesananModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail_pesanan)
        binding.lifecycleOwner = this
        val bundle: Bundle? = intent.extras

        val gson = Gson()
        pesanan = gson.fromJson(intent.getStringExtra("pesanan"), PesananModel::class.java)


        sessionManager = SessionManager(this)
        progressDialog = ProgressDialog(this)

        val sdf = SimpleDateFormat("dd MMMM yyyy hh:mm")
        val currentDate = sdf.format(Date())

        binding.txtnama.text = pesanan!!.nama.toString()
        binding.txttelepon.text = pesanan!!.telepon.toString()
        binding.txtalamat.text = pesanan!!.alamat.toString()
        binding.txtkurir.text = pesanan!!.kurir.toString()

        val formatter: NumberFormat = DecimalFormat("#,###")
        val myNumber = pesanan!!.harga
        val harga: String = formatter.format(myNumber)

        binding.txtharga.text = "Rp. ${harga.toString()}"
        binding.txttotalharga.text = "Rp. ${formatter.format(pesanan!!.hargaTotal)}"
        binding.txtkalender.text = currentDate.toString()
        binding.txtongkoskirim.text = "Rp. ${formatter.format(pesanan!!.ongkir)}"

        if (pesanan!!.is_status == 0){
            binding.btnselesai.visibility = View.VISIBLE
        }else{
            binding.btnselesai.visibility = View.GONE

        }

        binding.btnselesai.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Pesanan")
            builder.setMessage("Apakah anda akan menyelesaikan pesanan ? ")
            builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                selesaikan_pesanan()
            }

            builder.setNegativeButton(android.R.string.no) { dialog, which ->
            }

            builder.show()

        }
        binding.btncheckout.setOnClickListener {
            cetak_nota()
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

    fun cetak_nota(){
        loading(true)
        api.cetak_nota(pesanan!!.id!!,pesanan!!.nomorpesanan!!).enqueue(object : Callback<NotaResponse> {
            override fun onResponse(
                call: Call<NotaResponse>,
                response: Response<NotaResponse>
            ) {
                try {
                    if (response.isSuccessful){
                        if (response.body()!!.status == 1){
                            loading(false)
                            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(response.body()!!.data.toString()))
                            startActivity(browserIntent)
                            finish()
                        }else if (response.body()!!.status ==2){
                            loading(false)
                            toast("jangan kosongi kolom")
                        }else{
                            loading(false)
                            toast("silahkan ulangi lagi")

                        }
                    }
                }catch (e :Exception){
                    loading(false)
                    info { "dinda cath ${e.message}" }

                }

            }

            override fun onFailure(call: Call<NotaResponse>, t: Throwable) {
                loading(false)
                info { "dinda failure ${t.message}" }
                toast("silahkan hubungi developer")

            }

        })

    }
    fun selesaikan_pesanan(){
        loading(true)
        api.pesanan_selesai(pesanan!!.id!!).enqueue(object : Callback<PostProdukResponse> {
            override fun onResponse(
                call: Call<PostProdukResponse>,
                response: Response<PostProdukResponse>
            ) {
                try {
                    if (response.isSuccessful){
                        if (response.body()!!.status == 1){
                            loading(false)
                            toast("pesanan diselesaikan")
                            finish()
                        }else if (response.body()!!.status ==2){
                            loading(false)
                            toast("jangan kosongi kolom")
                        }else{
                            loading(false)
                            toast("silahkan ulangi lagi")

                        }
                    }
                }catch (e :Exception){
                    loading(false)
                    info { "dinda cath ${e.message}" }

                }

            }

            override fun onFailure(call: Call<PostProdukResponse>, t: Throwable) {
                loading(false)
                info { "dinda failure ${t.message}" }
                toast("silahkan hubungi developer")

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