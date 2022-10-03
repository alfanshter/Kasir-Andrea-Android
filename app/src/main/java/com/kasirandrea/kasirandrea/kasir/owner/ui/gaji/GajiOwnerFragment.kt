package com.kasirandrea.kasirandrea.kasir.owner.ui.gaji

import android.app.ProgressDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.databinding.DataBindingUtil
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.kasirandrea.kasirandrea.R
import com.kasirandrea.kasirandrea.databinding.FragmentGajiOwnerBinding
import com.kasirandrea.kasirandrea.databinding.FragmentProdukOwnerBinding
import com.kasirandrea.kasirandrea.kasir.adapter.ProdukAdapter
import com.kasirandrea.kasirandrea.kasir.model.gaji.GajiResponse
import com.kasirandrea.kasirandrea.kasir.model.produk.PostProdukResponse
import com.kasirandrea.kasirandrea.kasir.webservice.ApiClient
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.support.v4.startActivity
import org.jetbrains.anko.support.v4.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GajiOwnerFragment : Fragment(),AnkoLogger {
    lateinit var progressDialog: ProgressDialog

    lateinit var binding : FragmentGajiOwnerBinding
    var api = ApiClient.instance()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_gaji_owner,container,false)
        binding.lifecycleOwner = this
        progressDialog = ProgressDialog(requireActivity())

        binding.btnubah.setOnClickListener {
            binding.edtgajipokok.isEnabled = true
            binding.edtbonus.isEnabled = true
            binding.lnedit.visibility = View.VISIBLE

        }

        binding.btnsimpan.setOnClickListener {
            val gaji = binding.edtgajipokok.text.toString().trim()
            val bonus = binding.edtbonus.text.toString().trim()
            if (gaji.isNotEmpty() && bonus.isNotEmpty()){
                setgaji(gaji.toInt(),bonus.toInt())
            }else{
                Snackbar.make(it,"jangan kosongi kolom",3000).show()
            }
        }


        binding.btnbatalkan.setOnClickListener {
            onStart()
            binding.lnedit.visibility = View.GONE

        }
        return  binding.root
    }

    override fun onStart() {
        super.onStart()
        binding.edtgajipokok.isEnabled = false
        binding.edtbonus.isEnabled = false
        binding.lnedit.visibility = View.GONE


        getgaji()
    }

    fun setgaji(gaji : Int, bonus : Int){
        loading(true)
        api.set_gaji(gaji, bonus)
            .enqueue(object : Callback<PostProdukResponse> {
                override fun onResponse(
                    call: Call<PostProdukResponse>,
                    response: Response<PostProdukResponse>
                ) {
                    try {
                        if (response.isSuccessful) {
                            loading(false)
                            if (response.body()!!.status == 1){
                                toast("Gaji berhasil di tambah")
                                onStart()
                            }else{

                            }
                        } else {
                            loading(false)
                            toast("gagal mendapatkan response")
                        }
                    } catch (e: Exception) {
                        loading(false)
                        info { "dinda ${e.message}" }
                    }
                }

                override fun onFailure(call: Call<PostProdukResponse>, t: Throwable) {
                    info { "dinda ${t.message}" }
                    loading(false)
                    toast("kesalahan jaringan")
                }

            })

    }

    fun getgaji()
    {
        api.get_gaji()
            .enqueue(object : Callback<GajiResponse> {
                override fun onResponse(
                    call: Call<GajiResponse>,
                    response: Response<GajiResponse>
                ) {
                    try {
                        if (response.isSuccessful) {
                            binding.edtgajipokok.setText(response.body()!!.data!!.gaji.toString())
                            binding.edtbonus.setText(response.body()!!.data!!.bonus.toString())
                        } else {
                            toast("gagal mendapatkan response")
                        }
                    } catch (e: Exception) {
                        info { "dinda ${e.message}" }
                    }
                }

                override fun onFailure(call: Call<GajiResponse>, t: Throwable) {
                    info { "dinda ${t.message}" }
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