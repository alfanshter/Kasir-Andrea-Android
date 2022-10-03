package com.kasirandrea.kasirandrea.kasir

import com.google.gson.annotations.SerializedName
import com.kasirandrea.kasirandrea.kasir.model.UsersModel

data class LoginResponse(

	@field:SerializedName("access_token")
	val accessToken: String? = null,

	@field:SerializedName("data")
	val data: UsersModel? = null,

	@field:SerializedName("token_type")
	val tokenType: String? = null,

	@field:SerializedName("status")
	val status: Int? = null
)

