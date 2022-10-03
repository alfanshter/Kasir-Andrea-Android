package com.kasirandrea.kasirandrea.kasir.model.gaji

import com.google.gson.annotations.SerializedName

data class PostBayarGaji(

	@field:SerializedName("gaji_pokok")
	val gajiPokok: Int? = null,

	@field:SerializedName("bonus")
	val bonus: Int? = null,

	@field:SerializedName("id_user")
	val idUser: Int? = null,

	@field:SerializedName("total_penghasilan")
	val totalPenghasilan: Int? = null,

	@field:SerializedName("jumlah_penjualan")
	val jumlahPenjualan: Int? = null
)
