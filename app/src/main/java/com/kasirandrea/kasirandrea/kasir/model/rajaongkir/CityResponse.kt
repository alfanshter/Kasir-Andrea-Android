package com.kasirandrea.kasirandrea.kasir.model.rajaongkir

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CityResponse {
	@Expose
	@SerializedName("rajaongkir")
	var rajaOngkir : RajaOngkirCity = RajaOngkirCity()

	class City {
		var city_id: Int? = null
		var province_id: Int? = null
		var type: String? = null
		var city_name: String? = null
		override fun toString(): String {
			return "$type $city_name"
		}

	}
}

class RajaOngkirCity {
	@Expose
	@SerializedName("results")
	var results: List<CityResponse.City> = ArrayList()

}