package com.alqiran.quraanapp.data.datasources.remote.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Audio(
    val surahNumber: String = "",
    val server: String = "",
    val surah: String = "",
    val reciter: String = "",
) : Parcelable