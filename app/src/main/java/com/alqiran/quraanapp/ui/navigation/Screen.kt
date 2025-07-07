package com.alqiran.quraanapp.ui.navigation

import com.alqiran.quraanapp.data.datasources.remote.retrofit.model.reciters.RecitersMoshafReading
import kotlinx.serialization.Serializable


@Serializable
data object RecitersScreenRoute

@Serializable
data class RiwayatScreenRoute(val riwayatReciter: List<RecitersMoshafReading>, val reciterName: String)