package com.kasirandrea.kasirandrea.kasir.owner.ui.admin

import android.app.ProgressDialog
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.google.gson.Gson
import com.kasirandrea.kasirandrea.R
import com.kasirandrea.kasirandrea.databinding.ActivityDetailAdminBinding
import com.kasirandrea.kasirandrea.databinding.ActivityTambahAdminBinding
import com.kasirandrea.kasirandrea.kasir.model.admin.AdminModel
import com.kasirandrea.kasirandrea.kasir.webservice.ApiClient

class DetailAdminActivity : AppCompatActivity() {
    var adminmodel : AdminModel? = null
    var api = ApiClient.instance()
    lateinit var binding : ActivityDetailAdminBinding

    //foto
    var filePath: Uri? = null
    var data: ByteArray? = null
    private val REQUEST_PICK_IMAGE = 1

    lateinit var progressDialog: ProgressDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail_admin)
        binding.lifecycleOwner = this
        progressDialog = ProgressDialog(this)

        val gson = Gson()
        adminmodel = gson.fromJson(intent.getStringExtra("admin"), AdminModel::class.java)

        binding.txtinisialNama.text = adminmodel!!.nama!!.subSequence(0,1)
        binding.txtnama.text = adminmodel!!.nama.toString()
        binding.txtalamat.text = adminmodel!!.alamat.toString()
        binding.nohp.text = adminmodel!!.telepon.toString()
        binding.txtemail.text = adminmodel!!.username.toString()

    }
}