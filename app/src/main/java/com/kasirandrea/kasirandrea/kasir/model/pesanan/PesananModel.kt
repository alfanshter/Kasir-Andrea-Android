package com.kasirandrea.kasirandrea.kasir.model.pesanan

import com.google.gson.annotations.SerializedName
import java.sql.Timestamp


data class PesananModel(

    @field:SerializedName("nomorpesanan")
    val nomorpesanan: String? = null,

    @field:SerializedName("nama")
    val nama: String? = null,

    @field:SerializedName("harga")
    val harga: Int? = null,

    @field:SerializedName("ongkir")
    val ongkir: Int? = null,

    @field:SerializedName("updated_at")
    val updatedAt: String? = null,

    @field:SerializedName("harga_total")
    val hargaTotal: Int? = null,

    @field:SerializedName("telepon")
    val telepon: String? = null,

    @field:SerializedName("created_at")
    val createdAt: Timestamp? = null,

    @field:SerializedName("id_user")
    val idUser: Int? = null,

    @field:SerializedName("kurir")
    val kurir: String? = null,

    @field:SerializedName("id")
    val id: Int? = null,
    @field:SerializedName("is_status")
    val is_status: Int? = null,

    @field:SerializedName("alamat")
    val alamat: String? = null
)
