package com.kasirandrea.kasirandrea.kasir.model.keranjang

import com.google.gson.annotations.SerializedName
import com.kasirandrea.kasirandrea.kasir.model.UsersModel
import com.kasirandrea.kasirandrea.kasir.model.produk.ProdukModel


data class KeranjangModel(

    @field:SerializedName("id_produk")
    val idProduk: Int? = null,

    @field:SerializedName("harga")
    val harga: Int? = null,

    @field:SerializedName("jumlah")
    val jumlah: Int? = null,

    @field:SerializedName("updated_at")
    val updatedAt: String? = null,

    @field:SerializedName("produk")
    val produk: ProdukModel? = null,

    @field:SerializedName("created_at")
    val createdAt: String? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("id_user")
    val idUser: Int? = null,

    @field:SerializedName("user")
    val user: UsersModel? = null
)
