package com.kasirandrea.kasirandrea.kasir.model.pesanan

import com.google.gson.annotations.SerializedName

data class ListPesananResponse(

	@field:SerializedName("data")
	val data: List<PesananModel>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Int? = null
)
