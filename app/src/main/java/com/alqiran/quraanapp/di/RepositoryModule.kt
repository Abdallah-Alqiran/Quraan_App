package com.alqiran.quraanapp.di

import com.alqiran.quraanapp.data.RepositoryImpl
import com.alqiran.quraanapp.domain.repository.Repository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun provideRepositoryModuleImpl(
        repositoryImpl: RepositoryImpl
    ): Repository

}