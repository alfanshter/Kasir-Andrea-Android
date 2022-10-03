package com.kasirandrea.kasirandrea.kasir.admin.ongkir

import android.app.Activity
import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.kasirandrea.kasirandrea.R
import com.kasirandrea.kasirandrea.databinding.ActivityOngkirBinding
import com.kasirandrea.kasirandrea.kasir.adapter.KurirAdapter
import com.kasirandrea.kasirandrea.kasir.admin.datapembeli.DataPembeliActivity
import com.kasirandrea.kasirandrea.kasir.admin.detailpesanan.DetailPesananActivity
import com.kasirandrea.kasirandrea.kasir.admin.produk.ProdukAdminActivity
import com.kasirandrea.kasirandrea.kasir.model.pesanan.PesananResponse
import com.kasirandrea.kasirandrea.kasir.model.post.PostPesanan
import com.kasirandrea.kasirandrea.kasir.model.rajaongkir.*
import com.kasirandrea.kasirandrea.kasir.session.SessionManager
import com.kasirandrea.kasirandrea.kasir.webservice.ApiClient
import com.kasirandrea.kasirandrea.kasir.webservice.ApiRajaOngkir
import com.kasirandrea.kasirandrea.kasir.webservice.Constant
import org.jetbrains.anko.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OngkirActivity : AppCompatActivity(), AnkoLogger {
    lateinit var binding: ActivityOngkirBinding
    var api = ApiRajaOngkir.instance()
    var api_kasir = ApiClient.instance()
    var province: String? = null
    var province_id: String? = null
    private lateinit var mAdapter: KurirAdapter
    var city_id: String? = null
    var city_name: String? = null

    var city_name_tujuan: String? = null
    var city_id_tujuan: String? = null
    var province_tujuan: String? = null
    var province_id_tujuan: String? = null

    var nama : String? = null
    var telepon : String? = null
    var alamat : String? = null
    var total_belanja : Int? = null
    var modal : Int? = null
    lateinit var sessionManager: SessionManager
    companion object{
        var kurir: String? = null
        var activity : Activity? = null

    }

    lateinit var progressDialog: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_ongkir)
        binding.lifecycleOwner = this
        sessionManager = SessionManager(this)
        activity = this
        val bundle: Bundle? = intent.extras
        nama = bundle!!.getString("nama")
        telepon = bundle.getString("telepon")
        alamat = bundle.getString("alamat")
        total_belanja = bundle.getInt("total_belanja")
        modal = bundle.getInt("modal")

        progressDialog = ProgressDialog(this)
        binding.rvcekongkir.layoutManager = LinearLayoutManager(this)
        binding.rvcekongkir.setHasFixedSize(true)
        (binding.rvcekongkir.layoutManager as LinearLayoutManager).orientation =
            LinearLayoutManager.VERTICAL

        getprovinsi()
        getprovinsi_tujuan()
        pilih_kurir()
        binding.btncekongkir.setOnClickListener {
            val berat = binding.edtberat.text.toString().trim()
            if (city_id!=null && province_id!=null && berat.isNotEmpty() && kurir!=null){
                cek_ongkir(it,berat)
            }else{
                Snackbar.make(it,"Jangan kosongi kolom",3000).show()
            }
        }

        binding.btnproses.setOnClickListener {
            val ongkir = binding.edtongkirmanual.text.toString().trim()
            if (kurir =="manual"){
                val kurirmanual = binding.edtnamakurir.text.toString().trim()
                if (kurirmanual.isNotEmpty()){
                    tambah_transaksi(ongkir.toInt(),kurirmanual)
                }else{
                    Snackbar.make(it,"Jangan kosongi kolom nama kurir",3000).show()
                }
            }else{
                info { "dinda ongkir: $ongkir kurir: $kurir nama:$nama totalbelanja:$total_belanja telepon:$telepon iduser:${sessionManager.getid_user()} alamat : $alamat" }
                tambah_transaksi(ongkir.toInt(), kurir.toString())
            }


        }
    }

    fun tambah_transaksi(ongkir : Int,kurir_name : String){
        loading(true)
        PostPesanan()
        api_kasir.tambah_transaksi(PostPesanan(nama,ongkir,total_belanja!! + ongkir,total_belanja!!,telepon,sessionManager.getid_user(),
            kurir_name,alamat,modal)).enqueue(object : Callback<PesananResponse>{
            override fun onResponse(
                call: Call<PesananResponse>,
                response: Response<PesananResponse>
            ) {
                try {
                    if (response.isSuccessful){
                        if (response.body()!!.status == 1){
                            loading(false)
                            val gson = Gson()
                            val noteJson = gson.toJson(response.body()!!.data)
                            startActivity<DetailPesananActivity>(
                                "pesanan" to  noteJson
                            )
                            OngkirActivity.activity!!.finish()
                            DataPembeliActivity.activity!!.finish()
                            ProdukAdminActivity.sheet.dismiss()
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

            override fun onFailure(call: Call<PesananResponse>, t: Throwable) {
                loading(false)
                info { "dinda failure ${t.message}" }
                toast("silahkan hubungi developer")

            }

        })

    }

    fun getprovinsi() {
        api.province(Constant.api_rajaongkir).enqueue(object : Callback<ProvinsiResponse> {
            override fun onFailure(call: Call<ProvinsiResponse>, t: Throwable) {
                info { "dinda ${t.message}" }
            }

            override fun onResponse(
                call: Call<ProvinsiResponse>,
                response: Response<ProvinsiResponse>
            ) {
                if (response.isSuccessful) {
                    val provinsi = ProvinsiResponse.Provinsi()
                    provinsi.province = "Provinsi"
                    provinsi.province_id = -1
                    var spResponse: MutableList<ProvinsiResponse.Provinsi> =
                        response.body()!!.rajaOngkir.results as MutableList<ProvinsiResponse.Provinsi>
                    spResponse.add(0, provinsi)
                    val adapter: ArrayAdapter<ProvinsiResponse.Provinsi> =
                        ArrayAdapter<ProvinsiResponse.Provinsi>(
                            this@OngkirActivity,
                            android.R.layout.simple_spinner_item,
                            spResponse
                        )
                    binding.spnprovinsiAsal.adapter = adapter
                    binding.spnprovinsiAsal.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            override fun onNothingSelected(parent: AdapterView<*>?) {
                            }

                            override fun onItemSelected(
                                parent: AdapterView<*>?,
                                view: View?,
                                position: Int,
                                id: Long
                            ) {
                                if (position == 0) {

                                }

                                if (position > 0) {
                                    province_id = spResponse[position].province_id.toString()
                                    province = spResponse[position].province
                                    getkota()
                                }
                            }

                        }

                } else {
                    toast("kesalahan jaringan")
                }
            }

        })
    }

    fun getkota() {
        api.city(Constant.api_rajaongkir, province_id!!).enqueue(object : Callback<CityResponse> {
            override fun onFailure(call: Call<CityResponse>, t: Throwable) {
                info { "dinda ${t.message}" }
            }

            override fun onResponse(
                call: Call<CityResponse>,
                response: Response<CityResponse>
            ) {
                if (response.isSuccessful) {
                    val city = CityResponse.City()
                    city.city_name = "Kabupaten/Kota"
                    city.city_id = -1
                    var spResponse: MutableList<CityResponse.City> =
                        response.body()!!.rajaOngkir.results as MutableList<CityResponse.City>
                    spResponse.add(0, city)
                    val adapter: ArrayAdapter<CityResponse.City> =
                        ArrayAdapter<CityResponse.City>(
                            this@OngkirActivity,
                            android.R.layout.simple_spinner_item,
                            spResponse
                        )
                    binding.spnkotaAsal.adapter = adapter
                    binding.spnkotaAsal.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            override fun onNothingSelected(parent: AdapterView<*>?) {
                            }

                            override fun onItemSelected(
                                parent: AdapterView<*>?,
                                view: View?,
                                position: Int,
                                id: Long
                            ) {
                                if (position == 0) {

                                }

                                if (position > 0) {
                                    city_id = spResponse[position].city_id.toString()
                                    city_name = spResponse[position].city_name
                                }
                            }

                        }

                } else {
                    toast("kesalahan jaringan")
                }
            }

        })
    }

    fun getprovinsi_tujuan() {
        api.province(Constant.api_rajaongkir).enqueue(object : Callback<ProvinsiResponse> {
            override fun onFailure(call: Call<ProvinsiResponse>, t: Throwable) {
                info { "dinda ${t.message}" }
            }

            override fun onResponse(
                call: Call<ProvinsiResponse>,
                response: Response<ProvinsiResponse>
            ) {
                if (response.isSuccessful) {
                    val provinsi = ProvinsiResponse.Provinsi()
                    provinsi.province = "Provinsi"
                    provinsi.province_id = -1
                    var spResponse: MutableList<ProvinsiResponse.Provinsi> =
                        response.body()!!.rajaOngkir.results as MutableList<ProvinsiResponse.Provinsi>
                    spResponse.add(0, provinsi)
                    val adapter: ArrayAdapter<ProvinsiResponse.Provinsi> =
                        ArrayAdapter<ProvinsiResponse.Provinsi>(
                            this@OngkirActivity,
                            android.R.layout.simple_spinner_item,
                            spResponse
                        )
                    binding.spnprovinsiTujuan.adapter = adapter
                    binding.spnprovinsiTujuan.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            override fun onNothingSelected(parent: AdapterView<*>?) {
                            }

                            override fun onItemSelected(
                                parent: AdapterView<*>?,
                                view: View?,
                                position: Int,
                                id: Long
                            ) {
                                if (position == 0) {

                                }

                                if (position > 0) {
                                    province_id_tujuan = spResponse[position].province_id.toString()
                                    province_tujuan = spResponse[position].province
                                    getkota_tujuan()
                                }
                            }

                        }

                } else {
                    toast("kesalahan jaringan")
                }
            }

        })
    }

    fun getkota_tujuan() {
        api.city(Constant.api_rajaongkir, province_id_tujuan!!).enqueue(object : Callback<CityResponse> {
            override fun onFailure(call: Call<CityResponse>, t: Throwable) {
                info { "dinda ${t.message}" }
            }

            override fun onResponse(
                call: Call<CityResponse>,
                response: Response<CityResponse>
            ) {
                if (response.isSuccessful) {
                    val city = CityResponse.City()
                    city.city_name = "Kabupaten/Kota"
                    city.city_id = -1
                    var spResponse: MutableList<CityResponse.City> =
                        response.body()!!.rajaOngkir.results as MutableList<CityResponse.City>
                    spResponse.add(0, city)
                    val adapter: ArrayAdapter<CityResponse.City> =
                        ArrayAdapter<CityResponse.City>(
                            this@OngkirActivity,
                            android.R.layout.simple_spinner_item,
                            spResponse
                        )
                    binding.spnkotaTujuan.adapter = adapter
                    binding.spnkotaTujuan.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            override fun onNothingSelected(parent: AdapterView<*>?) {
                            }

                            override fun onItemSelected(
                                parent: AdapterView<*>?,
                                view: View?,
                                position: Int,
                                id: Long
                            ) {
                                if (position == 0) {

                                }

                                if (position > 0) {
                                    city_id_tujuan = spResponse[position].city_id.toString()
                                    city_name_tujuan = spResponse[position].city_name
                                }
                            }

                        }

                } else {
                    toast("kesalahan jaringan")
                }
            }

        })
    }

    fun pilih_kurir() {
        val spnkurir = arrayOf("jne", "pos", "tiki","manual")
        val spinner = find<Spinner>(R.id.spnkurir)
        if (spinner != null) {
            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item, spnkurir
            )
            spinner.adapter = adapter

            spinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View, position: Int, id: Long
                ) {
                    kurir = spnkurir[position]
                    if (kurir =="manual"){
                        binding.txtnamakurir.visibility = View.VISIBLE
                        binding.edtnamakurir.visibility = View.VISIBLE
                        binding.lnongkir.visibility = View.GONE
                    }else{
                        binding.txtnamakurir.visibility = View.GONE
                        binding.edtnamakurir.visibility = View.GONE
                        binding.lnongkir.visibility = View.VISIBLE

                    }


                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }

        }

    }

    fun cek_ongkir(view : View,berat :String) {

        if (kurir == "manual"){
            Snackbar.make(view,"Tidak berlaku untuk kurir manual",3000).show()
        }else{
            loading(true)
            api.cek_ongkir(
                PostCekOngkir(
                    kurir,
                    city_id!!.toInt(),
                    city_id_tujuan!!.toInt(),
                    berat.toInt()
                ), Constant.api_rajaongkir
            ).enqueue(object : Callback<CekOngkirResponse> {
                override fun onResponse(call: Call<CekOngkirResponse>, response: Response<CekOngkirResponse>) {
                    try {
                        if (response.isSuccessful) {
                            loading(false)
                            val notesList = mutableListOf<CostsItem>()
                            val data = response.body()
                            for (hasil in data!!.rajaongkir!!.results!![0]!!.costs!!) {
                                notesList.add(hasil!!)
                                mAdapter = KurirAdapter(notesList,this@OngkirActivity)
                                binding.rvcekongkir.adapter = mAdapter

                                mAdapter.setDialog(object : KurirAdapter.Dialog{
                                    override fun onClick(position: Int, CostsItem: CostsItem) {
                                        binding.edtongkirmanual.setText("${CostsItem.cost!![0]!!.value!!}")
                                    }

                                })
                                mAdapter.notifyDataSetChanged()
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

                override fun onFailure(call: Call<CekOngkirResponse>, t: Throwable) {
                    loading(false)
                    info { "dinda ${t.message}" }
                    Snackbar.make(view, "Silahkan coba lagi", Snackbar.LENGTH_SHORT).show()

                }

            })

        }

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