package com.kasirandrea.kasirandrea.kasir.model.register

import com.google.gson.annotations.SerializedName

data class RegisterResponse(

	@field:SerializedName("validator")
	val validator: Validator? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Int? = null
)

data class Validator(

	@field:SerializedName("password")
	val password: List<String?>? = null,

	@field:SerializedName("role")
	val role: List<String?>? = null,

	@field:SerializedName("nama")
	val nama: List<String?>? = null,

	@field:SerializedName("username")
	val username: List<String?>? = null,

	@field:SerializedName("confirm_password")
	val confirmPassword: List<String?>? = null
)
