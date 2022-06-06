package com.kasirandrea.kasirandrea.kasir.model.pesanan

import com.google.gson.annotations.SerializedName

data class PesananResponse(

	@field:SerializedName("data")
	val data: PesananModel? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Int? = null
)
