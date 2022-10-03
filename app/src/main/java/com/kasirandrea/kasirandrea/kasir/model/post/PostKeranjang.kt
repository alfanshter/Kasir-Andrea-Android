package com.kasirandrea.kasirandrea.kasir.model.post

import com.google.gson.annotations.SerializedName

data class PostKeranjang(

	@field:SerializedName("id_produk")
	val idProduk: Int? = null,

	@field:SerializedName("harga")
	val harga: Int? = null,

	@field:SerializedName("jumlah")
	val jumlah: Int? = null,

	@field:SerializedName("id_user")
	val idUser: Int? = null,

	@field:SerializedName("harga_modal")
	val harga_modal: Int? = null
)
