package com.kasirandrea.kasirandrea.kasir.model.gaji

import com.google.gson.annotations.SerializedName
import java.sql.Timestamp

data class RiwayatGajiModel(

    @field:SerializedName("gaji_pokok")
    val gajiPokok: Int? = null,

    @field:SerializedName("updated_at")
    val updatedAt: String? = null,

    @field:SerializedName("bonus")
    val bonus: Int? = null,

    @field:SerializedName("created_at")
    val createdAt: Timestamp? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("id_user")
    val idUser: Int? = null,

    @field:SerializedName("total_penghasilan")
    val totalPenghasilan: Int? = null,

    @field:SerializedName("jumlah_penjualan")
    val jumlahPenjualan: Int? = null
)
