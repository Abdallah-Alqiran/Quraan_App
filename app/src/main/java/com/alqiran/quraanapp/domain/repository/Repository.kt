package com.alqiran.quraanapp.domain.repository

import com.alqiran.quraanapp.data.datasources.remote.retrofit.model.moshaf.AllMoshaf
import com.alqiran.quraanapp.data.datasources.remote.retrofit.model.radio.AllRadios
import com.alqiran.quraanapp.data.datasources.remote.retrofit.model.recent_reads.AllRecentReads
import com.alqiran.quraanapp.data.datasources.remote.retrofit.model.riwayat.AllRiwayat
import com.alqiran.quraanapp.data.datasources.remote.retrofit.model.suwar.AllSuwar

interface Repository {

    suspend fun getAllSuwar(): AllSuwar

    suspend fun getAllRiwayat(): AllRiwayat

    suspend fun getAllMoshaf(): AllMoshaf

    suspend fun getAllRecentReads(): AllRecentReads

    suspend fun getAllRadios(): AllRadios

}