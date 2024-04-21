package com.turker.esatis.utils.di

import com.turker.esatis.data.NetworkService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class NetworkServiceDI {

    @Provides
    fun provideNetworkService(): NetworkService {
        return NetworkService()
    }
}