package com.example.coinlist.di

import coingecko.CoinGeckoClient
import com.example.coinlist.repositories.CoinListRepo
import com.example.coinlist.viewModels.CoinListViewmodel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HiltModule {
    @Singleton
    @Provides
    fun provideCoinGeckoApi(): CoinGeckoClient {
        return CoinGeckoClient()
    }

    @Singleton
    @Provides
    fun provideRepository(
        coinGeckoApi: CoinGeckoClient
    ): CoinListRepo {
        return CoinListRepo(coinGeckoApi)
    }

    @Singleton
    @Provides
    fun provideCoinListViewModel(
        coinListRepo: CoinListRepo
    ): CoinListViewmodel {
        return CoinListViewmodel(coinListRepo)
    }

}