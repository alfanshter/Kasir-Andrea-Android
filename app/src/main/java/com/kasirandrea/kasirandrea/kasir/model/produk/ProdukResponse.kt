package com.kasirandrea.kasirandrea.kasir.model.produk

import com.google.gson.annotations.SerializedName

data class ProdukResponse(

	@field:SerializedName("data")
	val data: List<ProdukModel>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Int? = null
)
