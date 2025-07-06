package com.alqiran.quraanapp.data.datasources.remote.retrofit.model.reciters

data class Reciter(
    val date: String,
    val id: Int,
    val letter: String,
    val moshaf: List<Moshaf>,
    val name: String
)