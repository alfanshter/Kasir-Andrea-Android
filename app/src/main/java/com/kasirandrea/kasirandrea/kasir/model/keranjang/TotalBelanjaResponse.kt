package com.kasirandrea.kasirandrea.kasir.model.keranjang

import com.google.gson.annotations.SerializedName

data class TotalBelanjaResponse(

	@field:SerializedName("total_belanja")
	val totalBelanja: Int? = null,

	@field:SerializedName("modal")
	val modal: Int? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Int? = null
)
