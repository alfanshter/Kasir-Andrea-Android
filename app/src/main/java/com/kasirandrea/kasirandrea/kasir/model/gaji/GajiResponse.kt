package com.kasirandrea.kasirandrea.kasir.model.gaji

import com.google.gson.annotations.SerializedName

data class GajiResponse(

	@field:SerializedName("data")
	val data: GajiModel? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Int? = null
)
