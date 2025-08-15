package com.alqiran.quraanapp.data.datasources.remote.retrofit.model.suwar

import com.google.gson.annotations.SerializedName

data class Suwar(
    @SerializedName("end_page")
    val endPage: Int,
    val id: Int,
    val makkia: Int,
    val name: String,
    @SerializedName("startPage")
    val startPage: Int,
    val type: Int
)

data class SuwarExist(
    val id: Int,
    val surahNumber: Int,
    val name: String,
)