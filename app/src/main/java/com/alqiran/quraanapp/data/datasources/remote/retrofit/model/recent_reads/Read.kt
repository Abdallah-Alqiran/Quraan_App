package com.alqiran.quraanapp.data.datasources.remote.retrofit.model.recent_reads

import com.google.gson.annotations.SerializedName

data class Read(
    val id: Int,
    val letter: String,
    val moshafRead: List<MoshafRead>,
    val name: String,
    @SerializedName("recent_date")
    val recentDate: String
)