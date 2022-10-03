package com.kasirandrea.kasirandrea.kasir.model.penghasilan

import com.google.gson.annotations.SerializedName

data class PenghasilanResponse(

	@field:SerializedName("harga")
	val harga: Int? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("modal")
	val modal: Int? = null,

	@field:SerializedName("status")
	val status: Int? = null
)
