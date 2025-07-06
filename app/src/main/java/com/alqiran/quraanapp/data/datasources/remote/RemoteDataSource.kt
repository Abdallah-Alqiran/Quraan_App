package com.alqiran.quraanapp.data.datasources.remote

import com.alqiran.quraanapp.data.datasources.remote.retrofit.api.SuwarApi
import com.alqiran.quraanapp.data.datasources.remote.retrofit.model.moshaf.AllMoshaf
import com.alqiran.quraanapp.data.datasources.remote.retrofit.model.radio.AllRadios
import com.alqiran.quraanapp.data.datasources.remote.retrofit.model.recent_reads.AllRecentReads
import com.alqiran.quraanapp.data.datasources.remote.retrofit.model.reciters.AllReciters
import com.alqiran.quraanapp.data.datasources.remote.retrofit.model.riwayat.AllRiwayat
import com.alqiran.quraanapp.data.datasources.remote.retrofit.model.suwar.AllSuwar
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val suwarApi: SuwarApi
) {

    private suspend inline fun <reified T> fetchData(crossinline apiCall: suspend() -> Response<T>): T {
        try {
            val response = apiCall()
            return response.body() ?: throw Exception("Response Body is null")
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun fetchAllSuwar(): AllSuwar {
        return fetchData { suwarApi.fetchAllSuwar() }
    }

    suspend fun fetchAllRiwayat(): AllRiwayat {
        return fetchData { suwarApi.fetchAllRiwayat() }
    }

    suspend fun fetchAllMoshaf(): AllMoshaf {
        return fetchData { suwarApi.fetchAllMoshaf() }
    }

    suspend fun fetchAllRecentReads(): AllRecentReads {
        return fetchData { suwarApi.fetchAllRecentReads() }
    }

    suspend fun fetchAllRadios(): AllRadios {
        return fetchData { suwarApi.fetchAllRadios() }
    }

    suspend fun fetchAllReciters(): AllReciters {
        return fetchData { suwarApi.fetchAllReciters() }
    }

}