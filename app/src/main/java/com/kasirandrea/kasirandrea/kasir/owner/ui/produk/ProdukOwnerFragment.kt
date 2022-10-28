package com.kasirandrea.kasirandrea.kasir.owner.ui.produk

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.kasirandrea.kasirandrea.R
import com.kasirandrea.kasirandrea.databinding.FragmentProdukOwnerBinding
import com.kasirandrea.kasirandrea.kasir.adapter.ProdukAdapter
import com.kasirandrea.kasirandrea.kasir.auth.LoginActivity
import com.kasirandrea.kasirandrea.kasir.model.produk.PostProdukResponse
import com.kasirandrea.kasirandrea.kasir.model.produk.ProdukModel
import com.kasirandrea.kasirandrea.kasir.model.produk.ProdukResponse
import com.kasirandrea.kasirandrea.kasir.session.SessionManager
import com.kasirandrea.kasirandrea.kasir.webservice.ApiClient
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.support.v4.startActivity
import org.jetbrains.anko.support.v4.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProdukOwnerFragment : Fragment(),AnkoLogger {
    private lateinit var mAdapter: ProdukAdapter

    lateinit var binding : FragmentProdukOwnerBinding
    var api = ApiClient.instance()
    lateinit var progressDialog: ProgressDialog
    lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_produk_owner,container,false)
        binding.lifecycleOwner = this
        progressDialog = ProgressDialog(requireActivity())
        sessionManager = SessionManager(requireContext().applicationContext)

        getproduk()

        binding.btntambah.setOnClickListener {
            startActivity<TambahProdukActivity>()
        }

        binding.rvproduk.layoutManager = LinearLayoutManager(requireContext().applicationContext)
        binding.rvproduk.setHasFixedSize(true)
        (binding.rvproduk.layoutManager as LinearLayoutManager).orientation =
            LinearLayoutManager.VERTICAL

        binding.imgFoto.setOnClickListener {
            logout()
        }


        return binding.root

    }

    fun logout(){
        loading(true)
        api.logout().enqueue(object : Callback<PostProdukResponse>{
            override fun onResponse(
                call: Call<PostProdukResponse>,
                response: Response<PostProdukResponse>
            ) {
                if (response.isSuccessful){
                    if (response.body()!!.status == 1){
                        loading(false)
                        sessionManager.setLoginowner(false)
                        startActivity<LoginActivity>()
                        requireActivity().finish()
                    }
                }
            }

            override fun onFailure(call: Call<PostProdukResponse>, t: Throwable) {
                loading(false)
                info { "dinda ${t.message}" }
                toast("Silahkan coba lagi")
            }

        })
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
                                        startActivity<DetailProdukActivity>("produkmodel" to noteJson)

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
                        getproduk()
                    }
                }

                override fun onFailure(call: Call<ProdukResponse>, t: Throwable) {
                    getproduk()
                    toast("jaringan tidak stabil")
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
                                        startActivity<DetailProdukActivity>("produkmodel" to noteJson)

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

                }

            })


            val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(
                requireContext().applicationContext,
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
                                            startActivity<DetailProdukActivity>("produkmodel" to noteJson)

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