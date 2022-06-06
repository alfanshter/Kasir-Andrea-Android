package com.kasirandrea.kasirandrea.kasir.admin.datapembeli

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.google.android.material.snackbar.Snackbar
import com.kasirandrea.kasirandrea.R
import com.kasirandrea.kasirandrea.databinding.ActivityDataPembeliBinding
import com.kasirandrea.kasirandrea.databinding.ActivityUtamaBinding
import com.kasirandrea.kasirandrea.kasir.admin.ongkir.OngkirActivity
import org.jetbrains.anko.startActivity

class DataPembeliActivity : AppCompatActivity() {
    lateinit var binding : ActivityDataPembeliBinding
    var total_belanja  : Int? = null
    companion object{
        var activity : Activity? = null
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_data_pembeli)
        binding.lifecycleOwner = this
        activity = this
        val bundle: Bundle? = intent.extras
        total_belanja = bundle!!.getInt("total_belanja")

        binding.btnselanjutnya.setOnClickListener {
            val nama = binding.edtnama.text.toString().trim()
            val telepon = binding.edttelepon.text.toString().trim()
            val alamat = binding.edtalamat.text.toString().trim()

            if (nama.isNotEmpty() && telepon.isNotEmpty() && alamat.isNotEmpty()){
                startActivity<OngkirActivity>(
                    "nama" to nama,
                    "telepon" to telepon,
                    "alamat" to alamat,
                    "total_belanja" to total_belanja
                )
            }else{
                Snackbar.make(it,"Jangan kosongi kolom",3000).show()
            }
        }

    }
}