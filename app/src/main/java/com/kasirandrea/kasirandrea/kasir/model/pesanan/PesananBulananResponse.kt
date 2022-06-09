package com.kasirandrea.kasirandrea.kasir.model.pesanan

import com.google.gson.annotations.SerializedName

data class PesananBulananResponse(

	@field:SerializedName("data")
	val data: PesananData? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Int? = null
)

data class PesananData(

	@field:SerializedName("11")
	val jsonMember11: Int? = null,

	@field:SerializedName("1")
	val jsonMember1: Int? = null,

	@field:SerializedName("12")
	val jsonMember12: Int? = null,

	@field:SerializedName("2")
	val jsonMember2: Int? = null,

	@field:SerializedName("3")
	val jsonMember3: Int? = null,

	@field:SerializedName("4")
	val jsonMember4: Int? = null,

	@field:SerializedName("5")
	val jsonMember5: Int? = null,

	@field:SerializedName("6")
	val jsonMember6: List<JsonMember6Item?>? = null,

	@field:SerializedName("7")
	val jsonMember7: Int? = null,

	@field:SerializedName("8")
	val jsonMember8: Int? = null,

	@field:SerializedName("9")
	val jsonMember9: Int? = null,

	@field:SerializedName("10")
	val jsonMember10: Int? = null
)

data class JsonMember6Item(

	@field:SerializedName("nomorpesanan")
	val nomorpesanan: String? = null,

	@field:SerializedName("ongkir")
	val ongkir: Int? = null,

	@field:SerializedName("harga_total")
	val hargaTotal: Int? = null,

	@field:SerializedName("telepon")
	val telepon: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id_user")
	val idUser: Int? = null,

	@field:SerializedName("kurir")
	val kurir: String? = null,

	@field:SerializedName("alamat")
	val alamat: String? = null,

	@field:SerializedName("is_status")
	val isStatus: Int? = null,

	@field:SerializedName("nama")
	val nama: String? = null,

	@field:SerializedName("harga")
	val harga: Int? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("id")
	val id: Int? = null
)
