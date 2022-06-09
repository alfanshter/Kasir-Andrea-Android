package com.kasirandrea.kasirandrea.kasir.model.gaji

import com.google.gson.annotations.SerializedName

data class GajiAdminResponse(

	@field:SerializedName("bonus")
	val bonus: Int? = null,

	@field:SerializedName("gaji")
	val gaji: Int? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Int? = null,

	@field:SerializedName("jumlah_penjualan")
	val jumlahPenjualan: Int? = null,

	@field:SerializedName("status_gaji")
	val status_gaji: Int? = null
)
