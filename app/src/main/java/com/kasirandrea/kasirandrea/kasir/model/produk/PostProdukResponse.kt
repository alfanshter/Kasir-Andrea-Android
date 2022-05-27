package com.kasirandrea.kasirandrea.kasir.model.produk

import com.google.gson.annotations.SerializedName

data class PostProdukResponse(

	@field:SerializedName("validator")
	val validator: ValidatorProduk? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Int? = null
)

data class ValidatorProduk(

	@field:SerializedName("foto")
	val foto: List<String?>? = null,

	@field:SerializedName("nama")
	val nama: List<String?>? = null,

	@field:SerializedName("harga")
	val harga: List<String?>? = null,

	@field:SerializedName("deskripsi")
	val deskripsi: List<String?>? = null,

	@field:SerializedName("stok")
	val stok: List<String?>? = null
)
