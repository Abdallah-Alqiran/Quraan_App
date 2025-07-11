package com.alqiran.quraanapp.data.datasources.remote.retrofit.api

import com.alqiran.quraanapp.data.APIConstants.MOSHAF
import com.alqiran.quraanapp.data.APIConstants.RADIOS
import com.alqiran.quraanapp.data.APIConstants.RECENT_READS
import com.alqiran.quraanapp.data.APIConstants.RECITERS
import com.alqiran.quraanapp.data.APIConstants.RIWAYAT
import com.alqiran.quraanapp.data.APIConstants.SUWAR
import com.alqiran.quraanapp.data.datasources.remote.retrofit.model.moshaf.AllMoshaf
import com.alqiran.quraanapp.data.datasources.remote.retrofit.model.radio.AllRadios
import com.alqiran.quraanapp.data.datasources.remote.retrofit.model.recent_reads.AllRecentReads
import com.alqiran.quraanapp.data.datasources.remote.retrofit.model.reciters.AllReciters
import com.alqiran.quraanapp.data.datasources.remote.retrofit.model.riwayat.AllRiwayat
import com.alqiran.quraanapp.data.datasources.remote.retrofit.model.suwar.AllSuwar
import retrofit2.Response
import retrofit2.http.GET

interface SuwarApi {

    @GET(SUWAR)
    suspend fun fetchAllSuwar(): Response<AllSuwar>

    @GET(RIWAYAT)
    suspend fun fetchAllRiwayat(): Response<AllRiwayat>

    @GET(MOSHAF)
    suspend fun fetchAllMoshaf(): Response<AllMoshaf>

    @GET(RECENT_READS)
    suspend fun fetchAllRecentReads(): Response<AllRecentReads>

    @GET(RADIOS)
    suspend fun fetchAllRadios(): Response<AllRadios>

    @GET(RECITERS)
    suspend fun fetchAllReciters(): Response<AllReciters>

}