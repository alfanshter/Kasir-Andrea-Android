package com.kasirandrea.kasirandrea.kasir.model.keranjang

import com.google.gson.annotations.SerializedName
import com.kasirandrea.kasirandrea.kasir.model.modal.HargaModalModel

data class KeranjangResponse(

	@field:SerializedName("data")
	val data: List<KeranjangModel>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Int? = null,

	@field:SerializedName("harga_modal")
	val harga_modal: HargaModalModel? = null,

)


