package com.kasirandrea.kasirandrea.kasir.admin.keranjang

import android.app.ProgressDialog
import android.graphics.Color
import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.andrefrsousa.superbottomsheet.SuperBottomSheetFragment
import com.andrefrsousa.superbottomsheet.databinding.SuperBottomSheetDialogBinding
import com.google.gson.Gson
import com.kasirandrea.kasirandrea.R
import com.kasirandrea.kasirandrea.databinding.ActivityProdukAdminBinding
import com.kasirandrea.kasirandrea.databinding.FragmentKeranjangBinding
import com.kasirandrea.kasirandrea.kasir.adapter.KeranjangAdapter
import com.kasirandrea.kasirandrea.kasir.admin.datapembeli.DataPembeliActivity
import com.kasirandrea.kasirandrea.kasir.admin.produk.OrderAdminActivity
import com.kasirandrea.kasirandrea.kasir.model.keranjang.KeranjangModel
import com.kasirandrea.kasirandrea.kasir.model.keranjang.KeranjangResponse
import com.kasirandrea.kasirandrea.kasir.model.keranjang.TotalBelanjaResponse
import com.kasirandrea.kasirandrea.kasir.model.post.PostKeranjang
import com.kasirandrea.kasirandrea.kasir.model.produk.PostProdukResponse
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


class KeranjangFragment : SuperBottomSheetFragment(),AnkoLogger {
    lateinit var binding : FragmentKeranjangBinding
    private lateinit var mAdapter: KeranjangAdapter
    var api = ApiClient.instance()
    lateinit var progressDialog: ProgressDialog
    lateinit var sessionManager: SessionManager
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_keranjang,container,false)
        binding.lifecycleOwner = this
        progressDialog = ProgressDialog(requireActivity())
        sessionManager = SessionManager(requireContext().applicationContext)
        binding.rvKeranjang.layoutManager = LinearLayoutManager(requireContext().applicationContext)
        binding.rvKeranjang.setHasFixedSize(true)
        (binding.rvKeranjang.layoutManager as LinearLayoutManager).orientation =
            LinearLayoutManager.VERTICAL


        return  binding.root

    }

    override fun onStart() {
        super.onStart()
        get_keranjang()
        total_belanja()

    }

    fun get_keranjang(){
        api.get_keranjang(sessionManager.getid_user()!!)
            .enqueue(object : Callback<KeranjangResponse> {
                override fun onResponse(
                    call: Call<KeranjangResponse>,
                    response: Response<KeranjangResponse>
                ) {
                    try {
                        if (response.isSuccessful) {
                            val notesList = mutableListOf<KeranjangModel>()
                            val data = response.body()
                            for (hasil in data!!.data!!) {
                                notesList.add(hasil)
                                mAdapter = KeranjangAdapter(notesList)
                                binding.rvKeranjang.adapter = mAdapter

                                mAdapter.setDialog(object : KeranjangAdapter.Dialog{
                                    override fun onClick(position: Int, KeranjangModel: KeranjangModel) {
                                        val gson = Gson()
                                        val noteJson = gson.toJson(KeranjangModel)
//                                        startActivity<OrderAdminActivity>("produk" to noteJson)

                                    }

                                    override fun onTambah(
                                        position: Int,
                                        KeranjangModel: KeranjangModel
                                    ) {
                                        tambah_keranjang(KeranjangModel.idProduk!!,
                                            KeranjangModel.harga!!
                                        )
                                    }

                                    override fun onKurang(
                                        position: Int,
                                        KeranjangModel: KeranjangModel
                                    ) {
                                        kurang_keranjang(
                                            KeranjangModel.id!!, KeranjangModel.jumlah!!
                                        )
                                    }

                                    override fun onHapus(
                                        position: Int,
                                        KeranjangModel: KeranjangModel
                                    ) {
                                        hapus_keranjang(KeranjangModel.id!!)
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

                override fun onFailure(call: Call<KeranjangResponse>, t: Throwable) {
                    info { "dinda ${t.message}" }
                }

            })

    }

    fun tambah_keranjang(id_produk: Int, harga : Int){
        loading(true)
        api.tambah_keranjang(PostKeranjang(id_produk,harga,1,sessionManager.getid_user())).enqueue(object : Callback<PostProdukResponse> {
            override fun onResponse(
                call: Call<PostProdukResponse>,
                response: Response<PostProdukResponse>
            ) {
                try {
                    if (response.isSuccessful){
                        if (response.body()!!.status == 1){
                            loading(false)
                            onStart()
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

    }

    fun kurang_keranjang(id: Int, jumlah : Int){
        loading(true)
        api.kurang_keranjang(id,jumlah).enqueue(object : Callback<PostProdukResponse> {
            override fun onResponse(
                call: Call<PostProdukResponse>,
                response: Response<PostProdukResponse>
            ) {
                try {
                    if (response.isSuccessful){
                        if (response.body()!!.status == 1){
                            loading(false)
                            onStart()
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

    }

    fun hapus_keranjang(id: Int){
        loading(true)
        api.hapus_keranjang(id).enqueue(object : Callback<PostProdukResponse> {
            override fun onResponse(
                call: Call<PostProdukResponse>,
                response: Response<PostProdukResponse>
            ) {
                try {
                    if (response.isSuccessful){
                        if (response.body()!!.status == 1){
                            loading(false)
                            onStart()
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

    fun total_belanja(){
        api.total_belanja(sessionManager.getid_user()!!).enqueue(object : Callback<TotalBelanjaResponse> {
            override fun onResponse(
                call: Call<TotalBelanjaResponse>,
                response: Response<TotalBelanjaResponse>
            ) {
                try {
                    if (response.isSuccessful){
                        val formatter: NumberFormat = DecimalFormat("#,###")
                        val myNumber = response.body()!!.totalBelanja
                        val harga: String = formatter.format(myNumber)
                        binding.txttotalharga.text = "Rp. ${harga}"
                        binding.btnproses.setOnClickListener {
                            proses(response.body()!!.totalBelanja!!)
                        }
                    }
                }catch (e :Exception){
                    loading(false)
                    info { "dinda cath ${e.message}" }

                }

            }

            override fun onFailure(call: Call<TotalBelanjaResponse>, t: Throwable) {
                loading(false)
                info { "dinda failure ${t.message}" }
                toast("silahkan hubungi developer")

            }

        })

    }

    fun proses(total_belanja : Int){
        startActivity<DataPembeliActivity>("total_belanja" to total_belanja)
    }
    override fun getCornerRadius() = requireContext().resources.getDimension(R.dimen.super_bottom_sheet_radius)

    override fun getStatusBarColor() = Color.BLACK

    override fun isSheetAlwaysExpanded(): Boolean = true



}