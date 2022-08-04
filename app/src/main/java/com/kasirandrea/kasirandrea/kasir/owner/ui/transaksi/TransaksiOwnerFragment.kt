package com.kasirandrea.kasirandrea.kasir.owner.ui.transaksi

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.kasirandrea.kasirandrea.R
import com.kasirandrea.kasirandrea.databinding.ActivityListPesananBinding
import com.kasirandrea.kasirandrea.databinding.FragmentTransaksiOwnerBinding
import com.kasirandrea.kasirandrea.kasir.adapter.listpesanan.ListPesananAdapter
import com.kasirandrea.kasirandrea.kasir.admin.detailpesanan.DetailPesananActivity
import com.kasirandrea.kasirandrea.kasir.model.penghasilan.PenghasilanResponse
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
import java.text.DecimalFormat
import java.text.NumberFormat

class TransaksiOwnerFragment : Fragment(),AnkoLogger {
    lateinit var sessionManager: SessionManager

    private lateinit var mAdapter: ListPesananAdapter
    var api = ApiClient.instance()

    lateinit var binding : FragmentTransaksiOwnerBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_transaksi_owner,container,false)
        binding.lifecycleOwner = this

        sessionManager = SessionManager(requireContext().applicationContext)
        binding.rvpesanan.layoutManager = LinearLayoutManager(requireContext().applicationContext)
        binding.rvpesanan.setHasFixedSize(true)
        (binding.rvpesanan.layoutManager as LinearLayoutManager).orientation =
            LinearLayoutManager.VERTICAL

        return binding.root

    }

    override fun onStart() {
        super.onStart()
        get_keranjang()
        penghasilan()
    }

    fun penghasilan(){
        api.penghasilan_bulanan().enqueue(object : Callback<PenghasilanResponse> {
            override fun onResponse(
                call: Call<PenghasilanResponse>,
                response: Response<PenghasilanResponse>
            ) {
                try {
                    if (response.isSuccessful) {
                        val notesList = mutableListOf<PesananModel>()
                        val data = response.body()
                        val formatter: NumberFormat = DecimalFormat("#,###")
                        binding.txtpenghasilan.text = "Rp. ${formatter.format(data!!.harga)}"
                        binding.txtmodal.text = "Rp. ${formatter.format(data.modal)}"

                    } else {
                        toast("gagal mendapatkan response")
                    }
                } catch (e: Exception) {
                    info { "dinda ${e.message}" }
                }
            }

            override fun onFailure(call: Call<PenghasilanResponse>, t: Throwable) {
                info { "dinda ${t.message}" }
            }

        })

    }

    fun get_keranjang(){
        api.get_pesanan_owner().enqueue(object : Callback<ListPesananResponse> {
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

    fun pesanan_selesai(){

    }
}