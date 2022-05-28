package com.kasirandrea.kasirandrea.kasir.webservice

import com.kasirandrea.kasirandrea.kasir.LoginResponse
import com.kasirandrea.kasirandrea.kasir.model.admin.AdminResponse
import com.kasirandrea.kasirandrea.kasir.model.produk.PostProdukResponse
import com.kasirandrea.kasirandrea.kasir.model.produk.ProdukResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("username") username : String,
        @Field("password") password: String
    ): Call<LoginResponse>

    //=======================PRODUK==================
    //UPLOAD PRODUK
    @Multipart
    @POST("produk")
    fun produk(
        @Part foto: MultipartBody.Part?,
        @Part("nama") nama: RequestBody,
        @Part("deskripsi") deskripsi: RequestBody,
        @Part("harga") harga: RequestBody,
        @Part("stok") stok: RequestBody,
    ): Call<PostProdukResponse>

    //EDIT PRODUK
    @Multipart
    @POST("update_produk")
    fun update_produk(
        @Part foto: MultipartBody.Part?,
        @Part("nama") nama: RequestBody,
        @Part("deskripsi") deskripsi: RequestBody,
        @Part("harga") harga: RequestBody,
        @Part("stok") stok: RequestBody,
        @Part("id") id: RequestBody,
        @Part("oldImage") oldImage: RequestBody,
    ): Call<PostProdukResponse>

    //EDIT PRODUK TANPA FOTO
    @Multipart
    @POST("update_produk")
    fun update_produk_foto(
        @Part("nama") nama: RequestBody,
        @Part("deskripsi") deskripsi: RequestBody,
        @Part("harga") harga: RequestBody,
        @Part("stok") stok: RequestBody,
        @Part("id") id: RequestBody,
        @Part("oldImage") oldImage: RequestBody,
    ): Call<PostProdukResponse>

    //Produk
    @GET("produk")
    fun getproduk(): Call<ProdukResponse>

    //Pencarian Produk
    @GET("search_produk")
    fun search_produk(
        @Query("nama") nama : String
    ): Call<ProdukResponse>
    //=======================END PRODUK==================
    //=======================ADMIN==================
    //Tambah ADMIN
    @Multipart
    @POST("tambah_admin")
    fun tambah_admin(
        @Part("nama") nama: RequestBody,
        @Part("alamat") alamat: RequestBody,
        @Part("telepon") telepon: RequestBody,
        @Part("email") email: RequestBody,
        @Part foto: MultipartBody.Part?,
        @Part("username") username: RequestBody,
        @Part("password") password: RequestBody,
        @Part("confirm_password") confirm_password: RequestBody,
    ): Call<PostProdukResponse>

    //Produk
    @GET("get_admin")
    fun get_admin(): Call<AdminResponse>
}