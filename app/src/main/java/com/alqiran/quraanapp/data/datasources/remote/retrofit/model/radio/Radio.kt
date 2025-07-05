package com.alqiran.quraanapp.data.datasources.remote.retrofit.model.radio

import com.google.gson.annotations.SerializedName

data class Radio(
    val id: Int,
    val name: String,
    @SerializedName("recent_date")
    val recentDate: String,
    val url: String
)