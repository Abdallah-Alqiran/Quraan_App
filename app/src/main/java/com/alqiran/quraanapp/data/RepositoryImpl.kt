package com.alqiran.quraanapp.data

import com.alqiran.quraanapp.data.datasources.remote.RemoteDataSource
import com.alqiran.quraanapp.data.datasources.remote.retrofit.model.moshaf.AllMoshaf
import com.alqiran.quraanapp.data.datasources.remote.retrofit.model.radio.AllRadios
import com.alqiran.quraanapp.data.datasources.remote.retrofit.model.recent_reads.AllRecentReads
import com.alqiran.quraanapp.data.datasources.remote.retrofit.model.riwayat.AllRiwayat
import com.alqiran.quraanapp.data.datasources.remote.retrofit.model.suwar.AllSuwar
import com.alqiran.quraanapp.domain.repository.Repository
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource
): Repository {
    override suspend fun getAllSuwar(): AllSuwar {
        return remoteDataSource.fetchAllSuwar()
    }

    override suspend fun getAllRiwayat(): AllRiwayat {
        return remoteDataSource.fetchAllRiwayat()
    }

    override suspend fun getAllMoshaf(): AllMoshaf {
        return remoteDataSource.fetchAllMoshaf()
    }

    override suspend fun getAllRecentReads(): AllRecentReads {
        return remoteDataSource.fetchAllRecentReads()
    }

    override suspend fun getAllRadios(): AllRadios {
        return remoteDataSource.fetchAllRadios()
    }
}