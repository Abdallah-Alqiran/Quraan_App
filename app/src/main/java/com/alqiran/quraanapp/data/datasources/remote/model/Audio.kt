package com.alqiran.quraanapp.data.datasources.remote.model

data class Audio(
    val surahNumber: String = "",
    val server: String = "",
    val surah: String = "",
    val reciter: String = "",
    val duration: Long = 0
)