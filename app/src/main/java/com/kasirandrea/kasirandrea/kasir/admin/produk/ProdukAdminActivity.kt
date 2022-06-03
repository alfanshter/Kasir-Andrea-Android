package com.kasirandrea.kasirandrea.kasir.admin.produk

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.kasirandrea.kasirandrea.R
import com.kasirandrea.kasirandrea.databinding.ActivityDeailProduktBinding
import com.kasirandrea.kasirandrea.databinding.ActivityProdukAdminBinding
import com.kasirandrea.kasirandrea.kasir.adapter.ProdukAdapter
import com.kasirandrea.kasirandrea.kasir.model.produk.ProdukModel
import com.kasirandrea.kasirandrea.kasir.model.produk.ProdukResponse
import com.kasirandrea.kasirandrea.kasir.owner.ui.produk.DetailProdukActivity
import com.kasirandrea.kasirandrea.kasir.session.SessionManager
import com.kasirandrea.kasirandrea.kasir.webservice.ApiClient
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.support.v4.startActivity
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProdukAdminActivity : AppCompatActivity(),AnkoLogger {

    private lateinit var mAdapter: ProdukAdapter
    lateinit var binding : ActivityProdukAdminBinding
    var api = ApiClient.instance()
    lateinit var progressDialog: ProgressDialog
    lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_produk_admin)
        binding.lifecycleOwner = this
        binding.rvproduk.layoutManager = LinearLayoutManager(this)
        binding.rvproduk.setHasFixedSize(true)
        (binding.rvproduk.layoutManager as LinearLayoutManager).orientation =
            LinearLayoutManager.VERTICAL


    }

    fun getproduk()
    {
        api.getproduk()
            .enqueue(object : Callback<ProdukResponse> {
                override fun onResponse(
                    call: Call<ProdukResponse>,
                    response: Response<ProdukResponse>
                ) {
                    try {
                        if (response.isSuccessful) {
                            val notesList = mutableListOf<ProdukModel>()
                            val data = response.body()
                            for (hasil in data!!.data!!) {
                                notesList.add(hasil)
                                mAdapter = ProdukAdapter(notesList)
                                binding.rvproduk.adapter = mAdapter

                                mAdapter.setDialog(object : ProdukAdapter.Dialog{
                                    override fun onClick(position: Int, produkModel: ProdukModel) {
                                        val gson = Gson()
                                        val noteJson = gson.toJson(produkModel)
                                        startActivity<OrderAdminActivity>("produk" to noteJson)

                                    }

                                })
                                mAdapter.notifyDataSetChanged()
                            }

                            binding.srcProduk.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
                                override fun onQueryTextSubmit(p0: String?): Boolean {
                                    notesList.clear()
                                    return false
                                }

                                override fun onQueryTextChange(p0: String?): Boolean {
                                    getsearch(p0,notesList)
                                    return false
                                }

                            })
                        } else {
                            toast("gagal mendapatkan response")
                        }
                    } catch (e: Exception) {
                        info { "dinda ${e.message}" }
                    }
                }

                override fun onFailure(call: Call<ProdukResponse>, t: Throwable) {
                    info { "dinda ${t.message}" }
                }

            })

    }

    private fun getsearch(searchTerm: String?,notelist : MutableList<ProdukModel>) {
        notelist.clear()
        if (!TextUtils.isEmpty(searchTerm)) {
            val serchtext: String =
                searchTerm!!.substring(0, 1).toUpperCase() + searchTerm.substring(1)

            api.search_produk(serchtext).enqueue(object : Callback<ProdukResponse>{
                override fun onResponse(
                    call: Call<ProdukResponse>,
                    response: Response<ProdukResponse>
                ) {
                    try {
                        if (response.isSuccessful) {
                            val notesList = mutableListOf<ProdukModel>()
                            val data = response.body()
                            for (hasil in data!!.data!!) {
                                notesList.add(hasil)
                                mAdapter = ProdukAdapter(notesList)
                                binding.rvproduk.adapter = mAdapter

                                mAdapter.setDialog(object : ProdukAdapter.Dialog{
                                    override fun onClick(position: Int, produkModel: ProdukModel) {
                                        val gson = Gson()
                                        val noteJson = gson.toJson(produkModel)
                                        startActivity<OrderAdminActivity>("produk" to noteJson)
                                    }

                                })
                                mAdapter.notifyDataSetChanged()
                            }

                            binding.srcProduk.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
                                override fun onQueryTextSubmit(p0: String?): Boolean {
                                    notesList.clear()
                                    return false
                                }

                                override fun onQueryTextChange(p0: String?): Boolean {
                                    getsearch(p0,notesList)
                                    return false
                                }

                            })
                        } else {
                            toast("gagal mendapatkan response")
                        }
                    } catch (e: Exception) {
                        info { "dinda ${e.message}" }
                    }
                }

                override fun onFailure(call: Call<ProdukResponse>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            })


            val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(
                this,
                RecyclerView.VERTICAL,
                false
            )

        } else {
            notelist.clear()
            api.getproduk()
                .enqueue(object : Callback<ProdukResponse> {
                    override fun onResponse(
                        call: Call<ProdukResponse>,
                        response: Response<ProdukResponse>
                    ) {
                        try {
                            if (response.isSuccessful) {
                                val notesList = mutableListOf<ProdukModel>()
                                val data = response.body()
                                for (hasil in data!!.data!!) {
                                    notesList.add(hasil)
                                    mAdapter = ProdukAdapter(notesList)
                                    binding.rvproduk.adapter = mAdapter

                                    mAdapter.setDialog(object : ProdukAdapter.Dialog{
                                        override fun onClick(position: Int, produkModel: ProdukModel) {
                                            val gson = Gson()
                                            val noteJson = gson.toJson(produkModel)
                                            startActivity<OrderAdminActivity>("produk" to noteJson)

                                        }

                                    })
                                    mAdapter.notifyDataSetChanged()
                                }

                            } else {
                                toast("gagal mendapatkan response")
                            }
                        } catch (e: Exception) {
                            info { "dinda ${e.message}" }
                        }
                    }

                    override fun onFailure(call: Call<ProdukResponse>, t: Throwable) {
                        info { "dinda ${t.message}" }
                    }

                })
        }
    }


    override fun onStart() {
        super.onStart()
        getproduk()

    }
}