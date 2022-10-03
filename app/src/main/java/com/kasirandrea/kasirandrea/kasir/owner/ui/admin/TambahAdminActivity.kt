package com.kasirandrea.kasirandrea.kasir.owner.ui.admin

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
import com.kasirandrea.kasirandrea.R
import com.kasirandrea.kasirandrea.databinding.ActivityDeailProduktBinding
import com.kasirandrea.kasirandrea.databinding.ActivityTambahAdminBinding
import com.kasirandrea.kasirandrea.kasir.model.produk.PostProdukResponse
import com.kasirandrea.kasirandrea.kasir.webservice.ApiClient
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

class TambahAdminActivity : AppCompatActivity(),AnkoLogger {
    var api = ApiClient.instance()
    lateinit var binding : ActivityTambahAdminBinding

    //foto
    var filePath: Uri? = null
    var data: ByteArray? = null
    private val REQUEST_PICK_IMAGE = 1

    lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_tambah_admin)
        binding.lifecycleOwner = this
        progressDialog = ProgressDialog(this)
        binding.btnuploadfoto.setOnClickListener {
            pilihfile()
        }

        binding.btnsimpan.setOnClickListener {
            tambahadmin(it)
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
                binding.imgfoto.visibility = View.VISIBLE
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

    private fun tambahadmin(view: View) {

        val edtnama = binding.edtnamalengkap.text.toString().trim()
        val edtalamat = binding.edtalamat.text.toString().trim()
        val edttelepon = binding.edttelepon.text.toString().trim()
        val edtusername = binding.edtusername.text.toString().trim()
        val edtpassword = binding.edtpassword.text.toString().trim()
        val edtulangi_password = binding.edtulangiPassword.text.toString().trim()

        if (edtnama.isNotEmpty() && edtalamat.isNotEmpty() && edttelepon.isNotEmpty()
            && edtusername.isNotEmpty() && edtpassword.isNotEmpty() && edtulangi_password.isNotEmpty() && data!=null
        ) {
            if (edtulangi_password == edtpassword){
                loading(true)
                val f: File = File(cacheDir, "foto")
                f.createNewFile()
                //Convert bitmap
                //ini bitmapnya

                val reqFile = RequestBody.create(MediaType.parse("image/*"), data)
                val body = MultipartBody.Part.createFormData("foto", f.name, reqFile)

                val nama: RequestBody = RequestBody.create(
                    MediaType.parse("text/plain"),
                    edtnama
                )

                val alamat: RequestBody = RequestBody.create(
                    MediaType.parse("text/plain"),
                    edtalamat
                )

                val telepon: RequestBody = RequestBody.create(
                    MediaType.parse("text/plain"),
                    edttelepon
                )

                val username: RequestBody = RequestBody.create(
                    MediaType.parse("text/plain"),
                    edtusername
                )

                val password: RequestBody = RequestBody.create(
                    MediaType.parse("text/plain"),
                    edtpassword
                )

                val ulangi_password: RequestBody = RequestBody.create(
                    MediaType.parse("text/plain"),
                    edtulangi_password
                )


                api.tambah_admin(nama,alamat,telepon,username,body,username,password,ulangi_password).enqueue(object :
                    Callback<PostProdukResponse> {
                    override fun onResponse(
                        call: Call<PostProdukResponse>,
                        response: Response<PostProdukResponse>
                    ) {
                        try {
                            if (response.isSuccessful){
                                if (response.body()!!.status == 1){
                                    loading(false)
                                    toast("berhasil tambah admin")
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

            }else{
                Snackbar.make(view, "Password tidak sama", 3000).show()

            }

        } else {
            Snackbar.make(view, "Jangan kosongi kolom", 3000).show()
        }

    }

}