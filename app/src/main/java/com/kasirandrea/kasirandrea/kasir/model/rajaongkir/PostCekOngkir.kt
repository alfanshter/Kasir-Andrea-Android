package com.kasirandrea.kasirandrea.kasir.model.rajaongkir

import com.google.gson.annotations.SerializedName

data class PostCekOngkir(

	@field:SerializedName("courier")
	val courier: String? = null,

	@field:SerializedName("origin")
	val origin: Int? = null,

	@field:SerializedName("destination")
	val destination: Int? = null,

	@field:SerializedName("weight")
	val weight: Int? = null
)
