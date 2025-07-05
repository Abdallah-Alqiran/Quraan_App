package com.alqiran.quraanapp.data.datasources.remote.retrofit.model.moshaf

import com.google.gson.annotations.SerializedName

data class Moshaf(
    val id: Int,
    @SerializedName("moshaf_type")
    val moshafType: Int,
    @SerializedName("moshaf_id")
    val moshafId: Int,
    val name: String
)