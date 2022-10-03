package com.kasirandrea.kasirandrea.kasir.admin.listpesanan

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.kasirandrea.kasirandrea.R
import com.kasirandrea.kasirandrea.databinding.ActivityListPesananBinding
import com.kasirandrea.kasirandrea.kasir.adapter.listpesanan.ListPesananAdapter
import com.kasirandrea.kasirandrea.kasir.admin.detailpesanan.DetailPesananActivity
import com.kasirandrea.kasirandrea.kasir.model.pesanan.ListPesananResponse
import com.kasirandrea.kasirandrea.kasir.model.pesanan.PesananModel
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

class ListPesananActivity : AppCompatActivity(),AnkoLogger {
    lateinit var binding: ActivityListPesananBinding
    lateinit var sessionManager: SessionManager

    private lateinit var mAdapter: ListPesananAdapter
    var api = ApiClient.instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_list_pesanan)
        binding.lifecycleOwner = this

        binding.btnclose.setOnClickListener {
            finish()
        }

        sessionManager = SessionManager(this)
        binding.rvpesanan.layoutManager = LinearLayoutManager(this)
        binding.rvpesanan.setHasFixedSize(true)
        (binding.rvpesanan.layoutManager as LinearLayoutManager).orientation =
            LinearLayoutManager.VERTICAL

    }

    override fun onStart() {
        super.onStart()
        get_keranjang()
    }

    fun get_keranjang(){
        api.get_pesanan_id(sessionManager.getid_user()!!)
            .enqueue(object : Callback<ListPesananResponse> {
                override fun onResponse(
                    call: Call<ListPesananResponse>,
                    response: Response<ListPesananResponse>
                ) {
                    try {
                        if (response.isSuccessful) {
                            val notesList = mutableListOf<PesananModel>()
                            val data = response.body()
                            for (hasil in data!!.data!!) {
                                notesList.add(hasil)
                                mAdapter = ListPesananAdapter(notesList)
                                binding.rvpesanan.adapter = mAdapter

                                mAdapter.setDialog(object : ListPesananAdapter.Dialog{
                                    override fun onClick(position: Int, PesananModel: PesananModel) {
                                        val gson = Gson()
                                        val noteJson = gson.toJson(PesananModel)
                                        startActivity<DetailPesananActivity>(
                                            "pesanan" to  noteJson
                                        )

                                    }
                                    

                                })

                                binding.btnscroll.setOnClickListener {
                                    val layoutManager = binding.rvpesanan.layoutManager
                                    layoutManager!!.smoothScrollToPosition(binding.rvpesanan, null,0)
                                }
                                mAdapter.notifyDataSetChanged()
                            }

                            binding.srcPesanan.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
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

                override fun onFailure(call: Call<ListPesananResponse>, t: Throwable) {
                    info { "dinda ${t.message}" }
                }

            })

    }

    private fun getsearch(searchTerm: String?,notelist : MutableList<PesananModel>) {
        notelist.clear()
        if (!TextUtils.isEmpty(searchTerm)) {
            val serchtext: String =
                searchTerm!!.substring(0, 1).toUpperCase() + searchTerm.substring(1)

            api.search_pesanan_id(sessionManager.getid_user()!!,serchtext).enqueue(object : Callback<ListPesananResponse>{
                override fun onResponse(
                    call: Call<ListPesananResponse>,
                    response: Response<ListPesananResponse>
                ) {
                    try {
                        if (response.isSuccessful) {
                            val notesList = mutableListOf<PesananModel>()
                            val data = response.body()
                            for (hasil in data!!.data!!) {
                                notesList.add(hasil)
                                mAdapter = ListPesananAdapter(notesList)
                                binding.rvpesanan.adapter = mAdapter

                                mAdapter.setDialog(object : ListPesananAdapter.Dialog{
                                    override fun onClick(position: Int, PesananModel: PesananModel) {
                                        val gson = Gson()
                                        val noteJson = gson.toJson(PesananModel)
                                        startActivity<DetailPesananActivity>(
                                            "pesanan" to noteJson
                                        )

                                    }

                                })
                                mAdapter.notifyDataSetChanged()
                            }

                            binding.srcPesanan.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
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

                override fun onFailure(call: Call<ListPesananResponse>, t: Throwable) {

                }

            })


            val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(
                this,
                RecyclerView.VERTICAL,
                false
            )

        } else {
            notelist.clear()
            api.get_pesanan_owner()
                .enqueue(object : Callback<ListPesananResponse> {
                    override fun onResponse(
                        call: Call<ListPesananResponse>,
                        response: Response<ListPesananResponse>
                    ) {
                        try {
                            if (response.isSuccessful) {
                                val notesList = mutableListOf<PesananModel>()
                                val data = response.body()
                                for (hasil in data!!.data!!) {
                                    notesList.add(hasil)
                                    mAdapter = ListPesananAdapter(notesList)
                                    binding.rvpesanan.adapter = mAdapter

                                    mAdapter.setDialog(object : ListPesananAdapter.Dialog{
                                        override fun onClick(position: Int, PesananModel: PesananModel) {
                                            val gson = Gson()
                                            val noteJson = gson.toJson(PesananModel)
                                            startActivity<DetailPesananActivity>(
                                                "pesanan" to noteJson
                                            )


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

                    override fun onFailure(call: Call<ListPesananResponse>, t: Throwable) {
                        info { "dinda ${t.message}" }
                    }

                })
        }
    }
}