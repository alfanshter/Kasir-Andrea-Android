package com.kasirandrea.kasirandrea.kasir.owner.ui.gaji

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.kasirandrea.kasirandrea.R
import com.kasirandrea.kasirandrea.databinding.ActivityGajiAdminBinding
import com.kasirandrea.kasirandrea.databinding.ActivityTambahProdukBinding
import com.kasirandrea.kasirandrea.kasir.adapter.GajiAdminAdapter
import com.kasirandrea.kasirandrea.kasir.admin.UtamaActivity
import com.kasirandrea.kasirandrea.kasir.model.admin.AdminModel
import com.kasirandrea.kasirandrea.kasir.model.gaji.*
import com.kasirandrea.kasirandrea.kasir.model.produk.PostProdukResponse
import com.kasirandrea.kasirandrea.kasir.owner.OwnerMenuActivity
import com.kasirandrea.kasirandrea.kasir.session.SessionManager
import com.kasirandrea.kasirandrea.kasir.webservice.ApiClient
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class GajiAdminActivity : AppCompatActivity(),AnkoLogger {
    var api = ApiClient.instance()
    lateinit var binding: ActivityGajiAdminBinding
    lateinit var progressDialog: ProgressDialog
    var adminmodel : AdminModel? = null
    private lateinit var mAdapter: GajiAdminAdapter

    lateinit var sessionManager: SessionManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_gaji_admin)
        binding.lifecycleOwner = this
        sessionManager = SessionManager(this)
        progressDialog = ProgressDialog(this)

        binding.rvgaji.layoutManager = LinearLayoutManager(this)
        binding.rvgaji.setHasFixedSize(true)
        (binding.rvgaji.layoutManager as LinearLayoutManager).orientation =
            LinearLayoutManager.VERTICAL


        val gson = Gson()
        adminmodel = gson.fromJson(intent.getStringExtra("adminmodel"), AdminModel::class.java)

    }

    override fun onStart() {
        super.onStart()
        getgaji()
        riwayat_gaji()

    }

    fun getgaji()
    {
        api.gaji_admin(adminmodel!!.id!!)
            .enqueue(object : Callback<GajiAdminResponse> {
                override fun onResponse(
                    call: Call<GajiAdminResponse>,
                    response: Response<GajiAdminResponse>
                ) {
                    try {
                        if (response.isSuccessful) {
                            val formatter: NumberFormat = DecimalFormat("#,###")
                            var gaji = response.body()!!.gaji
                            var bonus = response.body()!!.bonus
                            var jumlah_penjualan = response.body()!!.jumlahPenjualan
                            var total = gaji!! + (bonus!! * jumlah_penjualan!!)

                            val sdf = SimpleDateFormat("MMM y")
                            val currentDate = sdf.format(Date())
                            binding.txtbulan.text = currentDate

                            if (response.body()!!.status_gaji == 1){
                                binding.btnstatusgaji.text = "Status : Gaji Sudah Dibayarkan"
                            }else{
                                binding.btnstatusgaji.text = "Bayar Gaji"
                                binding.btnstatusgaji.setOnClickListener {
                                    val builder = AlertDialog.Builder(this@GajiAdminActivity)
                                    builder.setTitle("Gaji")
                                    builder.setMessage("bayar gaji karyawan ? ")
                                    builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                                        bayargaji(adminmodel!!.id!!,gaji,jumlah_penjualan,bonus,total,it)
                                    }

                                    builder.setNegativeButton(android.R.string.no) { dialog, which ->
                                        toast("tidak")
                                    }

                                    builder.show()

                                }
                            }
                            binding.txtgajipokok.text = "${formatter.format(response.body()!!.gaji)}"
                            binding.txtbonus.text = "${response.body()!!.jumlahPenjualan} X ${formatter.format(response.body()!!.bonus)}"
                            binding.txttotalpenghasilan.text = "${formatter.format(total)}"

                        } else {
                            toast("gagal mendapatkan response")
                        }
                    } catch (e: Exception) {
                        info { "dinda ${e.message}" }
                    }
                }

                override fun onFailure(call: Call<GajiAdminResponse>, t: Throwable) {
                    info { "dinda ${t.message}" }
                }

            })

    }

    private fun bayargaji(id_user: Int, gajipokok : Int, jumlah_penjualan : Int, bonus : Int, total_penghasilan: Int,view : View) {
        loading(true)
        api.bayar_gaji(PostBayarGaji(gajipokok,bonus,id_user,total_penghasilan,jumlah_penjualan)).enqueue(object : Callback<PostProdukResponse>{
            override fun onResponse(call: Call<PostProdukResponse>, response: Response<PostProdukResponse>) {
                try {
                    if (response.body()!!.status == 1) {
                        loading(false)
                        onStart()
                        toast("gaji sudah terbayarkan")

                    } else {
                        loading(false)
                        Snackbar.make(view, "coba lagi", Snackbar.LENGTH_SHORT).show()

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

    fun riwayat_gaji()
    {
        api.riwayat_gaji_admin(adminmodel!!.id!!)
            .enqueue(object : Callback<RiwayatGajiResponse> {
                override fun onResponse(
                    call: Call<RiwayatGajiResponse>,
                    response: Response<RiwayatGajiResponse>
                ) {
                    try {
                        if (response.isSuccessful) {
                            val notesList = mutableListOf<RiwayatGajiModel>()
                            val data = response.body()
                            if (data!!.data!!.isEmpty()){
                                binding.shimmmergaji.stopShimmer()
                                binding.shimmmergaji.visibility = View.GONE
                                binding.txtnogaji.visibility = View.VISIBLE
                                binding.rvgaji.visibility = View.GONE
                            }else{
                                binding.shimmmergaji.stopShimmer()
                                binding.shimmmergaji.visibility = View.GONE
                                binding.txtnogaji.visibility = View.GONE
                                binding.rvgaji.visibility = View.VISIBLE
                                for (hasil in data.data!!) {
                                    notesList.add(hasil)
                                    mAdapter = GajiAdminAdapter(notesList)
                                    binding.rvgaji.adapter = mAdapter
                                    mAdapter.notifyDataSetChanged()
                                }
                            }

                        } else {
                            toast("gagal mendapatkan response")
                        }
                    } catch (e: Exception) {
                        info { "dinda ${e.message}" }
                    }
                }

                override fun onFailure(call: Call<RiwayatGajiResponse>, t: Throwable) {
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