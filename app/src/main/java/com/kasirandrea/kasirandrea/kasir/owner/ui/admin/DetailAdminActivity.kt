package com.kasirandrea.kasirandrea.kasir.owner.ui.admin

import android.app.ProgressDialog
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.kasirandrea.kasirandrea.R
import com.kasirandrea.kasirandrea.databinding.ActivityDetailAdminBinding
import com.kasirandrea.kasirandrea.databinding.ActivityTambahAdminBinding
import com.kasirandrea.kasirandrea.kasir.model.admin.AdminModel
import com.kasirandrea.kasirandrea.kasir.model.produk.PostProdukResponse
import com.kasirandrea.kasirandrea.kasir.owner.ui.gaji.GajiAdminActivity
import com.kasirandrea.kasirandrea.kasir.webservice.ApiClient
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailAdminActivity : AppCompatActivity(),AnkoLogger {
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

        binding.btnback.setOnClickListener {
            finish()
        }
        binding.btnlihatgaji.setOnClickListener {
            val gson = Gson()
            val noteJson = gson.toJson(adminmodel)
            startActivity<GajiAdminActivity>("adminmodel" to noteJson)
        }

        binding.btnhapusakun.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Admin")
            builder.setMessage("Apakah anda akan menghapus admin ? ")
            builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                hapus_admin(it)
            }

            builder.setNegativeButton(android.R.string.no) { dialog, which ->
            }

            builder.show()
        }
    }

    private fun hapus_admin(view : View) {
        loading(true)
        api.delete_admin(adminmodel!!.id!!,adminmodel!!.foto!!).enqueue(object :
            Callback<PostProdukResponse> {
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