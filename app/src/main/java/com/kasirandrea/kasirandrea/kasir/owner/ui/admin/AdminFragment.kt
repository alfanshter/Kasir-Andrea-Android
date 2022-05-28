package com.kasirandrea.kasirandrea.kasir.owner.ui.admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.kasirandrea.kasirandrea.R
import com.kasirandrea.kasirandrea.databinding.ActivityTambahAdminBinding
import com.kasirandrea.kasirandrea.databinding.FragmentAdminBinding
import com.kasirandrea.kasirandrea.databinding.FragmentProdukOwnerBinding
import com.kasirandrea.kasirandrea.kasir.adapter.AdminAdapter
import com.kasirandrea.kasirandrea.kasir.adapter.ProdukAdapter
import com.kasirandrea.kasirandrea.kasir.model.admin.AdminModel
import com.kasirandrea.kasirandrea.kasir.model.admin.AdminResponse
import com.kasirandrea.kasirandrea.kasir.model.produk.ProdukModel
import com.kasirandrea.kasirandrea.kasir.model.produk.ProdukResponse
import com.kasirandrea.kasirandrea.kasir.owner.ui.produk.DetailProdukActivity
import com.kasirandrea.kasirandrea.kasir.webservice.ApiClient
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.support.v4.startActivity
import org.jetbrains.anko.support.v4.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdminFragment : Fragment(),AnkoLogger {
    lateinit var binding : FragmentAdminBinding
    private lateinit var mAdapter: AdminAdapter

    var api = ApiClient.instance()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_admin,container,false)
        binding.lifecycleOwner = this

        binding.btnTambah.setOnClickListener {
            startActivity<TambahAdminActivity>()
        }


        binding.rvadmin.layoutManager = LinearLayoutManager(requireContext().applicationContext)
        binding.rvadmin.setHasFixedSize(true)
        (binding.rvadmin.layoutManager as LinearLayoutManager).orientation =
            LinearLayoutManager.VERTICAL
        return  binding.root
    }

    fun getadmin()
    {
        api.get_admin()
            .enqueue(object : Callback<AdminResponse> {
                override fun onResponse(
                    call: Call<AdminResponse>,
                    response: Response<AdminResponse>
                ) {
                    try {
                        if (response.isSuccessful) {
                            val notesList = mutableListOf<AdminModel>()
                            val data = response.body()
                            for (hasil in data!!.data!!) {
                                notesList.add(hasil)
                                mAdapter = AdminAdapter(notesList)
                                binding.rvadmin.adapter = mAdapter

                                mAdapter.setDialog(object : AdminAdapter.Dialog{
                                    override fun onClick(position: Int, AdminModel: AdminModel) {
                                        val gson = Gson()
                                        val noteJson = gson.toJson(AdminModel)

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
//                                    getsearch(p0,notesList)
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

                override fun onFailure(call: Call<AdminResponse>, t: Throwable) {
                    info { "dinda ${t.message}" }
                }

            })

    }

    override fun onStart() {
        super.onStart()
        getadmin()
    }

}