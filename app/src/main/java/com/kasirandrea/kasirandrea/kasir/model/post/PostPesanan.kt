package com.kasirandrea.kasirandrea.kasir.model.post

import com.google.gson.annotations.SerializedName

data class PostPesanan(

	@field:SerializedName("nama")
	val nama: String? = null,

	@field:SerializedName("ongkir")
	val ongkir: Int? = null,

	@field:SerializedName("harga_total")
	val hargaTotal: Int? = null,

	@field:SerializedName("harga")
	val harga: Int? = null,

	@field:SerializedName("telepon")
	val telepon: String? = null,

	@field:SerializedName("id_user")
	val idUser: Int? = null,

	@field:SerializedName("kurir")
	val kurir: String? = null,

	@field:SerializedName("alamat")
	val alamat: String? = null,
	@field:SerializedName("modal")
	val modal: Int? = null
)
