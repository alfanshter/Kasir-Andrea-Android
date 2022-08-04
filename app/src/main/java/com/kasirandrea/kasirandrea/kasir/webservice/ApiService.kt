package com.kasirandrea.kasirandrea.kasir.webservice

import com.kasirandrea.kasirandrea.kasir.LoginResponse
import com.kasirandrea.kasirandrea.kasir.model.admin.AdminResponse
import com.kasirandrea.kasirandrea.kasir.model.gaji.GajiAdminResponse
import com.kasirandrea.kasirandrea.kasir.model.gaji.GajiResponse
import com.kasirandrea.kasirandrea.kasir.model.gaji.PostBayarGaji
import com.kasirandrea.kasirandrea.kasir.model.gaji.RiwayatGajiResponse
import com.kasirandrea.kasirandrea.kasir.model.keranjang.KeranjangResponse
import com.kasirandrea.kasirandrea.kasir.model.keranjang.TotalBelanjaResponse
import com.kasirandrea.kasirandrea.kasir.model.penghasilan.PenghasilanResponse
import com.kasirandrea.kasirandrea.kasir.model.pesanan.ListPesananResponse
import com.kasirandrea.kasirandrea.kasir.model.pesanan.NotaResponse
import com.kasirandrea.kasirandrea.kasir.model.pesanan.PesananBulananResponse
import com.kasirandrea.kasirandrea.kasir.model.pesanan.PesananResponse
import com.kasirandrea.kasirandrea.kasir.model.post.PostKeranjang
import com.kasirandrea.kasirandrea.kasir.model.post.PostPesanan
import com.kasirandrea.kasirandrea.kasir.model.produk.DetailProdukResponse
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
        @Field("username") username: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @GET("logout")
    fun logout(
    ): Call<PostProdukResponse>

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
        @Part("harga_grosir") harga_grosir: RequestBody,
        @Part("jumlah_grosir") jumlah_grosir: RequestBody,
        @Part("diskon") diskon: RequestBody,
        @Part("modal") modal: RequestBody
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

    //EDIT PRODUK TANPA FOTO
    @FormUrlEncoded
    @POST("hapus_produk")
    fun hapus_produk(
        @Field("oldImage") oldImage: String,
        @Field("id") id: Int    ): Call<PostProdukResponse>

    //Produk
    @GET("produk")
    fun getproduk(): Call<ProdukResponse>


    //EDIT PRODUK TANPA FOTO
    @FormUrlEncoded
    @POST("generate_qrcode")
    fun generate_qrcode(
        @Field("id") id: Int): Call<NotaResponse>

    //Pencarian Produk
    @GET("search_produk")
    fun search_produk(
        @Query("nama") nama: String
    ): Call<ProdukResponse>

    //Detail Produk
    @GET("detail_produk")
    fun detail_produk(
        @Query("id") id: String
    ): Call<DetailProdukResponse>

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

    //Admin
    @GET("get_admin")
    fun get_admin(): Call<AdminResponse>

    //Pencarian Admin
    @GET("search_admin")
    fun search_admin(
        @Query("nama") nama: String
    ): Call<AdminResponse>

    //tambah gaji
    @FormUrlEncoded
    @POST("delete_admin")
    fun delete_admin(
        @Field("id") id: Int,
        @Field("foto") foto: String
    ): Call<PostProdukResponse>
    //=======================END ADMIN==================

    //=======================KERANJANG==================
    @Headers("Content-Type: application/json")
    @POST("tambah_keranjang")
    fun tambah_keranjang(@Body post: PostKeranjang): Call<PostProdukResponse>

    //kurang keranjang
    @FormUrlEncoded
    @POST("kurang_keranjang")
    fun kurang_keranjang(
        @Field("id") id: Int,
        @Field("jumlah") jumlah: Int,
    ): Call<PostProdukResponse>

    //data keranjang sesuai id
    @GET("get_keranjang")
    fun get_keranjang(
        @Query("id_user") id_user: Int
    ): Call<KeranjangResponse>

    //data keranjang sesuai id
    @GET("get_keranjang_pesanan")
    fun get_keranjang_pesanan(
        @Query("nomorpesanan") nomorpesanan: String
    ): Call<KeranjangResponse>


    //Hapus Keranjang
    @FormUrlEncoded
    @POST("hapus_keranjang")
    fun hapus_keranjang(
        @Field("id") id: Int
    ): Call<PostProdukResponse>

    //Sum Keranjang
    @GET("total_belanja")
    fun total_belanja(
        @Query("id_user") id_user: Int
    ): Call<TotalBelanjaResponse>
    //=======================END KERANJANG==================
    //=======================PESANAN==================
    @Headers("Content-Type: application/json")
    @POST("tambah_transaksi")
    fun tambah_transaksi(@Body post: PostPesanan): Call<PesananResponse>

    //selesaikan pesanan
    @FormUrlEncoded
    @POST("pesanan_selesai")
    fun pesanan_selesai(
        @Field("id") id: Int): Call<PostProdukResponse>

    //get pesanan sesuai id
    @GET("get_pesanan_id")
    fun get_pesanan_id(
        @Query("id_user") id_user: Int
    ): Call<ListPesananResponse>

    //get pesanan bulanan
    @GET("get_pesanan_owner")
    fun get_pesanan_owner(): Call<ListPesananResponse>

    @FormUrlEncoded
    @POST("cetak_nota")
    fun cetak_nota(
        @Field("id") id: Int,
        @Field("nomorpesanan") nomorpesanan: String
    ): Call<NotaResponse>

    //=======================GAJI==================
    //get gaji
    @GET("get_gaji")
    fun get_gaji(): Call<GajiResponse>

    //get gaji admin
    @GET("gaji_admin")
    fun gaji_admin(
        @Query("id_user") id_user: Int
    ): Call<GajiAdminResponse>

    //riwayat gaji admin
    @GET("riwayat_gaji_admin")
    fun riwayat_gaji_admin(
        @Query("id_user") id_user: Int
    ): Call<RiwayatGajiResponse>


    //tambah gaji
    @FormUrlEncoded
    @POST("set_gaji")
    fun set_gaji(
        @Field("gaji") gaji: Int,
        @Field("bonus") bonus: Int
    ): Call<PostProdukResponse>

    @Headers("Content-Type: application/json")
    @POST("bayar_gaji")
    fun bayar_gaji(@Body post: PostBayarGaji): Call<PostProdukResponse>

    //=======================END GAJI==================
    //=======================PENGHASILAN====================
    //riwayat gaji admin
    @GET("penghasilan_bulanan")
    fun penghasilan_bulanan(
    ): Call<PenghasilanResponse>

    //=======================END PENGHASILAN==================

}