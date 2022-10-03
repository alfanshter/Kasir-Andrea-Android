package com.kasirandrea.kasirandrea.kasir.webservice

import com.kasirandrea.kasirandrea.kasir.LoginResponse
import com.kasirandrea.kasirandrea.kasir.model.admin.AdminResponse
import com.kasirandrea.kasirandrea.kasir.model.keranjang.KeranjangResponse
import com.kasirandrea.kasirandrea.kasir.model.keranjang.TotalBelanjaResponse
import com.kasirandrea.kasirandrea.kasir.model.post.PostKeranjang
import com.kasirandrea.kasirandrea.kasir.model.produk.PostProdukResponse
import com.kasirandrea.kasirandrea.kasir.model.produk.ProdukResponse
import com.kasirandrea.kasirandrea.kasir.model.rajaongkir.CekOngkirResponse
import com.kasirandrea.kasirandrea.kasir.model.rajaongkir.CityResponse
import com.kasirandrea.kasirandrea.kasir.model.rajaongkir.PostCekOngkir
import com.kasirandrea.kasirandrea.kasir.model.rajaongkir.ProvinsiResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiServiceRajaOngkir {

    @GET("province")
    fun province(@Header("key") key: String): Call<ProvinsiResponse>

    @GET("city")
    fun city(
        @Header("key") key: String,
        @Query("province") province: String
    ): Call<CityResponse>

    @Headers("Content-Type: application/json")
    @POST("cost")
    fun cek_ongkir(@Body post: PostCekOngkir, @Header("key") key: String): Call<CekOngkirResponse>

    //=======================PRODUK==================

}