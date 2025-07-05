package com.alqiran.quraanapp.di

import com.alqiran.quraanapp.data.Constants.BASE_URL
import com.alqiran.quraanapp.data.datasources.remote.retrofit.api.SuwarApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofitInstance(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideSurahApi(retrofit: Retrofit): SuwarApi {
        return retrofit.create(SuwarApi::class.java)
    }

}