package com.alqiran.quraanapp.data.datasources.remote.retrofit.model.moshaf

import com.google.gson.annotations.SerializedName

data class AllMoshaf(
    @SerializedName("riwayat")
    val moshaf: List<Moshaf>
)