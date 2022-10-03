package com.kasirandrea.kasirandrea.kasir.model.produk

import com.google.gson.annotations.SerializedName


data class ProdukModel(

    @field:SerializedName("nama")
    val nama: String? = null,

    @field:SerializedName("harga")
    val harga: Int? = null,

    @field:SerializedName("modal")
    val modal: Int? = null,

    @field:SerializedName("harga_grosir")
    val harga_grosir: Int? = null,

    @field:SerializedName("jumlah_grosir")
    val jumlah_grosir: Int? = null,

    @field:SerializedName("diskon")
    val diskon: Int? = null,

    @field:SerializedName("foto")
    val foto: String? = null,

    @field:SerializedName("updated_at")
    val updatedAt: String? = null,

    @field:SerializedName("created_at")
    val createdAt: String? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("deskripsi")
    val deskripsi: String? = null,

    @field:SerializedName("stok")
    val stok: Int? = null
)
