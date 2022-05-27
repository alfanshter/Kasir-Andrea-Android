package com.kasirandrea.kasirandrea.kasir.owner.ui.produk

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.google.gson.Gson
import com.kasirandrea.kasirandrea.R
import com.kasirandrea.kasirandrea.databinding.ActivityDeailProduktBinding
import com.kasirandrea.kasirandrea.kasir.model.produk.ProdukModel
import com.kasirandrea.kasirandrea.kasir.webservice.Constant
import com.squareup.picasso.Picasso
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.support.v4.startActivity
import java.text.DecimalFormat
import java.text.NumberFormat

class DetailProdukActivity : AppCompatActivity() {
    var produkmodel : ProdukModel? = null
    lateinit var binding : ActivityDeailProduktBinding
    companion object{
        lateinit var view : Activity
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        view = this
        binding = DataBindingUtil.setContentView(this,R.layout.activity_deail_produkt)
        binding.lifecycleOwner = this
        val gson = Gson()
        produkmodel = gson.fromJson(intent.getStringExtra("produkmodel"), ProdukModel::class.java)

        binding.txtjudul.text = produkmodel!!.nama
        val formatter: NumberFormat = DecimalFormat("#,###")
        val harga = produkmodel!!.harga

        binding.txtharga.text = "Rp. ${formatter.format(harga)}"
        binding.txtstok.text = "${produkmodel!!.stok} Produk"
        binding.txtdeskripsi.text = produkmodel!!.deskripsi
        Picasso.get().load(Constant.folder_foto+produkmodel!!.foto).centerCrop().fit().into(binding.imgreview)

        binding.btneditproduk.setOnClickListener {
            val gson = Gson()
            val noteJson = gson.toJson(produkmodel)
            startActivity<TambahProdukActivity>("produkmodel" to noteJson)

        }
    }
}