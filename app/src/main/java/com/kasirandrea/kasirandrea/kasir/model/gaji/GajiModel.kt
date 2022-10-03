package com.kasirandrea.kasirandrea.kasir.model.gaji

import com.google.gson.annotations.SerializedName


data class GajiModel(

    @field:SerializedName("updated_at")
    val updatedAt: String? = null,

    @field:SerializedName("bonus")
    val bonus: Int? = null,

    @field:SerializedName("gaji")
    val gaji: Int? = null,

    @field:SerializedName("created_at")
    val createdAt: String? = null,

    @field:SerializedName("id")
    val id: Int? = null
)
