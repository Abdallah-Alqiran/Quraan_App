package com.alqiran.quraanapp.ui.navigation

import androidx.navigation3.runtime.NavKey
import com.alqiran.quraanapp.data.datasources.remote.retrofit.model.reciters.RecitersMoshafReading
import kotlinx.serialization.Serializable


@Serializable
data object RecitersScreenRoute: NavKey

@Serializable
data class RiwayatScreenRoute(val riwayatReciter: List<RecitersMoshafReading>, val reciterName: String): NavKey

@Serializable
data class SuwarScreenRoute(val suwar: RecitersMoshafReading): NavKey