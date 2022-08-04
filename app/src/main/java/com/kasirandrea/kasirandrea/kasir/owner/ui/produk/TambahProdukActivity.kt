package com.kasirandrea.kasirandrea.kasir.owner.ui.produk

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.databinding.DataBindingUtil
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.kasirandrea.kasirandrea.R
import com.kasirandrea.kasirandrea.databinding.ActivityTambahProdukBinding
import com.kasirandrea.kasirandrea.kasir.model.produk.PostProdukResponse
import com.kasirandrea.kasirandrea.kasir.model.produk.ProdukModel
import com.kasirandrea.kasirandrea.kasir.webservice.ApiClient
import com.kasirandrea.kasirandrea.kasir.webservice.Constant
import com.squareup.picasso.Picasso
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File

class TambahProdukActivity : AppCompatActivity(),AnkoLogger {
    var api = ApiClient.instance()
    lateinit var binding: ActivityTambahProdukBinding
    //foto
    var filePath: Uri? = null
    var data: ByteArray? = null
    private val REQUEST_PICK_IMAGE = 1
    var produkmodel : ProdukModel? = null

    lateinit var progressDialog: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_tambah_produk)
        binding.lifecycleOwner = this
        progressDialog = ProgressDialog(this)
        val gson = Gson()
        produkmodel = gson.fromJson(intent.getStringExtra("produkmodel"), ProdukModel::class.java)
        if (produkmodel!=null){
            binding.edtnamaproduk.setText(produkmodel!!.nama)
            binding.edtdeskripsi.setText(produkmodel!!.deskripsi)
            binding.edtharga.setText(produkmodel!!.harga.toString())
            binding.edtstok.setText(produkmodel!!.stok)
            binding.edtmodal.setText(produkmodel!!.modal.toString())
            binding.edtdiskon.setText(produkmodel!!.diskon.toString())
            binding.edtjumlahgrosir.setText(produkmodel!!.jumlah_grosir.toString())
            binding.edthargagrosir.setText(produkmodel!!.harga_grosir.toString())
            Picasso.get().load(Constant.folder_foto+produkmodel!!.foto).fit().centerCrop().into(binding.imgfoto)
            binding.btnsimpan.setOnClickListener {
                if (data!=null){
                    updateproduk(it)
                }else{
                    update_produk_foto(it)

                }
            }


        }else{
            binding.btnsimpan.setOnClickListener {
                uploadproduk(it)
            }

        }
        binding.imgfoto.setOnClickListener {
            pilihfile()
        }

        binding.btnbatalkan.setOnClickListener {
            finish()
        }
        binding.btnback.setOnClickListener {
            finish()
        }





    }

    private fun uploadproduk(view: View) {

        val edt_nama = binding.edtnamaproduk.text.toString().trim()
        val edt_deskripsi = binding.edtdeskripsi.text.toString().trim()
        val edt_harga = binding.edtharga.text.toString().trim()
        val edt_stok = binding.edtstok.text.toString().trim()
        val edt_harga_grosir = binding.edthargagrosir.text.toString().trim()
        val edt_jumlah_grosir = binding.edtjumlahgrosir.text.toString().trim()
        val edt_diskon = binding.edtdiskon.text.toString().trim()
        val edt_modal = binding.edtmodal.text.toString().trim()

        if (edt_nama.isNotEmpty() && edt_deskripsi.isNotEmpty() && edt_harga.isNotEmpty()
            && edt_stok.isNotEmpty() && data!=null && edt_harga_grosir.isNotEmpty() &&
            edt_jumlah_grosir.isNotEmpty() && edt_diskon.isNotEmpty()
        ) {
            loading(true)
            val f: File = File(cacheDir, "foto")
            f.createNewFile()
            //Convert bitmap
            //ini bitmapnya

            val reqFile = RequestBody.create(MediaType.parse("image/*"), data)
            val body = MultipartBody.Part.createFormData("foto", f.name, reqFile)

            val nama: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                edt_nama
            )

            val deskripsi: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                edt_deskripsi
            )

            val harga: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                edt_harga
            )

            val stok: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                edt_stok
            )

            val harga_grosir: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                edt_harga_grosir
            )

            val jumlah_grosir: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                edt_jumlah_grosir
            )

            val diskon: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                edt_diskon
            )
            val modal: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                edt_modal
            )

            api.produk(body,nama,deskripsi,harga,stok,harga_grosir, jumlah_grosir, diskon,modal).enqueue(object : Callback<PostProdukResponse>{
                override fun onResponse(
                    call: Call<PostProdukResponse>,
                    response: Response<PostProdukResponse>
                ) {
                    try {
                        if (response.isSuccessful){
                            if (response.body()!!.status == 1){
                                loading(false)
                                toast("berhasil di input")
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

        } else {
            Snackbar.make(view, "Jangan kosongi kolom", 3000).show()
        }

    }

    private fun updateproduk(view: View) {

        val edt_nama = binding.edtnamaproduk.text.toString().trim()
        val edt_deskripsi = binding.edtdeskripsi.text.toString().trim()
        val edt_harga = binding.edtharga.text.toString().trim()
        val edt_stok = binding.edtstok.text.toString().trim()
        val edt_harga_grosir = binding.edthargagrosir.text.toString().trim()
        val edt_jumlah_grosir = binding.edtjumlahgrosir.text.toString().trim()
        val edt_diskon = binding.edtdiskon.text.toString().trim()
        val edt_modal = binding.edtmodal.text.toString().trim()


        if (edt_nama.isNotEmpty() && edt_deskripsi.isNotEmpty() && edt_harga.isNotEmpty()
            && edt_stok.isNotEmpty() &&  edt_harga_grosir.isNotEmpty() &&
            edt_jumlah_grosir.isNotEmpty() && edt_diskon.isNotEmpty()

        ) {
            loading(true)
            val f: File = File(cacheDir, "foto")
            f.createNewFile()
            //Convert bitmap
            //ini bitmapnya

            val reqFile = RequestBody.create(MediaType.parse("image/*"), data)
            val body = MultipartBody.Part.createFormData("foto", f.name, reqFile)

            val harga_grosir: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                edt_harga_grosir
            )
            val jumlah_grosir: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                edt_jumlah_grosir
            )
            val diskon: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                edt_diskon
            )
            val modal: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                edt_modal
            )
            val nama: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                edt_nama
            )

            val deskripsi: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                edt_deskripsi
            )

            val harga: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                edt_harga
            )

            val stok: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                edt_stok
            )

            val id: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                produkmodel!!.id.toString()
            )

            val oldImage: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                produkmodel!!.foto.toString()
            )

            api.update_produk(body,nama,deskripsi,harga,stok,id,oldImage,jumlah_grosir, harga_grosir, diskon, modal).enqueue(object : Callback<PostProdukResponse>{
                override fun onResponse(
                    call: Call<PostProdukResponse>,
                    response: Response<PostProdukResponse>
                ) {
                    try {
                        if (response.isSuccessful){
                            if (response.body()!!.status == 1){
                                loading(false)
                                toast("berhasil di update")
                                finish()
                                DetailProdukActivity.view.finish()
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

        } else {
            Snackbar.make(view, "Jangan kosongi kolom", 3000).show()
        }

    }

    private fun update_produk_foto(view: View) {

        val edt_nama = binding.edtnamaproduk.text.toString().trim()
        val edt_deskripsi = binding.edtdeskripsi.text.toString().trim()
        val edt_harga = binding.edtharga.text.toString().trim()
        val edt_stok = binding.edtstok.text.toString().trim()
        val edt_harga_grosir = binding.edthargagrosir.text.toString().trim()
        val edt_jumlah_grosir = binding.edtjumlahgrosir.text.toString().trim()
        val edt_diskon = binding.edtdiskon.text.toString().trim()
        val edt_modal = binding.edtmodal.text.toString().trim()


        if (edt_nama.isNotEmpty() && edt_deskripsi.isNotEmpty() && edt_harga.isNotEmpty()
            && edt_stok.isNotEmpty() && edt_harga_grosir.isNotEmpty() && edt_jumlah_grosir.isNotEmpty()
            && edt_diskon.isNotEmpty() && edt_modal.isNotEmpty()
        ) {
            loading(true)

            val nama: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                edt_nama
            )

            val harga_grosir: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                edt_harga_grosir
            )
            val jumlah_grosir: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                edt_jumlah_grosir
            )
            val diskon: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                edt_diskon
            )
            val modal: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                edt_modal
            )

            val deskripsi: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                edt_deskripsi
            )

            val harga: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                edt_harga
            )

            val stok: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                edt_stok
            )

            val id: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                produkmodel!!.id.toString()
            )

            val oldImage: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                produkmodel!!.foto.toString()
            )

            api.update_produk_foto(nama,deskripsi,harga,stok,id,oldImage,jumlah_grosir, harga_grosir, diskon, modal).enqueue(object : Callback<PostProdukResponse>{
                override fun onResponse(
                    call: Call<PostProdukResponse>,
                    response: Response<PostProdukResponse>
                ) {
                    try {
                        if (response.isSuccessful){
                            if (response.body()!!.status == 1){
                                loading(false)
                                toast("berhasil di update")
                                finish()
                                DetailProdukActivity.view.finish()
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

        } else {
            Snackbar.make(view, "Jangan kosongi kolom", 3000).show()
        }

    }

    private fun pilihfile() {
        //Intent to pick image
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_PICK_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_PICK_IMAGE) {
                filePath = data?.data
                Picasso.get().load(filePath).fit().centerCrop().into(binding.imgfoto)
                convert()
            }
        }
    }

    fun convert() {
        val bmp = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
        val baos = ByteArrayOutputStream()
        bmp.compress(Bitmap.CompressFormat.JPEG, 25, baos)
        data = baos.toByteArray()

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