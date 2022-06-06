package com.kasirandrea.kasirandrea.kasir.model.rajaongkir

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ProvinsiResponse {
	@Expose
	@SerializedName("rajaongkir")
	var rajaOngkir : RajaOngkir = RajaOngkir()

	class Provinsi {
		var province_id: Int? = null
		var province: String? = null
		override fun toString(): String {
			return province!!
		}

	}
}

class RajaOngkir {
	@Expose
	@SerializedName("results")
	var results: List<ProvinsiResponse.Provinsi> = ArrayList()

}