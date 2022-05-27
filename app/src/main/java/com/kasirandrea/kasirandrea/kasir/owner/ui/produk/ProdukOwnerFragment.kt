package com.kasirandrea.kasirandrea.kasir.owner.ui.produk

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.kasirandrea.kasirandrea.R
import com.kasirandrea.kasirandrea.databinding.FragmentProdukOwnerBinding
import com.kasirandrea.kasirandrea.kasir.adapter.ProdukAdapter
import com.kasirandrea.kasirandrea.kasir.model.produk.ProdukModel
import com.kasirandrea.kasirandrea.kasir.model.produk.ProdukResponse
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
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_produk_owner,container,false)
        binding.lifecycleOwner = this


        binding.btntambah.setOnClickListener {
            startActivity<TambahProdukActivity>()
        }

        binding.rvproduk.layoutManager = LinearLayoutManager(requireContext().applicationContext)
        binding.rvproduk.setHasFixedSize(true)
        (binding.rvproduk.layoutManager as LinearLayoutManager).orientation =
            LinearLayoutManager.VERTICAL



        return binding.root

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
    override fun onStart() {
        super.onStart()
        getproduk()
    }

}