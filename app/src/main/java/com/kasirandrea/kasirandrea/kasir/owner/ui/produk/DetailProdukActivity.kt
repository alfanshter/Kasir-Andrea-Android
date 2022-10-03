package com.kasirandrea.kasirandrea.kasir.owner.ui.produk

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Point
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Display
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.kasirandrea.kasirandrea.R
import com.kasirandrea.kasirandrea.databinding.ActivityDeailProduktBinding
import com.kasirandrea.kasirandrea.kasir.model.pesanan.NotaResponse
import com.kasirandrea.kasirandrea.kasir.model.produk.PostProdukResponse
import com.kasirandrea.kasirandrea.kasir.model.produk.ProdukModel
import com.kasirandrea.kasirandrea.kasir.owner.OwnerMenuActivity
import com.kasirandrea.kasirandrea.kasir.webservice.ApiClient
import com.kasirandrea.kasirandrea.kasir.webservice.Constant
import com.squareup.picasso.Picasso
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.support.v4.startActivity
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat
import java.text.NumberFormat
var qrcode : String? = null

class DetailProdukActivity : AppCompatActivity(),AnkoLogger {
    var produkmodel : ProdukModel? = null
    lateinit var binding : ActivityDeailProduktBinding
    companion object{
        lateinit var view : Activity
    }
    var api = ApiClient.instance()
    lateinit var progressDialog: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        view = this
        binding = DataBindingUtil.setContentView(this,R.layout.activity_deail_produkt)
        binding.lifecycleOwner = this
        progressDialog = ProgressDialog(this)

        val gson = Gson()
        produkmodel = gson.fromJson(intent.getStringExtra("produkmodel"), ProdukModel::class.java)

        binding.txtjudul.text = produkmodel!!.nama
        val formatter: NumberFormat = DecimalFormat("#,###")
        val harga = produkmodel!!.harga

        binding.txtharga.text = "Rp. ${formatter.format(harga)}"
        binding.txtmodal.text = "Rp. ${formatter.format(produkmodel!!.modal)}"
        binding.txtstok.text = "${produkmodel!!.stok} Produk"
        binding.txtdeskripsi.text = produkmodel!!.deskripsi
        Picasso.get().load(Constant.folder_foto+produkmodel!!.foto).centerCrop().fit().into(binding.imgreview)

        binding.imgback.setOnClickListener {
            finish()
        }
        binding.btneditproduk.setOnClickListener {
            val gson = Gson()
            val noteJson = gson.toJson(produkmodel)
            startActivity<TambahProdukActivity>("produkmodel" to noteJson)

        }

        binding.btnprint.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Produk")
            builder.setMessage("Apakah anda akan cetak qrcode ini ? ")
            builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                cetak_qrcode()
            }

            builder.setNegativeButton(android.R.string.no) { dialog, which ->
            }

            builder.show()
        }
        binding.btnhapus.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Produk")
            builder.setMessage("Apakah anda akan menghapus produk ini ? ")
            builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                hapus_produk(it)
            }

            builder.setNegativeButton(android.R.string.no) { dialog, which ->
            }

            builder.show()


        }


        
      
    }

    private fun cetak_qrcode() {
        loading(true)
        api.generate_qrcode(produkmodel!!.id!!).enqueue(object : Callback<NotaResponse> {
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


    fun hapus_produk(view : View){
        api.hapus_produk(produkmodel!!.foto!!,produkmodel!!.id!!).enqueue(object : Callback<PostProdukResponse> {
            override fun onResponse(call: Call<PostProdukResponse>, response: Response<PostProdukResponse>) {
                try {
                    if (response.body()!!.status == 1) {
                        loading(false)
                        toast("produk berhasil di hapus")
                    finish()
                    } else {
                        loading(false)
                        Snackbar.make(view, "Email atau password salah", Snackbar.LENGTH_SHORT).show()

                    }


                }catch (e : Exception){
                    loading(false)
                    info { "dinda ${e.message }${response.code()} " }
                }

            }

            override fun onFailure(call: Call<PostProdukResponse>, t: Throwable) {
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