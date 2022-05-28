package com.kasirandrea.kasirandrea.kasir.model.admin

import com.google.gson.annotations.SerializedName

data class AdminResponse(

    @field:SerializedName("data")
	val data: List<AdminModel>? = null,

    @field:SerializedName("message")
	val message: String? = null,

    @field:SerializedName("status")
	val status: Int? = null
)