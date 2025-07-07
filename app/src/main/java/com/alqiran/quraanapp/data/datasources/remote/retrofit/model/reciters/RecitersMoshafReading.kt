package com.alqiran.quraanapp.data.datasources.remote.retrofit.model.reciters

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class RecitersMoshafReading(
    val id: Int,
    @SerializedName("moshaf_type")
    val moshafType: Int,
    val name: String,
    val server: String,
    @SerializedName("surah_list")
    val surahList: String,
    @SerializedName("surah_total")
    val surahTotal: Int
)