package com.kasirandrea.kasirandrea.kasir.model.produk

import com.google.gson.annotations.SerializedName

data class DetailProdukResponse(

	@field:SerializedName("data")
	val data: ProdukModel? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Int? = null
)
