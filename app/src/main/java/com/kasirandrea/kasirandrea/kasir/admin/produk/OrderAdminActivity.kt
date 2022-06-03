package com.kasirandrea.kasirandrea.kasir.admin.produk

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.google.gson.Gson
import com.kasirandrea.kasirandrea.R
import com.kasirandrea.kasirandrea.databinding.ActivityOrderAdminBinding
import com.kasirandrea.kasirandrea.databinding.ActivityProdukAdminBinding
import com.kasirandrea.kasirandrea.kasir.adapter.ProdukAdapter
import com.kasirandrea.kasirandrea.kasir.model.post.PostKeranjang
import com.kasirandrea.kasirandrea.kasir.model.produk.PostProdukResponse
import com.kasirandrea.kasirandrea.kasir.model.produk.ProdukModel
import com.kasirandrea.kasirandrea.kasir.session.SessionManager
import com.kasirandrea.kasirandrea.kasir.webservice.ApiClient
import com.kasirandrea.kasirandrea.kasir.webservice.Constant
import com.squareup.picasso.Picasso
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat
import java.text.NumberFormat

class OrderAdminActivity : AppCompatActivity(),AnkoLogger {
    var produkmodel : ProdukModel? = null
    private lateinit var mAdapter: ProdukAdapter
    lateinit var binding : ActivityOrderAdminBinding
    var api = ApiClient.instance()
    lateinit var progressDialog: ProgressDialog
    lateinit var sessionManager: SessionManager
    var counter = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_order_admin)
        binding.lifecycleOwner = this
        sessionManager = SessionManager(this)
        progressDialog = ProgressDialog(this)
        val gson = Gson()
        produkmodel = gson.fromJson(intent.getStringExtra("produk"), ProdukModel::class.java)

        binding.txtnama.text = produkmodel!!.nama
        val formatter: NumberFormat = DecimalFormat("#,###")
        val harga = produkmodel!!.harga

        binding.txtharga.text = "Rp. ${formatter.format(harga)}"
        binding.txtstok.text = "${produkmodel!!.stok} Produk"
        binding.txtdeskripsi.text = produkmodel!!.deskripsi
        Picasso.get().load(Constant.folder_foto+produkmodel!!.foto).centerCrop().fit().into(binding.imgreview)

        binding.btnminus.setOnClickListener {
            if (counter == 0){
                binding.txtjumlah.text = counter.toString()
            }else if (counter >0){
                counter -= 1
                binding.txtjumlah.text = counter.toString()

            }
        }

        binding.btnplus.setOnClickListener {
            counter += 1
            binding.txtjumlah.text = counter.toString()
        }

        binding.btntambah.setOnClickListener {
            if (counter == 0){
             toast("Jumlah tidak boleh kosong")
            }else{
                tambah_keranjang()
            }
        }
    }

    fun tambah_keranjang(){
        loading(true)
        api.tambah_keranjang(PostKeranjang(produkmodel!!.id,produkmodel!!.harga,counter,sessionManager.getid_user())).enqueue(object : Callback<PostProdukResponse> {
            override fun onResponse(
                call: Call<PostProdukResponse>,
                response: Response<PostProdukResponse>
            ) {
                try {
                    if (response.isSuccessful){
                        if (response.body()!!.status == 1){
                            loading(false)
                            toast("tambah keranjang berhasil")
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